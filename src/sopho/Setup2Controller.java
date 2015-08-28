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

public class Setup2Controller implements Initializable {

    @FXML
    public TextField username;
    @FXML
    public TextField password;
    @FXML
    public TextField ipAddress;
    @FXML
    public Label title;
    
    PrefsHandler prefs = new PrefsHandler();
    
    public boolean ipIsset = false;
    public boolean allOk=false; // this var is used to go to next step only in the case that every field is filled correctly
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //now we need to hide the ipAddress textField if at the previous step the user chose to install the db to this pc
        if(prefs.getPrefs("dbIP").equals("localhost")){ // we get the ip from the prefs. If it is set to localhost we hide the field
            ipAddress.setStyle("-fx-opacity:0;");
            ipIsset=true;
            System.out.println("Hiding the ip field");
        }else{ 
            ipAddress.setStyle("-fx-opacity:1;");
            System.out.println("Showing the ip field");
        }
    }
    
    //this method is required so that the first textField is not in focus on the scene appereance. 
    //Otherwise the prompt text does not appear and the user doesn't know what to fill in.
    public void start(Stage primaryStage) throws Exception {
        title.requestFocus();
    }
    
    StageLoader sl = new StageLoader();
    
    @FXML
    private void NextButton(ActionEvent event) throws IOException {
        //we will set the prefs by getting the textfield values

        String user = username.getText();
        String pass = password.getText();
        
        if(user.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Προσοχή!");
            alert.setHeaderText("Δεν συμπληρώσατε username!");
            alert.setContentText("Συμπληρώστε το username για τη πρόσβαση στη βάση δεδομένων");
            alert.showAndWait();
            allOk=false;
        }else if(pass.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Προσοχή!");
            alert.setHeaderText("Δεν συμπληρώσατε password!");
            alert.setContentText("Συμπληρώστε το password για τη πρόσβαση στη βάση δεδομένων");
            alert.showAndWait();
            allOk=false;

        }else{
            allOk=true;

            prefs.setPrefs("dbUser", username.getText());
            prefs.setPrefs("dbPass", password.getText());
            
            if(!ipIsset){// we do not set the ip if it is already set to localhost by the previous step
                String ip = ipAddress.getText();
                if(ip.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Προσοχή!");
                    alert.setHeaderText("Δεν συμπληρώσατε τη διεύθυνση IP!");
                    alert.setContentText("Συμπληρώστε τη διεύθυνση IP του υπολογιστή που φιλοξενεί τη βάση");
                    alert.showAndWait();
                    allOk=false;
                }else{
                    prefs.setPrefs("dbIP", ip);
                    allOk=true;
                }
            }
        }
        
        if(allOk){
            Stage stage = (Stage) title.getScene().getWindow();
            sl.StageLoad("Setup3.fxml", stage, false, true); //resizable false, utility true
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Σφάλμα!");
            alert.setHeaderText("Ελέγξτε όλα τα πεδία!");
            alert.setContentText("Ενδεχομένως δεν έχετε συμπληρώσει κάποιο πεδίο...");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void PreviousButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) title.getScene().getWindow();
        sl.StageLoad("Setup1.fxml", stage, false, true); //resizable false, utility true
    }
    
}
