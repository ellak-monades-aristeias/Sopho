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
package sopho.Eidi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sopho.StageLoader;

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
        StageLoader.lastStage="/sopho/Eidi/EidiMain.fxml";
        Stage stage = (Stage) backButton.getScene().getWindow();
        //we load SearchToEditOfeloumenoi because we want to search ofeloumenoi and the process is the same. We avoid creating a new identical scene
        sl.StageLoad("/sopho/Ofeloumenoi/SearchToEditOfeloumenoi.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenIstorikoEidonOfeloumenou(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Eidi/IstorikoEidonOfeloumenou.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenStatistika(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Eidi/StatistikaMain.fxml", stage, false, true); //resizable false, utility true
    }
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("MainApp.fxml", stage, true, false); //resizable true, utility false
    }
    
}
