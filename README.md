# Studentloppet-backend

Backend services for the StudentLoppet project, providing a comprehensive solution for tracking user activities, calculating scores, and managing user profiles in a fitness context. This API handles complex data interactions and provides robust endpoints for front-end integration with the [Studentloppet-frontend](https://github.com/Amir10EA/Studentloppet-frontend) repository.

## Features
- **Activity Logging:** Allows users to log various activities with details such as distance and duration.
- **Score Calculation:** Computes scores based on user activities to foster a competitive environment.
- **User Management:** Handles user registrations, authentication, and profile management.
- **Analytics:** Provides insights into activities and scoring through detailed analytics.

## Technologies
- **Spring Boot:** Framework for creating the API and handling requests.
- **Maven:** Dependency management and project build configuration.
- **MySQL/H2:** Databases used for data persistence in production and development environments, respectively.
- **JUnit and Spring Boot Test:** Frameworks for unit and integration testing to ensure quality and functionality.
- **JSON:** Used for data interchange between the client and server, ensuring lightweight data exchange.
- **Jsoup:** For parsing HTML and scraping web data, particularly in the integration with third-party services.
- **Selenium:** Utilized for automated web testing and complex web scraping tasks, ensuring robust data collection mechanisms.

## Getting Started

### Prerequisites
- Java JDK 11 or newer
- Maven 3.6+
- MySQL 8.0+ / H2 Database

### Running Locally
1. Clone the repository:
   ```bash
   git clone https://github.com/Amir10EA/Studentloppet-backend.git

2. Navigate to the project directory:
cd Studentloppet-backend

3. Configure the database:
Ensure MySQL is running and create a database for the project.
Update the application.properties file with your database credentials.

4. Run the application:
mvn spring-boot:run

## Contributors
- Amir Eihab El Abidou
- M Yamen Barikhan

## Contributing
Contributions are welcome! Please ensure your pull requests are well-formed and include test coverage for new features.

## Acknowledgments
Thanks to all the libraries and frameworks we use, especially Spring Boot, JUnit, Jsoup, and Selenium.

## Frontend Repository
This backend is designed to work with the [Studentloppet-frontend](https://github.com/Amir10EA/Studentloppet-frontend) repository.
