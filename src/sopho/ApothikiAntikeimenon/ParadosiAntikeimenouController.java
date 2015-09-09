package sopho.ApothikiAntikeimenon;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class ParadosiAntikeimenouController implements Initializable {

    @FXML
    public TableView table;
    @FXML
    public TableColumn tableCol;
    @FXML
    public TextField eponimo, onoma, patronimo, dieuthinsi, tilefono;
    @FXML
    public DatePicker date;
    
    ObservableList<tableManager> data;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        date.setValue(LocalDate.now());
        
        //initialzing table
        data = getInitialTableData();
                
        table.setItems(data);
        Tooltip t = new Tooltip();
        t.setText("Για να αλλάξετε την τιμή στο κελί του πίνακα κάντε διπλό κλικ και έπειτα αφού συμπληρώσετε την τιμή που θέλετε πατήστε το enter.");
        table.setTooltip(t);
        table.setEditable(true);
        tableCol.setCellValueFactory(new PropertyValueFactory<tableManager, String>("antikeimeno"));
        
        //lets make the table cells editable
        tableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        tableCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<tableManager, String>>() {
            
            @Override
            public void handle(TableColumn.CellEditEvent<tableManager, String> t){
                ((tableManager) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setAntikeimeno(t.getNewValue());
            }
        });
        
        table.getColumns().setAll(tableCol);
        //end of initialization of table 
        
    }
    
    @FXML
    public void Save (ActionEvent e) throws IOException{
        sopho.DBClass db = new sopho.DBClass();
        Connection conn=null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        if(!eponimo.getText().isEmpty()&&!onoma.getText().isEmpty()&&!patronimo.getText().isEmpty()&&!tilefono.getText().isEmpty()){
            String sql = "INSERT INTO apothikiparadosi (eponimo, onoma, patronimo, dieuthinsi, tilefono, date, antikeimeno) VALUES ";

            conn=db.ConnectDB();

            LocalDate ld = date.getValue();
            Calendar c =  Calendar.getInstance();
            c.set(ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());
            Date mydate = c.getTime();
            java.sql.Date sqlDate = new java.sql.Date(mydate.getTime());
            
            int antikeimena=0;
            

            for(int i=0; i<table.getItems().size();i++){
                tableManager tbl = (tableManager) table.getItems().get(i);
                if(!tbl.getAntikeimeno().equals("Συμπληρώστε περιγραφή αντικειμένου")){//only if the user has filled a description
                    sql += "('"+eponimo.getText()+"','"+onoma.getText()+"','"+patronimo.getText()+"','"+dieuthinsi.getText()+"','"+tilefono.getText()+"','"+sqlDate+"','"+tbl.getAntikeimeno()+"'),";
                    antikeimena++;
                }
            }

            if(antikeimena>0){
                sql=sql.substring(0,sql.length()-1);
                try {
                    pst=conn.prepareStatement(sql);
                    
                    System.out.println(pst.toString());
                    
                    int flag = pst.executeUpdate();
                    
                    if(flag>0){
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Τα αντικείμενα έχουν καταχωρηθεί με επιτυχία στην αποθήκη αντικειμένων...", "confirm");
                        cm.showAndWait();
                        Stage stage = (Stage) onoma.getScene().getWindow();
                        sl.StageLoad("/sopho/ApothikiAntikeimenon/ApothikiMain.fxml", stage, false, true); //resizable false, utility true 
                    }else{
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Τα αντικείμενα δεν μπόρεσαν να καταχωρηθούν στη βάση δεδομένων. Προσπαθήστε και πάλι.", "error");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ParadosiAntikeimenouController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να καταχωρήσετε τουλάχιστον ένα αντικείμενο στον πίνακα για να προχωρήσετε.", "error");
                cm.showAndWait();
            }
        }else{//the user didn't fill the required fields
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε συμπληρώσει κάποιο από τα απαιτούμενα πεδία: Επώνυμο, Όνομα, Πατρώνυμο, Τηλέφωνο!", "error");
            cm.showAndWait();
        }
    }
    
    @FXML
    private void AddRow(ActionEvent event) {
        //create a new row after the last row
        tableManager tbl = new tableManager("Συμπληρώστε περιγραφή αντικειμένου");
                
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
    public void GoBack (ActionEvent e) throws IOException{
        Stage stage = (Stage) onoma.getScene().getWindow();
        sl.StageLoad("/sopho/ApothikiAntikeimenon/ApothikiMain.fxml", stage, false, true); //resizable false, utility true
    }
    
    public class tableManager {
        
        private SimpleStringProperty antikeimeno;
        
        public tableManager(){}
        
        public tableManager(String s){
            antikeimeno = new SimpleStringProperty(s);
        }
        
        public String getAntikeimeno(){
            return antikeimeno.get();
        }
        
        public void setAntikeimeno(String s){
            antikeimeno.set(s);
        }
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        
        List<tableManager> list = new ArrayList<>();
        
        // we can add data to the initial table using the following command
            list.add(new tableManager("Συμπληρώστε περιγραφή αντικειμένου"));
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
}
