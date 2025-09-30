# Numa - Multi-Tenant Restaurant Ordering Platform

A comprehensive restaurant ordering and management platform that enables QR-based ordering, group sessions, bill splitting, and real-time management.

## 🚀 Features

### Guest Experience
- **QR Code Ordering**: Guests scan QR codes at tables to access menus instantly
- **Group Sessions**: Multiple guests can join the same order session
- **Interactive Menu**: Browse categories, search items, filter by dietary preferences
- **Real-time Ordering**: Place orders that sync instantly with restaurant systems
- **Bill Splitting**: Split bills by item, percentage, or custom amounts
- **Waiter Requests**: Call waiters directly through the app

### Restaurant Management
- **Multi-tenant Architecture**: Support for multiple restaurants with isolated data
- **Menu Management**: Create categories, items, variations, and pricing
- **Table Management**: QR code generation and table status tracking
- **Order Management**: Real-time order tracking and status updates
- **Session Management**: Monitor active dining sessions
- **Analytics & Reporting**: Comprehensive insights into sales and operations

### Technical Features
- **Mobile-first Design**: Optimized for mobile devices with PWA capabilities
- **Real-time Updates**: WebSocket-based real-time synchronization
- **Multi-language Support**: Internationalization ready
- **Offline Capability**: Works offline with data synchronization
- **Security**: JWT-based authentication with multi-tenant isolation

## 🏗️ Architecture

### Backend (Spring Boot)
```
src/main/java/com/numa/
├── config/              # Configuration classes
├── controller/          # REST API controllers
├── domain/
│   ├── entity/         # JPA entities
│   └── enums/          # Enumerations
├── dto/
│   ├── request/        # Request DTOs
│   └── response/       # Response DTOs
├── exception/          # Custom exceptions
├── mapper/             # MapStruct mappers
├── repository/         # JPA repositories
├── security/           # Security configuration and JWT
└── service/            # Business logic services
```

### Frontend (Next.js)
```
src/
├── app/                # Next.js App Router pages
├── components/
│   ├── guest/          # Guest-facing components
│   ├── admin/          # Admin dashboard components
│   └── ui/             # Reusable UI components
├── services/           # API services and utilities
├── store/              # State management (Zustand)
├── types/              # TypeScript type definitions
├── utils/              # Utility functions
└── styles/             # CSS and styling
```

### Database Schema
- **Multi-tenant**: Restaurants are isolated with proper data separation
- **Scalable**: Optimized for high-volume operations
- **Flexible**: Supports various order types and payment methods
- **Analytics**: Built-in tracking for business intelligence

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.2
- **Database**: PostgreSQL with Liquibase migrations
- **Security**: Spring Security with JWT
- **API Documentation**: OpenAPI/Swagger
- **Caching**: Redis (optional)
- **Message Queue**: RabbitMQ (optional)

### Frontend
- **Framework**: Next.js 14 with App Router
- **Styling**: Tailwind CSS
- **State Management**: Zustand + React Query
- **UI Components**: Headless UI + Custom components
- **Forms**: React Hook Form with Yup validation
- **PWA**: Next-PWA for offline capability

### DevOps & Infrastructure
- **Containerization**: Docker & Docker Compose
- **CI/CD**: GitHub Actions
- **Monitoring**: Actuator endpoints
- **Logging**: Structured logging with correlation IDs

## 🚦 Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 13+
- Docker (optional)

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-org/numa.git
   cd numa
   ```

2. **Database Setup**
   ```bash
   # Create PostgreSQL database
   createdb numa_db
   
   # Update application.yml with your database credentials
   ```

3. **Run the application**
   ```bash
   # Using Maven
   ./mvnw spring-boot:run
   
   # Using Docker
   docker-compose up -d
   ```

4. **API Documentation**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/v3/api-docs

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Environment configuration**
   ```bash
   cp .env.example .env.local
   # Update environment variables
   ```

4. **Run development server**
   ```bash
   npm run dev
   ```

5. **Access the application**
   - Guest Interface: http://localhost:3000
   - Admin Dashboard: http://localhost:3000/admin

## 📱 Usage

### Restaurant Onboarding
1. Visit `/restaurants/register`
2. Fill in restaurant details and owner information
3. Verify email and complete setup
4. Configure menu, tables, and settings

### Guest Ordering Flow
1. Scan QR code at restaurant table
2. Join or create dining session
3. Browse menu and add items to cart
4. Place order and track status
5. Split bill and complete payment

### Restaurant Management
1. Login to admin dashboard
2. Manage menu items and categories
3. Monitor live orders and sessions
4. Generate QR codes for tables
5. View analytics and reports

## 🔧 Configuration

### Environment Variables

#### Backend (.env or application.yml)
```yaml
# Database
DB_USERNAME=numa_user
DB_PASSWORD=numa_password

# JWT
JWT_SECRET=your-secret-key

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:3000

# File Storage
FILE_STORAGE_PATH=./uploads

# Email (optional)
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=your-email
MAIL_PASSWORD=your-password
```

#### Frontend (.env.local)
```bash
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
NEXT_PUBLIC_APP_URL=http://localhost:3000
GOOGLE_SITE_VERIFICATION=your-verification-code
```

## 🧪 Testing

### Backend Testing
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=RestaurantServiceTest

# Integration tests
./mvnw test -Dtest=**/*IT
```

### Frontend Testing
```bash
# Run tests
npm test

# Run tests in watch mode
npm run test:watch

# Generate coverage report
npm run test:coverage
```

## 📊 Monitoring & Analytics

### Application Metrics
- **Health Checks**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Prometheus**: `/actuator/prometheus`

### Business Analytics
- Order volume and revenue tracking
- Popular items and category analysis
- Table utilization rates
- Peak hours identification
- Customer behavior insights

## 🔐 Security

### Authentication & Authorization
- JWT-based authentication
- Role-based access control (RBAC)
- Multi-tenant data isolation
- Session management for guests

### Data Protection
- Input validation and sanitization
- SQL injection prevention
- XSS protection
- CSRF protection
- Rate limiting

### API Security
- HTTPS enforcement
- CORS configuration
- Request/response logging
- Error handling without data exposure

## 🚀 Deployment

### Production Deployment

1. **Build applications**
   ```bash
   # Backend
   ./mvnw clean package -DskipTests
   
   # Frontend
   cd frontend && npm run build
   ```

2. **Docker deployment**
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

3. **Environment setup**
   - Configure production database
   - Set up SSL certificates
   - Configure CDN for static assets
   - Set up monitoring and logging

### Scaling Considerations
- Database read replicas
- Redis caching layer
- Load balancer configuration
- CDN for static assets
- Horizontal scaling with Docker Swarm/Kubernetes

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines
- Follow conventional commit messages
- Write tests for new features
- Update documentation
- Ensure code passes linting
- Follow security best practices

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


---

**Numa** - Transforming restaurant operations with modern technology 🍽️✨
