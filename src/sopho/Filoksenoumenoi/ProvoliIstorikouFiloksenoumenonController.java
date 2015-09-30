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
package sopho.Filoksenoumenoi;

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


public class ProvoliIstorikouFiloksenoumenonController implements Initializable {

    @FXML
    public Button backButton;
    @FXML
    public TableView<tableManager> table;
    @FXML
    public TableColumn<tableManager, String> eponimo, onoma, patronimo, date, aitia, loipa, dateApoxorisis, dieuthinsi, tilefono, tautotita;
    @FXML
    public TableColumn<tableManager, Integer> id;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    private ObservableList<tableManager> data;
        
    sopho.DBClass db = new sopho.DBClass();
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialzing table
        data = getInitialTableData();
                
        table.setItems(data);
        table.setEditable(true);
        id.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("id"));
        eponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        onoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        patronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        date.setCellValueFactory(new PropertyValueFactory<tableManager, String>("date"));
        dateApoxorisis.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dateApoxorisis"));
        aitia.setCellValueFactory(new PropertyValueFactory<tableManager, String>("aitia"));
        loipa.setCellValueFactory(new PropertyValueFactory<tableManager, String>("loipa"));
        dieuthinsi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dieuthinsi"));
        tilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tilefono"));
        tautotita.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tautotita"));
        
        table.getColumns().setAll(id, eponimo, onoma, patronimo, date, dateApoxorisis, aitia, loipa, dieuthinsi, tilefono, tautotita);

        //end of initialization of table
    }    
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Filoksenoumenoi/FiloksenoumenoiMain.fxml", stage, true, false); //resizable true, utility false
    }
    
    
    public class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private SimpleIntegerProperty id;
        private SimpleStringProperty eponimo;
        private SimpleStringProperty onoma;
        private SimpleStringProperty patronimo;
        private SimpleStringProperty date;
        private SimpleStringProperty aitia;
        private SimpleStringProperty loipa;
        private SimpleStringProperty dateApoxorisis;
        private SimpleStringProperty tilefono;
        private SimpleStringProperty dieuthinsi;
        private SimpleStringProperty tautotita;
        
        public tableManager(){}
        
        public tableManager(Integer id, String eponimo, String onoma, String patronimo, String date, String aitia, String loipa, String dateApoxorisis, String tilefono, String dieuthinsi, String tautotita){
            this.id = new SimpleIntegerProperty(id);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.onoma = new SimpleStringProperty(onoma);
            this.patronimo = new SimpleStringProperty(patronimo);
            this.date = new SimpleStringProperty(date);
            this.aitia = new SimpleStringProperty(aitia);
            this.loipa = new SimpleStringProperty(loipa);
            this.dateApoxorisis = new SimpleStringProperty(dateApoxorisis);
            this.tilefono = new SimpleStringProperty(tilefono);
            this.dieuthinsi = new SimpleStringProperty(dieuthinsi);
            this.tautotita = new SimpleStringProperty(tautotita);
            
        }
        
        public Integer getId(){
            return id.get();
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
        
        public String getDate(){
            return date.get();
        }
        
        public String getAitia(){
            return aitia.get();
        }
        
        public String getLoipa(){
            return loipa.get();
        }
        
        public String getDateApoxorisis(){
            return dateApoxorisis.get();
        }
        
        public String getTilefono(){
            return tilefono.get();
        }
        
        public String getDieuthinsi(){
            return dieuthinsi.get();
        }
        
        public String getTautotita(){
            return tautotita.get();
        }
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData() {
        
        List<tableManager> list = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM filoksenoumenoi WHERE apoxorisi=1";
            
            conn = db.ConnectDB();
            pst=conn.prepareStatement(sql);
            
            rs = pst.executeQuery();
            
            while(rs.next()){
                // we can add data to the initial table using the following command
                list.add(new tableManager(rs.getInt("id"), rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), rs.getDate("date").toString(), rs.getString("aitia"), rs.getString("loipa"), rs.getDate("dateApoxorisis").toString(), rs.getString("tilefono"), rs.getString("dieuthinsi"), rs.getString("tautotita")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProvoliTrexontonFiloksenoumenonController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
            
        return mydata;
    }
    
}
