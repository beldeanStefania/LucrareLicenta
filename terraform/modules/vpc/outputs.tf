output "vpc_id" { value = aws_vpc.this.id }
output "public_subnets" { value = aws_subnet.public[*].id }
output "private_app_subnets" { value = aws_subnet.app[*].id }
output "private_db_subnets" { value = aws_subnet.db[*].id }
