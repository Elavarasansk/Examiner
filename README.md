# Vexamine

### Stack Used:
    1. React - Frontend
    2. Springboot + Hibernate - Backend.
       {JWT + Cookie}
    
    Environment Setup:
    1. Java8
    2. Node 8.11.3 + Npm 5.6.0
    3. Postgresql 9.4
    
##Features

     1. Login {Session Validation + redirection}
     2. Signup
     3. User Info + Role Maintanence
     4. Moodle Test Application

### Setup Database - Postgresql
	CREATE DATABASE vexamine_moodle
	  WITH OWNER = postgres
	       ENCODING = 'UTF8'
	       TABLESPACE = pg_default
	       LC_COLLATE = 'English_United States.1252'
	       LC_CTYPE = 'English_United States.1252'
	       CONNECTION LIMIT = -1;

### Start the application
	1. open MoodleConductApplication.java
	2. Run as Java Application
	3. Application will be up in the port 2020
	
      http://localhost:2020/
      
### Development Support
	1. http://localhost:2020/swagger-ui.html
	2. Controller end-points will be available.
	3. Use it for independent backend development phase.


### Production Deployment
	1. mvn clean install package or IDE - mvn eclipse:clean eclipse:eclipse package
	2. war will be generated in the target
	3. deploy the war in a tomcat container.
	4. Application will be up in the port 2020
