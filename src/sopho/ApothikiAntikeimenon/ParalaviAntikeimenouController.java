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
package sopho.ApothikiAntikeimenon;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ParalaviAntikeimenouController implements Initializable {

    @FXML
    public TextField eponimo, onoma, patronimo, dieuthinsi, tilefono;
    @FXML
    public Label eponimo2, onoma2, patronimo2, dieuthinsi2, tilefono2, date2, antikeimeno;
    @FXML
    public DatePicker date;
    
    ObservableList<ParadosiAntikeimenouController.tableManager> data;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    sopho.DBClass db = new sopho.DBClass();
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    ResultSet oldrs = sopho.ResultKeeper.rs;
    int selectedIndex = sopho.ResultKeeper.selectedIndex;
    
    int antikeimenoID=-1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            oldrs.beforeFirst();
            oldrs.relative(selectedIndex);
            
            antikeimenoID=oldrs.getInt("id");
            
            antikeimeno.setText(oldrs.getString("antikeimeno"));
            onoma2.setText(oldrs.getString("onoma"));
            eponimo2.setText(oldrs.getString("eponimo"));
            patronimo2.setText(oldrs.getString("patronimo"));
            dieuthinsi2.setText(oldrs.getString("dieuthinsi"));
            tilefono2.setText(oldrs.getString("tilefono"));
            date2.setText(oldrs.getDate("date").toString());
            
        } catch (SQLException ex) {
            Logger.getLogger(ParalaviAntikeimenouController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        date.setValue(LocalDate.now());
    }
    
    @FXML
    public void Save (ActionEvent e ) throws IOException{
        if(!eponimo.getText().isEmpty()&&!onoma.getText().isEmpty()&&!patronimo.getText().isEmpty()&&!tilefono.getText().isEmpty()){
            String sql = "INSERT INTO apothikiparalavi (eponimo, onoma, patronimo, dieuthinsi, tilefono, date, antikeimeno) VALUES (?,?,?,?,?,?,?)";

            conn=db.ConnectDB();

            LocalDate ld = date.getValue();
            Calendar c =  Calendar.getInstance();
            c.set(ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());
            Date mydate = c.getTime();
            java.sql.Date sqlDate = new java.sql.Date(mydate.getTime());
            
            try {
                pst=conn.prepareStatement(sql);

                pst.setString(1, eponimo.getText());
                pst.setString(2, onoma.getText());
                pst.setString(3, patronimo.getText());
                pst.setString(4, dieuthinsi.getText());
                pst.setString(5, tilefono.getText());
                pst.setObject(6, sqlDate);
                pst.setString(7, antikeimeno.getText());
                
                System.out.println(pst.toString());

                int flag = pst.executeUpdate();

                if(flag>0){
                    
                    sql="DELETE FROM apothikiparadosi WHERE id = ?";
                    pst=conn.prepareStatement(sql);
                    pst.setInt(1, antikeimenoID);
                    flag=pst.executeUpdate();
                    
                    System.out.println("delete pst" + pst.toString());
                    
                    if(flag>0){
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Η παραλαβή του αντικειμένου έχει καταχωρηθεί με επιτυχία!", "confirm");
                        cm.showAndWait();
                        Stage stage = (Stage) onoma.getScene().getWindow();
                        sl.StageLoad("/sopho/ApothikiAntikeimenon/ApothikiMain.fxml", stage, false, true); //resizable false, utility true 
                    }else{
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Η παραλαβή του αντικειμένου έχει καταχωρηθεί. Ωστόσο δεν μπόρεσε να διαγραφεί το αντικείμενο από τη λίστα με τα διαθέσιμα αντικείμενα. Θα πρέπει να διαγράψετε το αντικείμενο χειροκίνητα.", "error");
                        cm.showAndWait();
                    }
                }else{
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Η παραλαβή του αντικειμένου δεν μπόρεσε να καταχωρηθεί στη βάση δεδομένων. Προσπαθήστε και πάλι.", "error");
                    cm.showAndWait();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ParadosiAntikeimenouController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else{//the user didn't fill the required fields
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε συμπληρώσει κάποιο από τα απαιτούμενα πεδία: Επώνυμο, Όνομα, Πατρώνυμο, Τηλέφωνο!", "error");
            cm.showAndWait();
        }
    }
    
    @FXML
    public void GoBack (ActionEvent e ) throws IOException{
        Stage stage = (Stage) onoma.getScene().getWindow();
        sl.StageLoad("/sopho/ApothikiAntikeimenon/DiathesimaAntikeimena.fxml", stage, true, false);
    }
    
    
}
