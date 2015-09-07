package sopho.Eidi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EidiDothikanController extends Application implements Initializable {

    @FXML
    public VBox choiceVbox;
    @FXML
    public Label barcode, eponimo, onoma, patronimo;
    @FXML
    public TableView <tableManager> history;
    @FXML
    public TableColumn <tableManager, String> historyDate, historyEidi;
    @FXML
    public TextField hours, minutes;
    @FXML
    public DatePicker date;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    private ObservableList<tableManager> data;
    
    ResultSet oldrs = sopho.ResultKeeper.rs;
    int selectedIndex = sopho.ResultKeeper.selectedIndex;
    
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    sopho.DBClass db = new sopho.DBClass();
    
    String selectedBarcode;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //setting current time and date
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int mins = rightNow.get(Calendar.MINUTE);
        
        hours.setText(hour+"");//this is a trick because int cannot be dereferenced
        minutes.setText(mins+"");
        
        date.setValue(LocalDate.now());
        
        try {
            selectedBarcode=oldrs.getString("barcode");
            barcode.setText(selectedBarcode);
            onoma.setText(oldrs.getString("onoma"));
            eponimo.setText(oldrs.getString("eponimo"));
            patronimo.setText(oldrs.getString("patronimo"));
        } catch (SQLException ex) {
            Logger.getLogger(EidiDothikanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //initialzing history table
        data = getInitialTableData();
                
        history.setItems(data);
        history.setEditable(true);
        historyDate.setCellValueFactory(new PropertyValueFactory<tableManager, String>("date"));
        historyEidi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eidi"));
        /*
        //lets make the table cells editable
        etosCol.setCellFactory(TextFieldTableCell.forTableColumn());
        etosCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<tableManager, String>>() {
            
            @Override
            public void handle(TableColumn.CellEditEvent<tableManager, String> t){
                ((tableManager) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setEtos(t.getNewValue());
            }
        });
                */
        
        history.getColumns().setAll(historyDate, historyEidi);
        //end of initialization of tekna table

    }    
    
    @Override
    public void start(Stage stage){
        
    }
    
    public class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private SimpleStringProperty date;
        private SimpleStringProperty eidi;
        
        public tableManager(){}
        
        public tableManager(String d, String e){
            date = new SimpleStringProperty(d);
            eidi = new SimpleStringProperty(e);
        }
        
        public String getDate(){
            return date.get();
        }
        
        public void setDate(String d){
            date.set(d);
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

        int eidiNumber = 0;
        
        //now we have to get the eidi names and create a key-value map with them to display their names
        Map eidinames = new HashMap();
        String sql = "SELECT * FROM eidinames WHERE active=1";
        conn = db.ConnectDB();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.last();
            if(rs.getRow()>0){
                rs.beforeFirst();
                int i=1;
                while(rs.next()){
                    eidinames.put(i, rs.getString("name"));
                    i++;
                }
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε εισάγει είδη στο μενού επεξεργασία ειδών, είτε όλα τα είδη είναι ανενεργά... Προσθέστε είδη ή ενεργοποιήστε τα υπάρχοντα και έπειτα καταχωρήστε είδη που παρέλαβαν οι ωφελούμενοι.", "error");
                cm.showAndWait();
                Stage stage = (Stage) barcode.getScene().getWindow();
                try {
                    sl.StageLoad("/sopho/Eidi/EpeksergasiaEidon.fxml", stage, true, false); //resizable true, utility false
                } catch (IOException ex) {
                    Logger.getLogger(EidiDothikanController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EidiDothikanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //print to ensure that the eidinames got properly inserted to the map
        for(int i=0; i<eidinames.size(); i++){
            System.out.println("eidinames "+eidinames.get(i+1));
        }
        
        sql = "SELECT * FROM eidiofeloumenoi WHERE barcode=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1,selectedBarcode);
            System.out.println("sql eidiofeloumenoi for table:"+pst);

            rs = pst.executeQuery();
            rs.last();
            if(rs.getRow()>0){
                rs.beforeFirst();
                
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                
                String eidiTaken="";
                //we will loop through the columns of the eidiofeloumenoi table to get data about what eidi the ofeloumenos took.
                for(int i=1; i<columnsNumber-2; i++){//we start from i=1 and end to columns -2 to ommit the columns: id,barcode and date. It is the same to starting from i=3 and end to i<columnsNumber
                    if(rs.getInt("eidos"+i)==1){//that is the case that ofeloumenos has taken an item (eidos)
                        eidiTaken+=eidinames.get(i-1)+", ";
                    }
                }
                eidiTaken=eidiTaken.substring(0, eidiTaken.length()-2);//to remove the last comma and space
                System.out.println("eidiTaken: "+eidiTaken);
                list.add(new tableManager(rs.getDate("date").toString(), eidiTaken));
            }

        } catch (SQLException ex) {
            Logger.getLogger(EidiDothikanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
            
        return mydata;
    }
    
    @FXML
    public void GoBack(ActionEvent e){
        //TODO
    }
    
    @FXML
    public void Save(ActionEvent e){
        //TODO
    }
    
}
