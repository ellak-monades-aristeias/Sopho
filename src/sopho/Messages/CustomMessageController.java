package sopho.Messages;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomMessageController extends Stage implements Initializable {

    @FXML
    public Label title;
    @FXML
    public Label message;
    @FXML
    public Button ok;
    @FXML
    public ImageView icon;
    
    private String myTitle;
    private String myMessage;
    private String myType;
    public Image image;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        message.setText(myMessage);
        title.setText(myTitle);
        if(myType.equals("error")){
            image = new Image(CustomMessageController.class.getResourceAsStream("errorIcon.png"));
        }else if(myType.equals("confirm")){
            image = new Image(CustomMessageController.class.getResourceAsStream("okIcon.png"));
        }
        icon.setImage(image);
    }    
    
    public CustomMessageController(Parent parent, String title, String message, String type){
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomMessage.fxml"));
        fxmlLoader.setController(this);
        
        myTitle = title;
        myMessage = message;
        myType=type;
        
        this.initStyle(StageStyle.UNDECORATED);

        try{
            setScene(new Scene((Parent) fxmlLoader.load()));
        }catch (IOException e){
            System.out.println("Exception " + e);
        }
    }
    
    @FXML
    public void OK(ActionEvent event){
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }
       
}
