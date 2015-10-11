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
package sopho.Eidi;

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
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sopho.StageLoader;

public class EidiMainController implements Initializable {

    @FXML
    public Button backButton;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    @FXML
    private void OpenEpeksergasiaEidon(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Eidi/EpeksergasiaEidon.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenEidiDothikan(ActionEvent event) throws IOException{
        sopho.DBClass db = new sopho.DBClass();
        Connection conn = db.ConnectDB();

        String sql = "SELECT * FROM eidinames WHERE active=1";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.last();
            if(rs.getRow()>0){
                StageLoader.lastStage="/sopho/Eidi/EidiMain.fxml";
                Stage stage = (Stage) backButton.getScene().getWindow();
                //we load SearchToEditOfeloumenoi because we want to search ofeloumenoi and the process is the same. We avoid creating a new identical scene
                sl.StageLoad("/sopho/Ofeloumenoi/SearchToEditOfeloumenoi.fxml", stage, true, false); //resizable true, utility false
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε εισάγει είδη στο μενού επεξεργασία ειδών, είτε έχετε επιλέξει να μην εμφανίζονται όλα τα καταχωρημένα είδη. Προσθέστε είδη ή επιλέξτε να εμφανίζονται τα υπάρχοντα είδη από το μενού 'επεξεργασία ειδών' και έπειτα καταχωρήστε είδη που παρέλαβαν οι ωφελούμενοι.", "error");
                cm.showAndWait();
            }
        } catch (SQLException ex) {
            Logger.getLogger(EidiMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void OpenIstorikoEidonOfeloumenou(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Eidi/IstorikoEidonOfeloumenou.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenStatistika(ActionEvent event) throws IOException, SQLException{
        String sql = "SELECT * FROM eidiofeloumenoi";
        sopho.DBClass db = new sopho.DBClass();
        Connection conn = db.ConnectDB();
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        
        rs.last();
        if(rs.getRow()>0){//you can't have statistics if there are no entries
            Stage stage = (Stage) backButton.getScene().getWindow();
            sl.StageLoad("/sopho/Eidi/StatistikaMain.fxml", stage, false, true); //resizable false, utility true
        }else{
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε καταχωρήσει είδη που δόθηκαν σε ωφελούμενους προκειμένου να εξάγετε στατιστικά. Καταχωρήστε είδη που δόθηκαν και έπειτα προσπαθήστε και πάλι.", "error");
            cm.showAndWait();
        }
        
        
    }
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("MainApp.fxml", stage, true, false); //resizable true, utility false
    }
    
}
