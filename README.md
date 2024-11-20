## Overview
This is a **Store Management** system built using **Spring Boot**, designed to handle different aspects of an online store, including **product management**, **order management**, **user management**, **exception handling** and **logging**.

## Features

### 1. **Order Management**
- **Order Creation**: Users can place orders by selecting products and specifying quantities.
- **Order Status Management**: The system supports multiple stages of an order, such as:
    - **Pending**: Initial stage when an order is created.
    - **Processing**: When the order is being prepared or packed.
    - **Shipped**: When the order is dispatched for delivery.
    - **Completed**: When the order is successfully delivered.
    - **Cancelled**: When an order is cancelled by an operator or because of insufficient stock.
- **Order History**: Users can view their past orders, including status and details.

### 2. **Product Management**
- **Product Catalog**: Admins and operators can manage the product catalog, including adding, editing, and removing products.
- **Stock Management**: The system keeps track of the stock for each product, automatically updating stock levels when orders are placed.

### 3. **User Management**
- **User Registration and Login**: Users can register an account by providing basic information (username, email and password).
- **Role-based Access Control**: The system supports different user roles like **Admin**, **Operator** and **User**, each with different permissions.
- **Password Security**: User passwords are hashed and stored securely.

### 4. **Review Management**
- **Product Reviews**: Customers can leave reviews for products they have purchased. Reviews include a star rating (1-5 stars) and a text comment.
- **Review Moderation**: Admins can delete inappropriate reviews.

### 5. **Exception Handling**
- **Validation Errors**: If invalid data is provided, a clear message is returned. For example, if a user places an order with incorrect product data, a validation error is thrown.

- **ProductNotFoundException**: Handles product lookup failures with a specific error message.
- **InvalidOrderStatusTransitionException**: Catches invalid status transitions and returns a relevant error.
### 6. **Logging**
- **Method Execution Logging**: The application uses **AOP** (Aspect-Oriented Programming) to log method calls for all service methods. This allows tracking of when methods are called and executed, providing insight into the application's behavior.
- **Logging Aspects**: The logs are recorded before and after execution of any method in the service layer, helping with debugging and monitoring system actions.

### 7. **Security Features**
- **Authentication**: The system uses secure authentication mechanisms, including JWT (JSON Web Tokens) and bcrypt, ensuring that only authorized users can perform sensitive operations.
- **Authorization**: Different access levels are enforced using Spring Security, ensuring that actions like managing products and processing orders are restricted to authorized users only.
