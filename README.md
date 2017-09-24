# oauth2_apache_oltu
A full demo of Oauth2 with Apache Oltu

## Configuration
* Mysql and MongoDB connection: resources/user-beans.xml
* To enable MongoDB connection: update user-beans.xml, inject MongoDB 
repository, for example: in UserService.java 




## References
* https://dzone.com/articles/spring-mvc-example-for-user-registration-and-login-1

## Run
* Make sure database engines are up and run
* mvn clean jetty:run
