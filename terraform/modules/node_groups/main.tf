resource "aws_iam_role" "nodes" {
  name = "${var.cluster_name}-nodes-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action    = "sts:AssumeRole",
        Effect    = "Allow",
        Principal = { Service = "ec2.amazonaws.com" }
      }
    ]
  })
}

locals {
  policies = [
    "AmazonEKSWorkerNodePolicy",
    "AmazonEKS_CNI_Policy",
    "AmazonEC2ContainerRegistryReadOnly",
    "AmazonSSMManagedInstanceCore"
  ]
}
resource "aws_iam_role_policy_attachment" "attach" {
  for_each   = toset(local.policies)
  role       = aws_iam_role.nodes.name
  policy_arn = "arn:aws:iam::aws:policy/${each.key}"
}

resource "aws_eks_node_group" "app" {
  cluster_name    = var.cluster_name
  node_group_name = "${var.cluster_name}-app-ng"
  node_role_arn   = aws_iam_role.nodes.arn
  subnet_ids      = var.private_app_subnets

  instance_types = var.instance_types
  scaling_config {
    desired_size = var.desired_size
    max_size     = var.max_size
    min_size     = var.min_size
  }

  labels = { tier = "app" }
}

resource "aws_eks_node_group" "db" {
  cluster_name    = var.cluster_name
  node_group_name = "${var.cluster_name}-db-ng"
  node_role_arn   = aws_iam_role.nodes.arn
  subnet_ids      = var.private_db_subnets

  scaling_config {
    desired_size = var.db_desired_size
    max_size     = var.db_max_size
    min_size     = var.db_min_size
  }

  labels = { tier = "mysql" }
}
