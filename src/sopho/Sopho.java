package sopho;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Sopho extends Application {
    
    public static String lastStageName=null;
        
    @Override
    public void start(Stage stage) throws Exception {
                
        Font.loadFont(getClass().getResourceAsStream("../Fonts/OpenSans-ExtraBold"), 14);
        Font.loadFont(getClass().getResourceAsStream("../Fonts/OpenSans-Light.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("../Fonts/OpenSans-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("../Fonts/OpenSans-Semibold.ttf"), 14);
        
        StageLoader sl = new StageLoader();
        
        PrefsHandler prefs = new PrefsHandler();
        
        String dbIP = prefs.getPrefs("dbIP");
        String dbUser = prefs.getPrefs("dbUser");
        String dbPass = prefs.getPrefs("dbPass");
        
        if (dbIP==null||dbUser==null||dbPass==null) {
            // we have NOT made the initial setup so that the app can NOT connect to database. Start the initial setup wizard
            sl.StageLoadNoClose("Welcome.fxml", false, true); //resizable false, utility true
        }else if(!UserExists()){
            sl.StageLoadNoClose("Setup4.fxml", false, true); //resizable false, utility true
        }else{
            // we have  made the initial setup so that the app can connect to database. Start the login screen
            sl.StageLoadNoClose("StartApp.fxml", false, true); //resizable false, utility true            
        }
        
    }
    
    //in this method we check if the user has specified a password during the initial setup
    public boolean UserExists(){
        Connection conn=null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        DBClass db = new DBClass();
        
        conn=db.ConnectDB();
        
        String sql = "SELECT * FROM users";
        
        try {
            
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            //now we will check if the query returned a result
            
            rs.last(); //i go to the last line of the result to find out the number of the line
            
            return rs.getRow()>0; //if rs.getRow() is greater than 0 it means that the table users has at least one record (the user has specified a password)
            
            
        }catch (SQLException e){
            System.out.println("Πρόβλημα κατά την αναζήτηση εγγραφών στον πίνακα users!" + e);
            return false;
        }
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
        
}
