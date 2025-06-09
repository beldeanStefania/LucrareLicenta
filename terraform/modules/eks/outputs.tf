output "cluster_endpoint" { value = aws_eks_cluster.this.endpoint }
output "cluster_cert" { value = aws_eks_cluster.this.certificate_authority[0].data }
output "cluster_name" {
  value = aws_eks_cluster.this.name
}
