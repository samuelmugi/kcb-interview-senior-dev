
Instructions to Run the Application on Docker

1. Build the Docker Image

From the root directory of your project (where the Dockerfile is located), run:

docker-compose build

 

2. Start the Containers

Run the application and H2 database using:

docker-compose up

This command will:
•	Start the Spring Boot application on port 8080 (host).
•	Start the H2 database container on its default ports (1521 for JDBC and 8082 for the H2 web console).

3. Access the Application

   •	Open your browser and navigate to http://localhost:8080 to access the Spring Boot application.
   . Swagger ui http://localhost:8080/swagger-ui/index.html user: user password: password

4. Access the H2 Web Console

   •	Open your browser and navigate to http://localhost:8082.
   •	Use the following credentials to log in:
   •	Driver Class: org.h2.Driver
   •	JDBC URL: jdbc:h2:tcp://h2-database:1521/~/test
   •	User Name: sa
   •	Password: Leave it blank (default).

5. Stop the Containers

When you’re done, stop the application and clean up the containers:

docker-compose down
