#!/bin/bash

# Numa Development Stop Script
# This script stops the development environment

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_status "Stopping Numa development environment..."

# Kill backend if running
if [ -f logs/backend.pid ]; then
    print_status "Stopping backend..."
    kill $(cat logs/backend.pid) 2>/dev/null || true
    rm logs/backend.pid
    print_success "Backend stopped"
fi

# Kill frontend if running
if [ -f logs/frontend.pid ]; then
    print_status "Stopping frontend..."
    kill $(cat logs/frontend.pid) 2>/dev/null || true
    rm logs/frontend.pid
    print_success "Frontend stopped"
fi

# Stop Docker services
print_status "Stopping Docker services..."
docker-compose -f scripts/docker-compose.dev.yml down

print_success "Development environment stopped"
print_status "All services have been shut down"
