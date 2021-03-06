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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Setup3Controller implements Initializable {

    @FXML
    public Label checkResult;
    @FXML
    public Button checkButton;
    
    public boolean checkok=false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }
    
    PrefsHandler prefs = new PrefsHandler();
    
    //getting the credentials from the preferencies
    String user = prefs.getPrefs("dbUser");
    String pass = prefs.getPrefs("dbPass");
    String dbIP = prefs.getPrefs("dbIP");
    
    StageLoader sl = new StageLoader();
    
    @FXML
    private void Check(ActionEvent event) throws IOException, ClassNotFoundException {
        if(!checkok){//we have not performed the check yet
            
            Class.forName("com.mysql.jdbc.Driver");
            
            Connection conn=null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://"+dbIP+":3306", user, pass);
            } catch (SQLException ex) {
                Logger.getLogger(Setup3Controller.class.getName()).log(Level.SEVERE, null, ex);
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Αδυναμία επικοινωνίας με τη βάση", "Ελέγξτε αν η mysql τρέχει και επιβεβαιώστε ότι χρησιμοποιείτε τα σωστά στοιχεία πρόσβασης. Το username είναι συνήθως root εκτός αν επιλέξατε κάτι άλλο και το password το έχετε ορίσει κατά την εγκατάσταση της mysql.", "error");
                cm.showAndWait();
                prefs.removePrefs("dbUser");
                prefs.removePrefs("dbPass");
            }
            
            if(conn==null){
                checkResult.setStyle("-fx-text-fill:red");
                checkResult.setText("Αποτυχία επικοινωνίας με τη βάση!");
                checkok=false;
            }else{
                checkResult.setStyle("-fx-text-fill:green");
                checkResult.setText("Επιτυχής επικοινωνία με τη βάση!");
                checkok=true;
                checkButton.setText("Επόμενο");
                
                if(prefs.getPrefs("dbIP").equals("localhost")){
                    //we have to create the database if the user choose to install ta db locally.
                    CreateDatabase cd = new CreateDatabase();
                    try {
                        cd.CreateDB();
                    } catch (SQLException ex) {
                        Logger.getLogger(Setup3Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }else{//the connectivity with the database is confirmed            
            Stage stage = (Stage) checkButton.getScene().getWindow();
            sl.StageLoad("Setup4.fxml", stage, false, true); //resizable false, utility true
        }
    }
    
    @FXML
    private void PreviousButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) checkButton.getScene().getWindow();
        sl.StageLoad("Setup2.fxml", stage, false, true); //resizable false, utility true
    }
    
}
