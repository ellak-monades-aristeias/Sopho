package sopho.Eidi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EidiMainController implements Initializable {

    @FXML
    public Button backButton;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    @FXML
    private void OpenEpeksergasiaEidon(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Eidi/EpeksergasiaEidon.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenEidiDothikan(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Eidi/EidiDothikan.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenIstorikoEidonOfeloumenou(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Eidi/IstorikoEidonOfeloumenou.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("MainApp.fxml", stage, true, false); //resizable true, utility false
    }
    
}
