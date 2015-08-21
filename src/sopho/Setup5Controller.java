package sopho;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Setup5Controller implements Initializable {
    
    @FXML
    public Button closeButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void QuitApp(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    StageLoader sl = new StageLoader();
    
    @FXML
    private void NextButton(ActionEvent event) throws IOException{
        Stage stage = (Stage) closeButton.getScene().getWindow();
        sl.StageLoad("StartApp.fxml", stage);
    }
}
