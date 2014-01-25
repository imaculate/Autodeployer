 // created by Imaculate Mosha 23/01/2014
import groovy.sql.Sql;
import java.sql.*
import com.mysql.jdbc.*;

public class Servers {
    String serverName;
	int id;
 
   static Collection<Object> getAll(){
	def db = Sql.newInstance('jdbc:mysql://localhost:3306/Autodeployer', 'root', 'freedom', 'com.mysql.jdbc.Driver');
    def serverList = [];
	db.eachRow("""Select PID, ServerName from Servers"""){row ->
            serverList << new Servers(id: row[0], serverName: row[1])}
	db.close();
	serverList;	
	}

	static void addServer(String server, String pwd){
		def db = Sql.newInstance('jdbc:mysql://localhost:3306/Autodeployer', 'root', 'freedom', 'com.mysql.jdbc.Driver');
		db.execute("""Insert into Servers (ServerName, Password) values ($server, $pwd)""" )
		db.close();
		
	}

	
	boolean isAdded(String server){
		def isValid = false
		def db = Sql.newInstance('jdbc:mysql://localhost:3306/Autodeployer', 'root', 'freedom', 'com.mysql.jdbc.Driver');
		db.eachRow("""Select ServerName from Servers"""){row -> if(row[0] == server){ isValid = true}}		
		db.close()
		isValid;
	}
}
