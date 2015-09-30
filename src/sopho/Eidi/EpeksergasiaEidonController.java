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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class EpeksergasiaEidonController implements Initializable {

    @FXML
    public TableView <tableManager> eidiTable;
    @FXML
    public TableColumn <tableManager, String> colEidi;
    @FXML
    public TableColumn <tableManager, String> colActive;
    @FXML
    public TableColumn <tableManager, Integer> colID;
    @FXML
    public Button saveButton;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    private ObservableList<tableManager> data;
    
    boolean noRecords=false;

    List<tableManager> list = new ArrayList<>();
    
    List<Integer> IDList = new ArrayList<>();
    
    Integer[] IDArray;
    
    sopho.DBClass db = new sopho.DBClass();
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialzing eidiTable table
        data = getInitialTableData();
                
        eidiTable.setItems(data);
        eidiTable.setEditable(true);
        colEidi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eidos"));
        colEidi.setCellFactory(TextFieldTableCell.forTableColumn());
        colActive.setCellValueFactory(new PropertyValueFactory<tableManager, String>("active"));
        colActive.setCellFactory(TextFieldTableCell.forTableColumn());
        colID.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("id"));
        
        
        //lets make the table cells editable
        
        colEidi.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<tableManager, String>>() {
            
            @Override
            public void handle(TableColumn.CellEditEvent<tableManager, String> t){
                ((tableManager) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setEidos(t.getNewValue());
            }
        });
        
        eidiTable.getColumns().setAll(colID, colEidi, colActive);
        //end of initialization of eidiTable table
    }
    
    @FXML
    public void GoBack (ActionEvent event) throws IOException{
        Stage stage = (Stage) saveButton.getScene().getWindow();
        sl.StageLoad("/sopho/Eidi/EidiMain.fxml", stage, false, true); //resizable false, utility true;
    }
    
    @FXML
    public void Save (ActionEvent event) throws IOException{
        conn=db.ConnectDB();
       
        String addCol = "ALTER TABLE eidiofeloumenoi ";
        
        String sql1 = "SELECT id FROM eidinames";
        int nextID=1;
        
        try {
            pst= conn.prepareStatement(sql1);
            rs = pst.executeQuery();
            rs.last();
            if(rs.getRow()>0){
                rs.beforeFirst();
                rs.last();
                nextID=rs.getInt("id")+1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EpeksergasiaEidonController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        String sqlInsert = "INSERT INTO eidinames (name, active) VALUES ";
        int rowsToAdd=0;     
        
        for(int i=0; i<eidiTable.getItems().size(); i++){
            tableManager tbl = (tableManager) eidiTable.getItems().get(i);
            
            int activeFlag;
            if(tbl.getActive().equals("Ενεργό")){
                activeFlag=1;
            }else{
                activeFlag=0;
            }
            
            //we are checking if the user has actually entered a name and if the name was already on the db. We don't want to have duplicate names. We only want to push to the db the new ones.
            if(!tbl.getEidos().equals("Συμπληρώστε όνομα είδους")){
                if(!IDList.contains(tbl.getId())||noRecords){//the line didn't exist in db. We have to insert it.
                    
                    sqlInsert += "('"+tbl.getEidos()+"', "+activeFlag+"), ";
                    rowsToAdd++;
                    
                    addCol+="ADD eidos"+nextID+" int, ";
                    nextID++;
                    
                }else{//the line was already in db. We have to update it's values.
                    
                    try {
                        
                        String sqlUpdate = "UPDATE eidinames SET name=?, active=? WHERE id=?";
                        pst = conn.prepareStatement(sqlUpdate);
                        pst.setString(1, tbl.getEidos());
                        pst.setInt(2, activeFlag);
                        pst.setInt(3, tbl.getId());
                        
                        int rowsAffected = pst.executeUpdate();

                        if(rowsAffected < 1){
                            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Οι ονομασίες των ειδών δεν μπόρεσαν να ενημερωθούν", "error");
                            cm.showAndWait();
                        }
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(EpeksergasiaEidonController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        if(rowsToAdd>0){//only if there are rows in the eidiTable.
        
            sqlInsert = sqlInsert.substring(0, sqlInsert.length()-2); //just to remove the comma and the space from the end of the sql string

            try {
                pst = conn.prepareStatement(sqlInsert);
                int rowsAffected = pst.executeUpdate();

                if(rowsAffected > 0){
                    
                    addCol = addCol.substring(0, addCol.length()-2);//just to remove the comma from the end of the string
                    
                    System.out.println(addCol);
                    
                    pst=conn.prepareStatement(addCol);
                    
                    int flag = pst.executeUpdate();
                    
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Οι ονομασίες των ειδών έχουν ενημερωθεί με επιτυχία", "confirm");
                    cm.showAndWait();
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    sl.StageLoad("/sopho/Eidi/EidiMain.fxml", stage, false, true); //resizable false, utility true
                    
                }else{
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Οι ονομασίες των ειδών δεν μπόρεσαν να ενημερωθούν", "error");
                    cm.showAndWait();
                }


            } catch (SQLException ex) {
                Logger.getLogger(EpeksergasiaEidonController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Οι ονομασίες των ειδών έχουν ενημερωθεί με επιτυχία", "confirm");
            cm.showAndWait();
            Stage stage = (Stage) saveButton.getScene().getWindow();
            sl.StageLoad("/sopho/Eidi/EidiMain.fxml", stage, false, true); //resizable false, utility true
}
    }
    
    @FXML
    private void AddRow(ActionEvent event) {
        //create a new row after the last row
        tableManager tbl = new tableManager(-100, "Συμπληρώστε όνομα είδους" , "Ενεργό"); //the -100 doesn't matter because the id is auto increment and we won't push it to the database
                
        data.add(tbl);
        int row = data.size()-1; //we compensate with -1 because the rows count from 0
        
        //selecting the new row
        eidiTable.requestFocus();
        eidiTable.getSelectionModel().select(row);
        eidiTable.getFocusModel().focus(row);
        Refresh(eidiTable, data);
    }
    
    
    
    @FXML
    private void ActivateDeactivate(ActionEvent event){
        //getting the selected row and deleting it
        int i = eidiTable.getSelectionModel().getSelectedIndex();
        if(i==-1){
            //the user did not select any line. We display a message
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null,"Προσοχή!", "Δεν έχετε επιλέξει κάποια γραμμή από τον πίνακα...", "error");
            cm.showAndWait();
        }else{
            tableManager tbl = eidiTable.getSelectionModel().getSelectedItem();
            
            if(tbl.getActive().equals("Ενεργό")){
                tbl.setActive("Ανενεργό");
            }else{
                tbl.setActive("Ενεργό");
            }
        
        }
        Refresh(eidiTable, data);
        
    }
    
    public <T> void Refresh(final TableView<T> table, final List<T> tableList) { 
        //Wierd JavaFX bug 
        table.setItems(null); 
        table.layout(); 
        table.setItems(FXCollections.observableList(tableList)); 
    }
    
    public class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private SimpleIntegerProperty id;
        private SimpleStringProperty eidos;
        private SimpleStringProperty active;

        
        public tableManager(){}
        
        public tableManager(Integer i, String s, String a){
            id = new SimpleIntegerProperty(i);
            eidos = new SimpleStringProperty(s);
            active = new SimpleStringProperty(a);
        }
        
        public int getId(){
            return id.get();
        }
        
        public void setId(int i){
            id.set(i);
        }
        
        public String getEidos(){
            return eidos.get();
        }
        
        public void setEidos(String s){
            eidos.set(s);
        }
        
        public String getActive(){
            return active.get();
        }
        
        public void setActive(String a){
            active.set(a);
        }
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        
        
        
        conn=db.ConnectDB();
        
        String sql = "SELECT * FROM eidinames";
        
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            rs.last(); // get to the last lint of the results.
            if(rs.getRow()>0){ //if the last line number is >0 we have results
                rs.beforeFirst(); // get to the first line to ensure we don't ommit any result
                while(rs.next()){
                    // we can add data to the initial table using the following command
                    list.add(new tableManager(rs.getInt("id"), rs.getString("name"), ConvertToYesNo(rs.getInt("active"))));
                }
            }else{
                // we can add data to the initial table using the following command
                list.add(new tableManager(-10,"Συμπληρώστε όνομα είδους", "Ενεργό")); //the -10 doesn't matter because the id is auto increment and we won't push it to the database
                noRecords=true;
            }
                    
            
        } catch (SQLException ex) {
            Logger.getLogger(EpeksergasiaEidonController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        
        
        //the following is a trick. We need to have a list of the initial table records but the List list is of type tableManager while we need a list of type String so we convert the list to an array and back to a list
        IDArray = new Integer[list.size()];
        for(int i = 0; i < list.size(); i++) IDArray[i] = list.get(i).getId();
        IDList.addAll(Arrays.asList(IDArray));
        
        IDList.stream().forEach((eidiList1) -> {
            System.out.println("***************eidos : " + eidiList1);
        });
        
        return mydata;
        
    }
    
    public String ConvertToYesNo(int flag){
            String s;
            if(flag==1){
                s="Ενεργό";
            }else{
                s="Ανενεργό";
            }
            
            return s;
    }
                
}
