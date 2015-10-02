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

public class ProvoliAtomonPouZitounController implements Initializable {

    @FXML
    public TableView<tableManager> table;
    @FXML
    public TableColumn<tableManager, String> eponimo, onoma, patronimo, imGennisis, eidikotita, tilefono, dieuthinsi, dimos, ekpaideusi, oikKatastasi, diploma, empeiria;
    @FXML
    public TableColumn<tableManager, Integer> id;
    @FXML
    public Button backButton;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    sopho.LockEdit le = new sopho.LockEdit();
    
    private ObservableList<tableManager> data;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialzing table
        data = getInitialTableData();
        
        table.setItems(data);
        id.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("id"));
        eponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        onoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        patronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        imGennisis.setCellValueFactory(new PropertyValueFactory<tableManager, String>("imGennisis"));
        eidikotita.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eidikotita"));
        tilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tilefono"));
        dieuthinsi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dieuthinsi"));
        dimos.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dimos"));
        ekpaideusi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("ekpaideusi"));
        oikKatastasi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("oikKatastasi"));
        diploma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("diploma"));
        empeiria.setCellValueFactory(new PropertyValueFactory<tableManager, String>("empeiria"));
                
        table.getColumns().setAll(id, eponimo, onoma, patronimo, imGennisis, eidikotita, tilefono, dieuthinsi, dimos, ekpaideusi, oikKatastasi, diploma, empeiria);
        //end of initialization of table 
    }
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/EuresiErgasias/EuresiErgasiasMain.fxml", stage, false, true); //resizable false, utility true
    }
    
    @FXML
    public void Edit(ActionEvent e) throws IOException, SQLException{
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε ένα άτομο από τον πίνακα.", "error");
            cm.showAndWait();
        }else{
            tableManager tbl = table.getSelectionModel().getSelectedItem();
            int id = tbl.getId();
            
            if(!le.CheckLock(id, "zitounergasia")){//check if editing is locked because another user is currently editing the data.
                if (!le.LockEditing(true, id, "zitounergasia")){//check if lock editing is successful else display message about it
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα", "Τα στοιχεία του ατόμου που επιλέξατε δεν μπόρεσαν να κλειδωθούν για επεξεργασία. Αυτό σημαίνει ότι μπορεί να επεξεργαστεί και άλλος χρήστης παράλληλα τα ίδια στοιχεία και να διατηρηθούν οι αλλαγές που θα αποθηκεύσει ο άλλος χρήστης. Μπορείτε να επεξεργαστείτε τα στοιχεία ή να βγείτε και να μπείτε και πάλι στα στοιχεία για να κλειδώσουν.", "error");
                    cm.showAndWait();
                }
                sopho.ResultKeeper.selectedIndex=sel;
                Stage stage = (Stage) backButton.getScene().getWindow();
                sl.StageLoad("/sopho/EuresiErgasias/EpeksergasiaAtomouPouZita.fxml", stage, true, false); //resizable true, utility false
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Κάποιος άλλος χρήστης επεξεργάζεται αυτή τη στιγμή το επιλεγμένο άτομο. Βεβαιωθείτε ότι η καρτέλα του επιλεγμένου ατόμου δεν είναι ανοιχτή σε κάποιον άλλον υπολογιστή και προσπαθήστε και πάλι.", "error");
                cm.showAndWait();
            }    
        }
    }
    
    @FXML
    public void Delete(ActionEvent e){
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε ένα άτομο από τον πίνακα για να διαγράψετε!", "error");
            cm.showAndWait();
        }else{
            tableManager tbl = table.getSelectionModel().getSelectedItem();
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Είστε σίγουροι;", "Θέλετε σίγουρα να διαγράψετε το άτομο με στοιχεία Επώνυμο: " + tbl.getEponimo() + " και Όνομα: " + tbl.getOnoma() + " από τη λίστα με τα άτομα που ζητούν εργασία; Δεν θα μπορείτε να ανακτήσετε τα στοιχεία του ατόμου αυτού στη συνέχεια...", "question");
            cm.showAndWait();
            if(cm.saidYes){
                
                int idNumber = tbl.getId();
                
                String sql="DELETE FROM zitounergasia WHERE id = ?";
                
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
                        sopho.Messages.CustomMessageController cm2 = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Δεν μπόρεσε να διαγραφεί το άτομο από τη βάση δεδομένων", "error");
                        cm2.showAndWait();
                    }else{
                        //get the new rs and set the table again
                        //this prevents the bug of deleting a line from the table and passing the oldrs to the ResultKeeper. If the oldrs was passed and the new selectedIndex was passed to ResultKeeper the selected row of rs would not correspond to the table row because the rs would have also the deleted row of the table.
                        data = getInitialTableData();
                
                        table.setItems(data);
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ProvoliAtomonPouZitounController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public class tableManager {
        
        
        private SimpleIntegerProperty id;
        private SimpleStringProperty eponimo;
        private SimpleStringProperty onoma;
        private SimpleStringProperty patronimo;
        private SimpleStringProperty imGennisis;
        private SimpleStringProperty eidikotita;
        private SimpleStringProperty tilefono;
        private SimpleStringProperty dieuthinsi;
        private SimpleStringProperty dimos;
        private SimpleStringProperty ekpaideusi;
        private SimpleStringProperty oikKatastasi;
        private SimpleStringProperty diploma;
        private SimpleStringProperty empeiria;
        
        public tableManager(){}
        
        public tableManager(int id, String eponimo, String onoma, String patronimo, String imGennisis, String eidikotita, String tilefono, String dieuthinsi, String dimos, String ekpaideusi, String oikKatastasi, String diploma, String empeiria){
            this.id = new SimpleIntegerProperty(id);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.onoma = new SimpleStringProperty(onoma);
            this.patronimo = new SimpleStringProperty(patronimo);
            this.imGennisis = new SimpleStringProperty(imGennisis);
            this.eidikotita = new SimpleStringProperty(eidikotita);
            this.tilefono = new SimpleStringProperty(tilefono);
            this.dieuthinsi = new SimpleStringProperty(dieuthinsi);
            this.dimos = new SimpleStringProperty(dimos);
            this.ekpaideusi = new SimpleStringProperty(ekpaideusi);
            this.oikKatastasi = new SimpleStringProperty(oikKatastasi);
            this.diploma = new SimpleStringProperty(diploma);
            this.empeiria = new SimpleStringProperty(empeiria);    
        }
        
        public Integer getId(){
            return id.get();
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
        
        public String getImGennisis(){
            return imGennisis.get();
        }
        
        public String getEidikotita(){
            return eidikotita.get();
        }
        
        public String getDieuthinsi(){
            return dieuthinsi.get();
        }
        
        public String getDimos(){
            return dimos.get();
        }
        
        public String getTilefono(){
            return tilefono.get();
        }
        
        public String getEkpaideusi(){
            return ekpaideusi.get();
        }
        
        public String getOikKatastasi(){
            return oikKatastasi.get();
        }
        
        public String getDiploma(){
            return diploma.get();
        }
        
        public String getEmpeiria(){
            return empeiria.get();
        }
        
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        List<tableManager> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM zitounergasia";
            
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
                    String ekpaideusi = "";
                    
                    //we have to convert the 0 and 1 value from the db to strings
                    if(rs.getInt("ye")==1){
                        ekpaideusi += "ΥΕ, ";
                    }
                    if(rs.getInt("de")==1){
                        ekpaideusi += "ΔΕ, ";
                    }
                    if(rs.getInt("te")==1){
                        ekpaideusi += "ΤΕ, ";
                    }
                    if(rs.getInt("pe")==1){
                        ekpaideusi += "ΠΕ, ";
                    }
                    if(ekpaideusi.length()>1){
                        ekpaideusi = ekpaideusi.substring(0, ekpaideusi.length()-2);//to remove the space and the comma
                    }
                    
                    String oikKatastasi = "";
                    
                    //we have to convert oikKatastasi to String
                    if(rs.getInt("oikKatastasi")==0){
                        oikKatastasi = "Άγαμος";
                    }else if(rs.getInt("oikKatastasi")==1){
                        oikKatastasi = "Έγγαμος";
                    }else if(rs.getInt("oikKatastasi")==2){
                        oikKatastasi = "Διαζευγμένος";
                    }else if(rs.getInt("oikKatastasi")==3){
                        oikKatastasi = "Χήρος";
                    }
                    
                    // we can add data to the initial table using the following command
                    list.add(new tableManager(rs.getInt("id"), rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), rs.getDate("imGennisis").toString(), rs.getString("eidikotita"), rs.getString("tilefono"), rs.getString("dieuthinsi"), rs.getString("dimos"), ekpaideusi, oikKatastasi, rs.getString("diploma"), rs.getString("empeiria")));
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ProvoliAtomonPouZitounController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        return mydata;
    }
    
}
