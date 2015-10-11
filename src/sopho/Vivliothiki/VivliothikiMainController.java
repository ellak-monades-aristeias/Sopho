/*
 * /* ---------------------------------------------LICENSE-----------------------------------------------------
 * *
 * *YOU ARE NOT ALLOWED TO MODIFY THE LICENSE OR DELETE THE LICENSE FROM THE FILES
 * *
 * *This is an open source project hosted at github: https://github.com/ellak-monades-aristeias/Sopho
 * *
 * *This application is distributed with the following license:
 * *code with license EUPL v1.1 and content with license CC-BY-SA 4.0.
 * *
 * *The development of the application is funded by EL/LAK (http://www.ellak.gr)
 * *
 * *
 */
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
        sl.StageLoad("/sopho/Vivliothiki/KataxorisiVivliou.fxml", stage, true, true); //resizable true, utility true
    }
    
    @FXML
    public void OpenDiathesima(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/ListaDiathesimon.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void OpenDaneismena(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/ListaDaneismenon.fxml", stage, true, false); //resizable true, utility false
    }    
    
    @FXML
    public void OpenIstoriko(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/IstorikoDaneismou.fxml", stage, true, false); //resizable true, utility false
    }
    
}
