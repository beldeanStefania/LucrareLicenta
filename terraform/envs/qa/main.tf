provider "aws" {
  region  = var.region
  profile = "simon-admin"
}

data "aws_caller_identity" "me" {}

output "aws_caller" {
  value = {
    account_id = data.aws_caller_identity.me.account_id
    user_arn   = data.aws_caller_identity.me.arn
    user_id    = data.aws_caller_identity.me.user_id
  }
}

module "vpc" {
  source            = "../../modules/vpc"
  cluster_name      = var.cluster_name
  vpc_cidr          = var.vpc_cidr
  public_cidrs      = var.public_cidrs
  private_app_cidrs = var.private_app_cidrs
  private_db_cidrs  = var.private_db_cidrs
  azs               = var.azs
}

# IAM role for EKS cluster
resource "aws_iam_role" "eks_cluster" {
  name               = "${var.cluster_name}-eks-role"
  assume_role_policy = <<EOF
{
  "Version":"2012-10-17",
  "Statement":[{"Effect":"Allow","Principal":{"Service":"eks.amazonaws.com"},"Action":"sts:AssumeRole"}]
}
EOF
}
resource "aws_iam_role_policy_attachment" "c1" {
  role       = aws_iam_role.eks_cluster.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
}
resource "aws_iam_role_policy_attachment" "c2" {
  role       = aws_iam_role.eks_cluster.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSServicePolicy"
}

module "eks" {
  source           = "../../modules/eks"
  cluster_name     = var.cluster_name
  cluster_role_arn = aws_iam_role.eks_cluster.arn
  public_subnets   = module.vpc.public_subnets
  region           = var.region
}

module "nodes" {
  source              = "../../modules/node_groups"
  cluster_name        = module.eks.cluster_name
  private_app_subnets = module.vpc.private_app_subnets
  private_db_subnets  = module.vpc.private_db_subnets
}
