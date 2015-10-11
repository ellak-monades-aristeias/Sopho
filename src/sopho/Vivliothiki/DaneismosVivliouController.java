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
package sopho.Vivliothiki;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class DaneismosVivliouController implements Initializable {

    @FXML
    public Label siggrafeas, titlos, ekdotikos, katigoria, selides, isbn;
    @FXML
    public TextField eponimo, onoma, patronimo, dieuthinsi, tilefono;
    @FXML
    public DatePicker date;
    
    sopho.StageLoader sl = new sopho.StageLoader();
       
    sopho.DBClass db = new sopho.DBClass();
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    ResultSet oldrs = sopho.ResultKeeper.rs;
    int sel = sopho.ResultKeeper.selectedIndex;
    int selID=-1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //setting the date to today
        date.setValue(LocalDate.now());
        
        try {
            
            oldrs.first();
            if(sel>0){//we move to the row we need from rs based on the tableSelection from the previous stage.
                oldrs.relative(sel);
            }
            selID = oldrs.getInt("id"); //we get the id from the table at the previous stage to be able to update the correct row at the db.
            
            siggrafeas.setText(oldrs.getString("siggrafeas"));
            titlos.setText(oldrs.getString("titlos"));
            ekdotikos.setText(oldrs.getString("ekdotikos"));
            selides.setText(oldrs.getString("selides"));
            katigoria.setText(oldrs.getString("katigoria"));
            isbn.setText(oldrs.getString("isbn"));
            
        } catch (SQLException ex) {
            Logger.getLogger(DaneismosVivliouController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) titlos.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/DiathesimaVivlia.fxml", stage, true, false); //resizable true, utility false 
    }
    
    @FXML
    private void Save(ActionEvent event) throws IOException, SQLException{
        
        if(!eponimo.getText().isEmpty()&&!onoma.getText().isEmpty()&&!patronimo.getText().isEmpty()&&!tilefono.getText().isEmpty()&&date.getValue()!=null){
            
            String sql = "UPDATE vivliothiki SET daneismeno=1, eponimo=?, onoma=?, patronimo=?, dieuthinsi=?, tilefono=?, date=? WHERE id=?";

            conn = db.ConnectDB();
            pst = conn.prepareStatement(sql);

            pst.setString(1, eponimo.getText());
            pst.setString(2, onoma.getText());
            pst.setString(3, patronimo.getText());
            pst.setString(4, dieuthinsi.getText());
            pst.setString(5, tilefono.getText());
            if(date.getValue()==null){
                pst.setDate(6, null);
            }else{
                //converting the date to a suitable format for the db.
                Date mydate = Date.from(date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                java.sql.Date sqlDate = new java.sql.Date(mydate.getTime());
                pst.setDate(6,sqlDate);
            }
            pst.setInt(7, selID);
            
            int flag = pst.executeUpdate();
            
            if(flag>0){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Ο δανεισμός του βιβλίου καταχωρήθηκε με επιτυχία.", "confirm");
                cm.showAndWait();
                Stage stage = (Stage) titlos.getScene().getWindow();
                sl.StageLoad("/sopho/Vivliothiki/VivliothikiMain.fxml", stage, true, false); //resizable true, utility false 
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Ο δανεισμός του βιβλίου δεν μπόρεσε να καταχωρηθεί στη βάση δεδομένων. Προσπαθήστε πάλι.", "error");
                cm.showAndWait();
            }
            
        }else{
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε συμπληρώσει κάποιο από τα απαιτούμενα πεδία Επώνυμο, Όνομα, Πατρώνυμο, Τηλέφωνο, Ημερομηνία δανεισμού.", "error");
            cm.showAndWait();
        }
        
    }
    
}
