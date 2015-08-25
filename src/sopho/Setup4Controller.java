package sopho;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Setup4Controller implements Initializable {

    @FXML
    public TextField password;
    @FXML
    public TextField password2;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    StageLoader sl = new StageLoader();
    DBClass db = new DBClass();
    
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    @FXML
    private void NextButton(ActionEvent event) throws IOException {
        if(password.getText().isEmpty()){
            //error the password field must not be empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Προσοχή!");
                alert.setHeaderText("Δεν συμπληρώσατε κωδικό!");
                alert.setContentText("Συμπληρώστε έναν κωδικό στο πεδίο για να διασφαλίσετε το απόρρητο των προσωπικών δεδομένων που περιλαμβάνονται στην εφαρμογή");
                alert.showAndWait();
        }else if(password2.getText().isEmpty()){
            //error the password confirm field must not be empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Προσοχή!");
                alert.setHeaderText("Δεν επιβεβαιώσατε τον κωδικό!");
                alert.setContentText("Συμπληρώστε ίδιο κωδικό για δεύτερη φορά για να επιβεβαιώσετε τον κωδικό που επιλέξατε.");
                alert.showAndWait();
        }else if(!password.getText().equals(password2.getText())){
            //if the passwords does not match
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Προσοχή!");
                alert.setHeaderText("Οι κωδικοί δεν ταιριάζουν!");
                alert.setContentText("Θα πρέπει να συμπληρώσετε τον ίδιο κωδικό και στα δύο πεδία.");
                alert.showAndWait();
        }else{
            //we can go to the next step
            conn=db.ConnectDB();
        
            System.out.println("the pass is: "+password.getText());

            MD5 md5 = new MD5();
            String encryptedPass = md5.getMD5(password.getText());
            
            String sql = "INSERT INTO users (pass) VALUES (?)";

            try {

                pst = conn.prepareStatement(sql);
                pst.setString(1, encryptedPass);
                System.out.println("the query is:" + pst.toString());
                int flag = pst.executeUpdate();
                
                //we check if everything was fine through the flag integer.
                
                if (flag==1){
                    Stage stage = (Stage) password.getScene().getWindow();
                    sl.StageLoad("Setup5.fxml", stage, "Setup4.fxml", false, true); //resizable false, utility true
                }
                else if(flag==0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Σφάλμα!");
                    alert.setHeaderText("Ο κωδικός δεν μπόρεσε να εισαχθεί στη βάση");
                    alert.setContentText("Προσπαθήστε και πάλι να εισάγετε τον κωδικό ή επικοινωνήστε με την τεχνική υποστήριξη.");
                    alert.showAndWait();
                }


            }catch (SQLException | IOException e){
                System.out.println("Πρόβλημα κατά την εισαγωγή του κωδικού στη βάση!" + e);
            }
        }
    }
    
    @FXML
    private void PreviousButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) password.getScene().getWindow();
        sl.StageLoad("Setup3.fxml", stage, "Setup4.fxml", false, true); //resizable false, utility true
    }
}
