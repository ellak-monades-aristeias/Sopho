package sopho;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Setup1Controller implements Initializable {
    
    @FXML
    public Button closeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    StageLoader sl = new StageLoader();
    
    @FXML
    private void QuitApp(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    PrefsHandler prefs = new PrefsHandler();
    
    @FXML
    private void YesButton(ActionEvent event) throws IOException {
        prefs.setPrefs("dbIP", "localhost");
        
        Stage stage = (Stage) closeButton.getScene().getWindow();
        sl.StageLoad("Setup2.fxml", stage);
    }
    
    @FXML
    private void NoButton(ActionEvent event) throws IOException {
        prefs.setPrefs("dbIP", "null");
        
        Stage stage = (Stage) closeButton.getScene().getWindow();
        sl.StageLoad("Setup2.fxml", stage);
    }
    
   
    
}
    

