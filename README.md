# SecureTaskManager

SecureTaskManager is a comprehensive task management application designed with a strong focus on security. This application not only allows users to manage their tasks but also ensures strict access control and secure data handling using JWT authentication and role-based permissions.

## Features

- **Task Management**: Create, edit, and delete tasks with a user-friendly interface.
- **Advanced Security**: JWT authentication to ensure secure access.
- **Account Activation**: Email-based account activation to ensure valid user registrations.
- **Role-Based Access Control**: Strict permissions for Admin and User roles to manage different levels of access.
- **Persistence**: Robust data handling and persistence using PostgreSQL.
- **Forgot Password**: Secure password reset functionality (coming soon...).
- **Admin Features**: Special endpoints and capabilities for admin users, including viewing all users and deleting any task.

## Technologies Used

- **Spring Boot**: For building the application.
- **Spring Security**: For implementing security features.
- **JWT (JSON Web Tokens)**: For secure authentication.
- **Hibernate**: For ORM and database interaction.
- **PostgreSQL**: For data persistence.
- **Lombok**: To reduce boilerplate code.

## Getting Started

### Prerequisites

- Java 11 or higher
- PostgreSQL
- Maven

### Installation

1. Clone the repository
    ```bash
    git clone https://github.com/yourusername/SecureTaskManager.git
    cd SecureTaskManager
    ```
2. Configure the database in `application.yml`
    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/secured-taskmanager-db
        username: yourusername
        password: yourpassword
        driver-class-name: org.postgresql.Driver
      jpa:
        hibernate:
          ddl-auto: update
        show-sql: false
        properties:
          format_sql: true
        database: postgresql
        database-platform: org.hibernate.dialect.PostgreSQLDialect
      mail:
        host: localhost
        port: 1025
        username: yourmailusername
        password: yourmailpassword
        properties:
          mail:
            smtp:
              trust: "*"
            auth: true
            starttls:
              enable: true
            connectiontimeout: 5000
            timeout: 3000
            writetimeout: 5000

    server:
      port: 8088
    ```
3. Run the application
    ```bash
    mvn spring-boot:run
    ```

## Usage

### Authentication

- **Register Admin**: POST `/auth/register-admin`
- **Register User**: POST `/auth/register-user`
- **Activate Account**: GET `/auth/activate-account?token=yourtoken` (only for non-admin users)
- **Login**: POST `/auth/login`
- **Logout**: POST `/logout` (coming soon...)

### Task Management

- **Create Task**: POST `/tasks`
    ```json
    {
        "title": "Task Title",
        "description": "Task Description"
    }
    ```
- **Find Task by ID**: GET `/tasks/{task-id}`
- **Find All Tasks**: GET `/tasks`
    - Query Parameters: `page`, `size`
- **Find All Tasks by Owner**: GET `/tasks/owner`
    - Query Parameters: `page`, `size`
- **Edit Task**: PATCH `/tasks/{taskId}`
    ```json
    {
        "title": "Updated Title",
        "description": "Updated Description"
    }
    ```
- **Update Task Status**: PATCH `/tasks/status/{task-id}`
- **Delete Task**: DELETE `/tasks/delete/{task-id}`

### Admin

- **View All Users**: GET `/admin/users`
    - Query Parameters: `page`, `size`
- **Delete Task**: DELETE `/admin/task/delete/{task-id}`

## Contributing

Feel free to fork this repository and contribute by submitting a pull request. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
