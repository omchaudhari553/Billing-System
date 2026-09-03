package com.ajalkarbill.website.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String home() {
        return """
            <html>
            <head>
                <title>Ajalkar Billing System - API</title>
                <style>
                    body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
                    h1 { color: #333; }
                    .endpoint { background: #f4f4f4; padding: 10px; margin: 10px 0; border-radius: 5px; }
                    a { color: #0066cc; }
                </style>
            </head>
            <body>
                <h1>Ajalkar Billing System - Admin Panel Backend API</h1>
                <p>Welcome to the Admin Panel Backend API. The server is running successfully!</p>
                
                <h2>Available Endpoints:</h2>
                
                <h3>Admin Authentication</h3>
                <div class="endpoint">POST /api/admin/auth/login - Admin login</div>
                <div class="endpoint">POST /api/admin/auth/logout - Admin logout</div>
                <div class="endpoint">GET /api/admin/auth/me - Get current admin</div>
                
                <h3>Public APIs</h3>
                <div class="endpoint">GET /api/public/home - Home page content</div>
                <div class="endpoint">GET /api/public/pricing - Pricing plans</div>
                <div class="endpoint">GET /api/public/solutions - Solutions/Modules</div>
                <div class="endpoint">GET /api/public/features - Features</div>
                <div class="endpoint">GET /api/public/faqs - FAQs</div>
                <div class="endpoint">GET /api/public/settings - Website settings</div>
                
                <h3>Admin APIs (Requires JWT Token)</h3>
                <div class="endpoint">GET/POST/PUT/DELETE /api/admin/pricing - Pricing management</div>
                <div class="endpoint">GET/POST/PUT/DELETE /api/admin/solutions - Solutions management</div>
                <div class="endpoint">GET/POST/PUT/DELETE /api/admin/features - Features management</div>
                <div class="endpoint">GET/POST/PUT/DELETE /api/admin/faqs - FAQ management</div>
                <div class="endpoint">GET/PUT /api/admin/settings - Website settings</div>
                <div class="endpoint">GET/POST/PUT/DELETE /api/admin/announcements - Announcements management</div>
                
                <h2>Documentation</h2>
                <p><a href="/swagger-ui/index.html">Swagger UI Documentation</a></p>
                <p><a href="/v3/api-docs">OpenAPI JSON</a></p>
                
                <h2>Default Admin Credentials</h2>
                <p>Email: admin@ajalkarbill.com</p>
                <p>Password: Admin@123</p>
            </body>
            </html>
            """;
    }
}
