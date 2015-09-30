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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

public class KatoxirosiThesisController implements Initializable {

    @FXML
    public TableView<tableManager> table;
    @FXML
    public TableColumn<tableManager, String> eponimo, onoma, patronimo, imGennisis, eidikotita, tilefono, dieuthinsi, dimos, ekpaideusi, oikKatastasi, diploma, empeiria;
    @FXML
    public TableColumn<tableManager, Integer> id;
    @FXML
    public Button backButton;
    
    ResultSet rsErgodoti=sopho.ResultKeeper.rs; //we have saved the ergodotis rs to a static var at the previous stage.
    int selErgodotis = sopho.ResultKeeper.selectedIndex; //the selected ergodotis from the previous stage
    
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
        id.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("id"));
        eponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        onoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        patronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        imGennisis.setCellValueFactory(new PropertyValueFactory<tableManager, String>("imGennisis"));
        eidikotita.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eidikotita"));
        tilefono.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tilefono"));
        dieuthinsi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dieuthinsi"));
        dimos.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dimos"));
        ekpaideusi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("ekpaideusi"));
        oikKatastasi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("oikKatastasi"));
        diploma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("diploma"));
        empeiria.setCellValueFactory(new PropertyValueFactory<tableManager, String>("empeiria"));
                
        table.getColumns().setAll(id, eponimo, onoma, patronimo, imGennisis, eidikotita, tilefono, dieuthinsi, dimos, ekpaideusi, oikKatastasi, diploma, empeiria);
        //end of initialization of table 
    }
    
    @FXML
    public void GoBack(ActionEvent e) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/EuresiErgasias/EuresiErgasiasMain.fxml", stage, false, true); //resizable false, utility true
    }
    
    @FXML
    public void Select(ActionEvent e) throws IOException, SQLException{
        int sel = table.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε ένα άτομο από τον πίνακα.", "error");
            cm.showAndWait();
        }else{
            
            //now we have to move the rsErgodoti to the row based on the selection we made at the previous stage.
            rsErgodoti.first();
            if(selErgodotis>0){
                rsErgodoti.relative(selErgodotis);
            }
            
            tableManager tbl = table.getSelectionModel().getSelectedItem();
            
            String sql = "INSERT INTO katoxiromenestheseis (ergodotisEponimia, ergodotisEponimo, ergodotisOnoma, ergodotisPatronimo, ergodotisDieuthinsi, ergodotisTilefono, eponimo, onoma, patronimo, dieuthinsi, tilefono, thesi, date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
            conn=db.ConnectDB();
            pst=conn.prepareStatement(sql);
            
            pst.setString(1, rsErgodoti.getString("eponimia"));
            pst.setString(2, rsErgodoti.getString("eponimo"));
            pst.setString(3, rsErgodoti.getString("onoma"));
            pst.setString(4, rsErgodoti.getString("patronimo"));
            pst.setString(5, rsErgodoti.getString("dieuthinsi"));
            pst.setString(6, rsErgodoti.getString("tilefono"));
            pst.setString(7, tbl.getEponimo());
            pst.setString(8, tbl.getOnoma());
            pst.setString(9, tbl.getPatronimo());
            pst.setString(10, tbl.getDieuthinsi());
            pst.setString(11, tbl.getTilefono());
            pst.setString(12, rsErgodoti.getString("thesi"));
            //setting the date to now... We have to convert from a type to another to be able to push to database.
            LocalDate localdate = LocalDate.now();
            Date date = Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            pst.setDate(13, sqlDate);
            
            int flag = pst.executeUpdate();
            
            if(flag>0){
                //we have successfully inserted the data to the database.
                //Now we have to remove the data from the zitounergasia table and the theseisergasias table
                
                sql = "DELETE FROM zitounergasia WHERE id=?";
                conn=db.ConnectDB();
                pst=conn.prepareStatement(sql);
                pst.setInt(1, tbl.getId());
                int flag2 = pst.executeUpdate();
                
                //now we have to remove data from the theseisergasias table
                sql = "DELETE FROM theseisergasias WHERE id=?";
                conn=db.ConnectDB();
                pst = conn.prepareStatement(sql);
                pst.setInt(1, rsErgodoti.getInt("id"));
                int flag3 = pst.executeUpdate();

                if(flag2<=0){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "H Θέση κατοχυρώθηκε στο επιλεγμένο άτομο αλλά δεν έχει διαγραφεί από τη λίστα με άτομα που ζητούν εργασία. Θα πρέπει να διαγράψετε το άτομο χειροκίνητα.", "error");
                    cm.showAndWait();
                    //we open the scene to delete the person immediately
                    sl.StageLoadNoClose("/sopho/EuresiErgasias/ProvoliAtomonPouZitoun.fxml", true, false);
                }
                
                if(flag3<=0){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "H Θέση κατοχυρώθηκε στο επιλεγμένο άτομο αλλά δεν έχει διαγραφεί από τη λίστα με τις διαθέσιμες θέσεις εργασίας. Θα πρέπει να τη διαγράψετε χειροκίνητα.", "error");
                    cm.showAndWait();
                    //we open the scene to delete the thesi immediately
                    sl.StageLoadNoClose("/sopho/EuresiErgasias/ProvoliDiathesimonTheseon.fxml", true, false);
                }
                
                if(flag2>0&&flag3>0){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Η θέση κατοχυρώθηκε με επιτυχία στο επιλεγμένο άτομο!", "confirm");
                    cm.showAndWait();
                }
                
                Stage stage = (Stage) backButton.getScene().getWindow();
                sl.StageLoad("/sopho/EuresiErgasias/EuresiErgasiasMain.fxml", stage, true, false);
                
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Δεν μπόρεσε να κατοχυρωθεί η θέση εργασίας στο άτομο που έχετε επιλέξει. Προσπαθήστε και πάλι.", "error");
                cm.showAndWait();
            }
        }
    }
    
    public class tableManager {
               
        private SimpleIntegerProperty id;
        private SimpleStringProperty eponimo;
        private SimpleStringProperty onoma;
        private SimpleStringProperty patronimo;
        private SimpleStringProperty imGennisis;
        private SimpleStringProperty eidikotita;
        private SimpleStringProperty tilefono;
        private SimpleStringProperty dieuthinsi;
        private SimpleStringProperty dimos;
        private SimpleStringProperty ekpaideusi;
        private SimpleStringProperty oikKatastasi;
        private SimpleStringProperty diploma;
        private SimpleStringProperty empeiria;
        
        public tableManager(){}
        
        public tableManager(int id, String eponimo, String onoma, String patronimo, String imGennisis, String eidikotita, String tilefono, String dieuthinsi, String dimos, String ekpaideusi, String oikKatastasi, String diploma, String empeiria){
            this.id = new SimpleIntegerProperty(id);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.onoma = new SimpleStringProperty(onoma);
            this.patronimo = new SimpleStringProperty(patronimo);
            this.imGennisis = new SimpleStringProperty(imGennisis);
            this.eidikotita = new SimpleStringProperty(eidikotita);
            this.tilefono = new SimpleStringProperty(tilefono);
            this.dieuthinsi = new SimpleStringProperty(dieuthinsi);
            this.dimos = new SimpleStringProperty(dimos);
            this.ekpaideusi = new SimpleStringProperty(ekpaideusi);
            this.oikKatastasi = new SimpleStringProperty(oikKatastasi);
            this.diploma = new SimpleStringProperty(diploma);
            this.empeiria = new SimpleStringProperty(empeiria);    
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
        
        public String getImGennisis(){
            return imGennisis.get();
        }
        
        public String getEidikotita(){
            return eidikotita.get();
        }
        
        public String getDieuthinsi(){
            return dieuthinsi.get();
        }
        
        public String getDimos(){
            return dimos.get();
        }
        
        public String getTilefono(){
            return tilefono.get();
        }
        
        public String getEkpaideusi(){
            return ekpaideusi.get();
        }
        
        public String getOikKatastasi(){
            return oikKatastasi.get();
        }
        
        public String getDiploma(){
            return diploma.get();
        }
        
        public String getEmpeiria(){
            return empeiria.get();
        }
        
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        List<tableManager> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM zitounergasia";
            
            conn=db.ConnectDB();
            pst=conn.prepareStatement(sql);
            
            rs=pst.executeQuery();
            
            rs.last();
            if(rs.getRow()>0){
                rs.beforeFirst();
                while(rs.next()){
                    String ekpaideusi = "";
                    
                    //we have to convert the 0 and 1 value from the db to strings
                    if(rs.getInt("ye")==1){
                        ekpaideusi += "ΥΕ, ";
                    }
                    if(rs.getInt("de")==1){
                        ekpaideusi += "ΔΕ, ";
                    }
                    if(rs.getInt("te")==1){
                        ekpaideusi += "ΤΕ, ";
                    }
                    if(rs.getInt("pe")==1){
                        ekpaideusi += "ΠΕ, ";
                    }
                    if(ekpaideusi.length()>1){
                        ekpaideusi = ekpaideusi.substring(0, ekpaideusi.length()-2);//to remove the space and the comma
                    }
                    
                    String oikKatastasi = "";
                    
                    //we have to convert oikKatastasi to String
                    if(rs.getInt("oikKatastasi")==0){
                        oikKatastasi = "Άγαμος";
                    }else if(rs.getInt("oikKatastasi")==1){
                        oikKatastasi = "Έγγαμος";
                    }else if(rs.getInt("oikKatastasi")==2){
                        oikKatastasi = "Διαζευγμένος";
                    }else if(rs.getInt("oikKatastasi")==3){
                        oikKatastasi = "Χήρος";
                    }
                    
                    // we can add data to the initial table using the following command
                    list.add(new tableManager(rs.getInt("id"), rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), rs.getDate("imGennisis").toString(), rs.getString("eidikotita"), rs.getString("tilefono"), rs.getString("dieuthinsi"), rs.getString("dimos"), ekpaideusi, oikKatastasi, rs.getString("diploma"), rs.getString("empeiria")));
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ProvoliAtomonPouZitounController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        return mydata;
    }   
    
}
