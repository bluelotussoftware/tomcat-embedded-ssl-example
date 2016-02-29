# tomcat-embedded-ssl-example
An example implementation of embedded Apache Tomcat 8.0 with SSL/TLS.

# Required Software
* Apache Maven 3.3
* Java 8 (Java 7 requires changing the POM)

# Running the application
Execute the following command from Maven:
```
mvn clean compile exec:java -Dexec.mainClass=com.bluelotussoftware.tomcat.Main
```
Then go to <a href="https://localhost:8443/hello">https://localhost:8443/hello</a> or <a href="https://localhost:8443/web/hello">https://localhost:8443/web/hello</a> for annotated Servlet 3.0 API web application.
