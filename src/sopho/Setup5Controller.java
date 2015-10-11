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
import java.net.URL;
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
        prefs.setPrefs("tableTipOfeloumenoi", "true");
        prefs.setPrefs("tableTipEidi", "true");
        prefs.setPrefs("tableTipMathites", "true");
        prefs.setPrefs("tableTipKathigites", "true");
        prefs.setPrefs("eidiNotification", "true");
        prefs.setPrefs("tableTipPerigrafiAntikeimenou", "true");
        prefs.setPrefs("eidiListTip", "true");
        
        
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        sl.StageLoad("StartApp.fxml", stage, false, true); //resizable false, utility true
         
    }
}
