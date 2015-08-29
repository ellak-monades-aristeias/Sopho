package sopho.Ofeloumenoi;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

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
    public TableView tekna;
    @FXML
    public TableColumn etosCol;
    @FXML
    public ImageView image;
        
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/OfeloumenoiMain.fxml", stage, true, false); //resizable true, utility false 
    }
    
    @FXML
    public void ChangePhoto(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/AllagiFotografias.fxml", stage, true, false); //resizable true, utility false
        /*// get default webcam and open it
	Webcam webcam = Webcam.getDefault();
	webcam.open();

	// get image
	BufferedImage image = webcam.getImage();

	// save image to PNG file
	ImageIO.write(image, "PNG", new File("test.png"));*/
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tekna.setEditable(true);
        etosCol.setCellValueFactory(new PropertyValueFactory<tableManager, Integer>("etos"));
        etosCol.setCellFactory(TextFieldTableCell.forTableColumn());
        etosCol.setOnEditCommit(new EventHandler<CellEditEvent<tableManager, Integer>>() {
            @Override
            public void handle(CellEditEvent<tableManager, Integer> t) {
                ((tableManager) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    ).setEtos(t.getNewValue());
            }
        });
    
    }
    
    
    
    /*
    public void ChangeImage(String filename){
        File file = new File(filename);
        Image img = new Image(file.toURI().toString());
        image.setImage(img);
    }*/
    
    @FXML
    public void Save(ActionEvent event){
        //TODO push data to database
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

        private tableManager(Integer etos){
            this.etos = new SimpleIntegerProperty(etos);
        }
        
        public void setEtos(Integer myEtos) {
            etos.set(myEtos);
        }
        
    }
    /*
    //this class is required to be able to edit the cells for the "tekna" tableView
    class EditingCell extends TableCell<tableManager, String> {
 
        private TextField textField;
 
        public EditingCell() {
        }
 
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
 
            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
 
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, 
                    Boolean arg1, Boolean arg2) {
                        if (!arg2) {
                            commitEdit(textField.getText());
                        }
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

    */
}
