package sopho.MathimataStiriksis;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MathimataMainController implements Initializable {

    @FXML
    public Button backButton;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("MainApp.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenKataxorisiMathiti(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/MathimataStiriksis/KataxorisiMathiti.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenListaMathiton(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/MathimataStiriksis/ListaMathiton.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenKataxorisiKathigiti(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/MathimataStiriksis/KataxorisiKathigiti.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenListaKathigiton(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/MathimataStiriksis/ListaKathigiton.fxml", stage, true, false); //resizable true, utility false
    }
    
    
    
}
