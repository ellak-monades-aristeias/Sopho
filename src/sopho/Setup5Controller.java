package sopho;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Setup5Controller implements Initializable {
    
    @FXML
    public AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    StageLoader sl = new StageLoader();
    
    @FXML
    private void NextButton(ActionEvent event) throws IOException, SQLException{
                
        PrefsHandler prefs = new PrefsHandler();
        
        //this is required so that a tip dialog will be displayed to user the first time in the AddOfeloumenoi stage.
        prefs.setPrefs("showAnenergosTip", "true"); 
 
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        sl.StageLoad("StartApp.fxml", stage, false, true); //resizable false, utility true
         
    }
}
