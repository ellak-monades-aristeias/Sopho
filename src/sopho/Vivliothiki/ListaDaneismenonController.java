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
package sopho.Vivliothiki;

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

public class ListaDaneismenonController implements Initializable {


    @FXML
    public Button backButton;
    @FXML
    public TableView<tableManager> table;
    @FXML
    public TableColumn<tableManager, String> titlos, siggrafeas, katigoria, selides, ekdotikos, isbn, date, eponimo, onoma, patronimo, tilefono, dieuthinsi;
    @FXML
    public TableColumn<tableManager, Integer> id;
    
    private ObservableList<tableManager> data;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialzing table
        data = getInitialTableData();
        
        table.setItems(data);
        id.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("id"));
        titlos.setCellValueFactory(new PropertyValueFactory<tableManager, String>("titlos"));
        siggrafeas.setCellValueFactory(new PropertyValueFactory<tableManager, String>("siggrafeas"));
        katigoria.setCellValueFactory(new PropertyValueFactory<tableManager, String>("katigoria"));
        ekdotikos.setCellValueFactory(new PropertyValueFactory<tableManager, String>("ekdotikos"));
        selides.setCellValueFactory(new PropertyValueFactory<tableManager, String>("selides"));
        isbn.setCellValueFactory(new PropertyValueFactory<tableManager, String>("isbn"));
        date.setCellValueFactory(new PropertyValueFactory<tableManager, String>("date"));
        date.setSortType(TableColumn.SortType.DESCENDING);
        eponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        onoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        patronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        tilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tilefono"));
        dieuthinsi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dieuthinsi"));
        
        table.getColumns().setAll(id, titlos, siggrafeas, katigoria, ekdotikos, selides, isbn, date, eponimo, onoma, patronimo, tilefono, dieuthinsi);
        table.getSortOrder().add(date);
        //end of initialization of table 
    }   
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/VivliothikiMain.fxml", stage, false, true); //resizable false, utility true
    }
    
    @FXML
    public void Epistrofi(ActionEvent e) throws IOException{
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε μια γραμμή από τον πίνακα για να διαγράψετε!", "error");
            cm.showAndWait();
        }else{
            sopho.ResultKeeper.selectedIndex=sel;//we keep the selected line of the table

            Stage stage = (Stage) backButton.getScene().getWindow();
            sl.StageLoad("/sopho/Vivliothiki/EpistrofiVivliou.fxml", stage, false, true); //resizable false, utility true
    
        } 
    }
    
    @FXML
    public void Delete(ActionEvent e){
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε μια γραμμή από τον πίνακα για να διαγράψετε!", "error");
            cm.showAndWait();
        }else{
            tableManager tbl = table.getSelectionModel().getSelectedItem();
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Είστε σίγουροι;", "Θέλετε σίγουρα να διαγράψετε τον δανεισμό του βιβλίου: "+tbl.getTitlos()+" στον/στην "+tbl.getEponimo()+" "+tbl.getOnoma()+"; Δεν θα μπορείτε να ανακτήσετε τα στοιχεία του δανεισμού αυτού στη συνέχεια...", "question");
            cm.showAndWait();
            if(cm.saidYes){
                                
                int idNumber = tbl.getId();
                
                String sql="UPDATE vivliothiki SET daneismeno=0, eponimo='', onoma='', patronimo='', dieuthinsi='', tilefono='', date=null WHERE id = ?";
                
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
                        sopho.Messages.CustomMessageController cm2 = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Δεν μπόρεσε να διαγραφεί ο δανεισμός από τη βάση δεδομένων", "error");
                        cm2.showAndWait();
                    }else{
                        //get the new rs and set the table again
                        //this prevents the bug of deleting a line from the table and passing the oldrs to the ResultKeeper. If the oldrs was passed and the new selectedIndex was passed to ResultKeeper the selected row of rs would not correspond to the table row because the rs would have also the deleted row of the table.
                        data = getInitialTableData();
                
                        table.setItems(data);
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ListaDaneismenonController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
    public class tableManager {
        
        private SimpleIntegerProperty id;
        private SimpleStringProperty titlos;
        private SimpleStringProperty siggrafeas;
        private SimpleStringProperty katigoria;
        private SimpleStringProperty ekdotikos;
        private SimpleStringProperty selides;
        private SimpleStringProperty isbn;
        private SimpleStringProperty date;
        private SimpleStringProperty eponimo;
        private SimpleStringProperty onoma;
        private SimpleStringProperty patronimo;
        private SimpleStringProperty dieuthinsi;
        private SimpleStringProperty tilefono;
        
        public tableManager(){}
        
        public tableManager(Integer id, String titlos, String siggrafeas, String katigoria, String ekdotikos, String selides, String isbn, String date, String eponimo, String onoma, String patronimo, String dieuthinsi, String tilefono){
            this.id = new SimpleIntegerProperty(id);
            this.titlos = new SimpleStringProperty(titlos);
            this.siggrafeas = new SimpleStringProperty(siggrafeas);
            this.katigoria = new SimpleStringProperty(katigoria);
            this.selides = new SimpleStringProperty(selides);
            this.ekdotikos = new SimpleStringProperty(ekdotikos);
            this.isbn = new SimpleStringProperty(isbn);
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
        
        public String getTitlos(){
            return titlos.get();
        }
        
        public String getSiggrafeas(){
            return siggrafeas.get();
        }
        
        public String getKatigoria(){
            return katigoria.get();
        }
        
        public String getEkdotikos(){
            return ekdotikos.get();
        }
        
        public String getSelides(){
            return selides.get();
        }
        
        public String getIsbn(){
            return isbn.get();
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
            String sql = "SELECT * FROM vivliothiki WHERE daneismeno=1";
            
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
                    list.add(new tableManager(rs.getInt("id"), rs.getString("titlos"), rs.getString("siggrafeas"), rs.getString("katigoria"), rs.getString("ekdotikos"), rs.getString("selides"), rs.getString("isbn"), rs.getDate("date").toString(), rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), rs.getString("dieuthinsi"), rs.getString("tilefono")));
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ListaDaneismenonController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        return mydata;
    }        
    
}
