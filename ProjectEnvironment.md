# Vexamine EnvSetup

## Project Pull:

    1. open your cmd
    2. exesute following command -> git clone https://github.com/Elavarasansk/Examiner.git
    3. Go to your cloned project folder:
    4. Execute following command:
          mvn clean install (or) mvn eclipse:clean eclipse:eclipse
    5. Build must be successful.
      

## Eclipse IDE:

     1. Install lombok in your IDE-eclipse by following th below tutorials
         https://howtodoinjava.com/automation/lombok-eclipse-installation-examples/
     2. Import the vexamine asw maven project.
        Use the following link to get to know how to import maven project.
         https://javapapers.com/java/import-maven-project-into-eclipse/
  
  After import open .project folder located in your vexamine project folder, comment the following build-commands.
  This will solve the maven build eclipse issue you are facing. 
			
			  <!-- <buildCommand>
				<name>org.eclipse.ui.externaltools.ExternalToolBuilder</name>
				<triggers>full,incremental,</triggers>
				<arguments>
					<dictionary>
						<key>LaunchConfigHandle</key>
						<value>org.eclipse.m2e.core.maven2Builder.launch</value>
					</dictionary>
				</arguments>
			</buildCommand> -->
			
			 <!-- <buildCommand>
			<name>org.eclipse.wst.validation.validationbuilder</name>
			<arguments>
			</arguments>
		</buildCommand> -->
		
### Start the application:

	1. open MoodleConductApplication.java
	2. Run as Java Application
	3. Application will be up in the port 2020
	
      http://localhost:2020/
       