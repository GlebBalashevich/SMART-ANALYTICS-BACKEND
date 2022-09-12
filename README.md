## SMART-ANALYTICS-BACKEND

### Overview
REST Service to make operations with Employees in Departments and collect analytics

### Stack:
- Java 17
- Spring Boot
- PostrgreSql
- Open API
- Docker

### Application launch:
Fastest way: **.\gradlew :composeUP**  
To disable security use profile - 'noauth'  
To run aplication locally (out of docker) user - 'local'

### Application Usage:
- Postman collection: https://www.getpostman.com/collections/924cb7d3b903dcdd33c2
- Swagger UI: http://localhost:8080/swagger-ui.html

### Data
On startup, the application will initialize the database and fill it with predefined values
1. Department.
  Two departments records will be inserted:
  - "Java Enterprise Solutions"
  - "Frontend Enterprise Solutions"
  For Departments only read operations available (create/update/delete - out of scope)
2. Employee.
  Several records will be inserted to the 'employee' table for more convenient testing.  
  For Employees all CRUD operations available. The manager cannot manage employees from other departments.
3. Authentication
  Two records will be added to the 'authentication' table, one for each department:
  - 'java_solutions@mail.com' email - for "Java Enterprise Solutions"
  - 'frontend_solutions@mail.com' - for "Frontend Enterprise Solutions"  
  To use the application api it is necessary to register with one of the email addresses.   
  Password requirements: 8+ characters, at least one UpperCase letter, and one LowerCase letter, and one number.

