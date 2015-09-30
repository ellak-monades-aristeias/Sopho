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
import sopho.StageLoader;

public class SearchToEditOfeloumenoiController implements Initializable {

    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    sopho.DBClass db = new sopho.DBClass();
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @FXML
    public TextField barcode,onoma,eponimo,tilefono,afm,tautotita;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    public void Search(ActionEvent event) throws IOException{
        if(CheckEmpty()){
            //we have at least one field filled
            String sql = "SELECT * FROM ofeloumenoi WHERE ";
            if(!barcode.getText().isEmpty()){
                sql = sql + "barcode = " + barcode.getText() + " AND";
            }
            if(!onoma.getText().isEmpty()){
                sql = sql + "onoma = '" + onoma.getText() + "' AND";
            }
            if(!eponimo.getText().isEmpty()){
                sql = sql + "eponimo = '" + eponimo.getText() + "' AND";
            }
            if(!tilefono.getText().isEmpty()){
                sql = sql + "tilefono = '" + tilefono.getText() + "' AND";
            }
            if(!afm.getText().isEmpty()){
                sql = sql + "afm = '" + afm.getText() + "' AND";
            }
            if(!tautotita.getText().isEmpty()){
                sql = sql + "tautotita = '" + tautotita.getText() + "'";
            }else{ // we need to remove the AND from the end of the sql string
                sql = sql.substring(0, sql.length()-3);
            }
            
            try{
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
                            if(rs.getInt("editing")==1){
                                //another user is currently editing the record.
                                sopho.Messages.CustomMessageController cmNew = new sopho.Messages.CustomMessageController(null, "Oops!", "Δυστυχώς κάποιος άλλος χρήστης επεξεργάζεται αυτή τη στιγμή τα στοιχεία του ωφελούμενου. Προσπαθήστε και πάλι αργότερα και βεβαιωθείτε ότι η καρτέλα αυτού του ωφελούμενου δεν είναι ανοιχτή σε κάποιον άλλον υπολογιστή." , "error");
                                cmNew.showAndWait();
                            }else{
                                int id = rs.getInt("id");
                                
                                //we jave to set editing to 1 to lock out other users trying to edit the same record
                                
                                sql = "UPDATE ofeloumenoi SET editing=1 WHERE id = ?";
                                
                                pst = conn.prepareStatement(sql);
                                pst.setInt(1, id);
                                
                                int flag = pst.executeUpdate();
                                
                                if(flag>0){
                                    // editing successfully set to 1.
                                    sl.StageLoad("/sopho/Ofeloumenoi/EditOfeloumenoi.fxml", stage, true, false); //resizable true, utility false.
                                }else{
                                    sopho.Messages.CustomMessageController cmNew = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Η καρτέλα του ωφελούμενο δεν κλειδώθηκε. Τα στοιχεία του ωφελούμενου μπορούν να επεξεργάζονται και από άλλον υπολογιστή αυτή τη στιγμή. Μπορεί να υπάρξει πρόβλημα κατά την αποθήκευση των αλλαγών. Προσπαθήστε και πάλι.","error");
                                    cmNew.showAndWait();
                                }
                                
                                
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

            }catch (SQLException e){
                System.out.println("Σφάλμα Βάσης Δεδομένων!");
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα...", "Υπήρξε κάποιο πρόβλημα με την αναζήτηση στη βάση δεδομένων. Δοκιμάστε και πάλι και αν δεν διορθωθεί επικοινωνήστε με τον Developer. ERROR:"+e , "error");
                cm.showAndWait();
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
