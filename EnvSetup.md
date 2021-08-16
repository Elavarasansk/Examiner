# Vexamine EnvSetup

## Java Environment:
  Download java from oracle site
    
    1. https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html
    2. Accept license agreement and download java based on your windows-bit configuration
    3. Install the downloaded content.
    4. Set the java_home variable with the details from following tutorial:
       https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html
    
## Maven Environment:

     1. Perform maven installation and environment setup by following the below tutorials
     2. https://www.mkyong.com/maven/how-to-install-maven-in-windows/

## Setup Database - Postgresql

    1. Download postgres 9.4.x version and install
    2. While installing give user-name password as "postgres/postgres"
	 3. Execute the following in postgres sql window.	 
		    CREATE DATABASE vexamine_moodle
				  WITH OWNER = postgres
				       ENCODING = 'UTF8'
				       TABLESPACE = pg_default
				       LC_COLLATE = 'English_India.1252'
				       LC_CTYPE = 'English_India.1252'
				       CONNECTION LIMIT = -1;
				       
## Setup Node

    1. Download node version 8.11.3 from nodejs site.
    2. You can download the above specified version from previous release option.
    
## Setup IDE - Eclipse
   
    1. Download latest eclipse version from the eclipse-site.
        Eclipse IDE for Enterprise JAVA developers. 
    
    