resource "aws_eks_cluster" "this" {
  name     = var.cluster_name
  role_arn = var.cluster_role_arn

  vpc_config {
    subnet_ids              = var.public_subnets
    endpoint_private_access = true
    endpoint_public_access  = true
  }

  tags = { Name = "${var.cluster_name}-eks" }
}

resource "aws_iam_role_policy_attachment" "gha_deploy_eks_describe" {
  role       = "GitHubActionsEKSDeploy"
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
}
