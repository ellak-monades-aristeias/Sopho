package sopho.Filoksenoumenoi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FiloksenoumenoiMainController implements Initializable {

    @FXML
    public Button backButton;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    @FXML
    public void OpenKataxorisi(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Filoksenoumenoi/KataxorisiFiloksenoumenou.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenIstoriko(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Filoksenoumenoi/ProvoliIstorikouFiloksenoumenon.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenTrexontes(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Filoksenoumenoi/ProvoliTrexontonFiloksenoumenon.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("MainApp.fxml", stage, true, false); //resizable true, utility false
    }
    
}
