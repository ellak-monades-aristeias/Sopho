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

public class EpeksergasiaStoixeionErgodotiController implements Initializable {
    
    @FXML
    public TextField eponimia, eponimo, onoma, patronimo, dieuthinsi, tilefono;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    public ResultSet oldrs = sopho.ResultKeeper.rs;
    public int sel = sopho.ResultKeeper.selectedIndex;
    
    public String selEponimia;
    public String selEponimo;
    public String selOnoma;
    public String selPatronimo;
    public String selTilefono;
    public String selDieuthinsi;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        try {
            oldrs.first();
            
            if(sel>0){
                oldrs.relative(sel);
            }
            
            //we keep the old values because we have to find which rows we have to update when the user saves the data.
            selEponimia = oldrs.getString("eponimia");
            selEponimo = oldrs.getString("eponimo");
            selOnoma = oldrs.getString("onoma");
            selPatronimo = oldrs.getString("patronimo");
            selDieuthinsi = oldrs.getString("dieuthinsi");
            selTilefono = oldrs.getString("tilefono");
            
            eponimia.setText(selEponimia);
            eponimo.setText(selEponimo);
            onoma.setText(selOnoma);
            patronimo.setText(selPatronimo);
            dieuthinsi.setText(selDieuthinsi);
            tilefono.setText(selTilefono);
            
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
        if((!eponimia.getText().isEmpty()||(!eponimo.getText().isEmpty()&&!onoma.getText().isEmpty()&&!patronimo.getText().isEmpty()))&&!tilefono.getText().isEmpty()){
            String sql = "UPDATE theseisergasias SET eponimia=?, eponimo=?, onoma=?, patronimo=?, dieuthinsi=?, tilefono=?, WHERE eponimia=?, eponimo=?, onoma=?, patronimo=?, dieuthinsi=?, tilefono=?";

            conn=db.ConnectDB();
            
            pst=conn.prepareStatement(sql);
            pst.setString(1, eponimia.getText());
            pst.setString(2, eponimo.getText());
            pst.setString(3, onoma.getText());
            pst.setString(4, patronimo.getText());
            pst.setString(5, dieuthinsi.getText());
            pst.setString(6, tilefono.getText());
            pst.setString(7, selEponimia);
            pst.setString(8, selEponimo);
            pst.setString(9, selOnoma);
            pst.setString(10, selPatronimo);
            pst.setString(11, selDieuthinsi);
            pst.setString(12, selTilefono);

            int flag = pst.executeUpdate();
            
            if(flag>0){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Τα στοιχεία του εργοδότη έχουν ενημερωθεί με επιτυχία!", "confirm");
                cm.showAndWait();
                Stage stage = (Stage) onoma.getScene().getWindow();
                sl.StageLoad("/sopho/EuresiErgasias/ProvoliDiathesimonTheseon.fxml", stage, true, false); //resizable true, utility false
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Τα στοιχεία του εργοδότη δεν μπόρεσαν να ενημερωθούν! Προσπαθήστε και πάλι.", "error");
                cm.showAndWait();
            }

            
        }else{//the user didn't fill the required fields
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε συμπληρώσει κάποιο από τα απαιτούμενα πεδία: Επωνυμία ή (Επώνυμο, Όνομα, Πατρώνυμο) και Τηλέφωνο!", "error");
            cm.showAndWait();
        }
    }
    
    @FXML
    public void GoBack (ActionEvent e) throws IOException{
        Stage stage = (Stage) onoma.getScene().getWindow();
        sl.StageLoad("/sopho/EuresiErgasias/EuresiErgasiasMain.fxml", stage, false, true); //resizable false, utility true
    }
}
