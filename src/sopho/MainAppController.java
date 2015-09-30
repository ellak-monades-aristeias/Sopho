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
package sopho;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainAppController implements Initializable {
    
    @FXML
    private Button settings;
    
    StageLoader sl = new StageLoader(); // instantiating the Stage Loader helper
    
    @FXML
    private void OpenSettings(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("Settings.fxml", oldstage, false, true); //resizable false, utility true
    }
    
    @FXML
    private void OpenOfeloumenoi(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml", oldstage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenEidi(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("/sopho/Eidi/EidiMain.fxml", oldstage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenApothiki(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("/sopho/ApothikiAntikeimenon/ApothikiMain.fxml", oldstage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenMathimataStiriksis(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("/sopho/MathimataStiriksis/MathimataMain.fxml", oldstage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenEuresiErgasias(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("/sopho/EuresiErgasias/EuresiErgasiasMain.fxml", oldstage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenFiloksenoumenoi(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("/sopho/Filoksenoumenoi/FiloksenoumenoiMain.fxml", oldstage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenVivliothiki(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/VivliothikiMain.fxml", oldstage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenStatistika(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("/sopho/Statistika/StatistikaMain.fxml", oldstage, true, false); //resizable true, utility false
    }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
}
