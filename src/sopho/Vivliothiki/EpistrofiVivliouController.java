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
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class EpistrofiVivliouController implements Initializable {

    @FXML
    public Label eponimo, onoma, patronimo, dieuthinsi, tilefono, date, titlos, siggrafeas, katigoria, selides, isbn, ekdotikos;
    @FXML
    public DatePicker dateEpistrofis;
    
    int sel = sopho.ResultKeeper.selectedIndex;
    ResultSet oldrs = sopho.ResultKeeper.rs;
            
    sopho.StageLoader sl = new sopho.StageLoader();
    
    sopho.DBClass db = new sopho.DBClass();
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            oldrs.first();
            if(sel>0){
                oldrs.relative(sel);
            }
            
            Tooltip t = new Tooltip();
            t.setText("Ημερομηνία επιστροφής");
            dateEpistrofis.setTooltip(t);//we set a tooltip because as the field is filled with the current date the prompt text will not appear.
            dateEpistrofis.setValue(LocalDate.now());//setting the date to today
            
            //setting the text to the labels
            eponimo.setText(oldrs.getString("eponimo"));
            onoma.setText(oldrs.getString("onoma"));
            patronimo.setText(oldrs.getString("patronimo"));
            dieuthinsi.setText(oldrs.getString("dieuthinsi"));
            tilefono.setText(oldrs.getString("tilefono"));
            date.setText(oldrs.getDate("date").toString());
            titlos.setText(oldrs.getString("titlos"));
            siggrafeas.setText(oldrs.getString("siggrafeas"));
            ekdotikos.setText(oldrs.getString("ekdotikos"));
            selides.setText(oldrs.getString("selides"));
            katigoria.setText(oldrs.getString("katigoria"));
            isbn.setText(oldrs.getString("isbn"));
            
        } catch (SQLException ex) {
            Logger.getLogger(EpistrofiVivliouController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) onoma.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/ListaDaneismenon.fxml", stage, false, true); //resizable false, utility true
    }
    
    @FXML
    public void Save(ActionEvent e) throws IOException, SQLException{
        
        if(dateEpistrofis.getValue()==null){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε ορίσει ημερομηνία επιστροφής του βιβλίου.", "error");
            cm.showAndWait();
        }else{
            int id = oldrs.getInt("id");
            
            String sql = "INSERT INTO vivliothikiistoriko (eponimo, onoma, patronimo, dieuthinsi, tilefono, date, dateEpistrofis, siggrafeas, titlos, ekdotikos, katigoria, selides, isbn) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            conn=db.ConnectDB();
            pst=conn.prepareStatement(sql);
            
            pst.setString(1, oldrs.getString(("eponimo")));
            pst.setString(2, oldrs.getString(("onoma")));
            pst.setString(3, oldrs.getString(("patronimo")));
            pst.setString(4, oldrs.getString(("dieuthinsi")));
            pst.setString(5, oldrs.getString(("tilefono")));
            pst.setDate(6, oldrs.getDate(("date")));
            //now we have to convert the date to a suitable format to be able to push it to the database
            if(dateEpistrofis.getValue()!=null){
                Date d = Date.from(dateEpistrofis.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                java.sql.Date sqlDate = new java.sql.Date(d.getTime());
                pst.setDate(7,sqlDate);
            }else{
                pst.setDate(7, null);
            }
            pst.setString(8, oldrs.getString(("siggrafeas")));
            pst.setString(9, oldrs.getString(("titlos")));
            pst.setString(10, oldrs.getString(("ekdotikos")));
            pst.setString(11, oldrs.getString(("katigoria")));
            pst.setString(12, oldrs.getString(("selides")));
            pst.setString(13, oldrs.getString(("isbn")));
            
            int flag = pst.executeUpdate();
            
            if(flag>0){
                
                sql="UPDATE vivliothiki SET daneismeno=0, eponimo='', onoma='', patronimo='', dieuthinsi='', tilefono='', date=null WHERE id = ?";
                
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1,oldrs.getInt("id"));
                    
                    int flag2 = pst.executeUpdate();
                    
                    if(flag2>0){
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Η επιστροφή του βιβλίου καταχωρήθηκε με επιτυχία. Δείτε το ιστορικό των δανεισμών βιβλίων.", "confirm");
                        cm.showAndWait();
                        Stage stage = (Stage) onoma.getScene().getWindow();
                        sl.StageLoad("/sopho/Vivliothiki/IstorikoDaneismou.fxml", stage, true, false); //resizable true, utility false
                    }else{
                        sopho.Messages.CustomMessageController cm2 = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Ο δανεισμός του βιβλίου καταγράφηκε στο ιστορικό αλλά δεν μπόρεσε να δηλωθεί η επιστροφή του βιβλίου στη βάση δεδομένων. Συνεπώς, το βιβλίο φαίνεται να είναι δανεισμένο ακόμα.", "error");
                        cm2.showAndWait();
                    }
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Δεν μπόρεσε να κατοχυρωθεί η επιστροφή του βιβλίου.", "error");
                cm.showAndWait();
            }
            
        }
    }
    
}
