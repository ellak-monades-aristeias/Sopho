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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sopho.Ofeloumenoi.PhotoListener;

public class EpeksergasiaAtomouPouZitaController implements Initializable {

    @FXML
    public TextField eponimo, onoma, patronimo, tilefono, dieuthinsi, dimos, eidikotita, diploma;
    @FXML
    public TextArea loipa;
    @FXML
    public DatePicker date;
    @FXML
    public TableView<tableManager> empeiria;
    @FXML
    public TableColumn<tableManager, String> empeiriaCol;
    @FXML
    public CheckBox ye, de, te, pe;
    @FXML
    public ComboBox oikKatastasi;
    @FXML
    public ImageView image;
    @FXML
    public Button changePhotoButton;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    private ObservableList<tableManager> data;
    
    public String PhotoID;
    
    sopho.DBClass db = new sopho.DBClass();
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    ResultSet oldrs = sopho.ResultKeeper.rs;
    int selIndex = sopho.ResultKeeper.selectedIndex;
    
    sopho.LockEdit le = new sopho.LockEdit();
    
    public static int selectedID=-1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            oldrs.first();
            if(selIndex>0){
                oldrs.relative(selIndex);
            }
        
            selectedID = oldrs.getInt("id");
            
            eponimo.setText(oldrs.getString("eponimo"));
            onoma.setText(oldrs.getString("onoma"));
            patronimo.setText(oldrs.getString("eponimo"));

            if(oldrs.getDate("imGennisis")!=null){
                    date.setValue(oldrs.getDate("imGennisis").toLocalDate());
            }

            tilefono.setText(oldrs.getString("eponimo"));
            dieuthinsi.setText(oldrs.getString("eponimo"));
            dimos.setText(oldrs.getString("eponimo"));
            
            PhotoID = oldrs.getString("photoID");
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
            
            eidikotita.setText(oldrs.getString("eidikotita"));
            
            //we have to convert the int values to selected or not checkboxes
            if(oldrs.getInt("ye")==1){
                ye.setSelected(true);
            }
            if(oldrs.getInt("de")==1){
                de.setSelected(true);
            }
            if(oldrs.getInt("te")==1){
                te.setSelected(true);
            }
            if(oldrs.getInt("pe")==1){
                pe.setSelected(true);
            }
            
            diploma.setText(oldrs.getString("diploma"));
            
            //the table is filled with values using getInitialTableData function bellow
            
            int oikKat = oldrs.getInt("oikKatastasi");
            
            if(oikKat>=0){// only if the user has selected a value before saving to the db.
                oikKatastasi.getSelectionModel().select(oikKat);
            }
            loipa.setText(oldrs.getString("loipa"));
        
        } catch (SQLException ex) {
            Logger.getLogger(EpeksergasiaAtomouPouZitaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        PhotoListener.setStr(null);//we set the var to null to know if the user has selected a photo to be shown
        
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
        
        //initialzing table
        data = getInitialTableData();
                
        empeiria.setItems(data);
        empeiria.setEditable(true);
        empeiriaCol.setCellValueFactory(new PropertyValueFactory<tableManager, String>("empeiria"));
        
        //lets make the table cells editable
        empeiriaCol.setCellFactory(TextFieldTableCell.forTableColumn());
        empeiriaCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<tableManager, String>>() {
            
            @Override
            public void handle(TableColumn.CellEditEvent<tableManager, String> t){
                ((tableManager) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setEmpeiria(t.getNewValue());
            }
        });
        
        empeiria.getColumns().setAll(empeiriaCol);
        //end of initialization of table
        
        
        //initialize oikKatastasi combobox
        oikKatastasi.getItems().addAll(
            "Άγαμος",
            "Έγγαμος",
            "Διαζευγμένος",
            "Χήρος" 
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
    private void GoBack(ActionEvent event) throws IOException, SQLException{
        if (le.LockEditing(false, selectedID, "zitounergasia")){//unlock editing and check if unlock was successful
            Stage stage = (Stage) eponimo.getScene().getWindow();
            sl.StageLoad("/sopho/EuresiErgasias/EuresiErgasiasMain.fxml", stage, true, false); //resizable true, utility false
        }else{
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα", "Τα στοιχεία του ατόμου που επεξεργάζεστε δεν μπόρεσαν να ξεκλειδωθούν για να είναι διαθέσιμα για επεξεργασία από άλλους χρήστες. Δοκιμάστε και πάλι.", "error");
            cm.showAndWait();
        }
        
    }
    
    @FXML
    public void ChangePhoto(ActionEvent event) throws IOException{
        sl.StageLoadNoClose("/sopho/Ofeloumenoi/AllagiFotografias.fxml", false, true); //resizable false, utility true
    }
    
    @FXML
    private void AddRow(ActionEvent event) {
        //create a new row after the last row
        tableManager tbl = new tableManager("Συμπληρώστε προηγούμενη εργασία");
                
        data.add(tbl);
        int row = data.size()-1; //we compensate with -1 because the rows count from 0
        
        //selecting the new row
        empeiria.requestFocus();
        empeiria.getSelectionModel().select(row);
        empeiria.getFocusModel().focus(row);
    }
    
    @FXML
    private void RemoveRow(ActionEvent event){
        //getting the selected row and deleting it
        int i = empeiria.getSelectionModel().getSelectedIndex();
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
            empeiria.requestFocus();
            empeiria.getSelectionModel().select(i);
            empeiria.getFocusModel().focus(i);
        }
    }
    
    @FXML
    public void Save(ActionEvent event) throws IOException{
        
        if(onoma.getText().isEmpty()||eponimo.getText().isEmpty()||patronimo.getText().isEmpty()||tilefono.getText().isEmpty()){ //checking if the user has filled the required fields
            
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε συμπληρώσει όλα τα απαιτούμενα πεδία. Θα πρέπει να συμπληρώσετε τα πεδία Επώνυμο, Όνομα, Πατρώνυμο και Τηλέφωνο προκειμένου να συνεχίσετε!", "error" );
            cm.showAndWait();
        
        }else{//the user has filled the required fields. We can proceed.
                        
            int empeiriaLines=0;
            String empeiriaDB = ""; //we create a var to push data to db.
            for(int i=0; i<empeiria.getItems().size(); i++){//we are converting the table rows to a single comma separated string to push it to the database in a single entry.
                tableManager tbl = (tableManager) empeiria.getItems().get(i);
                if(!tbl.getEmpeiria().equals("Συμπληρώστε προηγούμενη εργασία")){ //we are checking if the user has actually entered something to the table
                    empeiriaDB += tbl.getEmpeiria() + ","; //we have to call getEmpeiria from the tableManager class to get the actual value. We add the value to empeiriaDB and seperate with comma.
                    empeiriaLines++;
                }
            }
            if(empeiriaLines>0){// we need to catch the case that the user has not added any data to the table.
                empeiriaDB = empeiriaDB.substring(0, empeiriaDB.length()-1); // we have to remove the last comma.
            }
            conn=db.ConnectDB();
           
            String sql = "UPDATE zitounergasia SET eponimo=?, onoma=?, patronimo=?, imGennisis=?, tilefono=?, dieuthinsi=?, dimos=?, photoID=?, eidikotita=?, ye=?, de=?, te=?, pe=?, diploma=?, empeiria=?, oikKatastasi=?, loipa=?, editing=0 WHERE id=?";
            try{
                    pst=conn.prepareStatement(sql);
                    
                    pst.setString(1, eponimo.getText());
                    pst.setString(2, onoma.getText());
                    pst.setString(3, patronimo.getText());
                    
                    //now we have to convert the imGennisis to a suitable format to be able to push it to the database
                    if(date.getValue()!=null){
                        Date mydate = Date.from(date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                        java.sql.Date sqlDate = new java.sql.Date(mydate.getTime());
                        pst.setDate(4,sqlDate);
                    }else{
                        pst.setDate(4, null);
                    }
                    
                    pst.setString(5, tilefono.getText());
                    pst.setString(6, dieuthinsi.getText());
                    pst.setString(7, dimos.getText());
                    pst.setString(8, PhotoID);
                    pst.setString(9, eidikotita.getText());
                    pst.setInt(10, ye.isSelected()? 1 : 0);
                    pst.setInt(11, de.isSelected()? 1 : 0);
                    pst.setInt(12, te.isSelected()? 1 : 0);
                    pst.setInt(13, pe.isSelected()? 1 : 0);
                    pst.setString(14, diploma.getText());
                    pst.setString(15, empeiriaDB);
                    pst.setInt(16, (int) oikKatastasi.getSelectionModel().getSelectedIndex());//we are pushing to database the selected index
                    pst.setString(17, loipa.getText());
                    pst.setInt(18, selectedID);
                    
                    System.out.println("the query is:" + pst.toString());
                    int linesAffected = pst.executeUpdate();
                    
                    //checking if the data were inserted to the database successfully
                    if(linesAffected > 0){
                        Stage stage = (Stage) onoma.getScene().getWindow();
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Τα στοιχεία του ατόμου που ζητά εργασία έχουν ενημερωθεί με επιτυχία!", "confirm");
                        cm.showAndWait();

                        if(le.LockEditing(false, selectedID, "zitounergasia")){//unlocking the record because we finished editing
                            sl.StageLoad("/sopho/EuresiErgasias/EuresiErgasiasMain.fxml", stage, false, true); //resizable false, utility true
                        }else{
                            sopho.Messages.CustomMessageController cm2 = new sopho.Messages.CustomMessageController(null, "Πρόβλημα", "Τα στοιχεία του ατόμου που επεξεργαστήκατε δεν μπόρεσαν να ξεκλειδωθούν. Αυτό σημαίνει ότι δεν θα μπορείτε να τα επεξεργαστείτε ξανά. Για να διορθώσετε το πρόβλημα κάντε και πάλι αποθήκευση ή κλείστε το παράθυρο.", "error");
                            cm2.showAndWait();
                        }
                        
                    }else{//problem inserting data...
                        sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Τα στοιχεία του ατόμου που ζητά εργασία δεν μπόρεσαν να ενημερωθούν. Προσπαθήστε και πάλι...", "error");
                        cm.showAndWait();
                    }
                    
            }catch (SQLException e){
                System.out.println("Πρόβλημα κατά την ενημέρωση των στοιχείων στη βάση: " + e);
            }
        
        }
        
        
    }

    
    public class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private SimpleStringProperty empeiria;
        
        public tableManager(){}
        
        public tableManager(String s){
            empeiria = new SimpleStringProperty(s);
        }
        
        public String getEmpeiria(){
            return empeiria.get();
        }
        
        public void setEmpeiria(String s){
            empeiria.set(s);
        }
    }
    
    //this is required to get the initial data from the table and push them to an observableList.
    private ObservableList<tableManager> getInitialTableData(){
        
        List<tableManager> list = new ArrayList<>();
        
        //now we have to convert the single string empeiria that we get from the database to table rows. The empeiria string is formated as comma separated values.
        try {
            String empeiriaDB = oldrs.getString("empeiria");
            if(!empeiriaDB.isEmpty()){
                List<String> empeiriaList = Arrays.asList(empeiriaDB.split(","));
                for (int i = 0; i < empeiriaList.size(); i++) {
                    list.add(new tableManager(empeiriaList.get(i)));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EpeksergasiaAtomouPouZitaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);

        return mydata;
        
    }   
    
}
