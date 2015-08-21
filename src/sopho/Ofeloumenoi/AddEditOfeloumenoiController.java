package sopho.Ofeloumenoi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sopho.Mouse;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddEditOfeloumenoiController implements Initializable {
    
    double initialX;
    double initialY;
    public boolean maximized=false;
    
    @FXML
    private Pane movePane;
    @FXML
    private Button closeButton;
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
    private void QuitApp(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void Minimize(ActionEvent event){
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }
    
    @FXML
    private void Maximize(ActionEvent event){
        Stage applicationStage = (Stage) maximize.getScene().getWindow();
        applicationStage.setFullScreen(!maximized);//using !maximized because the window is not maximized at first. We initialize the var as false.
        maximized=!maximized;
    }
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("MainApp.fxml", stage); // be careful here. We use MainApp.fxml and not ../MainApp.fxml because the StageLoader class is setting the scene and the StageLoader is located to a different folder than this script. So we have to compensate for this and think the relative path to the StageLoader and not the relative path to this script.
    }
    
    @FXML
    public void ChangePhoto(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("Ofeloumenoi/AllagiFotografias.fxml", stage);
        /*// get default webcam and open it
	Webcam webcam = Webcam.getDefault();
	webcam.open();

	// get image
	BufferedImage image = webcam.getImage();

	// save image to PNG file
	ImageIO.write(image, "PNG", new File("test.png"));*/
    }
    
    // the following is required for moving the undecorated window
    Mouse mouse = new Mouse();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Stage thisStage = (Stage) closeButton.getScene().getWindow();
        sopho.ResizeHelper.addResizeListener(thisStage);//this is a resize helper for undecorated windows
        
        //Move Window Handler Starts
        movePane.setOnMousePressed(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            //System.out.println("Pressed");
            //System.out.println("Mouse : " + t.getX() + " | " + t.getY());
            mouse.setX(t.getX());
            mouse.setY(t.getY());
        }
        });
        movePane.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                //System.out.println("Dragged");
                //System.out.println("Mouse : " + t.getX() + " | " + t.getY());
                if(!maximized){//move only if window is not maximized
                    movePane.getScene().getWindow().setX( t.getScreenX() - mouse.getX() - 14);
                    movePane.getScene().getWindow().setY( t.getScreenY() - mouse.getY() - 14);
                }
            }
        });
        //Move Window Handler Ends
    }
    
    //this is a helper funstion to open easily new stages and closing the old ones.
    public void StageLoader(String fxmlName, Stage oldStage) throws IOException{
        
        Stage stage = new Stage();
        
        stage.initStyle(StageStyle.UNDECORATED);
        
        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));        
        
        Scene scene = new Scene(root);
        
        scene.getStylesheets().add( getClass().getResource("../myFonts.css").toExternalForm()); //adding the css for the fxml windows styling
        
        stage.setScene(scene);
        
        stage.show();
        
        oldStage.close();
    }
    
    public void ChangeImage(String filename){
        File file = new File(filename);
        Image img = new Image(file.toURI().toString());
        image.setImage(img);
    }
    
}
