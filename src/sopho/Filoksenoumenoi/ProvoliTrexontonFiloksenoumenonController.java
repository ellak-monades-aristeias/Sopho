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

public class ProvoliTrexontonFiloksenoumenonController implements Initializable {

    @FXML
    public Button backButton;
    @FXML
    public TableView<tableManager> table;
    @FXML
    public TableColumn<tableManager, String> eponimo, onoma, patronimo, date, aitia, loipa;
    @FXML
    public TableColumn<tableManager, Integer> id;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    private ObservableList<tableManager> data;
        
    sopho.DBClass db = new sopho.DBClass();
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialzing table
        data = getInitialTableData();
                
        table.setItems(data);
        table.setEditable(true);
        id.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("id"));
        eponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        onoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        patronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        date.setCellValueFactory(new PropertyValueFactory<tableManager, String>("date"));
        date.setSortType(TableColumn.SortType.DESCENDING);
        aitia.setCellValueFactory(new PropertyValueFactory<tableManager, String>("aitia"));
        loipa.setCellValueFactory(new PropertyValueFactory<tableManager, String>("loipa"));
        
        table.getColumns().setAll(id, eponimo, onoma, patronimo, date, aitia, loipa);
        table.getSortOrder().add(date);//sorting by date in descending order

        //end of initialization of table
    }    
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Filoksenoumenoi/FiloksenoumenoiMain.fxml", stage, false, true); //resizable false, utility true
    } 
    
    @FXML
    public void Apoxorisi(ActionEvent e) throws IOException{
        
        sopho.ResultKeeper.rs = rs;
        
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε έναν φιλοξενούμενο από τον πίνακα.", "error");
            cm.showAndWait();
        }else{
            sopho.ResultKeeper.selectedIndex=sel;
            Stage stage = (Stage) backButton.getScene().getWindow();
            sl.StageLoad("/sopho/Filoksenoumenoi/DilosiApoxorisis.fxml", stage, false, true); //resizable false, utility true
        }
    }
    
    @FXML
    public void Edit(ActionEvent e) throws IOException, SQLException{
        
        sopho.ResultKeeper.rs = rs;
        
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε έναν φιλοξενούμενο από τον πίνακα.", "error");
            cm.showAndWait();
        }else{
            sopho.LockEdit le = new sopho.LockEdit();
            
            tableManager tbl = table.getSelectionModel().getSelectedItem();
            int id = tbl.getId();
            
            if(!le.CheckLock(id, "filoksenoumenoi")){//check if editing is locked because another user is currently editing the data.
                if (!le.LockEditing(true, id, "filoksenoumenoi")){//check if lock editing is successful else display message about it
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα", "Τα στοιχεία του ατόμου που επιλέξατε δεν μπόρεσαν να κλειδωθούν για επεξεργασία. Αυτό σημαίνει ότι μπορεί να επεξεργαστεί και άλλος χρήστης παράλληλα τα ίδια στοιχεία και να διατηρηθούν οι αλλαγές που θα αποθηκεύσει ο άλλος χρήστης. Μπορείτε να επεξεργαστείτε τα στοιχεία ή να βγείτε και να μπείτε και πάλι στα στοιχεία για να κλειδώσουν.", "error");
                    cm.showAndWait();
                }
                sopho.ResultKeeper.selectedIndex=sel;
                Stage stage = (Stage) backButton.getScene().getWindow();
                sl.StageLoad("/sopho/Filoksenoumenoi/EpeksergasiaFiloksenoumenou.fxml", stage, false, true); //resizable false, utility true
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Κάποιος άλλος χρήστης επεξεργάζεται αυτή τη στιγμή τον επιλεγμένο φιλοξενούμενο. Βεβαιωθείτε ότι η καρτέλα του φιλοξενούμενου δεν είναι ανοιχτή σε κάποιον άλλον υπολογιστή και προσπαθήστε και πάλι.", "error");
                cm.showAndWait();
            }
        }
    }
    
    @FXML
    public void Delete(ActionEvent e) throws IOException{
        
        sopho.ResultKeeper.rs = rs;
        
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε έναν φιλοξενούμενο από τον πίνακα.", "error");
            cm.showAndWait();
        }else{
            tableManager tbl = table.getSelectionModel().getSelectedItem();
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Είστε σίγουροι;", "Θέλετε σίγουρα να διαγράψετε τον φιλοξενούμενο με επώνυμο: "+tbl.getEponimo()+" και όνομα: "+ tbl.getOnoma() +" από τη λίστα με τους τρέχοντες φιλοξενούμενους; Δεν θα μπορείτε να ανακτήσετε τα στοιχεία του φιλοξενούμενου αυτού στη συνέχεια...", "question");
            cm.showAndWait();
            if(cm.saidYes){
                
                int idNumber = tbl.getId();
                
                String sql="DELETE FROM filoksenoumenoi WHERE id = ?";
                
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
                        sopho.Messages.CustomMessageController cm2 = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Δεν μπόρεσε να διαγραφεί ο φιλοξενούμενος από τη βάση δεδομένων", "error");
                        cm2.showAndWait();
                    }else{
                        //get the new rs and set the table again
                        //this prevents the bug of deleting a line from the table and passing the oldrs to the ResultKeeper. If the oldrs was passed and the new selectedIndex was passed to ResultKeeper the selected row of rs would not correspond to the table row because the rs would have also the deleted row of the table.
                        data = getInitialTableData();
                
                        table.setItems(data);
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ProvoliTrexontonFiloksenoumenonController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    public class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private SimpleIntegerProperty id;
        private SimpleStringProperty eponimo;
        private SimpleStringProperty onoma;
        private SimpleStringProperty patronimo;
        private SimpleStringProperty date;
        private SimpleStringProperty aitia;
        private SimpleStringProperty loipa;
        
        public tableManager(){}
        
        public tableManager(Integer id, String eponimo, String onoma, String patronimo, String date, String aitia, String loipa){
            this.id = new SimpleIntegerProperty(id);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.onoma = new SimpleStringProperty(onoma);
            this.patronimo = new SimpleStringProperty(patronimo);
            this.date = new SimpleStringProperty(date);
            this.aitia = new SimpleStringProperty(aitia);
            this.loipa = new SimpleStringProperty(loipa);
            
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
        
        public String getDate(){
            return date.get();
        }
        
        public String getAitia(){
            return aitia.get();
        }
        
        public String getLoipa(){
            return loipa.get();
        }
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData() {
        
        List<tableManager> list = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM filoksenoumenoi WHERE apoxorisi=0";
            
            conn = db.ConnectDB();
            pst=conn.prepareStatement(sql);
            
            rs = pst.executeQuery();
            
            while(rs.next()){
                // we can add data to the initial table using the following command
                list.add(new tableManager(rs.getInt("id"), rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), rs.getDate("date").toString(), rs.getString("aitia"), rs.getString("loipa")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProvoliTrexontonFiloksenoumenonController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
            
        return mydata;
    }
    
    
}
