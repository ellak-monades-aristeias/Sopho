package sopho;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBClass {
    
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    PrefsHandler prefs = new PrefsHandler();
    
    public Connection ConnectDB(){
        try{Class.forName("com.mysql.jdbc.Driver");
        
        //getting the credentials from the preferencies
        String user = prefs.getPrefs("dbUser");
        String pass = prefs.getPrefs("dbPass");
        String dbIP = prefs.getPrefs("dbIP");
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://"+dbIP+":3306/sophodb", user, pass);
        System.out.println("Logged into database");
        return conn;
        }catch (ClassNotFoundException | SQLException e){
            System.out.println("Could not login to database. Error:" + e);
            return null;
        }
    }
   
}
