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
