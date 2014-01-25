/**
 * Created by imaculate on 12/18/13.
 */
 

import groovy.sql.Sql

import java.sql.*;

import groovy.lang.GroovyShell;
import com.jcraft.jsch.*

public class Project{
    long id;

    String name, grailsVersion,gitRepoSsh, destination, ownerEmail;







    static void addProject( email, name , grailsVersion, gitRepo, destination){
        def db = Sql.newInstance('jdbc:mysql://localhost:3306/Autodeployer', 'root', 'freedom', 'com.mysql.jdbc.Driver');
        db.execute("""Insert into Projects (Email, Name, GrailsVersion, gitRepo, Destination) values ($email, $name, $grailsVersion, $gitRepo, $destination)""" )
        db.close();

    }

    static void deleteProject( id){
        def db = Sql.newInstance('jdbc:mysql://localhost:3306/Autodeployer', 'root', 'freedom', 'com.mysql.jdbc.Driver');
        db.execute("""Delete from Projects where PID = $id""")


    }
    static void updateProject( id, name , grailsVersion, gitRepo, destination){
        def db = Sql.newInstance('jdbc:mysql://localhost:3306/Autodeployer', 'root', 'freedom', 'com.mysql.jdbc.Driver');
        db.execute("""update Projects set  Name = $name, GrailsVersion = $grailsVersion, gitRepo = $gitRepo, Destination = $destination where PID = $id""" )
        db.close();
    }

    static Collection<Object> getProjects(email){
        def db = Sql.newInstance('jdbc:mysql://localhost:3306/Autodeployer', 'root', 'freedom', 'com.mysql.jdbc.Driver');
        def projectList = [];
      

        
        // fetch each row
        db.eachRow("""Select * from Projects where Email = $email"""){row ->
            projectList << new Project(id: row[0], ownerEmail: row[1], name: row[2],grailsVersion: row[3],gitRepoSsh: row[4], destination: row[5] )
        }
        db.close();
        projectList
    }

	static String clone(git){
		try{
			def gitProcess = "git clone $git".execute()
			return "clone sucessful;"
		} catch (Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			stacktrace;		
		}
		
	
		
	}
	

	
	
	










}
