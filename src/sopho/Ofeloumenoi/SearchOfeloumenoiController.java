package sopho.Ofeloumenoi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SearchOfeloumenoiController implements Initializable {

    @FXML
    public TextField startAge, endAge, dimos, epaggelma, eisodima, eksartiseis, ethnikotita, arTeknon, pathisi;
    @FXML
    public CheckBox anergos, metanastis, roma, mellousaMama, monogoneiki, politeknos, amea, xronios, monaxiko, emfiliVia, spoudastis, anenergos;
    @FXML
    public ComboBox oikKatastasi, asfForeas;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    sopho.DBClass db = new sopho.DBClass();
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    @FXML
    public void Search (ActionEvent event){
        System.out.println("oikKatastasi sel index =" + oikKatastasi.getSelectionModel().getSelectedIndex());
        //now we must check if we have at least one field filled with data or one checkbox selected
        if(startAge.getText().isEmpty()&&endAge.getText().isEmpty()&&dimos.getText().isEmpty()&&!anergos.isSelected()&&epaggelma.getText().isEmpty()&&eisodima.getText().isEmpty()&&eksartiseis.getText().isEmpty()&&ethnikotita.getText().isEmpty()&&!metanastis.isSelected()&&!roma.isSelected()&&oikKatastasi.getSelectionModel().getSelectedIndex()==0&&arTeknon.getText().isEmpty()&&!mellousaMama.isSelected()&&!monogoneiki.isSelected()&&!politeknos.isSelected()&&asfForeas.getSelectionModel().getSelectedIndex()==0&&!amea.isSelected()&&!xronios.isSelected()&&pathisi.getText().isEmpty()&&!monaxiko.isSelected()&&!emfiliVia.isSelected()&&!spoudastis.isSelected()&&!anenergos.isSelected()){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Πρέπει να συμπληρώσετε τουλάχιστον ένα πεδίο ή να τσεκάρετε κάποια από τις επιλογές.", "error");
            cm.showAndWait();
        }else{//the user has filled at least one field or checked a checkbox
            if(Integer.parseInt(startAge.getText())>Integer.parseInt(endAge.getText())){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Ελέγξτε τα πεδία όπου αναγράφονται οι ηλικίες. Το πεδίο 'από' δεν μπορεί να έχει μεγαλύτερη τιμή από το πεδίο 'έως'", "error");
                cm.showAndWait();
            }else{
                try {
                    /*//we have to convert the age to year of birth because the data are stored in birthdate format in the database
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    Date today = new Date();//getting current date
                    System.out.println(sdf.format(today));
                    Calendar c = Calendar.getInstance();
                    c.setTime(today);
                    c.add(Calendar.YEAR, -Integer.parseInt(endAge.getText()));// it is kind of weird that we use the endAge for the startDate, but if we name them otherwise the startDate will be earlier in calendar than the endDate
                    Date startDate = c.getTime();
                    System.out.println("StartDate " + startDate);
                    c.setTime(today);
                    c.add(Calendar.YEAR, -Integer.parseInt(startAge.getText()));
                    Date endDate = c.getTime();
                    System.out.println("EndDate " + endDate);
                    */
                    String sql = "SELECT * FROM ofeloumenoi WHERE";
                    if(!startAge.getText().isEmpty()){
                        sql += "FLOOR(TIMESTAMPDIFF(hour,imGennisis,CURRENT_TIMESTAMP())/8766)>="+Integer.parseInt(startAge.getText())+" AND ";
                    }
                    if(!endAge.getText().isEmpty()){
                        sql += "FLOOR(TIMESTAMPDIFF(hour,imGennisis,CURRENT_TIMESTAMP())/8766)<="+Integer.parseInt(endAge.getText())+" AND ";
                    }
                    if(!dimos.getText().isEmpty()){
                        sql += "dimos = '"+dimos.getText()+"' AND ";
                    }
                    if(anergos.isSelected()){
                        sql += "anergos=1 AND ";
                    }
                    if(!epaggelma.getText().isEmpty()){
                        sql += "epaggelma='"+epaggelma.getText()+"' AND ";
                    }
                    if(!eisodima.getText().isEmpty()){
                        sql += "eisodima='"+eisodima.getText()+"' AND ";
                    }
                    if(!eksartiseis.getText().isEmpty()){
                        sql += "eksartiseis='"+eksartiseis.getText()+"' AND ";
                    }
                    if(!ethnikotita.getText().isEmpty()){
                        sql += "ethnikotita='"+ethnikotita.getText()+"' AND ";
                    }
                    if(metanastis.isSelected()){
                        sql += "metanastis=1 AND ";
                    }
                    if(roma.isSelected()){
                        sql += "roma=1 AND ";
                    }
                    int oikSelected = oikKatastasi.getSelectionModel().getSelectedIndex();
                    if(oikSelected>0){
                        sql += "oikKatastasi="+oikSelected + " AND ";
                    }
                    if(!arTeknon.getText().isEmpty()){
                        sql += "arithmosTeknon='"+arTeknon.getText()+"' AND ";
                    }
                    if(mellousaMama.isSelected()){
                        sql += "mellousaMama=1 AND ";
                    }
                    if(monogoneiki.isSelected()){
                        sql += "monogoneiki=1 AND ";
                    }
                    if(politeknos.isSelected()){
                        sql += "politeknos=1 AND ";
                    }
                    int asfSelected = asfForeas.getSelectionModel().getSelectedIndex();
                    if(asfSelected>0){
                        sql += "asfForeas="+asfSelected + " AND ";
                    }
                    if(amea.isSelected()){
                        sql += "amea=1 AND ";
                    }
                    if(xronios.isSelected()){
                        sql += "xronios=1 AND ";
                    }
                    if(!pathisi.getText().isEmpty()){
                        sql += "pathisi='"+pathisi.getText()+"' AND ";
                    }
                    if(monaxiko.isSelected()){
                        sql += "monaxikos=1 AND ";
                    }
                    if(emfiliVia.isSelected()){
                        sql += "emfiliVia=1 AND ";
                    }
                    if(spoudastis.isSelected()){
                        sql += "spoudastis=1 AND ";
                    }
                    if(anenergos.isSelected()){
                        sql += "anenergos=1 AND ";
                    }
                    sql = sql.substring(0, sql.length()-4); //we have to remove the AND form the end of the string
                    
                    conn=db.ConnectDB();
                    pst = conn.prepareStatement(sql);
                    
                    rs = pst.executeQuery();
                    
                    rs.last();//we go to the last line to get its number
                    
                    if(rs.getRow()>0){//we have results
                        sopho.ResultKeeper.rs = rs; // we keep the results to a static var to access them later.
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Βρέθηκαν αποτελέσματα...", "confirm");
                        cm.showAndWait();
                        //TODO stage load to display results.
                        
                    }else{//we don't have results
                        
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Κρίμα...", "Δεν βρέθηκαν αποτελέσματα με τα κριτήρια που καθορίσατε. Δοκιμάστε να αλλάξετε τα κριτήρια της αναζήτησης.", "error");
                        cm.showAndWait();
                        
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(SearchOfeloumenoiController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    @FXML
    public void GoBack (ActionEvent event) throws IOException{
        Stage stage = (Stage) startAge.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml", stage, false, true); //resizable false, utility true
    }
    
}
