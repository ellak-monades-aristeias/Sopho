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
package sopho.Ofeloumenoi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

public class ListaOfeloumenonController implements Initializable {

    @FXML
    public Button backButton;
    @FXML
    private TableView <tableManager> resultTable;
    @FXML
    private TableColumn <tableManager, Integer> colID;
    @FXML
    private TableColumn <tableManager, String> colRegisterDate, colEponimo, colPatronimo, colOnoma, colGennisi, colDimos, colAnergos, colEpaggelma, colEisodima, colEksartiseis, colEthnikotita, colMetanastis, colRoma, colOikKatastasi, colTekna, colMellousaMama, colMonogoneiki, colPoliteknos, colAsfForeas, colXronios, colPathisi, colAmea, colMonaxiko, colEmfiliVia, colSpoudastis, colAnenergos;
    
    private ObservableList <tableManager> data;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    sopho.LockEdit le = new sopho.LockEdit();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //initialzing result table
        data = getInitialTableData();
        
        //filling table with data
        resultTable.setItems(data);
        colID.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("id"));
        colRegisterDate.setCellValueFactory(new PropertyValueFactory<tableManager, String>("registerDate"));
        colEponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        colOnoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        colPatronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        colGennisi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("imGennisis"));
        colDimos.setCellValueFactory(new PropertyValueFactory<tableManager, String>("dimos"));
        colAnergos.setCellValueFactory(new PropertyValueFactory<tableManager, String>("anergos"));
        colEpaggelma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("epaggelma"));
        colEisodima.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eisodima"));
        colEksartiseis.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eksartiseis"));
        colEthnikotita.setCellValueFactory(new PropertyValueFactory<tableManager, String>("ethnikotita"));
        colMetanastis.setCellValueFactory(new PropertyValueFactory<tableManager, String>("metanastis"));
        colRoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("roma"));
        colOikKatastasi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("oikKatastasi"));
        colTekna.setCellValueFactory(new PropertyValueFactory<tableManager, String>("tekna"));
        colMellousaMama.setCellValueFactory(new PropertyValueFactory<tableManager, String>("mellousaMama"));
        colMonogoneiki.setCellValueFactory(new PropertyValueFactory<tableManager, String>("monogoneiki"));
        colPoliteknos.setCellValueFactory(new PropertyValueFactory<tableManager, String>("politeknos"));
        colAsfForeas.setCellValueFactory(new PropertyValueFactory<tableManager, String>("asfForeas"));
        colAmea.setCellValueFactory(new PropertyValueFactory<tableManager, String>("amea"));
        colXronios.setCellValueFactory(new PropertyValueFactory<tableManager, String>("xronios"));
        colPathisi.setCellValueFactory(new PropertyValueFactory<tableManager, String>("pathisi"));
        colMonaxiko.setCellValueFactory(new PropertyValueFactory<tableManager, String>("monaxiko"));
        colEmfiliVia.setCellValueFactory(new PropertyValueFactory<tableManager, String>("emfiliVia"));
        colSpoudastis.setCellValueFactory(new PropertyValueFactory<tableManager, String>("spoudastis"));
        colAnenergos.setCellValueFactory(new PropertyValueFactory<tableManager, String>("anenergos"));
        
        //setting the data to the table
        resultTable.getColumns().setAll(colID, colRegisterDate, colEponimo, colOnoma, colPatronimo, colGennisi, colDimos, colAnergos, colEpaggelma, colEisodima, colEksartiseis, colEthnikotita, colMetanastis, colRoma, colOikKatastasi, colTekna, colMellousaMama, colMonogoneiki, colPoliteknos, colAsfForeas, colAmea, colXronios, colPathisi, colMonaxiko, colEmfiliVia, colSpoudastis, colAnenergos);
    }
    
    @FXML
    public void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/SearchOfeloumenoi.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void Edit(ActionEvent event) throws IOException, SQLException{
        int index = resultTable.getSelectionModel().getSelectedIndex();
        if(index==-1){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε έναν ωφελούμενο από τον πίνακα προκειμένου να επεξεργαστείτε τα στοιχεία του", "error");
            cm.showAndWait();
        }else{
            tableManager tbl = resultTable.getSelectionModel().getSelectedItem();
            int id = tbl.getId();
            
            if(!le.CheckLock(id, "ofeloumenoi")){//check if editing is locked because another user is currently editing the data.
                if (!le.LockEditing(true, id, "ofeloumenoi")){//check if lock editing is successful else display message about it
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα", "Τα στοιχεία του ωφελούμενου που επιλέξατε δεν μπόρεσαν να κλειδωθούν για επεξεργασία. Αυτό σημαίνει ότι μπορεί να επεξεργαστεί και άλλος χρήστης παράλληλα τα ίδια στοιχεία και να διατηρηθούν οι αλλαγές που θα αποθηκεύσει ο άλλος χρήστης. Μπορείτε να επεξεργαστείτε τα στοιχεία ή να βγείτε και να μπείτε και πάλι στα στοιχεία για να κλειδώσουν.", "error");
                    cm.showAndWait();
                }
                sopho.ResultKeeper.selectedIndex=index;
                Stage stage = (Stage) backButton.getScene().getWindow();
                sl.StageLoad("/sopho/Ofeloumenoi/EditOfeloumenoi.fxml", stage, true, false); //resizable true, utility false
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Κάποιος άλλος χρήστης επεξεργάζεται αυτή τη στιγμή τον επιλεγμένο ωφελούμενο. Βεβαιωθείτε ότι η καρτέλα του ωφελούμενου δεν είναι ανοιχτή σε κάποιον άλλον υπολογιστή και προσπαθήστε και πάλι.", "error");
                cm.showAndWait();
            }
        }
    }
    
    @FXML
    public void Delete(ActionEvent event) throws IOException{
        int sel = resultTable.getSelectionModel().getSelectedIndex();
        if(sel==-1){
            //the user did not select a line
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε έναν ωφελούμενο από τον πίνακα για να διαγράψετε!", "error");
            cm.showAndWait();
        }else{
            tableManager tbl = resultTable.getSelectionModel().getSelectedItem();
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Είστε σίγουροι;", "Θέλετε σίγουρα να διαγράψετε τον ωφελούμενο με επώνυμο: "+tbl.getEponimo()+" και όνομα: "+tbl.getOnoma()+"; Δεν θα μπορείτε να ανακτήσετε τα στοιχεία του μετά τη διαγραφή...", "question");
            cm.showAndWait();
            if(cm.saidYes){
                                
                int idNumber = tbl.getId();
                
                String sql="DELETE FROM ofeloumenoi WHERE id=?";
                
                sopho.DBClass db = new sopho.DBClass();
                Connection conn=null;
                PreparedStatement pst = null;
                
                conn = db.ConnectDB();
                try {
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1,idNumber);
                    
                    int flag = pst.executeUpdate();
                    
                    if(flag<=0){
                        sopho.Messages.CustomMessageController cm2 = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Δεν μπόρεσε να διαγραφεί ο επιλεγμένος ωφελούμενος από τη βάση δεδομένων", "error");
                        cm2.showAndWait();
                    }else{
                        //get the new rs and set the table again
                        //this prevents the bug of deleting a line from the table and passing the oldrs to the ResultKeeper. If the oldrs was passed and the new selectedIndex was passed to ResultKeeper the selected row of rs would not correspond to the table row because the rs would have also the deleted row of the table.
                        data = getInitialTableData();
                
                        resultTable.setItems(data);
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(sopho.Ofeloumenoi.ListaOfeloumenonController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private IntegerProperty id;
        private StringProperty registerDate;
        private StringProperty eponimo;
        private StringProperty onoma;
        private StringProperty patronimo;
        private StringProperty imGennisis;
        private StringProperty dimos;
        private StringProperty anergos;
        private StringProperty epaggelma;
        private StringProperty eisodima;
        private StringProperty eksartiseis;
        private StringProperty ethnikotita;
        private StringProperty metanastis;
        private StringProperty roma;
        private StringProperty oikKatastasi;
        private StringProperty tekna;
        private StringProperty mellousaMama;
        private StringProperty monogoneiki;
        private StringProperty politeknos;
        private StringProperty asfForeas;
        private StringProperty amea;
        private StringProperty xronios;
        private StringProperty pathisi;
        private StringProperty monaxiko;
        private StringProperty emfiliVia;
        private StringProperty spoudastis;
        private StringProperty anenergos;
        
        
        public tableManager(){}

        private tableManager(Integer id, String registerDate, String eponimo, String onoma, String patronimo, String imGennisis, String dimos, String anergos, String epaggelma, String eisodima, String eksartiseis, String ethnikotita, String metanastis, String roma, String oikKatastasi, String tekna, String mellousaMama, String monogoneiki, String politeknos, String asfForeas, String amea, String xronios, String pathisi, String monaxiko, String emfiliVia, String spoudastis, String anenergos){
            this.id = new SimpleIntegerProperty(id);
            this.registerDate = new SimpleStringProperty(registerDate);
            this.eponimo = new SimpleStringProperty(eponimo);
            this.onoma = new SimpleStringProperty(onoma);
            this.patronimo = new SimpleStringProperty(patronimo);
            this.imGennisis = new SimpleStringProperty(imGennisis);
            this.dimos = new SimpleStringProperty(dimos);
            this.anergos = new SimpleStringProperty(anergos);
            this.epaggelma = new SimpleStringProperty(epaggelma);
            this.eisodima = new SimpleStringProperty(eisodima);
            this.eksartiseis = new SimpleStringProperty(eksartiseis);
            this.ethnikotita = new SimpleStringProperty(ethnikotita);
            this.metanastis = new SimpleStringProperty(metanastis);
            this.roma = new SimpleStringProperty(roma);
            this.oikKatastasi = new SimpleStringProperty(oikKatastasi);
            this.tekna = new SimpleStringProperty(tekna);
            this.mellousaMama = new SimpleStringProperty(mellousaMama);
            this.monogoneiki = new SimpleStringProperty(monogoneiki);
            this.politeknos = new SimpleStringProperty(politeknos);
            this.asfForeas = new SimpleStringProperty(asfForeas);
            this.amea = new SimpleStringProperty(amea);
            this.xronios = new SimpleStringProperty(xronios);
            this.pathisi = new SimpleStringProperty(pathisi);
            this.monaxiko = new SimpleStringProperty(monaxiko);
            this.emfiliVia = new SimpleStringProperty(emfiliVia);
            this.spoudastis = new SimpleStringProperty(spoudastis);
            this.anenergos = new SimpleStringProperty(anenergos);
        }
        
        
        //the following get and set methods are required. Else the table cells will appear blank
        public Integer getId(){
            return id.get();
        }
        
        public String getRegisterDate(){
            return registerDate.get();
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
        
        public String getDimos(){
            return dimos.get();
        }
        
        public String getAnergos(){
            return anergos.get();
        }
        
        public String getEpaggelma(){
            return epaggelma.get();
        }
        
        public String getEisodima(){
            return eisodima.get();
        }
        
        public String getEksartiseis(){
            return eksartiseis.get();
        }
        
        public String getEthnikotita(){
            return ethnikotita.get();
        }
        
        public String getMetanastis(){
            return metanastis.get();
        }
        
        public String getRoma(){
            return roma.get();
        }
        
        public String getOikKatastasi(){
            return oikKatastasi.get();
        }
        
        public String getTekna(){
            return tekna.get();
        }
        
        public String getMellousaMama(){
            return mellousaMama.get();
        }
        
        public String getMonogoneiki(){
            return monogoneiki.get();
        }
        
        public String getPoliteknos(){
            return politeknos.get();
        }
        
        public String getAsfForeas(){
            return asfForeas.get();
        }
        
        public String getAmea(){
            return amea.get();
        }
        
        public String getXronios(){
            return xronios.get();
        }
        
        public String getPathisi(){
            return pathisi.get();
        }
        
        public String getMonaxiko(){
            return monaxiko.get();
        }
        
        public String getEmfiliVia(){
            return emfiliVia.get();
        }
        
        public String getSpoudastis(){
            return spoudastis.get();
        }
        
        public String getAnenergos(){
            return anenergos.get();
        }
        
        
    }
    
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        
        List<tableManager> list = new ArrayList<>();
        
        sopho.DBClass db = new sopho.DBClass();
        Connection conn = db.ConnectDB();
        
        String sql = "SELECT * FROM ofeloumenoi";
        
        
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
        
            ResultSet rs = pst.executeQuery();
            rs.last();
            if(rs.getRow()>0){
                
                sopho.ResultKeeper.rs = rs;

                //we have to add the values from database to the table

                rs.beforeFirst();

                while(rs.next()){
                   

                    String oikKatastasi = "";
                    int oikKatIndex = rs.getInt("oikKatastasi");
                    if(oikKatIndex>=0){//otherwise no selection was made
                        switch(oikKatIndex){
                            case 0: oikKatastasi="Άγαμος";
                                break;
                            case 1: oikKatastasi="Έγγαμος";
                                break;
                            case 2: oikKatastasi="Διαζευγμένος";
                                break;
                            case 3: oikKatastasi="Χήρος";
                                break;
                            default: break;
                        }
                    }

                    String asfForeas = "";
                    int asfForeasIndex = rs.getInt("asfForeas");
                    if(asfForeasIndex>=0){//otherwise no selection was made
                        switch(asfForeasIndex){
                            case 0: asfForeas="Ανασφάλιστος";
                                break;
                            case 1: asfForeas="ΙΚΑ";
                                break;
                            case 2: asfForeas="ΟΓΑ";
                                break;
                            case 3: asfForeas="ΟΑΕΕ";
                                break;
                            case 4: asfForeas="ΕΤΑΑ";
                                break;
                            case 5: asfForeas="ΕΟΠΥΥ";
                                break;
                            case 6: asfForeas="ΤΠΔΥ";
                                break;
                            case 7: asfForeas="ΤΑΠΙΤ";
                                break;
                            case 8: asfForeas="ΕΤΑΠ – ΜΜΕ";
                                break;
                            case 9: asfForeas="Άλλο";
                                break;
                            default: break;
                        }
                    }

                    String birthdate="";
                    if(rs.getDate("imGennisis")!=null){
                        birthdate = rs.getDate("imGennisis").toString();
                    }
                     
                    
                    list.add(new tableManager(rs.getInt("id"), rs.getDate("registerDate").toString(), rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), birthdate, rs.getString("dimos"), ConvertToYesNo(rs.getInt("anergos")), rs.getString("epaggelma"), rs.getString("eisodima"), rs.getString("eksartiseis"), rs.getString("ethnikotita"), ConvertToYesNo(rs.getInt("metanastis")), ConvertToYesNo(rs.getInt("roma")), oikKatastasi, rs.getInt("arithmosTeknon")+"", ConvertToYesNo(rs.getInt("mellousaMama")), ConvertToYesNo(rs.getInt("monogoneiki")), ConvertToYesNo(rs.getInt("politeknos")), asfForeas, ConvertToYesNo(rs.getInt("amea")), ConvertToYesNo(rs.getInt("xronios")), rs.getString("pathisi"), ConvertToYesNo(rs.getInt("monaxikos")), ConvertToYesNo(rs.getInt("emfiliVia")), ConvertToYesNo(rs.getInt("spoudastis")), ConvertToYesNo(rs.getInt("anenergos"))));
                }
            
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Ενημέρωση", "Δεν έχετε προσθέσει ωφελούμενους ακόμη. Θέλετε να προσθέσετε έναν ωφελούμενο τώρα;", "question");
                cm.showAndWait();
                Stage stage = (Stage) backButton.getScene().getWindow();
                if(cm.saidYes){
                    sl.StageLoad("/sopho/Ofeloumenoi/AddOfeloumenoi.fxml", stage, true, false); //stage to open, stage to close, resizable, utility
                }else{
                    sl.StageLoad("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml", stage, false, true); //stage to open, stage to close, resizable, utility
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ListaOfeloumenonController.class.getName()).log(Level.SEVERE, null, ex);
        }

        ObservableList<tableManager> mydata = FXCollections.observableList(list);

        return mydata;
    }
    
    public String ConvertToYesNo(int flag){
            String s;
            if(flag==1){
                s="ΝΑΙ";
            }else{
                s="ΟΧΙ";
            }
            
            return s;
    }
    
}
    

