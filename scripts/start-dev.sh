#!/bin/bash

# Numa Development Startup Script
# This script starts the development environment with all required services

set -e

echo "🚀 Starting Numa Development Environment..."

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

# Check if Docker is installed and running
check_docker() {
    print_status "Checking Docker installation..."
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker first."
        exit 1
    fi

    if ! docker info &> /dev/null; then
        print_error "Docker is not running. Please start Docker first."
        exit 1
    fi

    print_success "Docker is installed and running"
}

# Check if Java is installed
check_java() {
    print_status "Checking Java installation..."
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed. Please install Java 17+ first."
        exit 1
    fi

    java_version=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$java_version" -lt 17 ]; then
        print_error "Java 17+ is required. Current version: $java_version"
        exit 1
    fi

    print_success "Java $java_version is installed"
}

# Check if Node.js is installed
check_node() {
    print_status "Checking Node.js installation..."
    if ! command -v node &> /dev/null; then
        print_error "Node.js is not installed. Please install Node.js 18+ first."
        exit 1
    fi

    node_version=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
    if [ "$node_version" -lt 18 ]; then
        print_error "Node.js 18+ is required. Current version: $node_version"
        exit 1
    fi

    print_success "Node.js v$node_version is installed"
}

# Start development database and services
start_services() {
    print_status "Starting development services..."
    
    # Stop any existing services
    docker-compose -f scripts/docker-compose.dev.yml down -v 2>/dev/null || true
    
    # Start services
    docker-compose -f scripts/docker-compose.dev.yml up -d
    
    print_status "Waiting for services to be ready..."
    sleep 10
    
    # Check if PostgreSQL is ready
    until docker exec numa-postgres-dev pg_isready -U numa_dev -d numa_dev; do
        print_status "Waiting for PostgreSQL to be ready..."
        sleep 2
    done
    
    print_success "Development services are running"
    print_status "PostgreSQL: localhost:5433"
    print_status "Redis: localhost:6380"
    print_status "MailHog: http://localhost:8025"
    print_status "pgAdmin: http://localhost:5050 (admin@numa.dev / admin)"
}

# Setup backend
setup_backend() {
    print_status "Setting up backend..."
    
    # Create application-dev.yml if it doesn't exist
    if [ ! -f "src/main/resources/application-dev.yml" ]; then
        print_status "Creating development configuration..."
        cat > src/main/resources/application-dev.yml << EOF
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/numa_dev
    username: numa_dev
    password: numa_dev_password
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  
  mail:
    host: localhost
    port: 1025
    properties:
      mail:
        smtp:
          auth: false
          starttls:
            enable: false

  redis:
    host: localhost
    port: 6380

logging:
  level:
    com.numa: DEBUG
    org.springframework.security: DEBUG
EOF
    fi
    
    print_success "Backend configuration ready"
}

# Setup frontend
setup_frontend() {
    print_status "Setting up frontend..."
    
    cd frontend
    
    # Create .env.local if it doesn't exist
    if [ ! -f ".env.local" ]; then
        print_status "Creating frontend environment configuration..."
        cat > .env.local << EOF
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
NEXT_PUBLIC_APP_URL=http://localhost:3000
EOF
    fi
    
    # Install dependencies if node_modules doesn't exist
    if [ ! -d "node_modules" ]; then
        print_status "Installing frontend dependencies..."
        npm install
    fi
    
    cd ..
    print_success "Frontend configuration ready"
}

# Start backend in development mode
start_backend() {
    print_status "Starting backend in development mode..."
    
    # Set Spring profile
    export SPRING_PROFILES_ACTIVE=dev
    
    # Start backend in background
    ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev > logs/backend.log 2>&1 &
    BACKEND_PID=$!
    echo $BACKEND_PID > logs/backend.pid
    
    print_status "Backend starting... (PID: $BACKEND_PID)"
    print_status "Logs: tail -f logs/backend.log"
    
    # Wait for backend to start
    print_status "Waiting for backend to be ready..."
    for i in {1..30}; do
        if curl -f http://localhost:8080/api/v1/actuator/health &>/dev/null; then
            print_success "Backend is ready at http://localhost:8080"
            break
        fi
        sleep 2
    done
}

# Start frontend in development mode
start_frontend() {
    print_status "Starting frontend in development mode..."
    
    cd frontend
    
    # Start frontend in background
    npm run dev > ../logs/frontend.log 2>&1 &
    FRONTEND_PID=$!
    echo $FRONTEND_PID > ../logs/frontend.pid
    
    cd ..
    
    print_status "Frontend starting... (PID: $FRONTEND_PID)"
    print_status "Logs: tail -f logs/frontend.log"
    
    # Wait for frontend to start
    print_status "Waiting for frontend to be ready..."
    for i in {1..20}; do
        if curl -f http://localhost:3000 &>/dev/null; then
            print_success "Frontend is ready at http://localhost:3000"
            break
        fi
        sleep 2
    done
}

# Create logs directory
mkdir -p logs

# Main execution
main() {
    print_status "Starting Numa development environment setup..."
    
    check_docker
    check_java
    check_node
    start_services
    setup_backend
    setup_frontend
    start_backend
    start_frontend
    
    print_success "🎉 Numa development environment is ready!"
    echo ""
    print_status "📱 Frontend: http://localhost:3000"
    print_status "🔧 Backend API: http://localhost:8080/api/v1"
    print_status "📖 API Docs: http://localhost:8080/swagger-ui.html"
    print_status "🗄️ Database: localhost:5433 (numa_dev/numa_dev_password)"
    print_status "📧 MailHog: http://localhost:8025"
    print_status "🔍 pgAdmin: http://localhost:5050"
    echo ""
    print_status "To stop the environment, run: ./scripts/stop-dev.sh"
    print_warning "Keep this terminal open or run processes in background"
}

# Handle script interruption
cleanup() {
    print_status "Shutting down development environment..."
    
    # Kill backend if running
    if [ -f logs/backend.pid ]; then
        kill $(cat logs/backend.pid) 2>/dev/null || true
        rm logs/backend.pid
    fi
    
    # Kill frontend if running
    if [ -f logs/frontend.pid ]; then
        kill $(cat logs/frontend.pid) 2>/dev/null || true
        rm logs/frontend.pid
    fi
    
    # Stop Docker services
    docker-compose -f scripts/docker-compose.dev.yml down
    
    print_success "Development environment stopped"
    exit 0
}

trap cleanup INT TERM

# Run main function
main

# Keep script running
if [ "$1" != "--background" ]; then
    print_status "Press Ctrl+C to stop the development environment"
    wait
fi
