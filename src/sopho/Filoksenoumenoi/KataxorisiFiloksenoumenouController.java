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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class KataxorisiFiloksenoumenouController implements Initializable {

    @FXML
    public TextField eponimo, onoma, patronimo, dieuthinsi, tilefono, tautotita, aitia;
    @FXML
    public TextArea loipa;
    @FXML
    public DatePicker date;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //setting a tooltip for the date because it will be filled and the prompt text will not appear
        Tooltip t = new Tooltip();
        t.setText("Ημερομηνία άφιξης");
        date.setTooltip(t);
        
        date.setValue(LocalDate.now()); //setting the datePicker to current date.
    }
    
    @FXML
    public void Save(ActionEvent e) throws IOException, SQLException{
        if(!eponimo.getText().isEmpty()&&!onoma.getText().isEmpty()&&!patronimo.getText().isEmpty()&&!aitia.getText().isEmpty()){
            
            sopho.DBClass db = new sopho.DBClass();
            Connection conn=null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            
            String sql = "INSERT INTO filoksenoumenoi (eponimo, onoma, patronimo, dieuthinsi, tilefono, tautotita, aitia, date, loipa, apoxorisi) VALUES (?,?,?,?,?,?,?,?,?,?)";

            conn=db.ConnectDB();
            pst = conn.prepareStatement(sql);
            pst.setString(1, eponimo.getText());
            pst.setString(2, onoma.getText());
            pst.setString(3, patronimo.getText());
            pst.setString(4, dieuthinsi.getText());
            pst.setString(5, tilefono.getText());
            pst.setString(6, tautotita.getText());
            pst.setString(7, aitia.getText());            
            //now we have to convert the date to a suitable format to be able to push it to the database
            if(date.getValue()!=null){
                Date d = Date.from(date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                java.sql.Date sqlDate = new java.sql.Date(d.getTime());
                pst.setDate(8,sqlDate);
            }else{
                pst.setDate(8, null);
            }
            pst.setString(9, loipa.getText());
            pst.setInt(10, 0);
            
            int flag = pst.executeUpdate();
            
            if(flag>0){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Τα στοιχεία του φιλοξενούμενου καταχωρήθηκαν με επιτυχία.", "confirm");
                cm.showAndWait();
                Stage stage = (Stage) onoma.getScene().getWindow();
                sl.StageLoad("/sopho/Filoksenoumenoi/FiloksenoumenoiMain.fxml", stage, false, true); //resizable false, utility true
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Τα στοιχεία του φιλοξενούμενου δεν μπόρεσαν να καταχωρηθούν. Προσπαθήστε και πάλι.", "error");
                cm.showAndWait();
            }
            
        }else{
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε συμπληρώσει κάποιο από τα απαιτούμενα πεδία: Επώνυμο, Όνομα, Πατρώνυμο και Αιτία φιλοξενίας.", "error");
            cm.showAndWait();
        }
    }
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) onoma.getScene().getWindow();
        sl.StageLoad("/sopho/Filoksenoumenoi/FiloksenoumenoiMain.fxml", stage, false, true); //resizable false, utility true
    }
    
}
