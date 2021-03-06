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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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

public class EditOfeloumenoiController implements Initializable {
    
    @FXML
    public Button backButton, changePhotoButton;
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
        
    sopho.StageLoader sl = new sopho.StageLoader();
    
    sopho.LockEdit le = new sopho.LockEdit();
    
    private ObservableList<tableManager> data;
    
    ResultSet rs = sopho.ResultKeeper.rs;
    int selectedIndex = sopho.ResultKeeper.selectedIndex;
    
    //we make it static to access it from the stageLoader class
    public static int selID=-1;
    
    public String PhotoID;
    public int arithmosTeknon=0;
    
    public String oldBarcode;//we have to keep the oldBarcode for reference to the database
    
    PrefsHandler prefs = new PrefsHandler();
    
    public String showAnenergosTip = prefs.getPrefs("showAnenergosTip"); //this var is required to show the tip about the anenergos checkbox. When that checkbox is checked the username field is marked red and we need to inform the user why this happens.   
       
    @FXML
    private void GoBack(ActionEvent event) throws IOException, SQLException{
        if(le.LockEditing(false, selID, "ofeloumenoi")){
            Stage stage = (Stage) backButton.getScene().getWindow();
            if(sopho.ResultKeeper.multipleResults){
                sl.StageLoad("/sopho/Ofeloumenoi/MultipleSearchResults.fxml", stage, true, false); //resizable true, utility false
            }else{
                sl.StageLoad("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml", stage, true, false); //resizable true, utility false
            }           
        }else{
            sopho.Messages.CustomMessageController cm2 = new sopho.Messages.CustomMessageController(null, "Πρόβλημα", "Τα στοιχεία του ωφελούμενου δεν μπόρεσαν να ξεκλειδωθούν. Αυτό σημαίνει ότι δεν θα μπορείτε να τα επεξεργαστείτε ξανά. Για να διορθώσετε το πρόβλημα προσπαθήστε να κλείσετε το παράθυρο.", "error");
            cm2.showAndWait();
        }
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
        
        
        try {
            
            rs.first();//move the cursor to the first row
            if(selectedIndex>0){//only if we need to move from the first line
                rs.relative(selectedIndex);//move to the row that we selected at the previous scene
            }
            
            //keep the selected ofeloumenos id
            selID = rs.getInt("id");
        
            System.out.println("The selected rs row is " + rs.getRow());
            
            oldBarcode=rs.getString("barcode");
            
            //now we will set all the fields using the data from database
            barcode.setText(rs.getString("barcode"));
            eponimo.setText(rs.getString("eponimo"));
            onoma.setText(rs.getString("onoma"));
            patronimo.setText(rs.getString("patronimo"));
            mitronimo.setText(rs.getString("mitronimo"));
            
            if(rs.getDate("imGennisis")!=null){
                imGennisis.setValue(rs.getDate("imGennisis").toLocalDate());
            }
            
            dieuthinsi.setText(rs.getString("dieuthinsi"));
            dimos.setText(rs.getString("dimos"));
            tilefono.setText(rs.getString("tilefono"));
            anergos.setSelected((rs.getInt("anergos") != 0)); //if 0 set false, else set true
            epaggelma.setText(rs.getString("epaggelma"));
            eisodima.setText(rs.getString("eisodima"));
            eksartiseis.setText(rs.getString("eksartiseis"));
            
            PhotoID = rs.getString("PhotoID");
            if(PhotoID != null){
                //we have a picture stored at the database.
                BufferedImage bf = bfImage(PhotoID);
                
                if(bf!=null){
                    Image im = SwingFXUtils.toFXImage(bf, null);
                    image.setImage(im);
                }
            
                //and we set the button bellow image to "Αλλαγή Φωτογραφίας"
                changePhotoButton.setText("Αλλαγή Φωτογραφίας");
            }
            
            afm.setText(rs.getString("afm"));
            tautotita.setText(rs.getString("tautotita"));
            ethnikotita.setText(rs.getString("ethnikotita"));
            metanastis.setSelected(rs.getInt("metanastis") != 0);
            roma.setSelected(rs.getInt("roma") != 0);
            int oikKatIndex = rs.getInt("oikKatastasi");
            if(oikKatIndex>=0){
                oikKatastasi.getSelectionModel().select(oikKatIndex);
            }
            
            //we add data to tekna table using the tableManager class bellow
            
            politeknos.setSelected(rs.getInt("politeknos") != 0);
            monogoneiki.setSelected(rs.getInt("monogoneiki") != 0);
            mellousaMama.setSelected(rs.getInt("mellousaMama") != 0);
            amea.setSelected(rs.getInt("amea") != 0);
            int asfForeasIndex = rs.getInt("asfForeas");
            if(asfForeasIndex>=0){
                asfForeas.getSelectionModel().select(asfForeasIndex);
            }
            xronios.setSelected(rs.getInt("xronios") != 0);
            pathisi.setText(rs.getString("pathisi"));
            anoTon60.setSelected(rs.getInt("anoTon60") != 0);
            monaxiko.setSelected(rs.getInt("monaxikos") != 0);
            emfiliVia.setSelected(rs.getInt("emfiliVia") != 0);
            spoudastis.setSelected(rs.getInt("spoudastis") != 0);
            int anenergosFlag = rs.getInt("anenergos");
            if(anenergosFlag==1){
                barcode.setStyle("-fx-background-color: #cc334a; -fx-text-fill:white;");
            }
            anenergos.setSelected(anenergosFlag != 0);
            loipa.setText(rs.getString("loipa"));

        } catch (SQLException ex) {
            Logger.getLogger(EditOfeloumenoiController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //adding a listener to anenergos checkbox to know if the user checks it
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
        
        //we use a listener to know if the user adds a photo using the TakePhoto class
        PhotoListener.strProperty().addListener(new ChangeListener(){
            @Override 
            public void changed(ObservableValue o,Object oldVal,Object newVal){
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
        
        
    }
    
    public BufferedImage bfImage(String rand){
        BufferedImage img = null;  //Buffered image coming from database
        InputStream fis = null;

        try{
            ResultSet myrs;
            
            sopho.DBClass db = new sopho.DBClass();
            
            Connection conn = db.ConnectDB();
            
            String sql = "SELECT * FROM images WHERE photoID =?";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, rand);

            myrs= pst.executeQuery();
            
            myrs.first();

            fis = myrs.getBinaryStream("image");

            img = javax.imageio.ImageIO.read(fis);  //create the BufferedImaged

        } catch (SQLException | IOException e){
            System.err.println("error " +e);
        }

       return img; //function returns a BufferedImage object
    }
    
    @FXML
    public void Save(ActionEvent event) throws IOException{
        
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
            ResultSet rset = null;
            
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
            
            try{
                // we can push the data to database...
                String sql = "UPDATE ofeloumenoi SET barcode=?, eponimo=?, onoma=?, patronimo=?, mitronimo=?, imGennisis=?, dieuthinsi=?, dimos=?, tilefono=?, anergos=?, epaggelma=?, eisodima=?, eksartiseis=?, photoID=?, afm=?, tautotita=?, ethnikotita=?, metanastis=?, roma=?, oikKatastasi=?, hasTekna=?, arithmosTeknon=?, ilikiesTeknon=?, politeknos=?, monogoneiki=?, mellousaMama=?, amea=?, asfForeas=?, xronios=?, pathisi=?, anoTon60=?, monaxikos=?, emfiliVia=?, spoudastis=?, anenergos=?, loipa=? WHERE barcode=?";
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
                
                pst.setString(37, oldBarcode); // we update the values to the database table where barcode = oldBarcode

                System.out.println("the query is:" + pst.toString());
                int linesAffected = pst.executeUpdate();

                //checking if the data were inserted to the database successfully
                if(linesAffected > 0){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Τα στοιχεία του ωφελούμενου έχουν ενημερωθεί με επιτυχία, σύμφωνα με τις αλλαγές που κάνατε.", "confirm");
                    cm.showAndWait();
                    
                    if(le.LockEditing(false, selID, "ofeloumenoi")){
                        Stage stage = (Stage) barcode.getScene().getWindow();
                        sl.StageLoad("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml", stage, true, false); //resizable true, utility false
                    }else{
                        sopho.Messages.CustomMessageController cm2 = new sopho.Messages.CustomMessageController(null, "Πρόβλημα", "Τα στοιχεία του ωφελούμενου δεν μπόρεσαν να ξεκλειδωθούν. Αυτό σημαίνει ότι δεν θα μπορείτε να τα επεξεργαστείτε ξανά. Για να διορθώσετε το πρόβλημα προσπαθήστε να κλείσετε το παράθυρο.", "error");
                        cm2.showAndWait();
                    }
                }else{//problem inserting data...
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Οι αλλαγές δεν μπόρεσαν να καταχωρηθούν στη βάση δεδομένων. Προσπαθήστε και πάλι...", "error");
                    cm.showAndWait();
                }
            }catch (SQLException e){
                System.out.println("Πρόβλημα κατά τον έλεγχο αν υπάρχει ηδη ο ωφελούμενος στη βάση!" + e);
            }
        }
    }
    
    @FXML
    private void AddRow() {
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
        tableManager tbl = (tableManager) tekna.getSelectionModel().getSelectedItem();
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
        
        //we have to add the values from database to the table
        try {
            
            String teknaDatabase = rs.getString("ilikiesTeknon");
            if(!teknaDatabase.isEmpty()){
                List<String> ilikiesList = Arrays.asList(teknaDatabase.split(","));
                for (int i = 0; i < ilikiesList.size(); i++) {
                    list.add(new tableManager(ilikiesList.get(i)));
		}
            }

        } catch (SQLException ex) {
            Logger.getLogger(EditOfeloumenoiController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
}
