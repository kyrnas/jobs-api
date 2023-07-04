# jobs-api
Use `Java 17` and `Spring Boot 3.1.1`
## H2 Database
This project uses an SQL H2 in-memory database. A DB console is accessible through http://localhost:8080/api/h2-console/
| Field         | Value                |
| ------------- | -------------------- |
| Driver Class  | org.h2.Driver        |
| JDBC URL      | jdbc:h2:file:./db/db |
| User Name     | sa                   |
| Password      |                      |

## Swagger Documentation
Go to http://localhost:8080/api/swagger-ui/index.html#/ to view all the endpoints in their uses

## Postman collection
Import the following collection: https://github.com/kyrnas/jobs-api/blob/master/Oleksx.postman_collection.json
