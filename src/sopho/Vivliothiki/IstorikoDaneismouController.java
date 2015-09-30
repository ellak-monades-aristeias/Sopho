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

public class IstorikoDaneismouController implements Initializable {

    
    @FXML
    public Button backButton;
    @FXML
    public TableView<tableManager> table;
    @FXML
    public TableColumn<tableManager, String> titlos, siggrafeas, katigoria, selides, ekdotikos, isbn, date, dateEpistrofis, eponimo, onoma, patronimo, tilefono, dieuthinsi;
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
        dateEpistrofis.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dateEpistrofis"));
        eponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        onoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        patronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        tilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tilefono"));
        dieuthinsi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dieuthinsi"));
        
        table.getColumns().setAll(id, titlos, siggrafeas, katigoria, ekdotikos, selides, isbn, date, dateEpistrofis, eponimo, onoma, patronimo, tilefono, dieuthinsi);
        //end of initialization of table 
    }   
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Vivliothiki/VivliothikiMain.fxml", stage, false, true); //resizable false, utility true
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
        private SimpleStringProperty dateEpistrofis;
        private SimpleStringProperty eponimo;
        private SimpleStringProperty onoma;
        private SimpleStringProperty patronimo;
        private SimpleStringProperty dieuthinsi;
        private SimpleStringProperty tilefono;
        
        public tableManager(){}
        
        public tableManager(Integer id, String titlos, String siggrafeas, String katigoria, String ekdotikos, String selides, String isbn, String date, String dateEpistrofis, String eponimo, String onoma, String patronimo, String dieuthinsi, String tilefono){
            this.id = new SimpleIntegerProperty(id);
            this.titlos = new SimpleStringProperty(titlos);
            this.siggrafeas = new SimpleStringProperty(siggrafeas);
            this.katigoria = new SimpleStringProperty(katigoria);
            this.selides = new SimpleStringProperty(selides);
            this.ekdotikos = new SimpleStringProperty(ekdotikos);
            this.isbn = new SimpleStringProperty(isbn);
            this.date = new SimpleStringProperty(date);
            this.dateEpistrofis = new SimpleStringProperty(dateEpistrofis);
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
        
        public String getDateEpistrofis(){
            return dateEpistrofis.get();
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
            String sql = "SELECT * FROM vivliothikiistoriko";
            
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
                    list.add(new tableManager(rs.getInt("id"), rs.getString("titlos"), rs.getString("siggrafeas"), rs.getString("katigoria"), rs.getString("ekdotikos"), rs.getString("selides"), rs.getString("isbn"), rs.getDate("date").toString(),rs.getDate("dateEpistrofis").toString(), rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), rs.getString("dieuthinsi"), rs.getString("tilefono")));
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(IstorikoDaneismouController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        return mydata;
    }           
    
}
