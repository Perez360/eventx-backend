#!/bin/bash

docker kill eventx-backend

docker rm eventx-backend

docker rmi eventx-backend:latest

DOCKER_BUILDKIT=1 docker buildx build --platform linux/amd64 -f  Dockerfile -t eventx-backend:latest .

docker compose up eventx-backend