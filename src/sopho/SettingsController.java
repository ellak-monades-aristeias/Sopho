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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SettingsController implements Initializable {
    
    @FXML
    public AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    StageLoader sl = new StageLoader();
    
    @FXML
    public void ChangeDBSetting(ActionEvent event) throws IOException{
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        sl.StageLoad("DBSettings.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void ChangePassword(ActionEvent event) throws IOException{
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        sl.StageLoad("ChangePassword.fxml", stage, false, true); //resizable false, utility true
    }
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        sl.StageLoad("MainApp.fxml", stage, true, false); //resizable true, utility false
    }
        
}
