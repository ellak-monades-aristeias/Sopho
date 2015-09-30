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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SearchOfeloumenoiResultsController implements Initializable {

    @FXML
    public Label info;
    @FXML
    private TableView <tableManager> resultTable;
    @FXML
    private TableColumn <tableManager, String> colEponimo;
    @FXML
    private TableColumn <tableManager, String> colOnoma;
    @FXML
    private TableColumn <tableManager, String> colPatronimo;
    @FXML
    private TableColumn <tableManager, String> colIlikia;
    @FXML
    private TableColumn <tableManager, String> colDimos;
    @FXML
    private TableColumn <tableManager, String> colAnergos;
    @FXML
    private TableColumn <tableManager, String> colEpaggelma;
    @FXML
    private TableColumn <tableManager, String> colEisodima;
    @FXML
    private TableColumn <tableManager, String> colEksartiseis;
    @FXML
    private TableColumn <tableManager, String> colEthnikotita;
    @FXML
    private TableColumn <tableManager, String> colMetanastis;
    @FXML
    private TableColumn <tableManager, String> colRoma;
    @FXML
    private TableColumn <tableManager, String> colOikKatastasi;
    @FXML
    private TableColumn <tableManager, String> colTekna;
    @FXML
    private TableColumn <tableManager, String> colMellousaMama;
    @FXML
    private TableColumn <tableManager, String> colMonogoneiki;
    @FXML
    private TableColumn <tableManager, String> colPoliteknos;
    @FXML
    private TableColumn <tableManager, String> colAsfForeas;
    @FXML
    private TableColumn <tableManager, String> colAmea;
    @FXML
    private TableColumn <tableManager, String> colXronios;
    @FXML
    private TableColumn <tableManager, String> colPathisi;
    @FXML
    private TableColumn <tableManager, String> colMonaxiko;
    @FXML
    private TableColumn <tableManager, String> colEmfiliVia;
    @FXML
    private TableColumn <tableManager, String> colSpoudastis;
    @FXML
    private TableColumn <tableManager, String> colAnenergos;
    
    private ObservableList <tableManager> data;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    public ResultSet rs = sopho.ResultKeeper.rs;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //setting a text about the results found
        int resultsNumber=0;
        try {
            rs.last();
            resultsNumber = rs.getRow();
            rs.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(SearchOfeloumenoiResultsController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        info.setText("Βρέθηκαν " + resultsNumber + " αποτελέσματα με βάση τα κριτήρια που έχετε θέσει στην αναζήτηση.");

        //initialzing result table
        data = getInitialTableData();
        
        //filling table with data
        resultTable.setItems(data);
        colEponimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("eponimo"));
        colOnoma.setCellValueFactory(new PropertyValueFactory<tableManager, String>("onoma"));
        colPatronimo.setCellValueFactory(new PropertyValueFactory<tableManager, String>("patronimo"));
        colIlikia.setCellValueFactory(new PropertyValueFactory<tableManager, String>("ilikia"));
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
        resultTable.getColumns().setAll(colEponimo, colOnoma, colPatronimo, colIlikia, colDimos, colAnergos, colEpaggelma, colEisodima, colEksartiseis, colEthnikotita, colMetanastis, colRoma, colOikKatastasi, colTekna, colMellousaMama, colMonogoneiki, colPoliteknos, colAsfForeas, colAmea, colXronios, colPathisi, colMonaxiko, colEmfiliVia, colSpoudastis, colAnenergos);
    }
    
    @FXML
    public void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) info.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/SearchOfeloumenoi.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void NewSearch(ActionEvent event) throws IOException{
        Stage stage = (Stage) info.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/SearchOfeloumenoi.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void Select(ActionEvent event) throws IOException{
        int index = resultTable.getSelectionModel().getSelectedIndex();
        if(index==-1){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Θα πρέπει να επιλέξετε έναν ωφελούμενο από τον πίνακα προκειμένου να επεξεργαστείτε τα στοιχεία του", "error");
            cm.showAndWait();
        }else{
            sopho.ResultKeeper.selectedIndex = index + 1; //we add 1 because of the different starting number between ResultSet and TableView
            Stage stage = (Stage) info.getScene().getWindow();
            sl.StageLoad("/sopho/Ofeloumenoi/EditOfeloumenoi.fxml", stage, true, false); //resizable true, utility false
        }
    }
    
    public static class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private StringProperty eponimo;
        private StringProperty onoma;
        private StringProperty patronimo;
        private StringProperty ilikia;
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

        private tableManager(String eponimo, String onoma, String patronimo, String ilikia, String dimos, String anergos, String epaggelma, String eisodima, String eksartiseis, String ethnikotita, String metanastis, String roma, String oikKatastasi, String tekna, String mellousaMama, String monogoneiki, String politeknos, String asfForeas, String amea, String xronios, String pathisi, String monaxiko, String emfiliVia, String spoudastis, String anenergos){
            this.eponimo = new SimpleStringProperty(eponimo);
            this.onoma = new SimpleStringProperty(onoma);
            this.patronimo = new SimpleStringProperty(patronimo);
            this.ilikia = new SimpleStringProperty(ilikia);
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
        public String getEponimo(){
            return eponimo.get();
        }
        
        public void setEponimo(String s){
            eponimo.set(s);
        }
        
        public String getOnoma(){
            return onoma.get();
        }
        
        public void setOnoma(String s){
            onoma.set(s);
        }
        
        public String getPatronimo(){
            return patronimo.get();
        }
        
        public void setPatronimo(String s){
            patronimo.set(s);
        }
        
        public String getIlikia(){
            return ilikia.get();
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
        
        //we have to add the values from database to the table
        try{
            rs.beforeFirst();
            while(rs.next()){
                
                Date imGennisis = rs.getDate("imGennisis");
                
                LocalDate today = LocalDate.now();
                LocalDate birthday = imGennisis.toLocalDate();

                Period p = Period.between(birthday, today);

                String age = p.getYears() + ""; //this trick is because int cannot be dereferenced.
                            
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
        
                
                list.add(new tableManager(rs.getString("eponimo"), rs.getString("onoma"), rs.getString("patronimo"), age, rs.getString("dimos"), ConvertToYesNo(rs.getInt("anergos")), rs.getString("epaggelma"), rs.getString("eisodima"), rs.getString("eksartiseis"), rs.getString("ethnikotita"), ConvertToYesNo(rs.getInt("metanastis")), ConvertToYesNo(rs.getInt("roma")), oikKatastasi, rs.getInt("arithmosTeknon")+"", ConvertToYesNo(rs.getInt("mellousaMama")), ConvertToYesNo(rs.getInt("monogoneiki")), ConvertToYesNo(rs.getInt("politeknos")), asfForeas, ConvertToYesNo(rs.getInt("amea")), ConvertToYesNo(rs.getInt("xronios")), rs.getString("pathisi"), ConvertToYesNo(rs.getInt("monaxikos")), ConvertToYesNo(rs.getInt("emfiliVia")), ConvertToYesNo(rs.getInt("spoudastis")), ConvertToYesNo(rs.getInt("anenergos"))));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MultipleSearchResultsController.class.getName()).log(Level.SEVERE, null, ex);
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
