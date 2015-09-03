package sopho.Ofeloumenoi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AddMoreController implements Initializable {

    @FXML
    public Button yesButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @FXML
    public void Yes(ActionEvent event) throws IOException{
        Stage stage = (Stage) yesButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/AddOfeloumenoi.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void No(ActionEvent event) throws IOException{
        Stage stage = (Stage) yesButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml", stage, false, true); //resizable false, utility true
    }
}
