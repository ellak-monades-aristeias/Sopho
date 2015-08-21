package sopho.Ofeloumenoi;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class AllagiFotografiasController implements Initializable {
    
    @FXML
    public Button closeButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    public void OpenPhotoFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
             
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
              
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
                       
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
                //myImageView.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(AllagiFotografiasController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @FXML
    public void TakePhoto(ActionEvent event) throws IOException{
        Stage stage = (Stage) closeButton.getScene().getWindow();
        sl.StageLoad("Ofeloumenoi/TakePhoto.fxml", stage);
    }
    
}
