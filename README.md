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

## Testing REST services:
* GET http://localhost:8080/api/test
* GET http://localhost:8080/api/users

## Oauth2 APIs:

#### Register a client
```
POST /api/register HTTP/1.1
        Host: localhost:8080
        Content-Type: application/json
        { type: "pull", client_name:"test-app", client_url:"localhost:8080", client_description:"example app", redirect_url:"localhost:8080" }
     
```

#### Login then redirect
These URIs act like when submitting a login form. It generate the code or the
 access token then redirect back to the client application.
```
http://localhost:8080/api/auth?redirect_uri=/redirect&uri=/uri&state=state&scope
=read_ekycclaims&response_type=code&client_id=clientid

http://localhost:8080/api/auth?redirect_uri=/redirect&uri=/uri&state=state&scope
=read_ekycclaims&response_type=token&client_id=clientid
```
