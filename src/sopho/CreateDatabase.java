/*
 * /* ---------------------------------------------LICENSE-----------------------------------------------------
 * *
 * *YOU ARE NOT ALLOWED TO MODIFY THE LICENSE OR DELETE THE LICENSE FROM THE FILES
 * *
 * *This is an open source project hosted at github: https://github.com/ellak-monades-aristeias/Sopho
 * *
 * *This application is distributed with the following license:
 * *code with license EUPL v1.1 and content with license CC-BY-SA 4.0.
 * *
 * *The development of the application is funded by EL/LAK (http://www.ellak.gr)
 * *
 * *
 */
package sopho;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.ibatis.jdbc.ScriptRunner;

public class CreateDatabase {
    
    public void CreateDB() throws ClassNotFoundException, SQLException {
		        
        PrefsHandler prefs = new PrefsHandler();

        //getting the credentials from the preferencies
        String user = prefs.getPrefs("dbUser");
        String pass = prefs.getPrefs("dbPass");
        String dbIP = prefs.getPrefs("dbIP");

        Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DriverManager.getConnection("jdbc:mysql://"+dbIP+":3306", user, pass);

        //we are using the myBatis library to create the initial database using an sql file as a template.

        InputStream in = CreateDatabase.class.getResourceAsStream("dbCreate.sql");
        try {
                // Initialize object for ScriptRunner
                ScriptRunner sr = new ScriptRunner(conn);                      

                // Give the input file to Reader
                Reader reader = new BufferedReader(
                       new InputStreamReader(in));

                // Execute script
                sr.runScript(reader);

        } catch (Exception e) {
                System.err.println("Failed to Execute"+" The error is " + e.getMessage());
        }
    }
}
