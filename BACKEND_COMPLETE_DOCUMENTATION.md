# Ajalkar Billing System - Admin Panel Backend Complete Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Architecture](#architecture)
4. [Database Schema](#database-schema)
5. [Security Configuration](#security-configuration)
6. [API Endpoints](#api-endpoints)
7. [Setup Instructions](#setup-instructions)
8. [Configuration Details](#configuration-details)
9. [Testing Guide](#testing-guide)
10. [Deployment Guide](#deployment-guide)

---

## Project Overview

The Ajalkar Billing System Admin Panel Backend is a Spring Boot-based REST API that provides comprehensive content management capabilities for the billing system website. It includes:

- **Admin Authentication**: JWT-based secure authentication for admin users
- **Content Management**: CRUD operations for pricing plans, solutions, features, FAQs, announcements, and website settings
- **Public APIs**: Read-only endpoints for the public website to display active content
- **Security**: Role-based access control with Spring Security
- **Database**: MySQL database with JPA/Hibernate ORM

### Key Features
- Secure JWT authentication with BCrypt password encryption
- Separate Admin and Public API endpoints
- Content activation/deactivation management
- Display order management for content
- Audit fields (createdAt, updatedAt)
- CORS configuration for Angular frontend integration
- Global exception handling
- Swagger/OpenAPI documentation

---

## Technology Stack

### Backend Framework
- **Spring Boot**: 3.5.0
- **Java**: 17.0.18
- **Spring Security**: 6.x
- **Spring Data JPA**: 3.x

### Security
- **JWT (JSON Web Tokens)**: jjwt 0.12.3
- **BCrypt**: Spring Security BCryptPasswordEncoder

### Database
- **MySQL**: 8.0.40
- **Hibernate**: 6.6.15.Final
- **HikariCP**: Connection pooling

### API Documentation
- **SpringDoc OpenAPI**: 2.5.0 (Swagger UI)

### Build Tool
- **Maven**: 3.x

---

## Architecture

### Project Structure
```
backend/
├── src/main/java/com/ajalkarbill/website/
│   ├── config/
│   │   ├── SecurityConfig.java          # Spring Security configuration
│   │   ├── CorsConfig.java              # CORS configuration
│   │   └── AdminDataInitializer.java    # Default admin account creation
│   ├── controller/
│   │   ├── admin/
│   │   │   ├── AdminAuthController.java
│   │   │   ├── AdminPricingController.java
│   │   │   ├── AdminSolutionsController.java
│   │   │   ├── AdminFeaturesController.java
│   │   │   ├── AdminFAQController.java
│   │   │   ├── AdminSettingsController.java
│   │   │   └── AdminAnnouncementController.java
│   │   ├── publicapi/
│   │   │   ├── PublicHomeController.java
│   │   │   ├── PublicPricingController.java
│   │   │   ├── PublicSolutionsController.java
│   │   │   ├── PublicFeaturesController.java
│   │   │   ├── PublicFAQController.java
│   │   │   ├── PublicSettingsController.java
│   │   │   └── PublicAnnouncementController.java
│   │   └── RootController.java          # Root endpoint for browser access
│   ├── dto/
│   │   ├── AdminLoginRequest.java
│   │   ├── AdminLoginResponse.java
│   │   ├── AdminDto.java
│   │   ├── PricingPlanRequest.java
│   │   ├── PricingPlanResponse.java
│   │   ├── ModuleRequest.java
│   │   ├── ModuleResponse.java
│   │   ├── FeatureRequest.java
│   │   ├── FeatureResponse.java
│   │   ├── FAQRequest.java
│   │   ├── FAQResponse.java
│   │   ├── WebsiteSettingsRequest.java
│   │   ├── WebsiteSettingsResponse.java
│   │   ├── AnnouncementRequest.java
│   │   └── AnnouncementResponse.java
│   ├── entity/
│   │   ├── Admin.java
│   │   ├── PricingPlan.java
│   │   ├── PricingFeature.java
│   │   ├── Module.java
│   │   ├── Feature.java
│   │   ├── FAQ.java
│   │   ├── WebsiteSettings.java
│   │   └── Announcement.java
│   ├── exception/
│   │   └── GlobalExceptionHandler.java
│   ├── repository/
│   │   ├── AdminRepository.java
│   │   ├── PricingPlanRepository.java
│   │   ├── ModuleRepository.java
│   │   ├── FeatureRepository.java
│   │   ├── FAQRepository.java
│   │   ├── WebsiteSettingsRepository.java
│   │   └── AnnouncementRepository.java
│   ├── security/
│   │   ├── JwtUtil.java
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── AdminUserDetailsService.java
│   │   └── AdminUserDetails.java
│   └── service/
│       ├── PricingPlanService.java
│       ├── ModuleService.java
│       ├── FeatureService.java
│       ├── FAQService.java
│       ├── WebsiteSettingsService.java
│       ├── AnnouncementService.java
│       └── impl/
│           ├── PricingPlanServiceImpl.java
│           ├── ModuleServiceImpl.java
│           ├── FeatureServiceImpl.java
│           ├── FAQServiceImpl.java
│           ├── WebsiteSettingsServiceImpl.java
│           └── AnnouncementServiceImpl.java
└── src/main/resources/
    └── application.properties
```

---

## Database Schema

### Admin Table
```sql
CREATE TABLE admins (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    role VARCHAR(50) DEFAULT 'ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Pricing Plans Table
```sql
CREATE TABLE pricing_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price VARCHAR(50) NOT NULL,
    billing_period VARCHAR(50) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    is_popular BOOLEAN DEFAULT FALSE,
    cta_text VARCHAR(255),
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Pricing Features Table
```sql
CREATE TABLE pricing_features (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    feature_name VARCHAR(255) NOT NULL,
    pricing_plan_id BIGINT,
    FOREIGN KEY (pricing_plan_id) REFERENCES pricing_plans(id)
);
```

### Modules (Solutions) Table
```sql
CREATE TABLE modules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    icon VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Features Table
```sql
CREATE TABLE features (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    icon VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### FAQs Table
```sql
CREATE TABLE faqs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Website Settings Table
```sql
CREATE TABLE website_settings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    website_name VARCHAR(255),
    logo_url VARCHAR(500),
    favicon_url VARCHAR(500),
    contact_email VARCHAR(255),
    contact_phone VARCHAR(50),
    address TEXT,
    social_facebook VARCHAR(500),
    social_twitter VARCHAR(500),
    social_linkedin VARCHAR(500),
    social_instagram VARCHAR(500),
    footer_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Announcements Table
```sql
CREATE TABLE announcements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    button_text VARCHAR(255),
    button_link VARCHAR(500),
    image_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    start_date DATE,
    end_date DATE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## Security Configuration

### JWT Authentication Flow

1. **Login Process**
   - Admin sends credentials to `/api/admin/auth/login`
   - Server validates credentials using BCrypt
   - Server generates JWT token with 24-hour expiration
   - Token is returned in response

2. **Token Validation**
   - Each subsequent request includes JWT in `Authorization: Bearer {token}` header
   - `JwtAuthenticationFilter` intercepts requests
   - Token is validated using `JwtUtil`
   - User details are loaded from database
   - Authentication context is set in SecurityContextHolder

3. **Authorization**
   - Public endpoints: No authentication required
   - Admin endpoints: JWT token required
   - Role-based access: Only ADMIN role can access admin endpoints

### Security Configuration Details

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/", "/error").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/auth/login").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Admin endpoints - require authentication
                .requestMatchers("/api/admin/**").authenticated()
                
                // Deny all other requests
                .anyRequest().denyAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

### JWT Configuration

**Properties (application.properties):**
```properties
jwt.secret=YourSuperSecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLong
jwt.expiration=86400000  # 24 hours in milliseconds
```

**Token Structure:**
- Header: Algorithm and token type
- Payload: Username (email), issued at, expiration
- Signature: HMAC-SHA384 with secret key

### CORS Configuration

**Allowed Origins:**
- `http://localhost:4200` (Public Angular App)
- `http://localhost:4300` (Admin Angular App)

**Allowed Methods:**
- GET, POST, PUT, DELETE, OPTIONS

**Allowed Headers:**
- Authorization, Content-Type, Accept, Origin, Access-Control-Request-Method, Access-Control-Request-Headers

---

## API Endpoints

### Base URL
```
http://localhost:8080
```

### Admin Authentication Endpoints

#### Login
```http
POST /api/admin/auth/login
Content-Type: application/json

Request Body:
{
  "email": "admin@ajalkarbill.com",
  "password": "Admin@123"
}

Response (200 OK):
{
  "success": true,
  "message": "Admin login successful",
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "admin": {
    "id": 1,
    "email": "admin@ajalkarbill.com",
    "name": "Super Admin",
    "role": "ADMIN"
  }
}
```

#### Logout
```http
POST /api/admin/auth/logout
Authorization: Bearer {token}

Response (200 OK):
{
  "success": true,
  "message": "Admin logged out successfully"
}
```

#### Get Current Admin
```http
GET /api/admin/auth/me
Authorization: Bearer {token}

Response (200 OK):
{
  "id": 1,
  "email": "admin@ajalkarbill.com",
  "name": "Super Admin",
  "role": "ADMIN"
}
```

### Admin Pricing Management Endpoints

#### Get All Pricing Plans
```http
GET /api/admin/pricing
Authorization: Bearer {token}

Response (200 OK):
[
  {
    "id": 1,
    "name": "Premium",
    "price": "₹1299",
    "billingPeriod": "month",
    "description": "Best for growing businesses",
    "isActive": true,
    "isPopular": true,
    "ctaText": "Get Started",
    "displayOrder": 1,
    "features": ["Invoice Management", "Reports"],
    "createdAt": "2026-09-03T10:35:00",
    "updatedAt": "2026-09-03T10:35:00"
  }
]
```

#### Create Pricing Plan
```http
POST /api/admin/pricing
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "name": "Basic",
  "price": "₹499",
  "billingPeriod": "month",
  "description": "Best for startups",
  "isActive": true,
  "isPopular": false,
  "ctaText": "Get Started",
  "displayOrder": 2,
  "features": ["Invoice Management", "Reports"]
}

Response (201 Created):
{
  "id": 2,
  "name": "Basic",
  "price": "₹499",
  "billingPeriod": "month",
  "description": "Best for startups",
  "isActive": true,
  "isPopular": false,
  "ctaText": "Get Started",
  "displayOrder": 2,
  "features": [],
  "createdAt": "2026-09-03T10:45:00",
  "updatedAt": "2026-09-03T10:45:00"
}
```

#### Update Pricing Plan
```http
PUT /api/admin/pricing/{id}
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "name": "Basic",
  "price": "₹599",
  "billingPeriod": "month",
  "description": "Best for startups",
  "isActive": true,
  "isPopular": false,
  "ctaText": "Get Started",
  "displayOrder": 2,
  "features": ["Invoice Management", "Reports", "Multi-user"]
}

Response (200 OK):
{
  "id": 2,
  "name": "Basic",
  "price": "₹599",
  "billingPeriod": "month",
  "description": "Best for startups",
  "isActive": true,
  "isPopular": false,
  "ctaText": "Get Started",
  "displayOrder": 2,
  "features": ["Invoice Management", "Reports", "Multi-user"],
  "createdAt": "2026-09-03T10:45:00",
  "updatedAt": "2026-09-03T10:50:00"
}
```

#### Update Pricing Plan Status
```http
PATCH /api/admin/pricing/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "isActive": false
}

Response (200 OK):
{
  "id": 2,
  "name": "Basic",
  "price": "₹599",
  "billingPeriod": "month",
  "description": "Best for startups",
  "isActive": false,
  "isPopular": false,
  "ctaText": "Get Started",
  "displayOrder": 2,
  "features": ["Invoice Management", "Reports", "Multi-user"],
  "createdAt": "2026-09-03T10:45:00",
  "updatedAt": "2026-09-03T10:55:00"
}
```

#### Delete Pricing Plan
```http
DELETE /api/admin/pricing/{id}
Authorization: Bearer {token}

Response (204 No Content)
```

### Admin Solutions (Modules) Management Endpoints

#### Get All Solutions
```http
GET /api/admin/solutions
Authorization: Bearer {token}

Response (200 OK):
[
  {
    "id": 1,
    "name": "Billing",
    "description": "Create professional invoices, quotes, and delivery challans.",
    "icon": "bi-file-earmark-text",
    "isActive": true,
    "displayOrder": 0,
    "createdAt": "2026-09-03T10:35:00",
    "updatedAt": "2026-09-03T10:35:00"
  }
]
```

#### Create Solution
```http
POST /api/admin/solutions
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "name": "Invoice Management",
  "description": "Create and manage invoices easily",
  "icon": "invoice-icon",
  "isActive": true,
  "displayOrder": 1
}

Response (201 Created):
{
  "id": 7,
  "name": "Invoice Management",
  "description": "Create and manage invoices easily",
  "icon": "invoice-icon",
  "isActive": true,
  "displayOrder": 1,
  "createdAt": "2026-09-03T10:45:00",
  "updatedAt": "2026-09-03T10:45:00"
}
```

#### Update Solution
```http
PUT /api/admin/solutions/{id}
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "name": "Invoice Management",
  "description": "Create and manage invoices easily with advanced features",
  "icon": "invoice-icon",
  "isActive": true,
  "displayOrder": 1
}

Response (200 OK):
{
  "id": 7,
  "name": "Invoice Management",
  "description": "Create and manage invoices easily with advanced features",
  "icon": "invoice-icon",
  "isActive": true,
  "displayOrder": 1,
  "createdAt": "2026-09-03T10:45:00",
  "updatedAt": "2026-09-03T10:50:00"
}
```

#### Update Solution Status
```http
PATCH /api/admin/solutions/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "isActive": false
}

Response (200 OK)
```

#### Delete Solution
```http
DELETE /api/admin/solutions/{id}
Authorization: Bearer {token}

Response (204 No Content)
```

### Admin Features Management Endpoints

#### Get All Features
```http
GET /api/admin/features
Authorization: Bearer {token}

Response (200 OK):
[
  {
    "id": 1,
    "title": "Fast Billing",
    "description": "Generate GST and non-GST bills in seconds with keyboard shortcuts.",
    "icon": "bi-receipt",
    "isActive": true,
    "displayOrder": 0,
    "createdAt": "2026-09-03T10:35:00",
    "updatedAt": "2026-09-03T10:35:00"
  }
]
```

#### Create Feature
```http
POST /api/admin/features
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "title": "Easy to Use",
  "description": "Simple and intuitive interface",
  "icon": "check-icon",
  "isActive": true,
  "displayOrder": 1
}

Response (201 Created):
{
  "id": 7,
  "title": "Easy to Use",
  "description": "Simple and intuitive interface",
  "icon": "check-icon",
  "isActive": true,
  "displayOrder": 1,
  "createdAt": "2026-09-03T10:45:00",
  "updatedAt": "2026-09-03T10:45:00"
}
```

#### Update Feature
```http
PUT /api/admin/features/{id}
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "title": "Easy to Use",
  "description": "Simple and intuitive interface with modern design",
  "icon": "check-icon",
  "isActive": true,
  "displayOrder": 1
}

Response (200 OK)
```

#### Update Feature Status
```http
PATCH /api/admin/features/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "isActive": false
}

Response (200 OK)
```

#### Delete Feature
```http
DELETE /api/admin/features/{id}
Authorization: Bearer {token}

Response (204 No Content)
```

### Admin FAQ Management Endpoints

#### Get All FAQs
```http
GET /api/admin/faqs
Authorization: Bearer {token}

Response (200 OK):
[
  {
    "id": 1,
    "question": "What is AjalkarBill?",
    "answer": "AjalkarBill is a comprehensive billing, inventory, and accounting software...",
    "isActive": true,
    "displayOrder": 0,
    "createdAt": "2026-09-03T10:35:00",
    "updatedAt": "2026-09-03T10:35:00"
  }
]
```

#### Create FAQ
```http
POST /api/admin/faqs
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "question": "How do I create an invoice?",
  "answer": "Go to the Invoice section and click on Create New Invoice.",
  "isActive": true,
  "displayOrder": 1
}

Response (201 Created)
```

#### Update FAQ
```http
PUT /api/admin/faqs/{id}
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "question": "How do I create an invoice?",
  "answer": "Go to the Invoice section and click on Create New Invoice. You can also use keyboard shortcuts.",
  "isActive": true,
  "displayOrder": 1
}

Response (200 OK)
```

#### Update FAQ Status
```http
PATCH /api/admin/faqs/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "isActive": false
}

Response (200 OK)
```

#### Delete FAQ
```http
DELETE /api/admin/faqs/{id}
Authorization: Bearer {token}

Response (204 No Content)
```

### Admin Website Settings Endpoints

#### Get Website Settings
```http
GET /api/admin/settings
Authorization: Bearer {token}

Response (200 OK):
{
  "id": 1,
  "websiteName": "Ajalkar Billing System",
  "logoUrl": "https://example.com/logo.png",
  "faviconUrl": null,
  "contactEmail": "contact@ajalkarbill.com",
  "contactPhone": "+91 9876543210",
  "address": "Mumbai, India",
  "socialFacebook": null,
  "socialTwitter": null,
  "socialLinkedin": null,
  "socialInstagram": null,
  "footerText": null,
  "createdAt": "2026-09-03T10:35:00",
  "updatedAt": "2026-09-03T10:35:00"
}
```

#### Update Website Settings
```http
PUT /api/admin/settings
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "websiteName": "Ajalkar Billing System",
  "logoUrl": "https://example.com/logo.png",
  "contactEmail": "contact@ajalkarbill.com",
  "contactPhone": "+91 9876543210",
  "address": "Mumbai, India"
}

Response (200 OK)
```

### Admin Announcements Endpoints

#### Get All Announcements
```http
GET /api/admin/announcements
Authorization: Bearer {token}

Response (200 OK):
[
  {
    "id": 1,
    "title": "New Feature Release",
    "description": "We have launched a new feature...",
    "buttonText": "Learn More",
    "buttonLink": "https://example.com",
    "imageUrl": "https://example.com/banner.jpg",
    "isActive": true,
    "startDate": "2026-09-01",
    "endDate": "2026-09-30",
    "displayOrder": 1,
    "createdAt": "2026-09-03T10:35:00",
    "updatedAt": "2026-09-03T10:35:00"
  }
]
```

#### Create Announcement
```http
POST /api/admin/announcements
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "title": "Special Offer",
  "description": "Get 20% off on all plans",
  "buttonText": "Claim Now",
  "buttonLink": "https://example.com/offers",
  "imageUrl": "https://example.com/offer.jpg",
  "isActive": true,
  "startDate": "2026-09-01",
  "endDate": "2026-09-30",
  "displayOrder": 1
}

Response (201 Created)
```

#### Update Announcement
```http
PUT /api/admin/announcements/{id}
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "title": "Special Offer",
  "description": "Get 25% off on all plans",
  "buttonText": "Claim Now",
  "buttonLink": "https://example.com/offers",
  "imageUrl": "https://example.com/offer.jpg",
  "isActive": true,
  "startDate": "2026-09-01",
  "endDate": "2026-09-30",
  "displayOrder": 1
}

Response (200 OK)
```

#### Delete Announcement
```http
DELETE /api/admin/announcements/{id}
Authorization: Bearer {token}

Response (204 No Content)
```

### Public API Endpoints

All public endpoints do not require authentication.

#### Home Page Content
```http
GET /api/public/home

Response (200 OK):
{
  "settings": {
    "id": 1,
    "websiteName": "Ajalkar Billing System",
    "logoUrl": "https://example.com/logo.png",
    "contactEmail": "contact@ajalkarbill.com",
    "contactPhone": "+91 9876543210",
    "address": "Mumbai, India"
  },
  "announcements": [],
  "features": [
    {
      "id": 1,
      "title": "Fast Billing",
      "description": "Generate GST and non-GST bills in seconds with keyboard shortcuts.",
      "icon": "bi-receipt",
      "isActive": true,
      "displayOrder": 0
    }
  ],
  "solutions": [
    {
      "id": 1,
      "name": "Billing",
      "description": "Create professional invoices, quotes, and delivery challans.",
      "icon": "bi-file-earmark-text",
      "isActive": true,
      "displayOrder": 0
    }
  ]
}
```

#### Public Pricing Plans
```http
GET /api/public/pricing

Response (200 OK):
[
  {
    "id": 2,
    "name": "Basic",
    "price": "₹499",
    "billingPeriod": "month",
    "description": "Best for startups",
    "isActive": true,
    "isPopular": false,
    "ctaText": "Get Started",
    "displayOrder": 2,
    "features": ["Invoice Management", "Reports"]
  }
]
```

#### Public Solutions
```http
GET /api/public/solutions

Response (200 OK):
[
  {
    "id": 1,
    "name": "Billing",
    "description": "Create professional invoices, quotes, and delivery challans.",
    "icon": "bi-file-earmark-text",
    "isActive": true,
    "displayOrder": 0
  }
]
```

#### Public Features
```http
GET /api/public/features

Response (200 OK):
[
  {
    "id": 1,
    "title": "Fast Billing",
    "description": "Generate GST and non-GST bills in seconds with keyboard shortcuts.",
    "icon": "bi-receipt",
    "isActive": true,
    "displayOrder": 0
  }
]
```

#### Public FAQs
```http
GET /api/public/faqs

Response (200 OK):
[
  {
    "id": 1,
    "question": "What is AjalkarBill?",
    "answer": "AjalkarBill is a comprehensive billing, inventory, and accounting software...",
    "isActive": true,
    "displayOrder": 0
  }
]
```

#### Public Website Settings
```http
GET /api/public/settings

Response (200 OK):
{
  "id": 1,
  "websiteName": "Ajalkar Billing System",
  "logoUrl": "https://example.com/logo.png",
  "contactEmail": "contact@ajalkarbill.com",
  "contactPhone": "+91 9876543210",
  "address": "Mumbai, India"
}
```

#### Public Announcements
```http
GET /api/public/announcements

Response (200 OK):
[
  {
    "id": 1,
    "title": "New Feature Release",
    "description": "We have launched a new feature...",
    "buttonText": "Learn More",
    "buttonLink": "https://example.com",
    "imageUrl": "https://example.com/banner.jpg",
    "isActive": true,
    "startDate": "2026-09-01",
    "endDate": "2026-09-30",
    "displayOrder": 1
  }
]
```

### Root Endpoint
```http
GET /

Response (200 OK):
HTML page with API documentation and links
```

---

## Setup Instructions

### Prerequisites

1. **Java Development Kit (JDK) 17**
   ```bash
   java -version
   # Should show: openjdk version "17.0.18" or similar
   ```

2. **Apache Maven 3.x**
   ```bash
   mvn -version
   # Should show: Apache Maven 3.x.x
   ```

3. **MySQL 8.0+**
   ```bash
   mysql --version
   # Should show: mysql  Ver 8.0.x
   ```

### Database Setup

1. **Create Database**
   ```sql
   CREATE DATABASE websitedb;
   ```

2. **Create User and Grant Privileges**
   ```sql
   CREATE USER 'company'@'localhost' IDENTIFIED BY 'company@123';
   GRANT ALL PRIVILEGES ON websitedb.* TO 'company'@'localhost';
   FLUSH PRIVILEGES;
   ```

### Application Configuration

1. **Clone the Repository**
   ```bash
   cd "h:\Billing System Project\Ajalkar-Billing-Website\backend"
   ```

2. **Configure application.properties**
   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/websitedb
   spring.datasource.username=company
   spring.datasource.password=company@123
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

   # JPA Configuration
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

   # Server Configuration
   server.port=8080

   # JWT Configuration
   jwt.secret=YourSuperSecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLong
   jwt.expiration=86400000

   # Mail Configuration (Optional)
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   spring.mail.properties.mail.smtp.starttls.required=true
   ```

### Build and Run

1. **Build the Project**
   ```bash
   mvn clean install
   ```

2. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

   Or with JAVA_HOME set:
   ```bash
   $env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.18"
   mvn spring-boot:run
   ```

3. **Verify Startup**
   - Application should start on port 8080
   - Default admin account will be created automatically
   - Check console for: "Default admin account created successfully!"

### Access the Application

- **Root Page**: http://localhost:8080/
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **API Docs**: http://localhost:8080/v3/api-docs

---

## Configuration Details

### application.properties Explained

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/websitedb
# MySQL database URL with database name

spring.datasource.username=company
# Database username

spring.datasource.password=company@123
# Database password

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# MySQL JDBC driver class

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
# Automatically updates database schema based on entities
# Options: validate, update, create, create-drop, none

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
# Hibernate dialect for MySQL

# Server Configuration
server.port=8080
# Server port (default: 8080)

# JWT Configuration
jwt.secret=YourSuperSecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLong
# Secret key for JWT token signing (must be at least 256 bits)

jwt.expiration=86400000
# JWT token expiration time in milliseconds (24 hours)

# Mail Configuration (Optional)
spring.mail.host=smtp.gmail.com
# SMTP server host

spring.mail.port=587
# SMTP server port

spring.mail.username=your-email@gmail.com
# SMTP username

spring.mail.password=your-app-password
# SMTP password (use app-specific password for Gmail)

spring.mail.properties.mail.smtp.auth=true
# Enable SMTP authentication

spring.mail.properties.mail.smtp.starttls.enable=true
# Enable STARTTLS

spring.mail.properties.mail.smtp.starttls.required=true
# Require STARTTLS
```

### Environment-Specific Configuration

For different environments (dev, test, prod), create separate property files:

- `application-dev.properties` - Development
- `application-test.properties` - Testing
- `application-prod.properties` - Production

Activate profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Testing Guide

### Manual Testing with cURL

#### Login
```bash
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@ajalkarbill.com","password":"Admin@123"}'
```

#### Get Current Admin
```bash
curl -X GET http://localhost:8080/api/admin/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Create Pricing Plan
```bash
curl -X POST http://localhost:8080/api/admin/pricing \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Basic","price":"₹499","billingPeriod":"month","description":"Best for startups","isActive":true,"isPopular":false,"ctaText":"Get Started","displayOrder":2,"features":["Invoice Management","Reports"]}'
```

#### Get Public Pricing
```bash
curl -X GET http://localhost:8080/api/public/pricing
```

### Testing with Postman

1. **Import Collection**
   - Create a new collection
   - Add endpoints from this documentation

2. **Environment Variables**
   - `base_url`: http://localhost:8080
   - `token`: (set after login)

3. **Test Sequence**
   - Login → Get token → Use token for admin endpoints
   - Test public endpoints without token

### Automated Testing

#### Unit Tests
```bash
mvn test
```

#### Integration Tests
```bash
mvn verify
```

### API Test Results Summary

All APIs have been tested and verified:

**Admin Authentication APIs:**
- ✅ POST /api/admin/auth/login
- ✅ POST /api/admin/auth/logout
- ✅ GET /api/admin/auth/me

**Admin CRUD APIs:**
- ✅ GET/POST/PUT/DELETE /api/admin/pricing
- ✅ GET/POST/PUT/DELETE /api/admin/solutions
- ✅ GET/POST/PUT/DELETE /api/admin/features
- ✅ GET/POST/PUT/DELETE /api/admin/faqs
- ✅ GET/PUT /api/admin/settings
- ✅ GET/POST/PUT/DELETE /api/admin/announcements

**Public APIs:**
- ✅ GET /api/public/home
- ✅ GET /api/public/pricing
- ✅ GET /api/public/solutions
- ✅ GET /api/public/features
- ✅ GET /api/public/faqs
- ✅ GET /api/public/settings
- ✅ GET /api/public/announcements

---

## Deployment Guide

### Production Deployment Checklist

1. **Security**
   - Change JWT secret to a strong, random value
   - Change default admin password after first login
   - Enable HTTPS/SSL
   - Configure firewall rules
   - Use environment variables for sensitive data

2. **Database**
   - Use production MySQL instance
   - Configure connection pool settings
   - Enable database backups
   - Set appropriate database user permissions

3. **Application**
   - Set `spring.jpa.hibernate.ddl-auto=validate` or `none`
   - Configure appropriate memory settings
   - Enable logging for monitoring
   - Set up health check endpoints

4. **Environment Variables**
   ```bash
   export DB_URL=jdbc:mysql://prod-db:3306/websitedb
   export DB_USERNAME=prod_user
   export DB_PASSWORD=secure_password
   export JWT_SECRET=your-production-secret-key
   ```

### Docker Deployment

**Dockerfile:**
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/website-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Build and Run:**
```bash
# Build
docker build -t ajalkar-billing-backend .

# Run
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/websitedb \
  -e SPRING_DATASOURCE_USERNAME=company \
  -e SPRING_DATASOURCE_PASSWORD=company@123 \
  ajalkar-billing-backend
```

### Cloud Deployment

**AWS EC2:**
1. Launch EC2 instance with Java 17
2. Install MySQL
3. Deploy JAR file
4. Configure security groups
5. Set up domain and SSL

**AWS RDS + Elastic Beanstalk:**
1. Create RDS MySQL instance
2. Deploy to Elastic Beanstalk
3. Configure environment variables
4. Set up load balancer

**Google Cloud Platform:**
1. Use Cloud SQL for MySQL
2. Deploy to App Engine or Cloud Run
3. Configure VPC and networking

---

## Default Credentials

### Default Admin Account
- **Email**: admin@ajalkarbill.com
- **Password**: Admin@123
- **Role**: ADMIN

⚠️ **Important**: Change the default password after first login!

---

## Error Handling

### Global Exception Handler

The application uses a global exception handler to provide consistent error responses:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        // Returns 404 with error details
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Returns 400 with validation error details
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        // Returns 500 with error details
    }
}
```

### Common Error Responses

**401 Unauthorized:**
```json
{
  "timestamp": "2026-09-03T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or expired token",
  "path": "/api/admin/pricing"
}
```

**403 Forbidden:**
```json
{
  "timestamp": "2026-09-03T10:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied",
  "path": "/api/admin/pricing"
}
```

**404 Not Found:**
```json
{
  "timestamp": "2026-09-03T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found with id: 999",
  "path": "/api/admin/pricing/999"
}
```

**400 Bad Request (Validation Error):**
```json
{
  "timestamp": "2026-09-03T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "name",
      "message": "Name is required"
    }
  ]
}
```

---

## Monitoring and Logging

### Application Logs

Logs are configured to output to console. For production, configure file logging:

**logback-spring.xml:**
```xml
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/application.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/application-%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

### Health Check Endpoint

Spring Boot Actuator provides health checks:

```properties
management.endpoints.web.exposure.include=health,info,metrics
```

Access: http://localhost:8080/actuator/health

---

## API Versioning

Current version: v1

All endpoints are currently version 1. Future versions will include version in URL path:

```
/api/v1/admin/auth/login
/api/v1/public/pricing
```

---

## Support and Maintenance

### Common Issues

**Issue**: Connection refused to MySQL
**Solution**: Check MySQL service is running and credentials are correct

**Issue**: JWT token expired
**Solution**: Token expires after 24 hours, login again to get new token

**Issue**: CORS errors in frontend
**Solution**: Ensure frontend origin is configured in CorsConfig

**Issue**: 403 Forbidden on admin endpoints
**Solution**: Ensure JWT token is included in Authorization header

### Backup and Recovery

**Database Backup:**
```bash
mysqldump -u company -p websitedb > backup.sql
```

**Database Restore:**
```bash
mysql -u company -p websitedb < backup.sql
```

---

## Contact Information

For support or questions about the Admin Panel Backend:

- **Project**: Ajalkar Billing System
- **Backend Version**: 1.0.0
- **Documentation Version**: 1.0.0
- **Last Updated**: September 3, 2026

---

## Appendix

### A. Complete API Endpoint List

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | /api/admin/auth/login | No | Admin login |
| POST | /api/admin/auth/logout | Yes | Admin logout |
| GET | /api/admin/auth/me | Yes | Get current admin |
| GET | /api/admin/pricing | Yes | Get all pricing plans |
| POST | /api/admin/pricing | Yes | Create pricing plan |
| PUT | /api/admin/pricing/{id} | Yes | Update pricing plan |
| PATCH | /api/admin/pricing/{id}/status | Yes | Update pricing status |
| DELETE | /api/admin/pricing/{id} | Yes | Delete pricing plan |
| GET | /api/admin/solutions | Yes | Get all solutions |
| POST | /api/admin/solutions | Yes | Create solution |
| PUT | /api/admin/solutions/{id} | Yes | Update solution |
| PATCH | /api/admin/solutions/{id}/status | Yes | Update solution status |
| DELETE | /api/admin/solutions/{id} | Yes | Delete solution |
| GET | /api/admin/features | Yes | Get all features |
| POST | /api/admin/features | Yes | Create feature |
| PUT | /api/admin/features/{id} | Yes | Update feature |
| PATCH | /api/admin/features/{id}/status | Yes | Update feature status |
| DELETE | /api/admin/features/{id} | Yes | Delete feature |
| GET | /api/admin/faqs | Yes | Get all FAQs |
| POST | /api/admin/faqs | Yes | Create FAQ |
| PUT | /api/admin/faqs/{id} | Yes | Update FAQ |
| PATCH | /api/admin/faqs/{id}/status | Yes | Update FAQ status |
| DELETE | /api/admin/faqs/{id} | Yes | Delete FAQ |
| GET | /api/admin/settings | Yes | Get website settings |
| PUT | /api/admin/settings | Yes | Update website settings |
| GET | /api/admin/announcements | Yes | Get all announcements |
| POST | /api/admin/announcements | Yes | Create announcement |
| PUT | /api/admin/announcements/{id} | Yes | Update announcement |
| DELETE | /api/admin/announcements/{id} | Yes | Delete announcement |
| GET | /api/public/home | No | Get home page content |
| GET | /api/public/pricing | No | Get public pricing |
| GET | /api/public/solutions | No | Get public solutions |
| GET | /api/public/features | No | Get public features |
| GET | /api/public/faqs | No | Get public FAQs |
| GET | /api/public/settings | No | Get public settings |
| GET | /api/public/announcements | No | Get public announcements |
| GET | / | No | Root endpoint |

### B. HTTP Status Codes

| Code | Description |
|------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 204 | No Content - Successful deletion |
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Authentication required |
| 403 | Forbidden - Access denied |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server error |

### C. Data Validation Rules

**Pricing Plan:**
- name: Required, max 255 characters
- price: Required, max 50 characters
- billingPeriod: Required, max 50 characters
- description: Optional, text
- isActive: Boolean, default true
- isPopular: Boolean, default false
- displayOrder: Integer, default 0

**Module/Solution:**
- name: Required, max 255 characters
- description: Optional, text
- icon: Optional, max 255 characters
- isActive: Boolean, default true
- displayOrder: Integer, default 0

**Feature:**
- title: Required, max 255 characters
- description: Optional, text
- icon: Optional, max 255 characters
- isActive: Boolean, default true
- displayOrder: Integer, default 0

**FAQ:**
- question: Required, text
- answer: Required, text
- isActive: Boolean, default true
- displayOrder: Integer, default 0

**Website Settings:**
- websiteName: Optional, max 255 characters
- logoUrl: Optional, max 500 characters
- contactEmail: Optional, email format
- contactPhone: Optional, max 50 characters
- address: Optional, text

**Announcement:**
- title: Required, max 255 characters
- description: Optional, text
- buttonText: Optional, max 255 characters
- buttonLink: Optional, max 500 characters
- imageUrl: Optional, max 500 characters
- isActive: Boolean, default true
- startDate: Optional, date
- endDate: Optional, date
- displayOrder: Integer, default 0

---

**End of Documentation**
