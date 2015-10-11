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
package sopho.Ofeloumenoi;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.math.NumberUtils;

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
    public void initialize(URL url, ResourceBundle rb) {
        
        //initialize oikKatastasi combobox
        oikKatastasi.getItems().addAll(
            "Άγαμος",
            "Έγγαμος",
            "Διαζευγμένος",
            "Χήρος" 
        );
        
        //initialize asfForeas comboBox
        asfForeas.getItems().addAll(
            "Ανασφάλιστος",
            "ΙΚΑ",
            "ΟΓΑ",
            "ΟΑΕΕ",
            "ΕΤΑΑ",
            "ΕΟΠΥΥ",
            "ΤΠΔΥ",
            "ΤΑΠΙΤ",
            "ΕΤΑΠ – ΜΜΕ",
            "Άλλο"
        );
    
    }
    
        
    @FXML
    public void Search (ActionEvent event){
        //System.out.println("oikKatastasi sel index =" + oikKatastasi.getSelectionModel().getSelectedIndex());
        
        
        //now we must check if we have at least one field filled with data or one checkbox selected
        if(startAge.getText().isEmpty()&&endAge.getText().isEmpty()&&dimos.getText().isEmpty()&&!anergos.isSelected()&&epaggelma.getText().isEmpty()&&eisodima.getText().isEmpty()&&eksartiseis.getText().isEmpty()&&ethnikotita.getText().isEmpty()&&!metanastis.isSelected()&&!roma.isSelected()&&oikKatastasi.getSelectionModel().getSelectedIndex()==-1&&arTeknon.getText().isEmpty()&&!mellousaMama.isSelected()&&!monogoneiki.isSelected()&&!politeknos.isSelected()&&asfForeas.getSelectionModel().getSelectedIndex()==-1&&!amea.isSelected()&&!xronios.isSelected()&&pathisi.getText().isEmpty()&&!monaxiko.isSelected()&&!emfiliVia.isSelected()&&!spoudastis.isSelected()&&!anenergos.isSelected()){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Πρέπει να συμπληρώσετε τουλάχιστον ένα πεδίο ή να τσεκάρετε κάποια από τις επιλογές.", "error");
            cm.showAndWait();
        }else if((!NumberUtils.isNumber(startAge.getText())&&!startAge.getText().isEmpty())&&(!NumberUtils.isNumber(endAge.getText())&&!endAge.getText().isEmpty())){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Στα πεδία ηλικία από και ηλικία έως μπορείτε να συμπληρώσετε μόνο αριθμούς. Διορθώστε το πεδίο και προσπαθήστε και πάλι.", "error");
            cm.showAndWait();        
        }else if(!NumberUtils.isNumber(eisodima.getText())&&!eisodima.getText().isEmpty()){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Στο πεδίο εισόδημα μπορείτε να συμπληρώσετε μόνο αριθμούς. Διορθώστε το πεδίο και προσπαθήστε και πάλι.", "error");
            cm.showAndWait();        
        }else if(!NumberUtils.isNumber(arTeknon.getText())&&!arTeknon.getText().isEmpty()){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Στο πεδίο αριθμός τέκνων μπορείτε να συμπληρώσετε μόνο αριθμούς. Διορθώστε το πεδίο και προσπαθήστε και πάλι.", "error");
            cm.showAndWait();        
        }else{//the user has filled at least one field or checked a checkbox
            if((!startAge.getText().isEmpty()&&!endAge.getText().isEmpty())&&(Integer.parseInt(startAge.getText())>Integer.parseInt(endAge.getText()))){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Ελέγξτε τα πεδία όπου αναγράφονται οι ηλικίες. Το πεδίο 'από' δεν μπορεί να έχει μεγαλύτερη τιμή από το πεδίο 'έως'", "error");
                cm.showAndWait();
            }else{
                try {
                    String sql = "SELECT * FROM ofeloumenoi WHERE ";
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
                    
                    System.out.println("bare sql :" + sql);
                    
                    conn=db.ConnectDB();
                    pst = conn.prepareStatement(sql);
                    
                    System.out.println("pst sql :" + pst);
                    
                    rs = pst.executeQuery();
                    
                    rs.last();//we go to the last line to get its number
                    
                    if(rs.getRow()>0){//we have results
                        sopho.ResultKeeper.rs = rs; // we keep the results to a static var to access them later.

                        if(sopho.StageLoader.lastStage.equals("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml")){
                        
                            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Βρέθηκαν αποτελέσματα...", "confirm");
                            cm.showAndWait();

                            Stage stage = (Stage) startAge.getScene().getWindow();
                            try {
                                sl.StageLoad("/sopho/Ofeloumenoi/SearchOfeloumenoiResults.fxml", stage, true, false); //resizable true, utility false
                            } catch (IOException ex) {
                                Logger.getLogger(SearchOfeloumenoiController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                        }else{
                            
                            Stage stage = (Stage) startAge.getScene().getWindow();
                            try {
                                sl.StageLoad("/sopho/Ofeloumenoi/FiltersStatistika.fxml", stage, true, false); //resizable true, utility false
                            } catch (IOException ex) {
                                Logger.getLogger(SearchOfeloumenoiController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                        }
                        
                    }else{//we don't have results
                        
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Κρίμα...", "Δεν βρέθηκαν αποτελέσματα με τα κριτήρια που καθορίσατε. Δοκιμάστε να αλλάξετε τα κριτήρια.", "error");
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
        sl.StageLoad(sopho.StageLoader.lastStage, stage, false, true); //resizable false, utility true
    }
    
}
