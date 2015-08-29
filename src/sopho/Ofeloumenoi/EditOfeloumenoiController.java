package sopho.Ofeloumenoi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class EditOfeloumenoiController implements Initializable {

    public ResultSet rs = sopho.ResultKeeper.rs;
    
    public Integer selBarcode; //the selected barcode from the search results
    
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet newrs = null; //the new resultSet that we will get
    sopho.DBClass db = new sopho.DBClass();
    
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
    public TableView tekna;
    @FXML
    public TableColumn etos;
    @FXML
    public ImageView image;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(sopho.ResultKeeper.selectedIndex==1){
            //we had only one result from the search
            try {
                
                selBarcode = rs.getInt("barcode");
                
                //now we will set the data to the fields
                
                
            } catch (SQLException ex) {
                Logger.getLogger(EditOfeloumenoiController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void ChangePhoto(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/AllagiFotografias.fxml", stage, true, false); //resizable true, utility false
    }
    
    @FXML
    public void SaveChanges(ActionEvent event){
        //TODO push results to database
    }
    
    @FXML
    private void AddRow(ActionEvent event) {
      ObservableList<Map> allData = tekna.getItems();
      int offset = allData.size();
      Map<String, String> dataRow = new HashMap<>();
      for (int j = 0; j < tekna.getColumns().size(); j++) {
        String mapKey = Character.toString((char) ('A' + j));
        String value1 = mapKey + (offset + 1);
        dataRow.put(mapKey, value1);
      }
      allData.add(dataRow);
    }
    
    @FXML
    private void RemoveRow(ActionEvent event){
        try {
            Integer sel = tekna.getSelectionModel().getSelectedIndex();
            
            if (sel==-1){
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Πρέπει να επιλέξετε τουλάχιστον μια γραμμή από τον πίνακα για να αφαιρεθεί.", "error");
                cm.showAndWait();
            }else{            
                System.out.println("the selected line is " + sel);
                
                ObservableList<Map> allData = tekna.getItems();
                allData.remove(sel);
                
                tekna.setItems(null); // we remove the items to clear the table;
                tekna.setItems(allData); // and then we set the new data with the selected line removed.
            }
        }catch(Exception e){
            System.out.println("table selection error " + e);
        }
    }
    
    public static class tableManager { //this is a helper class to display the data from the resultSet to the table properly.
        
        private IntegerProperty etos;

        private tableManager(Integer etos, String taksi){
            this.etos = new SimpleIntegerProperty(etos);
        }
        
    }
    
}
