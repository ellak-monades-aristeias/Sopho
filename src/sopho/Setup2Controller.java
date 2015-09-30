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
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή", "Δεν συμπληρώσατε username! Συμπληρώστε το username για τη πρόσβαση στη βάση δεδομένων", "error");
            cm.showAndWait();
            allOk=false;
        }else if(pass.isEmpty()){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή", "Δεν συμπληρώσατε password! Συμπληρώστε το password για τη πρόσβαση στη βάση δεδομένων", "error");
            cm.showAndWait();
            allOk=false;

        }else{
            allOk=true;

            prefs.setPrefs("dbUser", username.getText());
            prefs.setPrefs("dbPass", password.getText());
            
            if(!ipIsset){// we do not set the ip if it is already set to localhost by the previous step
                String ip = ipAddress.getText();
                if(ip.isEmpty()){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή", "Δεν συμπληρώσατε τη διεύθυνση IP! Συμπληρώστε τη διεύθυνση IP του υπολογιστή που φιλοξενεί τη βάση", "error");
                    cm.showAndWait();
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
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Σφάλμα", "Ελέγξτε όλα τα πεδία! Ενδεχομένως δεν έχετε συμπληρώσει κάποιο πεδίο...", "error");
            cm.showAndWait();
        }
    }
    
    @FXML
    private void PreviousButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) title.getScene().getWindow();
        sl.StageLoad("Setup1.fxml", stage, false, true); //resizable false, utility true
    }
    
}
