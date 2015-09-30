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
package sopho.MathimataStiriksis;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class KataxorisiMathitiController implements Initializable {

    @FXML
    public Label label;    
    @FXML
    public TextField eponimo, onoma, dieuthinsi, tilefono, patronimo;
    @FXML
    public DatePicker date;
    @FXML
    public TableView<tableManager> table;
    @FXML
    public TableColumn<tableManager, String> mathimata;
    
    private ObservableList<tableManager> data;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    sopho.DBClass db = new sopho.DBClass();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialzing table
        data = getInitialTableData();
                
        Tooltip t = new Tooltip();
        t.setText("Για να καταχωρήσετε μαθήματα κάντε διπλό κλικ σε κάποια γραμμή του πίνακα, συμπληρώστε το όνομα του μαθήματος και πατήστε enter");
        table.setTooltip(t);
        
        table.setItems(data);
        table.setEditable(true);
        
        mathimata.setCellValueFactory(new PropertyValueFactory<tableManager, String>("mathimata"));
        
        //lets make the table cells editable
        mathimata.setCellFactory(TextFieldTableCell.forTableColumn());
        mathimata.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<tableManager, String>>() {
            
            @Override
            public void handle(TableColumn.CellEditEvent<tableManager, String> t){
                ((tableManager) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setMathimata(t.getNewValue());
            }
        });
        
        table.getColumns().setAll(mathimata);
        //end of initialization of table
        
        
        //requesting focus to the label. Otherwise the focus will be on the first textField and the prompt text will not appear
        label.requestFocus();
    }
    
    @FXML
    private void AddRow(ActionEvent event) {
        //create a new row after the last row
        tableManager tbl = new tableManager("Συμπληρώστε το όνομα του μαθήματος");
                
        data.add(tbl);
        int row = data.size()-1; //we compensate with -1 because the rows count from 0
        
        //selecting the new row
        table.requestFocus();
        table.getSelectionModel().select(row);
        table.getFocusModel().focus(row);
    }
    
    
    
    @FXML
    private void RemoveRow(ActionEvent event){
        //getting the selected row and deleting it
        int i = table.getSelectionModel().getSelectedIndex();
        if(i==-1){
            //the user did not select any line. We display a message
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null,"Προσοχή!", "Δεν έχετε επιλέξει κάποια γραμμή από τον πίνακα για να διαγραφεί.", "error");
            cm.showAndWait();
        }else{
            //we focus on the previous line of the line that the user deleted
            data.remove(i);
            if(i!=0){//the i is equal to 0 in the case that the first line was selected.
                i=i--;
            }
            table.requestFocus();
            table.getSelectionModel().select(i);
            table.getFocusModel().focus(i);
        }
    }
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) onoma.getScene().getWindow();
        sl.StageLoad("/sopho/MathimataStiriksis/MathimataMain.fxml", stage, false, true); //resizable false, utility true
    }
    
    @FXML
    public void Save(ActionEvent e) throws IOException{
        if(tilefono.getText().isEmpty()||onoma.getText().isEmpty()||eponimo.getText().isEmpty()||patronimo.getText().isEmpty()){ //checking if the user has filled the required fields
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε συμπληρώσει όλα τα απαιτούμενα πεδία. Θα πρέπει να συμπληρώσετε τα πεδία: Επώνυμο, Όνομα, Πατρώνυμο και τηλέφωνο προκειμένου να καταχωρήσετε έναν ωφελούμενο", "error" );
            cm.showAndWait();
        }else{//the user has filled the required fields. We can proceed.
            
            String totalMathimata="";
            int mathimata=0;
            
            for(int i=0; i<table.getItems().size(); i++){//we are converting the table rows to a single comma separated string to push it to the database in a single entry.
                tableManager tbl = (tableManager) table.getItems().get(i);
                if(!tbl.getMathimata().equals("Συμπληρώστε το όνομα του μαθήματος")){ //we are checking if the user has actually entered something
                    totalMathimata += tbl.getMathimata() + ","; //we have to call getMathimata from the tableManager class to get the actual value. We add the value to totalMathimata and seperate with comma.
                    mathimata++;
                }
            }
            if(mathimata>0){// we need to catch the case that the user has not added any data to the table.
                totalMathimata = totalMathimata.substring(0, totalMathimata.length()-1); // we have to remove the last comma.
            }
            
                String sql = "INSERT INTO mathites (eponimo, onoma, patronimo, dieuthinsi, tilefono, date, mathimata) VALUES (?,?,?,?,?,?,?)";

                conn=db.ConnectDB();
                
            try {
                pst=conn.prepareStatement(sql);
                pst.setString(1, eponimo.getText());
                pst.setString(2, onoma.getText());
                pst.setString(3, patronimo.getText());
                pst.setString(4, dieuthinsi.getText());
                pst.setString(5, tilefono.getText());
                if(date.getValue()!=null){
                    Date mydate = Date.from(date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    java.sql.Date sqlDate = new java.sql.Date(mydate.getTime());
                    pst.setDate(6,sqlDate);
                }else{
                    pst.setDate(6, null);
                }
                pst.setString(7, totalMathimata);
                
                
                int flag = pst.executeUpdate();
                
                if(flag>0){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Τα στοιχεία του μαθητή έχουν ενημερωθεί με επιτυχία.", "confirm");
                    cm.showAndWait();
                    Stage stage = (Stage) onoma.getScene().getWindow();
                    sl.StageLoad("/sopho/MathimataStiriksis/MathimataMain.fxml", stage, false, true); //resizable false, utility true
                }else{
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Υπήρξε πρόβλημα κατά την ενημέρωση των στοιχείων στη βάση δεδομένων. Προσπαθήστε και πάλι.", "error");
                    cm.showAndWait();
                }
            } catch (SQLException ex) {
                Logger.getLogger(KataxorisiKathigitiController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

    
    public class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private SimpleStringProperty mathimata;
        
        public tableManager(){}
        
        public tableManager(String s){
            mathimata = new SimpleStringProperty(s);
        }
        
        public String getMathimata(){
            return mathimata.get();
        }
        
        public void setMathimata(String s){
            mathimata.set(s);
        }
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
              
        List<tableManager> list = new ArrayList<>();
        
        list.add(new tableManager("Συμπληρώστε το όνομα του μαθήματος"));
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
}
