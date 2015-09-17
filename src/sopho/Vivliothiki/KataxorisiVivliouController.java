package sopho.Vivliothiki;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class KataxorisiVivliouController implements Initializable {

    @FXML
    public TextField siggrafeas, titlos, ekdotikos, katigoria, selides, isbn;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) titlos.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/VivliothikiMain.fxml", stage, true, false); //resizable true, utility false 
    }
    
    @FXML
    private void Save(ActionEvent event) throws IOException, SQLException{
        if(!siggrafeas.getText().isEmpty()&&!titlos.getText().isEmpty()&&!ekdotikos.getText().isEmpty()){
            sopho.DBClass db = new sopho.DBClass();
            Connection conn=null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            
            String sql = "INSERT INTO vivliothiki (siggrafeas, titlos, ekdotikos, katigoria, selides, isbn, daneismeno) VALUES (?,?,?,?,?,?,0)";
            
            conn = db.ConnectDB();
            pst = conn.prepareStatement(sql);
            pst.setString(1, siggrafeas.getText());
            pst.setString(2, titlos.getText());
            pst.setString(3, ekdotikos.getText());
            pst.setString(4, katigoria.getText());
            pst.setString(5, selides.getText());
            pst.setString(6, isbn.getText());
            
            int flag = pst.executeUpdate();
            
            if(flag>0){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Το βιβλίο καταχωρήθηκε με επιτυχία.", "confirm");
                cm.showAndWait();
                Stage stage = (Stage) titlos.getScene().getWindow();
                sl.StageLoad("/sopho/Vivliothiki/VivliothikiMain.fxml", stage, true, false); //resizable true, utility false 
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Το βιβλίο δεν μπόρεσε να καταχωρηθεί στη βάση δεδομένων. Προσπαθήστε πάλι.", "error");
                cm.showAndWait();
            }
            
        }else{
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε συμπληρώσει τα απαιτούμενα πεδία Συγγραφέας, Τίτλος και Εκδοτικός οίκος.", "error");
            cm.showAndWait();
        }
    }
    
}
