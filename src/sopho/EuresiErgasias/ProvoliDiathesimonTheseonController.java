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
package sopho.EuresiErgasias;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ProvoliDiathesimonTheseonController implements Initializable {
    @FXML
    public Button backButton;
    @FXML
    public TableView<tableManager> table;
    @FXML
    public TableColumn<tableManager, String> thesi, eponimia, eponimo, onoma, patronimo, tilefono, dieuthinsi;
    @FXML
    public TableColumn<tableManager, Integer> id;
    
    private ObservableList<tableManager> data;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    sopho.LockEdit le = new sopho.LockEdit();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialzing table
        data = getInitialTableData();
        
        table.setItems(data);
        
        id.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("id"));
        thesi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("thesi"));
        eponimia.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimia"));
        eponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        onoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        patronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        tilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tilefono"));
        dieuthinsi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dieuthinsi"));
        
        table.getColumns().setAll(id, thesi, eponimia, eponimo, onoma, patronimo, tilefono, dieuthinsi);
        //end of initialization of table 
    }   
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/EuresiErgasias/EuresiErgasiasMain.fxml", stage, false, true); //resizable false, utility true
    }  
        
    @FXML
    public void Select(ActionEvent e) throws IOException{
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε μια θέση από τον πίνακα προκειμένου να την κατοχυρώσετε σε κάποιον", "error");
            cm.showAndWait();
        }else{
            sopho.ResultKeeper.selectedIndex=sel;
            Stage stage = (Stage) backButton.getScene().getWindow();
            sl.StageLoad("/sopho/EuresiErgasias/KatoxirosiThesis.fxml", stage, true, false); //resizable true, utility false
        }
    }
    
    @FXML
    public void Edit(ActionEvent e) throws IOException, SQLException{
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε μια θέση από τον πίνακα.", "error");
            cm.showAndWait();
        }else{
            sopho.ResultKeeper.selectedIndex=sel;
        
            tableManager tbl = table.getSelectionModel().getSelectedItem();
            int id = tbl.getId();
            
            if(!le.CheckLock(id, "theseisergasias")){//check if editing is locked because another user is currently editing the data.
                if (!le.LockEditing(true, id, "theseisergasias")){//check if lock editing is successful else display message about it
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα", "Τα στοιχεία του ατόμου που επιλέξατε δεν μπόρεσαν να κλειδωθούν για επεξεργασία. Αυτό σημαίνει ότι μπορεί να επεξεργαστεί και άλλος χρήστης παράλληλα τα ίδια στοιχεία και να διατηρηθούν οι αλλαγές που θα αποθηκεύσει ο άλλος χρήστης. Μπορείτε να επεξεργαστείτε τα στοιχεία ή να βγείτε και να μπείτε και πάλι στα στοιχεία για να κλειδώσουν.", "error");
                    cm.showAndWait();
                }
                sopho.ResultKeeper.selectedIndex=sel;
                Stage stage = (Stage) backButton.getScene().getWindow();
                sl.StageLoad("/sopho/EuresiErgasias/EpeksergasiaThesisErgasias.fxml", stage, true, false); //resizable true, utility false
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Κάποιος άλλος χρήστης επεξεργάζεται αυτή τη στιγμή την επιλεγμένη θέση εργασίας. Βεβαιωθείτε ότι η καρτέλα της θέσης εργασίας δεν είναι ανοιχτή σε κάποιον άλλον υπολογιστή και προσπαθήστε και πάλι.", "error");
                cm.showAndWait();
            }
        }
    }
    
    @FXML
    public void EditErgodotis(ActionEvent e) throws IOException{
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε έναν εργοδότη από τον πίνακα.", "error");
            cm.showAndWait();
        }else{
            sopho.ResultKeeper.selectedIndex=sel;
            Stage stage = (Stage) backButton.getScene().getWindow();
            sl.StageLoad("/sopho/EuresiErgasias/EpeksergasiaStoixeionErgodoti.fxml", stage, true, false); //resizable true, utility false
        }
    }
    
    @FXML
    public void Delete(ActionEvent e){
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε μια θέση από τον πίνακα για να διαγράψετε!", "error");
            cm.showAndWait();
        }else{
            tableManager tbl = table.getSelectionModel().getSelectedItem();
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Είστε σίγουροι;", "Θέλετε σίγουρα να διαγράψετε τη θέση: "+tbl.getThesi()+" από τις διαθέσιμες θέσεις; Δεν θα μπορείτε να ανακτήσετε τα στοιχεία της θέσης αυτής στη συνέχεια...", "question");
            cm.showAndWait();
            if(cm.saidYes){
                                
                int idNumber = tbl.getId();
                
                String sql="DELETE FROM theseisergasias WHERE id = ?";
                
                sopho.DBClass db = new sopho.DBClass();
                Connection conn=null;
                PreparedStatement pst = null;
                ResultSet rs = null;
                
                conn = db.ConnectDB();
                try {
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1,idNumber);
                    
                    int flag = pst.executeUpdate();
                    
                    if(flag<=0){
                        sopho.Messages.CustomMessageController cm2 = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Δεν μπόρεσε να διαγραφεί η θέση εργασίας από τη βάση δεδομένων", "error");
                        cm2.showAndWait();
                    }else{
                        //get the new rs and set the table again
                        //this prevents the bug of deleting a line from the table and passing the oldrs to the ResultKeeper. If the oldrs was passed and the new selectedIndex was passed to ResultKeeper the selected row of rs would not correspond to the table row because the rs would have also the deleted row of the table.
                        data = getInitialTableData();
                
                        table.setItems(data);
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ProvoliDiathesimonTheseonController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
        
    public class tableManager {
        
        private SimpleIntegerProperty id;
        private SimpleStringProperty eponimia;
        private SimpleStringProperty eponimo;
        private SimpleStringProperty onoma;
        private SimpleStringProperty patronimo;
        private SimpleStringProperty dieuthinsi;
        private SimpleStringProperty tilefono;
        private SimpleStringProperty thesi;
        
        public tableManager(){}
        
        public tableManager(Integer id, String eponimia, String eponimo, String onoma, String patronimo, String dieuthinsi, String tilefono, String thesi){
            this.id = new SimpleIntegerProperty(id);
            this.eponimia = new SimpleStringProperty(eponimia);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.onoma = new SimpleStringProperty(onoma);
            this.patronimo = new SimpleStringProperty(patronimo);
            this.dieuthinsi = new SimpleStringProperty(dieuthinsi);
            this.tilefono = new SimpleStringProperty(tilefono);
            this.thesi = new SimpleStringProperty(thesi);
        }
        
        public Integer getId(){
            return id.get();
        }
        
        public String getEponimia(){
            return eponimia.get();
        }
        
        public String getEponimo(){
            return eponimo.get();
        }
        
        public String getOnoma(){
            return onoma.get();
        }
        
        public String getPatronimo(){
            return patronimo.get();
        }
        
        public String getDieuthinsi(){
            return dieuthinsi.get();
        }
        
        public String getTilefono(){
            return tilefono.get();
        }
        
        public String getThesi(){
            return thesi.get();
        }
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        List<tableManager> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM theseisergasias";
            
            sopho.DBClass db = new sopho.DBClass();
            Connection conn=null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            
            conn=db.ConnectDB();
            pst=conn.prepareStatement(sql);
            
            rs=pst.executeQuery();
            
            sopho.ResultKeeper.rs=rs;
            
            rs.last();
            if(rs.getRow()>0){
                rs.beforeFirst();
                while(rs.next()){
                    // we can add data to the initial table using the following command
                    list.add(new tableManager(rs.getInt("id"), rs.getString("eponimia"), rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), rs.getString("dieuthinsi"), rs.getString("tilefono"), rs.getString("thesi")));
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ProvoliDiathesimonTheseonController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        return mydata;
    }    
    
}
