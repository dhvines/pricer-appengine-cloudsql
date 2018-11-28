# Pricer 

Pricer is a generic Spring Boot Application containing:  

* RESTful endpoint
* Swagger UI
* Angular UI
* Spring Data JPA persistence  
   - H2 in-memory database.
   


Pricer on Google App Engine and Cloud SQL
=========================================

This sample shows you how to convert Pricer into Google App Engine Standard and Cloud SQL for MySQL.

* [Google App Engine](https://cloud.google.com/appengine/docs/) is a platform as a service to build, deploy, and run applications on the Google Cloud Platform (GCP). The App Engine provides two environments: the Standard Environment and the Flexible Environment. Both support Java, Node.js, Python, and Go. For an in depth comparison, see [Choosing an App Engine Environment](https://cloud.google.com/appengine/docs/the-appengine-environments). 

* [Google Cloud SQL](https://cloud.google.com/sql/docs/) is a fully managed relational database for running MySQL and PostgreSQL on the Google Cloud Platform (GCP).

## Prerequisites

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/download.cgi) (at least 3.5)


## Setup

* Sign-in to Google Cloud Platform console (console.cloud.google.com) and create the project: `pricer`

* Download and initialize the [Cloud SDK](https://cloud.google.com/sdk/)

    `gcloud init`

* Install the App Engine component as part of the Cloud SDK

	`gcloud components install app-engine-java`
	
* Create an App Engine Standard application within the current Google Cloud Project

    `gcloud app create`
    
* Create a Cloud SQL (MySQL) instance named "pricer"

	`gcloud sql instances create pricer`

* Set a <password> for the root user of the MySQL instance named "pricer"	

	`gcloud sql users set-password root --host % --instance pricer --password <password>`
	
* Obtain the <connection name> for the MySQL instance, it is included in the output of the following command

  `gcloud sql instances describe pricer`

* Create a Cloud SQL (MySQL) database named "pricer"

	`gcloud sql databases create pricer --instance pricer`

* Edit the application.properties file in ..\pricer-appengine-cloudsql\src\main\resources, and update with your <connection name> and <password>

	`spring.cloud.gcp.sql.instance-connection-name=<connection name>`
	`spring.datasource.password=<password>`

## Build, Deploy, and Run

### Run on Local Development Server

`mvn appengine:run`

To access the Pricer application, `http://localhost:8181`

To access the Swagger UI, `http://localhost:8181/swagger-ui.html`


* Note: If you get the following failure messages in the console when attempting to deploy  

	`[INFO] GCLOUD: WARNING: Failed to detect whether we are running on Google Compute Engine`
	`[INFO] GCLOUD: java.net.SocketException: Network is unreachable: connect`

  Then you can run the following command to re-login

  `gcloud auth application-default login`

### Deploy to Google App Engine 

`mvn appengine:deploy`

`gcloud app describe | grep id`    ## Record YOUR_PROJECT_ID

To access the Pricer application, `https://YOUR-PROJECT-ID.appspot.com`

To access the Swagger UI, `https://YOUR-PROJECT-ID.appspot.com/swagger-ui.html`

For additional information, see
[Java App Engine](https://developers.google.com/appengine/docs/java/overview) documentation.

## Steps to Convert Pricer to App Engine Standard

### Use WAR Packaging

You must use WAR packaging to deploy into Google App Engine Standard. 

To convert Pricer from using `JAR` packaging to using `WAR` packaging,

1. In `pom.xml`, change `<packaging>jar</packaging>` to `<packaging>war</packaging>`
2. Create a new `SpringBootServletInitializer` class:

```java
package hellocloud.pricer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
public class ServletInitializer extends SpringBootServletInitializer 
{
	  @Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
	  {
		  return application.sources(DefaultPricerApplication.class);
	  }	
}
```

For additional information, see [Spring Boot App Engine Standard](https://github.com/GoogleCloudPlatform/getting-started-java/blob/master/appengine-standard-java8/springboot-appengine-standard

### Remove Tomcat Starter
Google App Engine Standard deploys your `WAR` into a Tomcat server. Spring Boot's starter
includes Tomcat by default. This will introduce conflicts. To fix this, a dependencies to 
exclude Tomcat from the runtime using <scope>provided<scope>

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
```


### Add App Engine Maven Plugin
In the `pom.xml`, add the App Engine Maven plugin and specify a port for local testing:
```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>appengine-maven-plugin</artifactId>
    <version>1.3.1</version>
    <!--
   	When you run your app in the local development server, the default port is 8080.
   	If you get a port binding exception, then configure a different default. Notice
 	that the <port> sets the port here to 8181 and the address 0.0.0.0 is specified,
	which means the development server will listen for requests coming in from local 
	network. Note the server.port setting in SpringBoot configuration has no bearing 
	when you run you app in the local development server
    -->
    <configuration>
        <enableJarClasses>false</enableJarClasses>
        <port>8181</port>
        <address>0.0.0.0</address>
    </configuration>
</plugin>
```

This plugin is used to run local development server as well as deploying the application into Google App Engine.

### Add App Engine Configuration
Add a `src/main/webapp/WEB-INF/appengine-web.xml`:
```xml
<?xml version="1.0" encoding="utf-8" standalone="no"?>
<appengine-web-app xmlns="http://appengine.google.com/ns/1.0">
  <version>1</version>
  <threadsafe>true</threadsafe>
  <runtime>java8</runtime>
</appengine-web-app>
```

This configuration is required for applications running in Google App Engine.

### Overwrite Spring Boot's Maven Surefire Plugin
In the `pom.xml`, overwrite the Spring Boot `maven-surefire-plugin` and set `useSystemClassLoader` to `false` 
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <useSystemClassLoader>false</useSystemClassLoader>
    </configuration>
</plugin>
```

This configuration is required to build the Pricer Spring Boot application; otherwise it fails executing the maven-surefire-plugin.


## Steps to Convert Pricer to Cloud SQL for MySQL

### Update Maven POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
    ...
    <!-- Add Spring Cloud GCP Dependency BOM -->
    <dependencyManagement>
        <dependencies>
            <dependency>
    	        <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-gcp-dependencies</artifactId>
                <version>1.0.0.RC1</version>
     	     	<type>pom</type>
                <scope>import</scope>
            </dependency>
      	</dependencies>
    </dependencyManagement>
    ...
    </dependencies>
    	<!-- Add CloudSQL Starter for MySQL -->
    	<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-gcp-starter-sql-mysql</artifactId>
    	</dependency>
    	<!-- Add MySQL JDBC Driver for Java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
    	</dependency>
      	...
    </dependencies>
    ...
    <repositories>
        <!-- Use Spring Milestone Repository - spring-cloud-gcp-started-sql-myql is not found in Maven central -->
    	<repository>
            <id>repository.spring.milestone</id>
            <name>Spring Milestones Repository</name>
            <url>http://repo.spring.io/milestone</url>
      	</repository>
    </repositories>
</project>
```

### Update Application Properties

In `application.properties`, replace the original configuration with the following properties

```yml
## Logging settings
logging.level.org.springframework=INFO
logging.level.hellocloud=DEBUG
## Database settings
spring.jpa.hibernate.ddl-auto=create-drop
spring.cloud.gcp.sql.database-name=pricer
spring.cloud.gcp.sql.instance-connection-name=pricer-222512:us-central1:pricer
spring.datasource.password=pricer
spring.datasource.username=root
spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect
# Initialize database since newly created Cloud SQL database has no tables.
spring.datasource.initialization-mode=always
# Set continue on error so app starts despite "table already exists" error.
spring.datasource.continue-on-error=true
```
