package sopho.Ofeloumenoi;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
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
    private TableColumn <tableManager, Integer> colBarcode;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ResultSet rs = sopho.ResultKeeper.rs;
        
        
        try{
                    data = FXCollections.observableArrayList();

                    rs.beforeFirst(); // we ensure that the resultSet will start displaying from the first row.
                    
                    while (rs.next()){
                        //here we use the column names as specified at the database table
                        data.add(new tableManager(rs.getInt("barcode"), rs.getString("onoma"), rs.getString("eponimo"), rs.getString("tilefono"), rs.getString("afm"), rs.getString("tautotita")));
                    }
                    
                    //we can only insert the data to the table by column and not by row.
                    colBarcode.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("barcode"));
                    colOnoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
                    colEponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
                    colTilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tilefono"));
                    colAfm.setCellValueFactory(new PropertyValueFactory<tableManager, String>("afm"));
                    colTautotita.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tautotita"));
   
                }catch(Exception e){
                    System.out.println("Πρόβλημα στη δημιουργία πίνακα " + e);
                }
                try{
                    resultTable.setItems(null);
                    resultTable.setItems(data);
                }catch(Exception ex){
                    System.out.println("Δεν μπορεί να εισάγει στον πίνακα " + ex);
                }
             
    }   
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @FXML
    public void GoBack (ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/SearchOfeloumenoi.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void Select (ActionEvent event) throws IOException{
        try {
            sopho.ResultKeeper.selectedIndex = resultTable.getSelectionModel().getSelectedIndex();
            
            if (sopho.ResultKeeper.selectedIndex==-1){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Πρέπει να επιλέξετε τουλάχιστον έναν ωφελούμενο από τη λίστα με τα αποτελέσματα!", "error");
                cm.showAndWait();
            }else{            
                System.out.println("the selected line is " + sopho.ResultKeeper.selectedIndex);

                //loading selected row data to variables.
                
                /*

                tableManager tbl = (tableManager)resultTable.getSelectionModel().getSelectedItem();
                
                myCustomerID = tbl.getID();
                myBarcode = tbl.getBarcode();
                myName = tbl.getName();
                mySurname = tbl.getSurname();
                myPhone = tbl.getPhone();
                myPoints = tbl.getPoints();

                System.out.println("value sel " + myCustomerID);
                
                if(select){             //μόνο αν πατήσει το κουμπί επιλογή!
                    Notifier.INSTANCE.notifySuccess("OK", "Ο πελάτης "+myName+" "+mySurname+" επιλέχθηκε!");
                    select=false;
                }*/
                
                Stage stage = (Stage) backButton.getScene().getWindow();
                sl.StageLoad("/sopho/Ofeloumenoi/EditOfeloumenoi.fxml", stage, true, false); //resizable true, utility false
                
            }
        }catch(Exception e){
            System.out.println("table selection error " + e);
        }
                
    }


    public static class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private IntegerProperty barcode;
        private StringProperty onoma;
        private StringProperty eponimo;
        private StringProperty tilefono;
        private StringProperty afm;
        private StringProperty tautotita;

        private tableManager(Integer barcode, String onoma, String eponimo, String tilefono, String afm, String tautotita ){
            this.barcode = new SimpleIntegerProperty(barcode);
            this.onoma = new SimpleStringProperty(onoma);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.tilefono = new SimpleStringProperty(tilefono);
            this.afm = new SimpleStringProperty(afm);
            this.tautotita = new SimpleStringProperty(tautotita);
        }
        
        /*
        //the following are required to insert the value in the cell. Otherwise it will display value="theValue"
        public Integer getBarcode() {
           return barcode.get();
        }
        public String getOnoma() {
           return onoma.get();
        }
        public String getEponimo() {
            return eponimo.get();
        }
        public String getTilefono() {
           return tilefono.get();
        }
        public String getAfm() {
           return afm.get();
        }
        public String getTautotita() {
           return tautotita.get();
        }*/
    }
}
