package sopho.Ofeloumenoi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AllagiFotografiasController implements Initializable {
    
    @FXML
    public AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    public void OpenPhotoFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
             
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG);
              
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            
            if(file!=null){
                try {
                    String path = file.getAbsolutePath();
                    
                    String photoName = file.getName();
                    
                    Path FROM = Paths.get(path);
                    
                    System.out.println("FROM path is:"+FROM.toString());
                    
                    //we need to create the destination directory if it doesn't exist
                    File theDir = new File(System.getProperty("user.home")+"/Documents/Sopho/Images");
                    if (!theDir.exists()) {
                        System.out.println("creating directory");
                        boolean result = false;

                        try{
                            theDir.mkdirs();
                            result = true;
                        } 
                        catch(SecurityException se){
                            System.out.println(se);
                        }        
                        if(result) {    
                            System.out.println("DIR created");
                        }
                    }
                    String destPath = System.getProperty("user.home")+"/Documents/Sopho/Images/"+photoName;
                    System.out.println("Destination path:"+destPath);
                    Path TO = Paths.get(destPath);
                    CopyOption[] options = new CopyOption[]{
                      StandardCopyOption.REPLACE_EXISTING,
                      StandardCopyOption.COPY_ATTRIBUTES
                    }; 
                    java.nio.file.Files.copy(FROM, TO, options);
                    
                    //finally we call the photoListener class to change the photo at the AddOfeloumenoi Stage.
                    PhotoListener.setStr(photoName);
                    Stage stage = (Stage) anchorPane.getScene().getWindow();
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(AllagiFotografiasController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{//the user didn't choose any file
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Προσοχή!");
                alert.setHeaderText("Δεν επιλέξατε κάποιο αρχείο εικόνας");
                alert.setContentText("Επιλέξτε κάποιο αρχείο εικόνας ή τραβήξτε μια φωτογραφία από την κάμερα.");
                alert.showAndWait();
            }
    }
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @FXML
    public void TakePhoto(ActionEvent event) throws IOException{
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/TakePhoto.fxml", stage, false, true); //resizable false, utility true
    }
    
}
