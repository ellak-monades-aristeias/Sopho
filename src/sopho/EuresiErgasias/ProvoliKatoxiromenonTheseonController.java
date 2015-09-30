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
package sopho.EuresiErgasias;

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

public class ProvoliKatoxiromenonTheseonController implements Initializable {
    
    @FXML
    public Button backButton;
    @FXML
    public TableView<tableManager> table;
    @FXML
    public TableColumn<tableManager, String> date, thesi, eponimia, eponimo, onoma, patronimo, tilefono, dieuthinsi, ergEponimo, ergOnoma, ergPatronimo, ergDieuthinsi, ergTilefono;
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
        date.setCellValueFactory(new PropertyValueFactory<tableManager, String>("date"));
        date.setSortType(TableColumn.SortType.DESCENDING);
        thesi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("thesi"));
        eponimia.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimia"));
        eponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        onoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        patronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        tilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tilefono"));
        dieuthinsi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dieuthinsi"));
        ergEponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("ergEponimo"));
        ergOnoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("ergOnoma"));
        ergPatronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("ergPatronimo"));
        ergDieuthinsi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("ergDieuthinsi"));
        ergTilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("ergTilefono"));
        
        table.getColumns().setAll(id, date, thesi, eponimia, eponimo, onoma, patronimo, tilefono, dieuthinsi, ergEponimo, ergOnoma, ergPatronimo, ergDieuthinsi, ergTilefono);
        table.getSortOrder().add(date);

        //end of initialization of table 
    }   
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/EuresiErgasias/EuresiErgasiasMain.fxml", stage, false, true); //resizable false, utility true
    }  
     
    
    public class tableManager {
        
        private SimpleIntegerProperty id;
        private SimpleStringProperty date;
        private SimpleStringProperty eponimia;
        private SimpleStringProperty eponimo;
        private SimpleStringProperty onoma;
        private SimpleStringProperty patronimo;
        private SimpleStringProperty dieuthinsi;
        private SimpleStringProperty tilefono;
        private SimpleStringProperty thesi;
        private SimpleStringProperty ergEponimo;
        private SimpleStringProperty ergOnoma;
        private SimpleStringProperty ergPatronimo;
        private SimpleStringProperty ergDieuthinsi;
        private SimpleStringProperty ergTilefono;
        
        public tableManager(){}
        
        public tableManager(Integer id, String date, String eponimia, String eponimo, String onoma, String patronimo, String dieuthinsi, String tilefono, String thesi, String ergEponimo, String ergOnoma, String ergPatronimo, String ergDieuthinsi, String ergTilefono){
            this.id = new SimpleIntegerProperty(id);
            this.date = new SimpleStringProperty(date);
            this.eponimia = new SimpleStringProperty(eponimia);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.onoma = new SimpleStringProperty(onoma);
            this.patronimo = new SimpleStringProperty(patronimo);
            this.dieuthinsi = new SimpleStringProperty(dieuthinsi);
            this.tilefono = new SimpleStringProperty(tilefono);
            this.thesi = new SimpleStringProperty(thesi);
            this.ergEponimo = new SimpleStringProperty(ergEponimo);
            this.ergOnoma = new SimpleStringProperty(ergOnoma);
            this.ergPatronimo = new SimpleStringProperty(ergPatronimo);
            this.ergDieuthinsi = new SimpleStringProperty(ergDieuthinsi);
            this.ergTilefono = new SimpleStringProperty(ergTilefono);

        }
        
        public Integer getId(){
            return id.get();
        }
        
        public String getDate(){
            return date.get();
        }
        
        public String getEponimia(){
            return eponimia.get();
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
        
        public String getThesi(){
            return thesi.get();
        }
        
        public String getErgEponimo(){
            return ergEponimo.get();
        }
        
        public String getErgOnoma(){
            return ergOnoma.get();
        }
        
        public String getErgPatronimo(){
            return ergPatronimo.get();
        }
        
        public String getErgDieuthinsi(){
            return ergDieuthinsi.get();
        }
        
        public String getErgTilefono(){
            return ergTilefono.get();
        }
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        List<tableManager> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM katoxiromenestheseis";
            
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
                    list.add(new tableManager(rs.getInt("id"), rs.getDate("date").toString(), rs.getString("ergodotisEponimia"), rs.getString("ergodotisEponimo"), rs.getString("ergodotisOnoma"), rs.getString("ergodotisPatronimo"), rs.getString("ergodotisDieuthinsi"), rs.getString("ergodotisTilefono"), rs.getString("thesi"), rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), rs.getString("dieuthinsi"), rs.getString("tilefono")));
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ProvoliDiathesimonTheseonController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        return mydata;
    }    
    
}
