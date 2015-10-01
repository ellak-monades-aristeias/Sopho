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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


//this is a helper class to connect to the database from other classes
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
            String message;
            boolean localhost = prefs.getPrefs("dbIP").equals("localhost");
            //we have to customize the message in two cases. The case that the db is in localhost and the case that it is hosted in an other computer.
            if(localhost){
                message=". Έχετε επιλέξει να είναι εγκατεστημένη η βάση δεδομένων σε αυτόν τον υπολογιστή";
            }else{
                message= " και ότι η διεύθυνση ip του υπολογιστή που φιλοξενεί τη βάση δεδομένων είναι η σωστή";
            }
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Η εφαρμογή δεν μπόρεσε να συνδεθεί με τη βάση δεδομένων. Βεβαιωθείτε ότι ο MySQL server τρέχει"+message+".", "error");
            cm.showAndWait();
            if(!localhost){
                StageLoader sl = new StageLoader();
                try {
                    sl.StageLoadNoClose("DBSettings.fxml", false, true); //stage name, resizable, utility
                } catch (IOException ex) {
                    Logger.getLogger(DBClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return null;
        }
    }
   
}
