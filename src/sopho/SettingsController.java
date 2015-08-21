package sopho;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SettingsController implements Initializable {

    @FXML
    public Button closeButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    StageLoader sl = new StageLoader();
    
    @FXML
    public void ChangeDBSetting(ActionEvent event) throws IOException{
        Stage stage = (Stage) closeButton.getScene().getWindow();
        sl.StageLoad("DBSettings.fxml", stage);
    }
    
    @FXML
    public void ChangePassword(ActionEvent event) throws IOException{
        Stage stage = (Stage) closeButton.getScene().getWindow();
        sl.StageLoad("ChangePassword.fxml", stage);
    }
    
    @FXML
    private void QuitApp(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
        
}
