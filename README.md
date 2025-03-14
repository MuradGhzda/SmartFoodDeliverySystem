# Smart Food Delivery System

## Overview
A comprehensive web-based food delivery management system built with Spring Boot and MySQL. This application streamlines the food ordering process, featuring real-time order tracking, a loyalty program, and an administrative dashboard for analytics.

## Features
- **User Authentication & Authorization**: Secure login and registration system with role-based access control
- **Restaurant Management**: Add, update, and manage restaurant details and menus
- **Order Processing**: Seamless order placement, tracking, and delivery workflow
- **Loyalty Program**: Customer rewards system to encourage repeat business
- **Admin Dashboard**: Comprehensive analytics and system management interface
- **Real-time Tracking**: Monitor delivery progress in real-time

## Technologies Used
- **Backend**: Spring Boot, Java
- **Database**: MySQL
- **Security**: Spring Security
- **ORM**: Hibernate/JPA
- **API**: RESTful architecture
- **Build Tool**: Maven

## Project Structure
The project follows a standard Spring Boot architecture:
- `controller`: REST endpoints for handling HTTP requests
- `model`: Entity classes that map to database tables
- `repository`: Data access interfaces
- `service`: Business logic implementation
- `config`: Application configurations

## Installation & Setup
```bash
# Clone the repository
git clone https://github.com/MuradGhzda/SmartFoodDeliverySystem.git

# Navigate to the project directory
cd SmartFoodDeliverySystem

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Database Configuration
Configure the MySQL database connection in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fooddelivery
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

## API Endpoints
The system provides several RESTful endpoints:

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate a user

### Restaurants
- `GET /api/restaurants` - Get all restaurants
- `GET /api/restaurants/{id}` - Get restaurant by ID
- `POST /api/restaurants` - Add a new restaurant
- `PUT /api/restaurants/{id}` - Update a restaurant
- `DELETE /api/restaurants/{id}` - Delete a restaurant

### Orders
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Place a new order
- `PUT /api/orders/{id}/status` - Update order status

## Screenshots
![Screenshot 2025-01-14 231645](https://github.com/user-attachments/assets/c19eb28d-63c7-477f-b71f-628af6ec303c)

![Screenshot 2025-01-14 231632](https://github.com/user-attachments/assets/b9853202-7797-4e9b-bcd8-45d41a74b539)
![Screenshot 2025-01-14 231617](https://github.com/user-attachments/assets/b8c3f5c3-2657-4780-83fa-b02341b202c3)
![Screenshot 2025-01-14 231601](https://github.com/user-attachments/assets/a6e7e2b2-e9e3-4541-b4b7-bf6f76a95557)

## Future Enhancements
- Mobile application integration
- Payment gateway integration
- AI-powered recommendation system
- Advanced analytics dashboard
- Delivery route optimization

## Contact
Murad Aghazada - muradagazade@icloud.com

Project Link: [https://github.com/MuradGhzda/SmartFoodDeliverySystem](https://github.com/MuradGhzda/SmartFoodDeliverySystem)
