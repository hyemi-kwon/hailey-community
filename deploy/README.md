# Split EC2 deployment

The frontend and backend EC2 instances each run only their own application
container. Copy the matching example to `.env`, replace its placeholders, then
start that service from its directory.

```bash
cp .env.example .env
chmod 600 .env
sudo docker compose config --quiet
sudo docker compose pull
sudo docker compose up -d
```

Do not commit either server's `.env`. The image tag must be the full commit SHA
published by the CI workflow.

The frontend EC2 security group should accept port 8080 only from the ALB
security group. The backend EC2 security group should accept port 8090 only
from the ALB security group. The database should accept port 3306 only from
the backend EC2 security group.
