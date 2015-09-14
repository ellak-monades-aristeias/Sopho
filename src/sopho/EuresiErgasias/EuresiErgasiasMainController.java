package sopho.EuresiErgasias;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EuresiErgasiasMainController implements Initializable {

    @FXML
    public Button backButton;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    @FXML
    public void OpenKataxorisiThesis(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/EuresiErgasias/KataxorisiThesisErgasias.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenDiathesimesTheseis(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/EuresiErgasias/ProvoliDiathesimonTheseon.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenKataxorisiAtomou(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/EuresiErgasias/KataxorisiAtomouPouZita.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenListaAtomon(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/EuresiErgasias/ProvoliAtomonPouZitoun.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("MainApp.fxml", stage, true, false); //resizable true, utility false
    }
}
