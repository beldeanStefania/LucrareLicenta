variable "cluster_name" { type = string }
variable "vpc_cidr" { type = string }
variable "public_cidrs" { type = list(string) }
variable "private_app_cidrs" { type = list(string) }
variable "private_db_cidrs" { type = list(string) }
variable "azs" { type = list(string) }
