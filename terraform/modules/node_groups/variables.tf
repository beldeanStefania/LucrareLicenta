variable "cluster_name" {
  description = "EKS cluster name"
  type        = string
}

variable "private_app_subnets" {
  description = "List of private subnets for the app node group"
  type        = list(string)
}

variable "private_db_subnets" {
  description = "List of private subnets for the db node group"
  type        = list(string)
}

variable "desired_size" {
  description = "Desired number of app nodes"
  type        = number
  default     = 1
}

variable "max_size" {
  description = "Maximum number of app nodes"
  type        = number
  default     = 2
}

variable "min_size" {
  description = "Minimum number of app nodes"
  type        = number
  default     = 1
}

variable "db_desired_size" {
  description = "Desired number of db nodes"
  type        = number
  default     = 1
}

variable "db_max_size" {
  description = "Maximum number of db nodes"
  type        = number
  default     = 1
}

variable "db_min_size" {
  description = "Minimum number of db nodes"
  type        = number
  default     = 1
}

variable "instance_types" {
  description = "List of instance types for the app node group"
  type        = list(string)
  default     = ["t3.small"]
}
