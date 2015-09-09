package sopho.Ofeloumenoi;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //initialzing result table
        data = getInitialTableData();
        
        //filling table with data
        resultTable.setItems(data);
        colBarcode.setCellValueFactory(new PropertyValueFactory<tableManager, String>("barcode"));
        colOnoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        colEponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        colTilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tilefono"));
        colAfm.setCellValueFactory(new PropertyValueFactory<tableManager, String>("afm"));
        colTautotita.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tautotita"));
        
        //setting the data to the table
        resultTable.getColumns().setAll(colBarcode, colOnoma, colEponimo, colTilefono, colAfm, colTautotita);
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
            sopho.ResultKeeper.selectedIndex = resultTable.getSelectionModel().getSelectedIndex() + 1; //we have to compensate for the difference in start of counting. The table starts at 0 while the resultSet starts at 1
            
            if (sopho.ResultKeeper.selectedIndex==-1){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Πρέπει να επιλέξετε τουλάχιστον έναν ωφελούμενο από τη λίστα με τα αποτελέσματα!", "error");
                cm.showAndWait();
            }else{            
                System.out.println("the selected line is " + sopho.ResultKeeper.selectedIndex);

                //loading selected row data to variables.
                sopho.ResultKeeper.selectedIndex = resultTable.getSelectionModel().getSelectedIndex();
                
                Stage stage = (Stage) backButton.getScene().getWindow();
                if (sopho.StageLoader.lastStage.equals("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml")) {
                    sl.StageLoad("/sopho/Ofeloumenoi/EditOfeloumenoi.fxml", stage, true, false); //resizable true, utility false.
                }else if(sopho.StageLoader.lastStage.equals("/sopho/Eidi/EidiMain.fxml")){
                    sl.StageLoad("/sopho/Eidi/EidiDothikan.fxml", stage, true, false); //resizable true, utility false.
                }                
            }
        }catch(Exception e){
            System.out.println("table selection error " + e);
        }
                
    }


    public static class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private StringProperty barcode;
        private StringProperty onoma;
        private StringProperty eponimo;
        private StringProperty tilefono;
        private StringProperty afm;
        private StringProperty tautotita;
        
        public tableManager(){}

        private tableManager(String barcode, String onoma, String eponimo, String tilefono, String afm, String tautotita ){
            this.barcode = new SimpleStringProperty(barcode);
            this.onoma = new SimpleStringProperty(onoma);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.tilefono = new SimpleStringProperty(tilefono);
            this.afm = new SimpleStringProperty(afm);
            this.tautotita = new SimpleStringProperty(tautotita);
        }
        
        
        //the following get and set methods are required. Else the table cells will appear blank
        public String getBarcode(){
            return barcode.get();
        }
        
        public void setBarcode(String s){
            barcode.set(s);
        }
        
        public String getOnoma(){
            return onoma.get();
        }
        
        public void setOnoma(String s){
            onoma.set(s);
        }
        
        public String getEponimo(){
            return eponimo.get();
        }
        
        public void setEponimo(String s){
            eponimo.set(s);
        }
        
        public String getTilefono(){
            return tilefono.get();
        }
        
        public void setTilefono(String s){
            tilefono.set(s);
        }
        
        public String getAfm(){
            return afm.get();
        }
        
        public void setAfm(String s){
            afm.set(s);
        }
        
        public String getTautotita(){
            return tautotita.get();
        }
        
        public void setTautotita(String s){
            tautotita.set(s);
        }
        
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        
        List<tableManager> list = new ArrayList<>();
        
        //we have to add the values from database to the table
        try{
            rs.beforeFirst();
            while(rs.next()){
                list.add(new tableManager(rs.getString("barcode"), rs.getString("onoma"), rs.getString("eponimo"), rs.getString("tilefono"), rs.getString("afm"), rs.getString("tautotita")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MultipleSearchResultsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
}
