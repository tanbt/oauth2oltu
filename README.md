# oauth2_apache_oltu
A full demo of Oauth2 with Apache Oltu

## Configuration
* Mysql and MongoDB injection with Spring Bean: resources/applicationContext.xml
* Database connection: application.properties 
* To enable MongoDB connection: update applicationContext.xml, inject MongoDB 
repository into service classes, for example: in UserService.java 

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
All the example requests below are hard-code.

Request header has `Content-Type: application/x-www-form-urlencoded`.
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

#### Get access token with access code
Access token expires in 3600s by default.
##### For server-based application
When *client_secret* is secured, use a post request:
```
POST http://localhost:8080/api/token?redirect_uri=/redirect&grant_type=authorization_code&code=known_authz_code&client_id=test_id&client_secret=test_secret
```
*For client-based or mobile app, where `client_secret` is unsecured, it should 
get the access token right after login, using `response_type=token`*
##### For the absolute trust
Usually use when the client and authorization company are the same company.
```
POST http://localhost:8080/api/token?grant_type=client_credentials&client_id
=test_id&client_secret=test_secret
```
##### For login with client-defined UI
Client app has its own Login screen. It can collect username/password.
```
POST http://localhost:8080/api/token?grant_type=password&username=test_username
&password=test_password&refresh_token=aaaa&client_id=test_id&client_secret=test_secret
```
##### For revoking new access token
Using the refresh token
```
POST http://localhost:8080/api/token?grant_type=refresh_token&refresh_token=known_refresh_token&client_id=test_id&client_secret=test_secret
```
An example response:
``` 
{
    "access_token": "6409db9dca9e3992b8909837750ad958",
    "refresh_token": "55cfd18666a3f4efcbdd72b3c2239590",
    "expires_in": 3600
}
```
