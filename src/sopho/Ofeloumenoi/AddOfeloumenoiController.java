package sopho.Ofeloumenoi;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
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
import javafx.scene.control.ButtonType;

import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;

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
        
    sopho.StageLoader sl = new sopho.StageLoader();
    
    private ObservableList<tableManager> data;
    
    public String PhotoID;
    
       
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml", stage, true, false); //resizable true, utility false 
    }
    
    @FXML
    public void ChangePhoto(ActionEvent event) throws IOException{
        sl.StageLoadNoClose("/sopho/Ofeloumenoi/AllagiFotografias.fxml", false, true); //resizable false, utility true
        //showImageChangeDialog();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //we use a listener to know if the user adds a photo using the TakePhoto class
        PhotoListener.strProperty().addListener(new ChangeListener(){
            @Override 
            public void changed(ObservableValue o,Object oldVal,Object newVal){
                PhotoID=(String) newVal; 
                File file = new File(System.getProperty("user.home")+"/Documents/Sopho/Images/photo" + newVal + ".jpg");
                Image img = new Image(file.toURI().toString());
                image.setImage(img);
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
                ((tableManager) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setEtos(t.getNewValue());
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
    
    public void showImageChangeDialog(){
        Alert c = new Alert(Alert.AlertType.NONE);
        
        c.setTitle("Αλλαγή φωτογραφίας");
        c.initStyle(StageStyle.UNDECORATED);
        c.setHeaderText("Μπορείτε να επιλέξετε ένα αρχείο εικόνας είτε να τραβήξετε μια φωτογραφία από την κάμερα του υπολογιστή σας.");
        //c.setGraphic(new ImageView(this.getClass().getResource("/sopho/Messages/okIcon.png").toString()));
        
        //set the buttons
        ButtonType bt1 = new ButtonType("Επιλογή από αρχείο");
        ButtonType bt2 = new ButtonType("Λήψη εικόνας από κάμερα");
        
        c.getButtonTypes().setAll(bt1, bt2);
        
        Optional<ButtonType> result = c.showAndWait();
        if(result.get() == bt1){
            System.out.println("apo arxeio");
        }else if(result.get() == bt2){
            System.out.println("apo kamera");
        }
        
    }
    
    public void SetImage(String filename){
        File file = new File(filename);
        Image myimage = new Image(file.toURI().toString());
        image.setImage(myimage);
        //TODO we have to get the photo from the TakePhotoController
    }
    
    @FXML
    public void Save(ActionEvent event){
        //TODO push data to database
        String teknaDB = ""; //we create a var to push data to db.
        for(int i=0; i<tekna.getItems().size(); i++){
            tableManager tbl = (tableManager) tekna.getItems().get(i);
            teknaDB += tbl.getEtos() + ","; //we have to call getEtos from the tableManager class to get the actual value. We add the value to teknaDB and seperate with comma.
        }
        teknaDB = teknaDB.substring(0, teknaDB.length()-1); // we have to remove the last comma.
    }
    
    @FXML
    private void AddRow(ActionEvent event) {
        //create a new row after the last row
        tableManager tbl = new tableManager("Συμπληρώστε έτος γέννησης");
        
        PhotoListener.setStr("this new str");
        
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
        
        // we can add data to the initial table using the following command
            list.add(new tableManager("Συμπληρώστε έτος γέννησης"));
        
        ObservableList<tableManager> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    
}
