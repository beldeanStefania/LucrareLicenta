variable "cluster_name" {
  type = string
}

variable "region" {
  type    = string
  default = "eu-west-1"
}

variable "vpc_cidr" {
  default = "10.0.0.0/16"
}

variable "public_cidrs" {
  default = ["10.0.1.0/24", "10.0.2.0/24"]
}

variable "private_app_cidrs" {
  default = ["10.0.3.0/24", "10.0.4.0/24"]
}

variable "private_db_cidrs" {
  default = ["10.0.5.0/24", "10.0.6.0/24"]
}

variable "azs" {
  default = ["eu-west-1a", "eu-west-1b"]
}
