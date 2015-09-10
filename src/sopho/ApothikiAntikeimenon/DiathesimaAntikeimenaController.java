package sopho.ApothikiAntikeimenon;

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

public class DiathesimaAntikeimenaController implements Initializable {

    @FXML
    public Button backButton;
    @FXML
    public TableView<tableManager> diathesima;
    @FXML
    public TableColumn<tableManager, String> antikeimeno, date, eponimo, onoma, patronimo, tilefono, dieuthinsi;
    @FXML
    public TableColumn<tableManager, Integer> id;
    
    private ObservableList<tableManager> data;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialzing table
        data = getInitialTableData();
        
        diathesima.setItems(data);
        id.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("id"));
        antikeimeno.setCellValueFactory(new PropertyValueFactory<tableManager, String>("antikeimeno"));
        date.setCellValueFactory(new PropertyValueFactory<tableManager, String>("date"));
        eponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        onoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        patronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        tilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tilefono"));
        dieuthinsi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dieuthinsi"));
        
        diathesima.getColumns().setAll(id, antikeimeno, date, eponimo, onoma, patronimo, tilefono, dieuthinsi);
        //end of initialization of diathesima 
    }   
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/ApothikiAntikeimenon/ApothikiMain.fxml", stage, false, true); //resizable false, utility true
    }  
        
    @FXML
    public void Select(ActionEvent e) throws IOException{
        int sel = diathesima.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε ένα αντικείμενο από τον πίνακα προκειμένου να καταχωρήσετε ότι παραδόθηκε σε κάποιον.", "error");
            cm.showAndWait();
        }else{
            sopho.ResultKeeper.selectedIndex=sel+1;//we have to compensate because the table rows start to count from 0 while the resultSet starts from 1
            Stage stage = (Stage) backButton.getScene().getWindow();
            sl.StageLoad("/sopho/ApothikiAntikeimenon/ParalaviAntikeimenou.fxml", stage, true, false); //resizable true, utility false
        }
    }
    
    @FXML
    public void Delete(ActionEvent e){
        int sel = diathesima.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε ένα αντικείμενο από τον πίνακα για να διαγράψετε!", "error");
            cm.showAndWait();
        }else{
            tableManager tbl = diathesima.getSelectionModel().getSelectedItem();
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Είστε σίγουροι;", "Θέλετε σίγουρα να διαγράψετε το αντικείμενο: "+tbl.getAntikeimeno()+" από τα διαθέσιμα αντικείμενα; Δεν θα μπορείτε να ανακτήσετε το αντικείμενο αυτό στη συνέχεια...", "question");
            cm.showAndWait();
            if(cm.saidYes){
                System.out.println("deleting the item...");
                //we focus on the previous line of the line that the user deleted
                data.remove(sel);
                if(sel!=0){//the i is equal to 0 in the case that the first line was selected.
                    sel--;
                }
                diathesima.requestFocus();
                diathesima.getSelectionModel().select(sel);
                diathesima.getFocusModel().focus(sel);
                
                int idNumber = tbl.getId();
                
                String sql="DELETE FROM apothikiparadosi WHERE id = ?";
                
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
                        sopho.Messages.CustomMessageController cm2 = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Δεν μπόρεσε να διαγραφεί το αντικείμενο από τη βάση δεδομένων", "error");
                        cm2.showAndWait();
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(DiathesimaAntikeimenaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public class tableManager {
        
        private SimpleIntegerProperty id;
        private SimpleStringProperty antikeimeno;
        private SimpleStringProperty date;
        private SimpleStringProperty eponimo;
        private SimpleStringProperty onoma;
        private SimpleStringProperty patronimo;
        private SimpleStringProperty dieuthinsi;
        private SimpleStringProperty tilefono;
        
        public tableManager(){}
        
        public tableManager(Integer id, String antikeimeno, String date, String eponimo, String onoma, String patronimo, String dieuthinsi, String tilefono){
            this.id = new SimpleIntegerProperty(id);
            this.antikeimeno = new SimpleStringProperty(antikeimeno);
            this.date = new SimpleStringProperty(date);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.onoma = new SimpleStringProperty(onoma);
            this.patronimo = new SimpleStringProperty(patronimo);
            this.dieuthinsi = new SimpleStringProperty(dieuthinsi);
            this.tilefono = new SimpleStringProperty(tilefono);
        }
        
        public Integer getId(){
            return id.get();
        }
        
        public String getAntikeimeno(){
            return antikeimeno.get();
        }
        
        public String getDate(){
            return date.get();
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
        
        public String getDieuthinsi(){
            return dieuthinsi.get();
        }
        
        public String getTilefono(){
            return tilefono.get();
        }
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        List<tableManager> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM apothikiparadosi";
            
            sopho.DBClass db = new sopho.DBClass();
            Connection conn=null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            
            conn=db.ConnectDB();
            pst=conn.prepareStatement(sql);
            
            rs=pst.executeQuery();
            
            sopho.ResultKeeper.rs=rs;
            
            rs.last();
            if(rs.getRow()>0){
                rs.beforeFirst();
                while(rs.next()){
                    // we can add data to the initial table using the following command
                    list.add(new tableManager(rs.getInt("id"), rs.getString("antikeimeno"), rs.getDate("date").toString(), rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), rs.getString("dieuthinsi"), rs.getString("tilefono")));
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DiathesimaAntikeimenaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        return mydata;
    }
    
}
