variable "cluster_name" {
  type        = string
  description = "The name of the EKS cluster"
}

variable "cluster_role_arn" {
  type        = string
  description = "The ARN of the IAM role for the EKS cluster"
}

variable "public_subnets" {
  type        = list(string)
  description = "A list of public subnet IDs for the EKS cluster"
}

variable "region" {
  type        = string
  description = "The AWS region to deploy the EKS cluster"
}
