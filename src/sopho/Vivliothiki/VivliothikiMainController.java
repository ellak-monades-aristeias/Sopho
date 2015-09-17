package sopho.Vivliothiki;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class VivliothikiMainController implements Initializable {

    @FXML
    public Button backButton;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/MainApp.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenKataxorisi(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/KataxorisiVivliou.fxml", stage, false, true); //resizable false, utility true
    }
    
    @FXML
    public void OpenDiathesima(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/DiathesimaVivlia.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenDaneismena(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/DaneismenaVivlia.fxml", stage, true, false); //resizable true, utility false
    }    
    
}
