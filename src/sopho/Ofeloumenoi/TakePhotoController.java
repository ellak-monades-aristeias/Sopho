package sopho.Ofeloumenoi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TakePhotoController implements Initializable {
    
    @FXML
    public Button takePhoto;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) takePhoto.getScene().getWindow();
        sl.StageLoad("sopho/Ofeloumenoi/AllagiFotografias.fxml", stage, false, true); //resizable true, utility false 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
