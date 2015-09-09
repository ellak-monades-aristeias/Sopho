package sopho.Eidi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class IstorikoEidonOfeloumenouController implements Initializable {

    @FXML
    public TableView results;
    @FXML
    public TableColumn date, eponimo, onoma, patronimo, eidi;
    
    
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    sopho.DBClass db = new sopho.DBClass();
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    Map eidinames = new HashMap();
    int eidiNumber = 0;
    
    private ObservableList<tableManager> data;
        
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //initialzing results table
        data = getInitialTableData();
                
        results.setItems(data);
        results.setEditable(true);
        date.setSortType(TableColumn.SortType.DESCENDING);
        date.setCellValueFactory(new PropertyValueFactory<tableManager, String>("date"));
        eponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        onoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        patronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        eidi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eidi"));
        
        results.getColumns().setAll(date, eponimo, onoma, patronimo, eidi);
        results.getSortOrder().add(date);
        //end of initialization of results table
    }
    
    @FXML
    public void GoBack (ActionEvent e) throws IOException{
        Stage stage = (Stage) results.getScene().getWindow();
        sl.StageLoad("/sopho/Eidi/EidiMain.fxml", stage, false, true); //resizable false, utility true
    }
    
     public class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        private SimpleStringProperty date;
        private SimpleStringProperty onoma;
        private SimpleStringProperty eponimo;
        private SimpleStringProperty patronimo;
        private SimpleStringProperty eidi;
        
        public tableManager(){}
        
        public tableManager(String date, String eponimo, String onoma, String patronimo, String eidi){
            this.date = new SimpleStringProperty(date);
            this.onoma = new SimpleStringProperty(onoma);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.patronimo = new SimpleStringProperty(patronimo);
            this.eidi = new SimpleStringProperty(eidi);
        }
        
        public String getDate(){
            return date.get();
        }
        
        public void setDate(String d){
            date.set(d);
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
        
        public String getPatronimo(){
            return patronimo.get();
        }
        
        public void setPatronimo(String s){
            patronimo.set(s);
        }
        
        public String getEidi(){
            return eidi.get();
        }
        
        public void setEidi(String e){
            eidi.set(e);
        }
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
              
        List<tableManager> list = new ArrayList<>();

        
        
        //now we have to get the eidi names and create a key-value map with them to display their names
        
        String sql = "SELECT * FROM eidinames";
        conn = db.ConnectDB();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.last();
            if(rs.getRow()>0){
                rs.beforeFirst();
                int i=1;
                while(rs.next()){
                    /*if(rs.getInt("active")==1){
                        activeEidiNumber++;
                        activeeidinames.put(activeEidiNumber, rs.getString("name"));
                    }*/
                    eidinames.put(i, rs.getString("name"));
                    i++;
                }
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε εισάγει είδη στο μενού επεξεργασία ειδών, είτε όλα τα είδη είναι ανενεργά... Προσθέστε είδη ή ενεργοποιήστε τα υπάρχοντα και έπειτα καταχωρήστε είδη που παρέλαβαν οι ωφελούμενοι.", "error");
                cm.showAndWait();
                Stage stage = (Stage) results.getScene().getWindow();
                try {
                    sl.StageLoad("/sopho/Eidi/EpeksergasiaEidon.fxml", stage, true, false); //resizable true, utility false
                } catch (IOException ex) {
                    Logger.getLogger(EidiDothikanController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EidiDothikanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sql = "SELECT * FROM eidiofeloumenoi";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            //we keep the eidiNumber because we will need it at the Save function
            eidiNumber=columnsNumber-6;            
            
            rs.last();
            if(rs.getRow()>0){
                rs.beforeFirst();
                while(rs.next()){
                    String eidiTaken="";
                    //we will loop through the columns of the eidiofeloumenoi table to get data about what eidi the ofeloumenos took.
                    for(int i=1; i<=eidiNumber; i++){
                        if(rs.getInt("eidos"+i)==1){//that is the case that ofeloumenos has taken an item (eidos)
                            eidiTaken+=eidinames.get(i)+", ";
                        }
                    }
                    eidiTaken=eidiTaken.substring(0, eidiTaken.length()-2);//to remove the last comma and space
                    String t = rs.getTimestamp("date").toString();
                    t=t.substring(0,t.length()-5);//to display one hours and mins.
                    list.add(new tableManager(t,rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), eidiTaken));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(EidiDothikanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
}
