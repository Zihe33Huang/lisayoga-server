# lisayoga-server
The backend of lisayoga


# Tech Stack

SpringBoot
Docker
Docker-Compose
MySQL
Nginx


# Nginx 

Used for Port Forwarding.  
PS:  server_name is necessary, otherwise nginx may not work 

```
server {
    listen 80;
    server_name 13.42.17.166;  

    location /lisayoga/ {
        proxy_pass http://127.0.0.1:12010/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}

```
