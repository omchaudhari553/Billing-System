# Admin Panel Backend - API Documentation & Integration Guide

## Overview

This Spring Boot backend provides a complete Admin Panel API for managing the Billing Website content. Admin changes made through the Admin Panel are saved in the database and automatically reflected on the public website.

## Default Admin Credentials

```
Email: admin@ajalkarbill.com
Password: Admin@123
```

**IMPORTANT:** Change the default password after first login!

## Architecture

```
Admin Panel (Angular - Port 4300)
    ↓ JWT Authentication
Spring Boot Backend (Port 8080)
    ↓
MySQL Database
    ↓
Public Website (Angular - Port 4200)
```

## API Endpoints

### Admin Authentication APIs

#### Login
```http
POST /api/admin/auth/login
Content-Type: application/json

{
  "email": "admin@ajalkarbill.com",
  "password": "Admin@123"
}

Response:
{
  "success": true,
  "message": "Admin login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
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

Response:
{
  "success": true,
  "message": "Admin logged out successfully"
}
```

#### Get Current Admin
```http
GET /api/admin/auth/me
Authorization: Bearer {token}

Response:
{
  "id": 1,
  "email": "admin@ajalkarbill.com",
  "name": "Super Admin",
  "role": "ADMIN"
}
```

### Admin Pricing APIs

#### Get All Pricing Plans
```http
GET /api/admin/pricing
Authorization: Bearer {token}
```

#### Get Pricing Plan by ID
```http
GET /api/admin/pricing/{id}
Authorization: Bearer {token}
```

#### Create Pricing Plan
```http
POST /api/admin/pricing
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Premium",
  "price": "₹999",
  "billingPeriod": "month",
  "description": "Best for growing businesses",
  "isActive": true,
  "isPopular": true,
  "ctaText": "Get Started",
  "displayOrder": 1,
  "features": [
    "Invoice Management",
    "Customer Management",
    "GST Billing",
    "Reports"
  ]
}
```

#### Update Pricing Plan
```http
PUT /api/admin/pricing/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Premium",
  "price": "₹1299",
  "billingPeriod": "month",
  "description": "Best for growing businesses",
  "isActive": true,
  "isPopular": true,
  "ctaText": "Get Started",
  "displayOrder": 1,
  "features": [
    "Invoice Management",
    "Customer Management",
    "GST Billing",
    "Reports",
    "Multi-user Support"
  ]
}
```

#### Delete Pricing Plan
```http
DELETE /api/admin/pricing/{id}
Authorization: Bearer {token}
```

#### Update Pricing Plan Status
```http
PATCH /api/admin/pricing/{id}/status?isActive=true
Authorization: Bearer {token}
```

### Admin Solutions (Modules) APIs

#### Get All Solutions
```http
GET /api/admin/solutions
Authorization: Bearer {token}
```

#### Create Solution
```http
POST /api/admin/solutions
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Invoice Management",
  "description": "Create and manage invoices easily",
  "icon": "invoice-icon",
  "isActive": true,
  "displayOrder": 1
}
```

#### Update Solution
```http
PUT /api/admin/solutions/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Invoice Management",
  "description": "Create and manage invoices easily",
  "icon": "invoice-icon",
  "isActive": true,
  "displayOrder": 1
}
```

#### Delete Solution
```http
DELETE /api/admin/solutions/{id}
Authorization: Bearer {token}
```

#### Update Solution Status
```http
PATCH /api/admin/solutions/{id}/status?isActive=true
Authorization: Bearer {token}
```

### Admin Features APIs

#### Get All Features
```http
GET /api/admin/features
Authorization: Bearer {token}
```

#### Create Feature
```http
POST /api/admin/features
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Easy to Use",
  "description": "Simple and intuitive interface",
  "icon": "check-icon",
  "isActive": true,
  "displayOrder": 1
}
```

#### Update Feature
```http
PUT /api/admin/features/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Easy to Use",
  "description": "Simple and intuitive interface",
  "icon": "check-icon",
  "isActive": true,
  "displayOrder": 1
}
```

#### Delete Feature
```http
DELETE /api/admin/features/{id}
Authorization: Bearer {token}
```

### Admin FAQ APIs

#### Get All FAQs
```http
GET /api/admin/faqs
Authorization: Bearer {token}
```

#### Create FAQ
```http
POST /api/admin/faqs
Authorization: Bearer {token}
Content-Type: application/json

{
  "question": "How do I create an invoice?",
  "answer": "Go to the Invoice section and click on Create New Invoice.",
  "isActive": true,
  "displayOrder": 1
}
```

#### Update FAQ
```http
PUT /api/admin/faqs/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "question": "How do I create an invoice?",
  "answer": "Go to the Invoice section and click on Create New Invoice.",
  "isActive": true,
  "displayOrder": 1
}
```

#### Delete FAQ
```http
DELETE /api/admin/faqs/{id}
Authorization: Bearer {token}
```

### Admin Settings APIs

#### Get Website Settings
```http
GET /api/admin/settings
Authorization: Bearer {token}
```

#### Update Website Settings
```http
PUT /api/admin/settings
Authorization: Bearer {token}
Content-Type: application/json

{
  "websiteName": "Ajalkar Billing System",
  "logoUrl": "https://example.com/logo.png",
  "faviconUrl": "https://example.com/favicon.ico",
  "contactEmail": "contact@ajalkarbill.com",
  "contactPhone": "+91 9876543210",
  "address": "Mumbai, India",
  "whatsappNumber": "+91 9876543210",
  "facebookUrl": "https://facebook.com/ajalkarbill",
  "twitterUrl": "https://twitter.com/ajalkarbill",
  "instagramUrl": "https://instagram.com/ajalkarbill",
  "linkedinUrl": "https://linkedin.com/company/ajalkarbill",
  "footerContent": "© 2024 Ajalkar Billing System. All rights reserved.",
  "copyrightText": "© 2024 Ajalkar Billing System"
}
```

### Admin Announcements APIs

#### Get All Announcements
```http
GET /api/admin/announcements
Authorization: Bearer {token}
```

#### Create Announcement
```http
POST /api/admin/announcements
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Special Offer",
  "description": "Get 20% OFF on our Premium Plan",
  "buttonText": "Claim Offer",
  "buttonLink": "/pricing",
  "imageUrl": "https://example.com/banner.jpg",
  "isActive": true,
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59"
}
```

#### Update Announcement
```http
PUT /api/admin/announcements/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Special Offer",
  "description": "Get 20% OFF on our Premium Plan",
  "buttonText": "Claim Offer",
  "buttonLink": "/pricing",
  "imageUrl": "https://example.com/banner.jpg",
  "isActive": true,
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59"
}
```

#### Delete Announcement
```http
DELETE /api/admin/announcements/{id}
Authorization: Bearer {token}
```

## Public APIs (No Authentication Required)

### Home Content
```http
GET /api/public/home

Response:
{
  "settings": { ... },
  "announcements": [ ... ],
  "features": [ ... ],
  "solutions": [ ... ]
}
```

### Pricing
```http
GET /api/public/pricing

Response:
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
    "features": [
      "Invoice Management",
      "Customer Management",
      "GST Billing",
      "Reports",
      "Multi-user Support"
    ]
  }
]
```

### Solutions
```http
GET /api/public/solutions
```

### Features
```http
GET /api/public/features
```

### FAQs
```http
GET /api/public/faqs
```

### Settings
```http
GET /api/public/settings
```

### Announcements
```http
GET /api/public/announcements
```

## Legacy Public APIs (Backward Compatible)

These endpoints continue to work for backward compatibility:

```http
GET /api/faqs
GET /api/features
GET /api/modules
GET /api/testimonials
GET /api/downloads
GET /api/contact-enquiries
```

## How Admin Changes Reflect on Public Website

### Example Flow:

1. **Admin updates pricing:**
   ```http
   PUT /api/admin/pricing/1
   {
     "price": "₹1499"
   }
   ```

2. **Backend saves to MySQL:**
   ```sql
   UPDATE pricing_plans SET price = '₹1499' WHERE id = 1;
   ```

3. **Public website visitor opens pricing page:**
   ```http
   GET /api/public/pricing
   ```

4. **Backend returns latest data:**
   ```json
   {
     "price": "₹1499"
   }
   ```

5. **Angular displays ₹1499** (no code changes needed!)

## Angular Integration

### Admin Panel (Port 4300)

1. **Create HTTP Interceptor for JWT:**
```typescript
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('adminToken');
    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    return next.handle(req);
  }
}
```

2. **Login Service:**
```typescript
login(email: string, password: string) {
  return this.http.post<any>('/api/admin/auth/login', { email, password })
    .pipe(
      tap(response => {
        localStorage.setItem('adminToken', response.token);
        localStorage.setItem('adminUser', JSON.stringify(response.admin));
      })
    );
}
```

3. **Pricing Service:**
```typescript
getPricingPlans() {
  return this.http.get<any[]>('/api/admin/pricing');
}

createPricingPlan(plan: any) {
  return this.http.post('/api/admin/pricing', plan);
}

updatePricingPlan(id: number, plan: any) {
  return this.http.put(`/api/admin/pricing/${id}`, plan);
}
```

### Public Website (Port 4200)

1. **Public API Service:**
```typescript
getPricingPlans() {
  return this.http.get<any[]>('/api/public/pricing');
}

getFeatures() {
  return this.http.get<any[]>('/api/public/features');
}

getFAQs() {
  return this.http.get<any[]>('/api/public/faqs');
}
```

2. **Component Example:**
```typescript
export class PricingComponent implements OnInit {
  pricingPlans: any[] = [];

  constructor(private publicApiService: PublicApiService) {}

  ngOnInit() {
    this.publicApiService.getPricingPlans().subscribe(
      plans => this.pricingPlans = plans
    );
  }
}
```

## Security Features

- **JWT Authentication** for all Admin APIs
- **BCrypt Password Encryption**
- **CORS Configuration** for specific origins
- **Input Validation** on all requests
- **Global Exception Handling**
- **Role-based Access Control**
- **No password exposure** in API responses

## Database Tables

- `admins` - Admin accounts
- `pricing_plans` - Pricing plans
- `pricing_features` - Pricing plan features
- `modules` - Solutions/Modules
- `features` - Website features
- `faqs` - Frequently asked questions
- `website_settings` - Global website settings
- `announcements` - Website announcements

## Error Responses

### Validation Error (400)
```json
{
  "success": false,
  "message": "Validation failed",
  "errors": {
    "name": "Plan name is required",
    "price": "Price is required"
  }
}
```

### Unauthorized (401)
```json
{
  "success": "false",
  "message": "Invalid email or password"
}
```

### Forbidden (403)
```json
{
  "success": "false",
  "message": "Access denied. You do not have permission to access this resource."
}
```

### Not Found (404)
```json
{
  "success": "false",
  "message": "Pricing plan not found with id: 1"
}
```

## Testing with Postman

1. **Import the collection** (create one based on these endpoints)
2. **Set base URL:** `http://localhost:8080`
3. **Login first** to get JWT token
4. **Add token** to Authorization header: `Bearer {token}`
5. **Test all CRUD operations**

## Production Deployment

1. **Change JWT Secret:**
```properties
jwt.secret=YourProductionSecretKeyMustBeVeryLongAndSecure
```

2. **Change Default Admin Password:**
   - Login with default credentials
   - Implement password change endpoint
   - Or manually update in database

3. **Update CORS Origins:**
```java
.allowedOriginPatterns("https://your-admin-domain.com", "https://your-public-domain.com")
```

4. **Use Environment Variables:**
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
```

## Support

For issues or questions, refer to the Spring Boot application logs or check the database for data integrity.
