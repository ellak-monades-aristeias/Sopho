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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EidiDothikanController extends Application implements Initializable {

    @FXML
    public VBox choiceVbox;
    @FXML
    public ListView<String> listView;
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
    
    Map eidinames = new HashMap();
    Map activeeidinames = new HashMap();
    int eidiNumber = 0;
    int activeEidiNumber=0;
    
    ObservableList<String> listViewList = FXCollections.observableArrayList();
    
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
        
        try {
            oldrs.first();//move the cursor to the first row
            if(sopho.ResultKeeper.multipleResults){//only if we need to move from the first line
                oldrs.relative(selectedIndex);//move to the row that we selected at the previous scene
            }
        } catch (SQLException ex) {
            Logger.getLogger(EidiDothikanController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
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
        
        historyDate.setSortType(TableColumn.SortType.DESCENDING);
        historyDate.setCellValueFactory(new PropertyValueFactory<tableManager, String>("date"));
        historyEidi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eidi"));
        
        history.getColumns().setAll(historyDate, historyEidi);
        history.getSortOrder().add(historyDate);
        //end of initialization of history table
        
        listView.setItems(listViewList);
        for(int i=1; i<=activeEidiNumber; i++){
            listViewList.add((String) activeeidinames.get(i));
        }
        
        Tooltip t = new Tooltip();
        t.setText("Κρατήστε πατημένο το ctrl για να επιλέξετε πολλά είδη");
        listView.setTooltip(t);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        listView.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                ObservableList<String> selectedItems =  listView.getSelectionModel().getSelectedItems();
                for(String s : selectedItems){
                    System.out.println("selected item " + s);
                }
            }
        });
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
                    if(rs.getInt("active")==1){
                        activeEidiNumber++;
                        activeeidinames.put(activeEidiNumber, rs.getString("name"));
                    }
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
        
        sql = "SELECT * FROM eidiofeloumenoi WHERE barcode=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1,selectedBarcode);
            rs = pst.executeQuery();
            
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            //we keep the eidiNumber because we will need it at the Save function
            eidiNumber=columnsNumber-6;//we subtract 6 beacause the first 6 columns are not for eidi
            
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

                    t= t.substring(0, t.length()-5);//to display only hours and mins

                    list.add(new tableManager(t, eidiTaken));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(EidiDothikanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) history.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/SearchToEditOfeloumenoi.fxml", stage, false, true); //resizable false, utility true
    }
    
    @FXML
    public void Save(ActionEvent e) throws IOException{
        LocalDate ld = date.getValue();
        Calendar c =  Calendar.getInstance();
        c.set(ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());
        Date mydate = c.getTime();
        
        if(hours.getText().isEmpty()||minutes.getText().isEmpty()||date.getValue()==null){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε ημερομηνία και να ορίσετε την ώρα πριν κάνετε την καταχώρηση!", "error");
            cm.showAndWait();
        }else if(listView.getSelectionModel().getSelectedIndex()==-1){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε τουλάχιστον ένα είδος πριν κάνετε την καταχώρηση! Για να επιλέξετε πολλά είδη κρατήστε πατημένο το ctrl και επιλέξτε με κλικ τα είδη.", "error");
            cm.showAndWait();
        }else{
            String t = ld.getYear()+"-"+ld.getMonthValue()+"-"+ld.getDayOfMonth()+" "+hours.getText()+":"+minutes.getText()+":0.0";
            System.out.println("datetime is " +t);
            Timestamp timestamp = Timestamp.valueOf(t);
            System.out.println("timestamp "+timestamp);

            String sqlEidi="";

            for(int i=1; i<=eidiNumber; i++){
                sqlEidi+="eidos"+i+", ";
            }
            sqlEidi=sqlEidi.substring(0, sqlEidi.length()-2);//to remove the space and the comma

            String sql = "INSERT INTO eidiofeloumenoi (barcode, date, eponimo, onoma, patronimo, "+sqlEidi+") VALUES (?,?,?,?,?,";

            for (int i=1; i<=eidiNumber; i++){
                sql+="?,";
            }
            sql = sql.substring(0,sql.length()-1);//to remove the last comma
            sql += ")";
            
            ObservableList selIndeces ;
            Map selNames = new HashMap();
            selIndeces=listView.getSelectionModel().getSelectedIndices();
            System.out.println("sel indeces size " + selIndeces.size());
            for(int i=0; i<selIndeces.size();i++){
               int sel = (int) selIndeces.get(i);
               System.out.println("selected index is "+sel);
               String s = (String) activeeidinames.get(sel+1);//we have to compensate because activeeidinames starts from 1
               System.out.println("Selected name is"+s);
               selNames.put(i,s);
            }
            
            //we initialize all eidi as inactive
            Map eidosIsActive = new HashMap();
            for(int i=0; i<eidinames.size();i++){
                eidosIsActive.put(i, "0");
            }
            
            //we search which eidi are active and change for them the eidosIsActive value to 1
            for(int i=0; i<selNames.size();i++){
                for(int j=0; j<eidinames.size();j++){
                    String selname = (String) selNames.get(i);
                    String eidosname = (String) eidinames.get(j+1);
                    System.out.println("selNames.get("+i+")="+selname);
                    System.out.println("eidosNames.get("+j+")="+eidosname);
                    if(selname.equals(eidosname)){
                        eidosIsActive.replace(j, "1");
                    }
                }
            }
                        
            
  
            try {
                
                conn=db.ConnectDB();
                pst=conn.prepareStatement(sql);
                pst.setString(1, selectedBarcode);
                pst.setTimestamp(2, timestamp);
                pst.setString(3, eponimo.getText());
                pst.setString(4, onoma.getText());
                pst.setString(5, patronimo.getText());
                
                for(int i=0; i<eidosIsActive.size();i++){
                    System.out.println("eidos"+(i+1)+" is "+eidosIsActive.get(i));
                    pst.setInt(i+6, Integer.parseInt((String) eidosIsActive.get(i)));//we use i+6 to start from index 6
                }
                
                System.out.println("the final sql is: "+pst.toString());
                
                int rowsAffected=pst.executeUpdate();
                
                if(rowsAffected>0){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Τα είδη καταχωρήθηκαν με επιτυχία στον επιλεγμένο ωφελούμενο!", "confirm");
                    cm.showAndWait();
                    Stage stage = (Stage) history.getScene().getWindow();
                    //opening stage for another search to add eidi to ofeloumenoi 
                    sl.StageLoad("/sopho/Ofeloumenoi/SearchToEditOfeloumenoi.fxml", stage, false, true); //resizable false, utility true
                }else{
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Υπήρξε πρόβλημα κατά την καταχώρηση των ειδών στον επιλεγμένο ωφελούμενο... Προσπαθήστε και πάλι.", "confirm");
                    cm.showAndWait();
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(EidiDothikanController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
}
