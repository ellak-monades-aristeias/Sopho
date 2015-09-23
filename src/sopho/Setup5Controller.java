package sopho;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Setup5Controller implements Initializable {
    
    @FXML
    public AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    StageLoader sl = new StageLoader();
    
    @FXML
    private void NextButton(ActionEvent event) throws IOException, SQLException{
                
        PrefsHandler prefs = new PrefsHandler();
        
        //this is required so that a tip dialog will be displayed to user the first time in the AddOfeloumenoi stage.
        prefs.setPrefs("showAnenergosTip", "true"); 
    
        //now we have to create a folder to store the images taken from the webcam. But we only have to do this on the server machine or the standalone one.
        if(prefs.getPrefs("dbIP").equals("localhost")){
            String dir = System.getProperty("user.home")+"/Documents/Sopho/Images";
        
            File theDir = new File(dir);
            System.out.println(theDir.toString());
            if (!theDir.exists()) {
                System.out.println("creating directory");
                boolean result = false;

                try{
                    theDir.mkdirs();
                    result = true;
                } 
                catch(SecurityException se){
                    System.out.println(se);
                }        
                if(result) {    
                    System.out.println("DIR created");
                }
            }
            
            String sql = "INSERT INTO preferencies (key, value) VALUES (?,?)";
            
            sopho.DBClass db = new sopho.DBClass();

            Connection conn = db.ConnectDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "ServerImageDir");
            pst.setString(2, dir);
            
            int flag = pst.executeUpdate();
            
            if(flag>0){
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                sl.StageLoad("StartApp.fxml", stage, false, true); //resizable false, utility true
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα", "Αδυναμία αποθήκευσης της διαδρομής του φακέλου των εικόνων στη βάση δεδομένων.", "error");
                cm.showAndWait();
            }
        }
        
        
        
    }
}
