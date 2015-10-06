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
package sopho.Ofeloumenoi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sopho.StageLoader;

public class OfeloumenoiMainController implements Initializable {

    @FXML
    public Button backButton;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/MainApp.fxml", stage, true, false); //resizable true, utility false 
    }
    
    @FXML
    private void AddOfeloumenoi(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/AddOfeloumenoi.fxml", stage, true, false); //resizable true, utility false 
    }
    
    @FXML
    private void EditOfeloumenoi(ActionEvent event) throws IOException{
        StageLoader.lastStage="/sopho/Ofeloumenoi/OfeloumenoiMain.fxml";
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/SearchToEditOfeloumenoi.fxml", stage, true, false); //resizable true, utility false 
    }
    
    @FXML
    private void SearchOfeloumenoi(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/SearchOfeloumenoi.fxml", stage, true, false); //resizable true, utility false 
    }
    
    @FXML
    private void ListaOfeloumenon(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/ListaOfeloumenon.fxml", stage, true, false); //resizable true, utility false 
    }
    
}
