package sopho;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Setup3Controller implements Initializable {

    @FXML
    public Button closeButton;
    @FXML
    public Label checkResult;
    @FXML
    public Button checkButton;
    
    public boolean checkok=false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }
        
    @FXML
    private void QuitApp(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    StageLoader sl = new StageLoader();
    
    @FXML
    private void Check(ActionEvent event) throws IOException {
        if(!checkok){//we have not performed the check yet
            DBClass db = new DBClass();
            if(db.ConnectDB()==null){
                checkResult.setStyle("-fx-text-fill:red");
                checkResult.setText("Αποτυχία επικοινωνίας με τη βάση!");
                checkok=false;
            }else{
                checkResult.setStyle("-fx-text-fill:green");
                checkResult.setText("Επιτυχής επικοινωνία με τη βάση!");
                checkok=true;
                checkButton.setText("Επόμενο");
            }
        }else{//the connectivity with the database is confirmed
            Stage stage = (Stage) closeButton.getScene().getWindow();
            sl.StageLoad("Setup4.fxml", stage);
        }
    }
    
    @FXML
    private void PreviousButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        sl.StageLoad("Setup2.fxml", stage);
    }
    
}
