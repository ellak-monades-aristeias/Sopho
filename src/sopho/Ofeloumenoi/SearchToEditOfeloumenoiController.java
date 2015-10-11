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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.math.NumberUtils;
import sopho.StageLoader;

public class SearchToEditOfeloumenoiController implements Initializable {

    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    sopho.DBClass db = new sopho.DBClass();
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    sopho.LockEdit le = new sopho.LockEdit();
       
    @FXML
    public TextField barcode,onoma,eponimo,tilefono,afm,tautotita;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    public void Search(ActionEvent event) throws IOException, SQLException{
        if(CheckEmpty()){
            if(!NumberUtils.isNumber(barcode.getText())&&!barcode.getText().isEmpty()){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Στο πεδίο barcode μπορείτε να συμπληρώσετε μόνο αριθμούς. Διορθώστε το πεδίο και προσπαθήστε και πάλι.", "error");
                cm.showAndWait();        
            }else{
            
                //we have at least one field filled
                String sql = "SELECT * FROM ofeloumenoi WHERE ";
                if(!barcode.getText().isEmpty()){
                    sql = sql + "barcode = " + barcode.getText() + " AND ";
                }
                if(!onoma.getText().isEmpty()){
                    sql = sql + "onoma = '" + onoma.getText() + "' AND ";
                }
                if(!eponimo.getText().isEmpty()){
                    sql = sql + "eponimo = '" + eponimo.getText() + "' AND ";
                }
                if(!tilefono.getText().isEmpty()){
                    sql = sql + "tilefono = '" + tilefono.getText() + "' AND ";
                }
                if(!afm.getText().isEmpty()){
                    sql = sql + "afm = '" + afm.getText() + "' AND ";
                }
                if(!tautotita.getText().isEmpty()){
                    sql = sql + "tautotita = '" + tautotita.getText() + "'";
                }else{ // we need to remove the AND from the end of the sql string
                    sql = sql.substring(0, sql.length()-4);
                }


                    System.out.println("Search sql: " + sql);
                    conn = db.ConnectDB();
                    pst = conn.prepareStatement(sql);
                    rs= pst.executeQuery();

                    // checking if there is at least one result

                    rs.last(); // setting the resultSet to the last line to get its index number.

                    if(rs.getRow()>0){
                        sopho.ResultKeeper.rs = rs; //we are keeping the resultSet to a static var to be able to get the data at the next scene.
                        Stage stage = (Stage) onoma.getScene().getWindow();
                        if(rs.getRow()==1){
                            // there is only one result
                            sopho.ResultKeeper.selectedIndex=0;
                            sopho.ResultKeeper.multipleResults=false; //we have to know if there are multiple results to set properly the backButton at EditOfeloumenoiController

                            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Βρέθηκε ο ωφελούμενος: " + rs.getString("eponimo") + " " + rs.getString("onoma") , "confirm");
                            cm.showAndWait();
                            if(StageLoader.lastStage.equals("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml")){

                                int id = rs.getInt("id");

                                if(!le.CheckLock(id, "ofeloumenoi")){//check if editing is locked because another user is currently editing the data.
                                    if (!le.LockEditing(true, id, "ofeloumenoi")){//check if lock editing is successful else display message about it
                                        sopho.Messages.CustomMessageController cm2 = new sopho.Messages.CustomMessageController(null, "Πρόβλημα", "Τα στοιχεία του ατόμου που επιλέξατε δεν μπόρεσαν να κλειδωθούν για επεξεργασία. Αυτό σημαίνει ότι μπορεί να επεξεργαστεί και άλλος χρήστης παράλληλα τα ίδια στοιχεία και να διατηρηθούν οι αλλαγές που θα αποθηκεύσει ο άλλος χρήστης. Μπορείτε να επεξεργαστείτε τα στοιχεία ή να βγείτε και να μπείτε και πάλι στα στοιχεία για να κλειδώσουν.", "error");
                                        cm2.showAndWait();
                                    }
                                    sl.StageLoad("/sopho/Ofeloumenoi/EditOfeloumenoi.fxml", stage, true, false); //resizable true, utility false.
                                }else{
                                    sopho.Messages.CustomMessageController cm3 = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Κάποιος άλλος χρήστης επεξεργάζεται αυτή τη στιγμή τον επιλεγμένο ωφελούμενο. Βεβαιωθείτε ότι η καρτέλα του ωφελούμενου δεν είναι ανοιχτή σε κάποιον άλλον υπολογιστή και προσπαθήστε και πάλι.", "error");
                                    cm3.showAndWait();
                                }

                            }else if (StageLoader.lastStage.equals("/sopho/Eidi/EidiMain.fxml")){
                                sl.StageLoad("/sopho/Eidi/EidiDothikan.fxml", stage, true, false); //resizable true, utility false.
                            }
                        }else{
                            //there are more than one results. We need to choose from them
                            sopho.ResultKeeper.multipleResults=true; //we have to know if there are multiple results to set properly the backButton at EditOfeloumenoiController
                            sl.StageLoad("/sopho/Ofeloumenoi/MultipleSearchResults.fxml", stage, true, false); //resizable true, utility false.

                        }
                    }else{
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Κρίμα...", "Δε βρέθηκε κάποιος ωφελούμενος με τα στοιχεία που συμπληρώσατε. Δοκιμάστε να αλλάξετε τα στοιχεία.", "error");
                        cm.showAndWait();
                    }
            }

               
        }
    }
    
    public boolean CheckEmpty(){
        if(barcode.getText().isEmpty()&&onoma.getText().isEmpty()&&eponimo.getText().isEmpty()&&tilefono.getText().isEmpty()&&afm.getText().isEmpty()&&tautotita.getText().isEmpty()){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να συμπληρώσετε τουλάχιστον ένα πεδίο προκειμένου να κάνετε αναζήτηση στους ωφελούμενους.", "error");
            cm.showAndWait();
            return false;
        }
        return true;
    }
    
    @FXML
    public void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) onoma.getScene().getWindow();
        sl.StageLoad(StageLoader.lastStage, stage, true, false); // resizable true, utility false
    }
    
}
