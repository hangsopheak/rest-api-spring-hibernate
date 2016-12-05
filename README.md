# Description
This is about java web backend service with REST-APIs, hibernate database connection

# Clone this project
 git clone git@github.com:sophea/rest-api-spring-hibernate.git
  

# To run this backend project in JAVA technology with Maven build tool:

1 : install JAVA JDK version >=1.8.x  (http://www.oracle.com/technetwork/java/javase/downloads/index.html)

2 : install maven 3  (https://maven.apache.org/install.html)

3 : go to this project location by console

4 : run command >> mvn clean jetty:run (Jetty Server) or mvn clean tomcat7:run (Tomcat Server)

   
===============Test result with API when server started(Jetty/Tomcat)=======


get all persons : GET http://localhost:8080/api/persons/v1/all

get details : GET  http://localhost:8080/api/persons/v1/1

remove item : DELETE http://localhost:8080/api/persons/v1/1

update item : POST http://localhost:8080/api/persons/v1/1

=======================================

6 : Integrate Test cases : run command >> mvn clean test


# How to get Srping bean from Servlet or Filter: