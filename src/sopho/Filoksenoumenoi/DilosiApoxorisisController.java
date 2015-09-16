package sopho.Filoksenoumenoi;

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

public class DilosiApoxorisisController implements Initializable {

    @FXML
    public Label eponimo, onoma, patronimo, date;
    @FXML
    public DatePicker dateApoxorisis;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    sopho.DBClass db = new sopho.DBClass();
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    ResultSet oldrs = sopho.ResultKeeper.rs;
    int sel = sopho.ResultKeeper.selectedIndex;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip t = new Tooltip();
        t.setText("Ημερομηνία αποχώρησης");
        dateApoxorisis.setTooltip(t);//we set tooltip because the prompt text will not appear as the datePicker is set with a value
        dateApoxorisis.setValue(LocalDate.now());
        
        try {
            oldrs.first();
            if(sel>0){
                oldrs.relative(sel);
            }
            
            eponimo.setText(oldrs.getString("eponimo"));
            onoma.setText(oldrs.getString("onoma"));
            patronimo.setText(oldrs.getString("patronimo"));
            date.setText(oldrs.getDate("date").toString());
            

        } catch (SQLException ex) {
            Logger.getLogger(DilosiApoxorisisController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) onoma.getScene().getWindow();
        sl.StageLoad("/sopho/Filoksenoumenoi/ProvoliTrexontonFiloksenoumenon.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void Save(ActionEvent e) throws IOException, SQLException{
        
        if(dateApoxorisis.getValue()==null){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε συμπληρώσει ημερομηνία αποχώρησης!", "error");
            cm.showAndWait();
        }else{
        
            String sql = "UPDATE filoksenoumenoi SET dateApoxorisis=?, apoxorisi=1 WHERE id=?";

            conn=db.ConnectDB();
            pst=conn.prepareStatement(sql);

            Date d = Date.from(dateApoxorisis.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());
            pst.setDate(1,sqlDate);
            
            pst.setInt(2, oldrs.getInt("id"));

            int flag = pst.executeUpdate();
            
            if(flag>0){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Η αποχώρηση του φιλοξενούμενου καταχωρήθηκε με επιτυχία!", "confirm");
                cm.showAndWait();
                
                Stage stage = (Stage) onoma.getScene().getWindow();
                sl.StageLoad("/sopho/Filoksenoumenoi/ProvoliIstorikouFiloksenoumenon.fxml", stage, true, false); //resizable true, utility false
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Η αποχώρηση του φιλοξενούμενου δεν μπόρεσε να καταχωρηθεί στη βάση δεδομένων. Προσπαθήστε και πάλι!", "error");
                cm.showAndWait();
            }

            
        }
    }
    
}
