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
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) password.getScene().getWindow();
        //we have to direct to different stages if the user has not signed in
        if(Sopho.hasSignedIn = true){
            sl.StageLoad("MainApp.fxml", stage, false, true); //resizable false, utility true
        }else{
            sl.StageLoad("StartApp.fxml", stage, false, true); //resizable false, utility true
        }
        
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
            if(Sopho.hasSignedIn){// the user has signed into the mainApp interface
                sl.StageLoad("MainApp.fxml", stage, false, true); //resizable false, utility true          
            }else{// the user hasn't sign into the mainApp yet. We redirect to the sign in scene.
                sl.StageLoad("StartApp.fxml", stage, false, true); //resizable false, utility true          
            }
        }
    }
    
}
