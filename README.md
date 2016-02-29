# tomcat-embedded-ssl-example
An example implementation of embedded Apache Tomcat 8.0 with SSL/TLS.

# Required Software
* Apache Maven 3.3
* Java 8 (Java 7 requires changing the POM)

# Running the application
Execute the following command from Maven:
mvn clean compile exec:java -Dexec.mainClass=com.bluelotussoftware.tomcat.Main
