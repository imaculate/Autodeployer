
/**
 * Created by imaculate on 12/18/13.
 */
import groovy.sql.Sql;
import java.sql.*
import com.mysql.jdbc.*;

public class Users {
    String email,firstName,lastName;
    private String password;




public void deleteUser(){

    //some code to open database and delete user as well as his or her projects

}

private void addUser(){
    def db = Sql.newInstance('jdbc:mysql://localhost:3306/Autodeployer', 'root', 'freedom', 'com.mysql.jdbc.Driver');
    db.executeInsert(""" Insert into Users (Email, First_Name, Last_Name, Password) values ($email, $firstName, $lastName, $password)""")
    db.close()
}


static  void changePassword(String email, String newPassword){
    def db = Sql.newInstance('jdbc:mysql://localhost:3306/Autodeployer', 'root', 'freedom', 'com.mysql.jdbc.Driver');
    db.execute(""" UPDATE Users SET Password = $newPassword WHERE Email=$email""")
    db.close();

    //some code to open database and change the password
}

    

    static String getName(email){
        def db = Sql.newInstance('jdbc:mysql://localhost:3306/Autodeployer', 'root', 'freedom', 'com.mysql.jdbc.Driver');
        def firstName = "";
        firstName = db.firstRow("""Select First_Name from Users where Email = $email""").First_Name;

        db.close();
        firstName

    }


static boolean validateUser(String email, String password){
	println "$email";
        def db = Sql.newInstance('jdbc:mysql://localhost:3306/Autodeployer', 'root' ,  'freedom', 'com.mysql.jdbc.Driver');
        def valid = false
        def row = db.firstRow("""select Password from Users where Email = $email""")
        if (row.Password == password){valid = true}

        
	db.close()
        valid
        
    }




}
