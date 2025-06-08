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

# resource "null_resource" "kubeconfig" {
#   provisioner "local-exec" {
#     command = "aws eks update-kubeconfig --region ${var.region} --name ${aws_eks_cluster.this.name} --profile simon-admin"
#   }
#   depends_on = [aws_eks_cluster.this]
# }
