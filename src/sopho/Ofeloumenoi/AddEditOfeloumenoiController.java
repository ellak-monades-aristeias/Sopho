package sopho.Ofeloumenoi;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddEditOfeloumenoiController implements Initializable {
    
    @FXML
    private Button minimize;
    @FXML
    private Button maximize;
    @FXML
    private Button backButton;
    @FXML
    public ImageView image;
        
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("MainApp.fxml", stage, "Ofeloumenoi/AddEditOfeloumenoi.fxml", true, false); //resizable true, utility false // be careful here. We use MainApp.fxml and not ../MainApp.fxml because the StageLoader class is setting the scene and the StageLoader is located to a different folder than this script. So we have to compensate for this and think the relative path to the StageLoader and not the relative path to this script.
    }
    
    @FXML
    public void ChangePhoto(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("Ofeloumenoi/AllagiFotografias.fxml", stage, "Ofeloumenoi/AddEditOfeloumenoi.fxml", true, false); //resizable true, utility false
        /*// get default webcam and open it
	Webcam webcam = Webcam.getDefault();
	webcam.open();

	// get image
	BufferedImage image = webcam.getImage();

	// save image to PNG file
	ImageIO.write(image, "PNG", new File("test.png"));*/
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    public void ChangeImage(String filename){
        File file = new File(filename);
        Image img = new Image(file.toURI().toString());
        image.setImage(img);
    }
    
}
