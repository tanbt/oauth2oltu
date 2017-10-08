# oauth2_apache_oltu
A full demo of Oauth2 with Apache Oltu

## Configuration
* Mysql and MongoDB connection: resources/user-beans.xml
* To enable MongoDB connection: update user-beans.xml, inject MongoDB 
repository, for example: in UserService.java 

## Run
* Make sure database engines are up and run
* mvn clean jetty:run
* See controllers source code for knowing accessible URI and REST (will be updated)

## References:
* https://dzone.com/articles/spring-mvc-example-for-user-registration-and-login-1
* https://www.java2blog.com/spring-restful-web-services-json-example/

## Rest services:
* GET http://localhost:8080/api/test
* GET http://localhost:8080/api/users

## Oauth 2 apis:

#### Register a client
```
POST /api/register HTTP/1.1
        Host: localhost:8080
        Content-Type: application/json
        { type: "pull", client_name:"test-app", client_url:"localhost:8080", client_description:"example app", redirect_url:"localhost:8080" }
     
```
