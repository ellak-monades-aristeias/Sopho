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
package sopho.EuresiErgasias;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EpeksergasiaThesisErgasiasController implements Initializable {

    @FXML
    public TextField eponimia, eponimo, onoma, patronimo, dieuthinsi, tilefono, thesi;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    public ResultSet oldrs = sopho.ResultKeeper.rs;
    public int sel = sopho.ResultKeeper.selectedIndex;
    
    public int selID = -1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        try {
            oldrs.first();
            
            if(sel>0){
                oldrs.relative(sel);
            }
            
            selID=oldrs.getInt("id");
            
            eponimia.setText(oldrs.getString("eponimia"));
            eponimo.setText(oldrs.getString("eponimo"));
            onoma.setText(oldrs.getString("onoma"));
            patronimo.setText(oldrs.getString("patronimo"));
            dieuthinsi.setText(oldrs.getString("dieuthinsi"));
            tilefono.setText(oldrs.getString("tilefono"));
            thesi.setText(oldrs.getString("thesi"));
            
            
        } catch (SQLException ex) {
            Logger.getLogger(EpeksergasiaThesisErgasiasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    public void Save (ActionEvent e) throws IOException, SQLException{
        sopho.DBClass db = new sopho.DBClass();
        Connection conn=null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        if((!eponimia.getText().isEmpty()||(!eponimo.getText().isEmpty()&&!onoma.getText().isEmpty()&&!patronimo.getText().isEmpty()))&&!tilefono.getText().isEmpty()&&!thesi.getText().isEmpty()){
            String sql = "UPDATE theseisergasias SET eponimia=?, eponimo=?, onoma=?, patronimo=?, dieuthinsi=?, tilefono=?, thesi=? WHERE id=?";

            conn=db.ConnectDB();
            
            pst=conn.prepareStatement(sql);
            pst.setString(1, eponimia.getText());
            pst.setString(2, eponimo.getText());
            pst.setString(3, onoma.getText());
            pst.setString(4, patronimo.getText());
            pst.setString(5, dieuthinsi.getText());
            pst.setString(6, tilefono.getText());
            pst.setString(7, thesi.getText());
            pst.setInt(8, selID);
            
            int flag = pst.executeUpdate();
            
            if(flag>0){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Τα στοιχεία για την επιλεγμένη θέση έχουν ενημερωθεί με επιτυχία!", "confirm");
                cm.showAndWait();
                Stage stage = (Stage) onoma.getScene().getWindow();
                sl.StageLoad("/sopho/EuresiErgasias/ProvoliDiathesimonTheseon.fxml", stage, true, false); //resizable true, utility false
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Τα στοιχεία για την επιλεγμένη θέση δεν μπόρεσαν να ενημερωθούν! Προσπαθήστε και πάλι.", "error");
                cm.showAndWait();
            }

            
        }else{//the user didn't fill the required fields
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε συμπληρώσει κάποιο από τα απαιτούμενα πεδία: Επωνυμία ή (Επώνυμο, Όνομα, Πατρώνυμο), Τηλέφωνο και Θέση!", "error");
            cm.showAndWait();
        }
    }
    
    @FXML
    public void GoBack (ActionEvent e) throws IOException{
        Stage stage = (Stage) onoma.getScene().getWindow();
        sl.StageLoad("/sopho/EuresiErgasias/EuresiErgasiasMain.fxml", stage, false, true); //resizable false, utility true
    }
    
}
