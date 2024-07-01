<h3 align="center">API DIAZERO - Incident</h3>
<br><br>

## 💻 About The Project

This application is a RESTful API that represents the incident creation process. It includes login functionalities that allow users to create, edit, list, and delete incidents.

---

## 🛠 Technologies
The current project follows the DDD, Clean Architecture, and SOLID principles. The analysis was conducted using Event Storming, facilitated through the **[Miro](https://miro.com/welcomeonboard/b1p1TFRCSWlXczRKNlVub3ZucW5GWVhHN1pWNVZ3MU03NDFNTDRTa0I1ZkRLRDQwdkEwdEpkdm1neGRrNDlMZXwzNDU4NzY0NTkyNjkyMzEzNzgwfDI=?share_link_id=913066943868)** tool. The methodology was managed on Trello, using Kanban.
The following technologies were used in the development of the project's REST API:

- **[Java 17](https://www.oracle.com/java)**: Chosen for its robustness, performance, and support for object-oriented programming.
- **[Spring Boot 3](https://spring.io/projects/spring-boot)**: Facilitates the development of Spring-based Java applications with minimal configuration.
- **[Maven](https://maven.apache.org)**: Dependency management tool that simplifies the building and management process of Java projects.
- **[Spring Securiy](https://spring.io/projects/spring-security)**: Offers robust security and easy configuration for Spring applications.
- **[JWT](https://jwt.io/)**: Used for token-based authentication and authorization, ideal for REST APIs.
- **[MySQL](https://www.mysql.com)**: Widely used relational database known for its performance and scalability.
- **[Hibernate](https://hibernate.org)**: Object-relational mapping framework that simplifies interaction with databases.
- **[JPA](https://www.oracle.com/java/technologies/persistence-jsp.html)**: Java specification for object-relational mapping, providing an abstraction layer over Hibernate.
- **[Flyway](https://flywaydb.org)**: Used for automated and consistent database versioning and migration.
- **[Lombok](https://projectlombok.org)**: Reduces Java code verbosity through annotations for automatic generation of getters, setters, and more.
- **[Swagger](https://swagger.io/)**: Facilitates REST API documentation, allowing for simplified visualization and interaction with endpoints.
- **[TDD](https://medium.com/@berrachdim/test-driven-development-tdd-in-java-a-comprehensive-guide-with-examples-c66a77afe036)**: Ensures code quality through automated testing of individual units of functionality.

---

## Trading Rules
Below are listed the business rules of the systems.

🛠 **Register**
1) To register a user, it is necessary to define the user's name, login, and password information.
2) If the login already exists in the database, a status 400 will be returned, indicating that this login is already in use.
3) The password registration system uses the Bcrypt password hashing algorithm.
4) Upon making the request, if successful (status 200), return the user's name and login information as part of the JSON response.

🛠 **LogIn**
1) Login is performed using JWT, where login and password information is passed to obtain a JWT token for future requests. The process does not employ role-based access control, maintaining only one defined role throughout the project.
2) If login information does not exist or is not compatible, a status 401 will be returned.

🛠 **Incident**

**Functionality to create the incident**
1) You need to be logged into the system to perform any operation on the incident endpoint.
2) We need two entities: the Incident entity to store information such as id, name, description, priority, and a list of events related to the incident. The second entity is IncidentEvent, responsible for storing details of events occurring in incidents, such as id, incident reference, event type (defined by an enum of created, updated, closed, and deleted), event description, event timestamp, and the user who created the event.
3) To create an incident, you need to fill out the following information: name, description, priority (which must be an enum with values Low, Medium, High, or Critical), and the user who created it.
4) As soon as the request to create an incident is made, automatically create its event with type CREATED.
---
**Return Information about the Incident**
1) Functionality to list all incidents with their events, sorted by priority order (critical, high, medium, low).
2) Functionality to list the last 20 registered incidents with their events, sorted by date in descending order.
3) Functionality to list the incident with their events by registration ID, sorted by priority order (critical, high, medium, low).
---
**Functionality to update the incident by ID**
1) Can update the following information: priority, event (mandatory), event description (mandatory), and user ID (mandatory).
2) With each incident update, it creates a new event instead of modifying an existing one.
3) Cannot update the incident to the event type CREATED or DELETED; if attempted, return status 404.
4) If the incident has an event of type CLOSED or DELETED, it will no longer be possible to update information for this incident, return status 404.
---
**Functionality to delete the incident by ID**
1) No information will be deleted from the database. The 'delete' function will disable the incident by updating its event to DELETED.
2) If the incident already has an event of type CLOSED or DELETED, it will not be possible to change its status.

---

## ☕ Deploy

Below are the three available deployment methods for the application:


🛠 **Running Via Terminal**
```
java -Dspring.profiles.active=prod -DDATASOURCE_URL=jdbc:mysql://localhost:3306/diazero_api -DDATASOURCE_USERNAME=root -DDATASOURCE_PASSWORD=root -jar target/diazero-00.00.01.jar 
```


🛠 **Build with file .war**
1) Add the <packaging>war</packaging> tag to the pom.xml file of the project, with this tag being a child of the root <project> tag.
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.3.1</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.luizromao</groupId>
	<artifactId>diazero</artifactId>
	<version>00.00.01</version>
	<name>diazero</name>

  <packaging>war</packaging>
```

2) Add the following dependency:
```
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-tomcat</artifactId>
  <scope>provided</scope>
</dependency>
```

3) Change the project's main class (DiazeroApplication) to inherit from the SpringBootServletInitializer class, as well as override the configure method:
```
@SpringBootApplication
public class DiazeroApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(ApiApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }
}
```


🛠 **GraalVM Native Image**
- **[Documentation](https://www.graalvm.org/native-image)**

1) Add plugin in pom.xml file:
```
<plugin>
  <groupId>org.graalvm.buildtools</groupId>
  <artifactId>native-maven-plugin</artifactId>
</plugin>
```



