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

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.math.NumberUtils;
import sopho.PrefsHandler;

public class AddOfeloumenoiController implements Initializable {
    
    @FXML
    public Button backButton;
    @FXML
    public TextField barcode, onoma, eponimo, patronimo, mitronimo, dieuthinsi, dimos, tilefono, epaggelma, eisodima, eksartiseis, afm, tautotita, ethnikotita, pathisi;
    @FXML
    public TextArea loipa;
    @FXML
    public CheckBox anergos, metanastis, roma, politeknos, monogoneiki, mellousaMama, amea, xronios, anoTon60, monaxiko, emfiliVia, spoudastis, anenergos;
    @FXML
    public ComboBox oikKatastasi, asfForeas;
    @FXML
    public DatePicker imGennisis;
    @FXML
    private TableView<tableManager> tekna;
    @FXML
    public TableColumn etosCol;
    @FXML
    public ImageView image;
    @FXML
    public Button changePhotoButton;
        
    sopho.StageLoader sl = new sopho.StageLoader();
    
    private ObservableList<tableManager> data;
    
    public String PhotoID;
    public int arithmosTeknon=0;
    
    PrefsHandler prefs = new PrefsHandler();
        
    public String showAnenergosTip = prefs.getPrefs("showAnenergosTip"); //this var is required to show the tip about the anenergos checkbox. When that checkbox is checked the username field is marked red and we need to inform the user why this happens.
       
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml", stage, true, false); //resizable true, utility false 
    }
    
    @FXML
    public void ChangePhoto(ActionEvent event) throws IOException{
        sl.StageLoadNoClose("/sopho/Ofeloumenoi/AllagiFotografias.fxml", false, true); //resizable false, utility true
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tekna.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(prefs.getPrefs("tableTipOfeloumenoi").equals("true")){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Ενημέρωση", "Για να επεξεργαστείτε τα τέκνα κάντε διπλό κλικ στο 'Συμπληρώστε έτος γέννησης' και συμπληρώστε το έτος. Αφού συμπληρώσετε το έτος πατήστε enter για να καταχωρηθεί η τιμή. Πατήστε οκ για μην εμφανιστεί ξανά το μήνυμα αυτό.", "notify");
                    cm.showAndWait();
                    prefs.setPrefs("tableTipOfeloumenoi", "false");
                }
            }
        });
        
        anenergos.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    barcode.setStyle("-fx-background-color: #cc334a; -fx-text-fill:white;");
                    if(showAnenergosTip.equals("true")){
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Ενημέρωση...", "Όταν είναι τσεκαρισμένο το πεδίο ανενεργός, τότε το πεδίο barcode είναι κόκκινο για να είναι ευκολότερο να καταλαβαίνει ο χρήστης ότι ο συγκεκριμένος ωφελούμενος έχει χαρακτηριστεί ανενεργός. Το μήνυμα αυτό δεν θα εμφανίζεται στο μέλλον!", "confirm");
                        cm.showAndWait();
                        prefs.setPrefs("showAnenergosTip", "false");
                    }
                }else{
                    barcode.setStyle("");
                }
            }
        });
        
        anergos.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                epaggelma.setDisable(!newValue);//if the anergos checkbox is selected the epaggelma textFiels is disabled and vise versa
            }
        });
        
        //we use a listener to know if the user adds a photo using the TakePhoto class
        PhotoListener.strProperty().addListener(new ChangeListener(){
            @Override 
            public void changed(ObservableValue o,Object oldVal,Object newVal){
                if(PhotoListener.getStr()!=null){
                    changePhotoButton.setText("Αλλαγή Φωτογραφίας");
                }
                PhotoID=(String) newVal; 
                
                BufferedImage bf = bfImage(PhotoID);
                
                if(bf!=null){
                    Image im = SwingFXUtils.toFXImage(bf, null);
                    image.setImage(im);
                }
                
            }
        });
        
        //initialzing tekna table
        data = getInitialTableData();
                
        tekna.setItems(data);
        tekna.setEditable(true);
        etosCol.setCellValueFactory(new PropertyValueFactory<tableManager, String>("etos"));
        
        //lets make the table cells editable
        etosCol.setCellFactory(TextFieldTableCell.forTableColumn());
        etosCol.setOnEditCommit(new EventHandler<CellEditEvent<tableManager, String>>() {
            
            @Override
            public void handle(CellEditEvent<tableManager, String> t){
                if(!NumberUtils.isNumber(t.getNewValue())){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Στον πίνακα με τα έτη γέννησης μπορείτε να συμπληρώσετε μόνο αριθμούς. Διορθώστε το πεδίο και προσπαθήστε και πάλι.", "error");
                    cm.showAndWait();
                    ((tableManager) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setEtos("Συμπληρώστε έτος γέννησης");
                }else{
                    ((tableManager) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setEtos(t.getNewValue());
                }
            }
        });
        
        tekna.getColumns().setAll(etosCol);
        //end of initialization of tekna table
        
        //initialize oikKatastasi combobox
        oikKatastasi.getItems().addAll(
            "Άγαμος",
            "Έγγαμος",
            "Διαζευγμένος",
            "Χήρος" 
        );
        
        //initialize asfForeas comboBox
        asfForeas.getItems().addAll(
            "Ανασφάλιστος",
            "ΙΚΑ",
            "ΟΓΑ",
            "ΟΑΕΕ",
            "ΕΤΑΑ",
            "ΕΟΠΥΥ",
            "ΤΠΔΥ",
            "ΤΑΠΙΤ",
            "ΕΤΑΠ – ΜΜΕ",
            "Άλλο"
        );
    }
    
    public BufferedImage bfImage(String rand){
        BufferedImage img = null;  //Buffered image coming from database
        InputStream fis = null;

        try{
            ResultSet rs;
            
            sopho.DBClass db = new sopho.DBClass();
            
            Connection conn = db.ConnectDB();
            
            String sql = "SELECT * FROM images WHERE photoID =?";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, rand);

            rs= pst.executeQuery();
            
            rs.first();

            fis = rs.getBinaryStream("image");

            img = javax.imageio.ImageIO.read(fis);  //create the BufferedImaged

        } catch (SQLException | IOException e){
            System.err.println("error " +e);
        }

       return img; //function returns a BufferedImage object
    }
    
    @FXML
    public void Save(ActionEvent event){
        
        if(barcode.getText().isEmpty()||onoma.getText().isEmpty()||eponimo.getText().isEmpty()||patronimo.getText().isEmpty()){ //checking if the user has filled the required fields
            
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε συμπληρώσει όλα τα απαιτούμενα πεδία. Θα πρέπει να συμπληρώσετε τα πεδία Barcode, Επώνυμο, Όνομα και Πατρώνυμο προκειμένου να καταχωρήσετε έναν ωφελούμενο", "error" );
            cm.showAndWait();
        
        }else if(!NumberUtils.isNumber(barcode.getText())&&!barcode.getText().isEmpty()){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Στο πεδίο barcode μπορείτε να συμπληρώσετε μόνο αριθμούς. Διορθώστε το πεδίο και προσπαθήστε και πάλι.", "error");
            cm.showAndWait();        
        }else if(!NumberUtils.isNumber(eisodima.getText())&&!eisodima.getText().isEmpty()){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Στο πεδίο εισόδημα μπορείτε να συμπληρώσετε μόνο αριθμούς. Διορθώστε το πεδίο και προσπαθήστε και πάλι.", "error");
            cm.showAndWait();        
        }else{//the user has filled the required fields. We can proceed.
            sopho.DBClass db = new sopho.DBClass();
            Connection conn=null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            
            String teknaDB = ""; //we create a var to push data to db.
            for(int i=0; i<tekna.getItems().size(); i++){//we are converting the table rows to a single comma separated string to push it to the database in a single entry.
                tableManager tbl = (tableManager) tekna.getItems().get(i);
                if(!tbl.getEtos().equals("Συμπληρώστε έτος γέννησης")){ //we are checking if the user has actually entered a number
                    teknaDB += tbl.getEtos() + ","; //we have to call getEtos from the tableManager class to get the actual value. We add the value to teknaDB and seperate with comma.
                    arithmosTeknon++;
                }
            }
            if(arithmosTeknon>0){// we need to catch the case that the user has not added any data to the table.
                teknaDB = teknaDB.substring(0, teknaDB.length()-1); // we have to remove the last comma.
            }
            conn=db.ConnectDB();
            
            //Now we will check if the user has already registered this ofeloumenos
            String sql = "SELECT * FROM ofeloumenoi WHERE barcode =?";
            try{
                pst = conn.prepareStatement(sql);
                pst.setString(1, barcode.getText());
                System.out.println("the query is:" + pst.toString());
                rs=pst.executeQuery();
                rs.last(); //i go to the last line of the result to find out the number of the line
                if(rs.getRow()>0){// ofeloumenos is already registered to the database
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Υπάρχει ήδη ωφελούμενος με αυτό το barcode. Τα στοιχεία του καταχωρημένου ωφελούμενου είναι τα εξής. Barcode:" + rs.getString("barcode") + " Επώνυμο: " + rs.getString("eponimo") + " Όνομα: " + rs.getString("onoma") + " Πατρώνυμο: " + rs.getString("patronimo"), "error" );
                    cm.showAndWait();
                }else{ // we can push the data to database...
                    sql = "INSERT INTO ofeloumenoi (barcode, eponimo, onoma, patronimo, mitronimo, imGennisis, dieuthinsi, dimos, tilefono, anergos, epaggelma, eisodima, eksartiseis, photoID, afm, tautotita, ethnikotita, metanastis, roma, oikKatastasi, hasTekna, arithmosTeknon, ilikiesTeknon, politeknos, monogoneiki, mellousaMama, amea, asfForeas, xronios, pathisi, anoTon60, monaxikos, emfiliVia, spoudastis, anenergos, loipa, registerDate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    //now we will set the values to the sql statement
                    pst.setString(1, barcode.getText());
                    pst.setString(2, eponimo.getText());
                    pst.setString(3, onoma.getText());
                    pst.setString(4, patronimo.getText());
                    pst.setString(5, mitronimo.getText());
                    //now we have to convert the imGennisis to a suitable format to be able to push it to the database
                    if(imGennisis.getValue()!=null){
                        Date date = Date.from(imGennisis.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        pst.setDate(6,sqlDate);
                    }else{
                        pst.setDate(6, null);
                    }
                    pst.setString(7, dieuthinsi.getText());
                    pst.setString(8, dimos.getText());
                    pst.setString(9, tilefono.getText());
                    pst.setInt(10, anergos.isSelected() ? 1 : 0); //set 1 if selected and 0 if not. We will use this method for all the checkboxes.
                    pst.setString(11, epaggelma.getText());
                    pst.setString(12, eisodima.getText());
                    pst.setString(13, eksartiseis.getText());
                    pst.setString(14, PhotoID);
                    pst.setString(15, afm.getText());
                    pst.setString(16, tautotita.getText());
                    pst.setString(17, ethnikotita.getText());
                    pst.setInt(18, metanastis.isSelected() ? 1 : 0);
                    pst.setInt(19, roma.isSelected() ? 1 : 0);
                    pst.setInt(20, (int) oikKatastasi.getSelectionModel().getSelectedIndex());//we are pushing to database the selected index
                    pst.setInt(21, arithmosTeknon>0 ? 1 : 0); //checking number of tekna. if >0 has tekna gets 1
                    pst.setInt(22, arithmosTeknon);
                    pst.setString(23, teknaDB); //here we use the converted to comma separated values variable in order to save the tableView data using only one field in database.
                    pst.setInt(24, politeknos.isSelected() ? 1 : 0);
                    pst.setInt( 25, monogoneiki.isSelected() ? 1 : 0);
                    pst.setInt( 26, mellousaMama.isSelected() ? 1 : 0);
                    pst.setInt( 27, amea.isSelected() ? 1 : 0);
                    pst.setInt( 28, (int) asfForeas.getSelectionModel().getSelectedIndex());//we are pushing to database the selected index
                    pst.setInt( 29, xronios.isSelected() ? 1 : 0);
                    pst.setString( 30, pathisi.getText());
                    pst.setInt(31, monaxiko.isSelected() ? 1 : 0);
                    pst.setInt(32, anoTon60.isSelected() ? 1 : 0);
                    pst.setInt(33, emfiliVia.isSelected() ? 1 : 0);
                    pst.setInt(34, spoudastis.isSelected() ? 1 : 0);
                    pst.setInt(35, anenergos.isSelected() ? 1 : 0);
                    pst.setString(36, loipa.getText());
                    //insert today's date as registerDate
                    LocalDate now = LocalDate.now();
                    java.sql.Date sqlToday = java.sql.Date.valueOf(now);
                    pst.setDate(37, sqlToday);
                    
                    System.out.println("the query is:" + pst.toString());
                    int linesAffected = pst.executeUpdate();
                    
                    //checking if the data were inserted to the database successfully
                    if(linesAffected > 0){
                        Stage stage = (Stage) barcode.getScene().getWindow();
                        try {
                            sl.StageLoad("/sopho/Ofeloumenoi/AddMore.fxml", stage, false, true); //resizable false, utility true
                        } catch (IOException ex) {
                            Logger.getLogger(AddOfeloumenoiController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else{//problem inserting data...
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Ο ωφελούμενος δεν μπόρεσε να καταχωρηθεί στη βάση δεδομένων. Προσπαθήστε και πάλι...", "error");
                        cm.showAndWait();
                    }
                }
                
            }catch (SQLException e){
                System.out.println("Πρόβλημα κατά τον έλεγχο αν υπάρχει ηδη ο ωφελούμενος στη βάση!" + e);
            }
        
        }
        
    }
    
    @FXML
    private void AddRow(ActionEvent event) {
        //create a new row after the last row
        tableManager tbl = new tableManager("Συμπληρώστε έτος γέννησης");
                
        data.add(tbl);
        int row = data.size()-1; //we compensate with -1 because the rows count from 0
        
        //selecting the new row
        tekna.requestFocus();
        tekna.getSelectionModel().select(row);
        tekna.getFocusModel().focus(row);
    }
    
    
    
    @FXML
    private void RemoveRow(ActionEvent event){
        //getting the selected row and deleting it
        int i = tekna.getSelectionModel().getSelectedIndex();
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
            tekna.requestFocus();
            tekna.getSelectionModel().select(i);
            tekna.getFocusModel().focus(i);
        }
    }
    
    public class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private SimpleStringProperty etos;
        
        public tableManager(){}
        
        public tableManager(String s){
            etos = new SimpleStringProperty(s);
        }
        
        public String getEtos(){
            return etos.get();
        }
        
        public void setEtos(String s){
            etos.set(s);
        }
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        
        List<tableManager> list = new ArrayList<>();
        
        // we can add data to the initial table using the following command
            list.add(new tableManager("Συμπληρώστε έτος γέννησης"));
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    
}
