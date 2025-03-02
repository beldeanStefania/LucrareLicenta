#!/bin/bash
set -e

APP_DIR="/home/ubuntu/app"
REPO_URL="https://github.com/${GITHUB_REPOSITORY}.git"

# If the app directory doesn't exist, clone the repository
if [ ! -d "$APP_DIR" ]; then
    echo "Directory $APP_DIR does not exist. Cloning repository..."
    git clone "$REPO_URL" "$APP_DIR" || {
        echo "Failed to clone repository"
        exit 1
    }
fi

cd "$APP_DIR" || {
    echo "Failed to change directory to $APP_DIR"
    exit 1
}

# Verify it's a git repository
if [ ! -d .git ]; then
    echo "Error: $APP_DIR is not a git repository."
    exit 1
fi

echo "Pulling latest changes..."
git pull origin main || {
    echo "Failed to pull latest changes"
    exit 1
}

# Use the environment variable passed from GitHub Actions
if [ -z "$SQL_PASSWORD" ]; then
    echo "SQL_PASSWORD is not set."
    exit 1
fi

# Find docker command
DOCKER_BIN=$(command -v docker)
if [ -z "$DOCKER_BIN" ]; then
    echo "docker command not found in PATH"
    exit 1
fi

echo "Pruning old docker images..."
sudo -E "$DOCKER_BIN" image prune -a -f || {
    echo "Failed to prune docker images"
    exit 1
}

echo "Pulling new docker images..."
sudo -E "$DOCKER_BIN" compose pull || {
    echo "Failed to pull new docker images"
    exit 1
}

echo "Taking down current docker containers..."
sudo -E "$DOCKER_BIN" compose down || {
    echo "Failed to bring down docker containers"
    exit 1
}

echo "Starting docker containers..."
sudo -E "$DOCKER_BIN" compose up -d || {
    echo "Failed to start docker containers"
    exit 1
}
