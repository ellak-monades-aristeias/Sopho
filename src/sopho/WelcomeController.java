package sopho;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WelcomeController implements Initializable {

    @FXML
    public AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    StageLoader sl = new StageLoader();
    
    @FXML
    private void Next(ActionEvent event) throws IOException {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        sl.StageLoad("Setup1.fxml", stage, "Welcome.fxml", false, true);  //resizable false, utility true
    }
    
}
