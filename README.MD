# Async rest client example  

Technologies:
* Spring Boot - launches Tomcat and serves static resources
* CallableFuture and fork-join pool
* WebSockets - async messaging between web client and server. Spring's STOMP template provides messaging
* json2pojo - build POJO classes from json schema (see src/main/resources/schema)
* Lombok - syntax sugar for Java (@Data, @Log etc...)
* JUnit runner for Spring Boot - unit tests
* JQuery for sample web ui
* WEbJars for autowiring web dependencies

# Usage

`>mvn spring-boot:run`

maven builds tests and runs the application

`open http://localhost:8080/index.html in browser`

type numeric userId (1, 2 etc) in the text filed and press the button 
user's posts should start loading below  
