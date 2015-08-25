package sopho;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DBSettingsController implements Initializable {
    
    @FXML
    public Label checkResult;
    @FXML
    public TextField username;
    @FXML
    public TextField password;
    @FXML
    public TextField ipAddress;
    @FXML
    public Button checkButton;
    
    public boolean allOk = false;
    public boolean connectionOK = false;
    
    PrefsHandler prefs = new PrefsHandler();
    
    StageLoader sl = new StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //showing the values (the old values that we are currently using) in the textfields
        username.setText(prefs.getPrefs("dbUser"));
        password.setText(prefs.getPrefs("dbPass"));
        ipAddress.setText(prefs.getPrefs("dbIP"));
    }   
    
    @FXML
    private void CheckConnection(ActionEvent event) throws IOException{
        if(!connectionOK){
            if(username.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Προσοχή!");
                alert.setHeaderText("Δεν συμπληρώσατε username!");
                alert.setContentText("Συμπληρώστε το username για τη πρόσβαση στη βάση δεδομένων");
                alert.showAndWait();
                allOk=false;
            }else if(password.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Προσοχή!");
                alert.setHeaderText("Δεν συμπληρώσατε password!");
                alert.setContentText("Συμπληρώστε το password για τη πρόσβαση στη βάση δεδομένων");
                alert.showAndWait();
                allOk=false;
            }else if(ipAddress.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Προσοχή!");
                alert.setHeaderText("Δεν συμπληρώσατε τη διεύθυνση IP!");
                alert.setContentText("Σε περίπτωση που χρησιμοποιείτε βάση που είναι εγκατεστημένη σε αυτόν τον υπολογιστή συμπληρώστε τη λέξη localhost");
                alert.showAndWait();
                allOk=false;
            }else{
                prefs.setPrefs("dbUser", username.getText());
                prefs.setPrefs("dbPass", password.getText());
                prefs.setPrefs("dbIP", ipAddress.getText());
                allOk=true;
                DBClass db = new DBClass();
                
                if(db.ConnectDB()==null){
                    checkResult.setStyle("-fx-text-fill:red");
                    checkResult.setText("Αποτυχία επικοινωνίας με τη βάση! Ελέγξτε τα στοιχεία και προσπαθήστε πάλι...");
                    connectionOK=false;
                }else{
                    checkResult.setStyle("-fx-text-fill:green");
                    checkResult.setText("Επιτυχής επικοινωνία με τη βάση!");
                    connectionOK=true;
                    checkButton.setText("Αποθήκευση");
                }
            }

        }else{
            Stage stage = (Stage) password.getScene().getWindow();
            sl.StageLoad(Sopho.lastStageName, stage, "DBSettings.fxml", false, true); //resizable false, utility true          
        }
        
    }
    
}
