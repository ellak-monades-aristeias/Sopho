package sopho.ApothikiAntikeimenon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ApothikiMainController implements Initializable {

    @FXML
    public Button backButton;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/MainApp.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenParadosi(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/ApothikiAntikeimenon/ParadosiAntikeimenou.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenDiathesima(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/ApothikiAntikeimenon/DiathesimaAntikeimena.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenIstorikoDothikan(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/ApothikiAntikeimenon/IstorikoAntikeimena.fxml", stage, true, false); //resizable true, utility false
    }
}
