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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

public class MultipleSearchResultsController implements Initializable {

    @FXML
    public Button backButton;
    @FXML
    private TableView <tableManager> resultTable;
    @FXML
    private TableColumn <tableManager, Integer> colId;
    @FXML
    private TableColumn <tableManager, String> colBarcode;
    @FXML
    private TableColumn <tableManager, String> colOnoma;
    @FXML
    private TableColumn <tableManager, String> colEponimo;
    @FXML
    private TableColumn <tableManager, String> colTilefono;
    @FXML
    private TableColumn <tableManager, String> colAfm;
    @FXML
    private TableColumn <tableManager, String> colTautotita;
    
    private ObservableList <tableManager> data;
    
    ResultSet rs = sopho.ResultKeeper.rs;
    
    sopho.LockEdit le = new sopho.LockEdit();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //initialzing result table
        data = getInitialTableData();
        
        //filling table with data
        resultTable.setItems(data);
        
        colId.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("id"));
        colBarcode.setCellValueFactory(new PropertyValueFactory<tableManager, String>("barcode"));
        colOnoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        colEponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        colTilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tilefono"));
        colAfm.setCellValueFactory(new PropertyValueFactory<tableManager, String>("afm"));
        colTautotita.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tautotita"));
        
        //setting the data to the table
        resultTable.getColumns().setAll(colId, colBarcode, colOnoma, colEponimo, colTilefono, colAfm, colTautotita);
    }   
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @FXML
    public void GoBack (ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad(sopho.StageLoader.lastStage, stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void Select (ActionEvent event) throws IOException{
        try {            
            if (sopho.ResultKeeper.selectedIndex==-1){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Πρέπει να επιλέξετε τουλάχιστον έναν ωφελούμενο από τη λίστα με τα αποτελέσματα!", "error");
                cm.showAndWait();
            }else{            
                //loading selected row data to variables.
                
                int sel = resultTable.getSelectionModel().getSelectedIndex();
                
                sopho.ResultKeeper.selectedIndex = sel;
                
                Stage stage = (Stage) backButton.getScene().getWindow();
                
                //now we have to make some distinctions because we use the same stage for two different purposes.
                if (sopho.StageLoader.lastStage.equals("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml")) {
                    
                    tableManager tbl = resultTable.getSelectionModel().getSelectedItem();
                    int id = tbl.getId();

                    if(!le.CheckLock(id, "ofeloumenoi")){//check if editing is locked because another user is currently editing the data.
                        if (!le.LockEditing(true, id, "ofeloumenoi")){//check if lock editing is successful else display message about it
                            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα", "Τα στοιχεία του ατόμου που επιλέξατε δεν μπόρεσαν να κλειδωθούν για επεξεργασία. Αυτό σημαίνει ότι μπορεί να επεξεργαστεί και άλλος χρήστης παράλληλα τα ίδια στοιχεία και να διατηρηθούν οι αλλαγές που θα αποθηκεύσει ο άλλος χρήστης. Μπορείτε να επεξεργαστείτε τα στοιχεία ή να βγείτε και να μπείτε και πάλι στα στοιχεία για να κλειδώσουν.", "error");
                            cm.showAndWait();
                        }
                        sopho.ResultKeeper.selectedIndex=sel;
                        sl.StageLoad("/sopho/Ofeloumenoi/EditOfeloumenoi.fxml", stage, true, false); //resizable true, utility false.
                    }else{
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Κάποιος άλλος χρήστης επεξεργάζεται αυτή τη στιγμή τον επιλεγμένο ωφελούμενο. Βεβαιωθείτε ότι η καρτέλα του ωφελούμενου δεν είναι ανοιχτή σε κάποιον άλλον υπολογιστή και προσπαθήστε και πάλι.", "error");
                        cm.showAndWait();
                    }
                    
                }else if(sopho.StageLoader.lastStage.equals("/sopho/Eidi/EidiMain.fxml")){
                    sopho.ResultKeeper.selectedIndex=sel;
                    sl.StageLoad("/sopho/Eidi/EidiDothikan.fxml", stage, true, false); //resizable true, utility false. 
                }                
            }
        }catch(Exception e){
            System.out.println("table selection error " + e);
        }
                
    }

    public static class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private IntegerProperty id;
        private StringProperty barcode;
        private StringProperty onoma;
        private StringProperty eponimo;
        private StringProperty tilefono;
        private StringProperty afm;
        private StringProperty tautotita;
        
        public tableManager(){}

        private tableManager(Integer id, String barcode, String onoma, String eponimo, String tilefono, String afm, String tautotita ){
            
            this.id = new SimpleIntegerProperty(id);
            this.barcode = new SimpleStringProperty(barcode);
            this.onoma = new SimpleStringProperty(onoma);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.tilefono = new SimpleStringProperty(tilefono);
            this.afm = new SimpleStringProperty(afm);
            this.tautotita = new SimpleStringProperty(tautotita);
        }
                
        //the following get and set methods are required. Else the table cells will appear blank
        public Integer getId(){
            return id.get();
        }
        
        public String getBarcode(){
            return barcode.get();
        }
        
        public String getOnoma(){
            return onoma.get();
        }
        
        public String getEponimo(){
            return eponimo.get();
        }
        
        public String getTilefono(){
            return tilefono.get();
        }
        
        public String getAfm(){
            return afm.get();
        }
        
        public String getTautotita(){
            return tautotita.get();
        }
        
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        
        List<tableManager> list = new ArrayList<>();
        
        //we have to add the values from database to the table
        try{
            rs.beforeFirst();
            while(rs.next()){
                list.add(new tableManager( rs.getInt("id"), rs.getString("barcode"), rs.getString("onoma"), rs.getString("eponimo"), rs.getString("tilefono"), rs.getString("afm"), rs.getString("tautotita")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MultipleSearchResultsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
}
