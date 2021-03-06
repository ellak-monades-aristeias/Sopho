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
    public Button ok, yes, no;
    @FXML
    public ImageView icon;
    
    private String myTitle;
    private String myMessage;
    private String myType;
    public Image image;
    public boolean saidYes=false;
    public boolean showButtons=true;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        message.setText(myMessage);
        title.setText(myTitle);
        if(myType.equals("error")){
            image = new Image(CustomMessageController.class.getResourceAsStream("errorIcon.png"));
            yes.setVisible(false);
            no.setVisible(false);
        }else if(myType.equals("confirm")){
            image = new Image(CustomMessageController.class.getResourceAsStream("okIcon.png"));
            yes.setVisible(false);
            no.setVisible(false);
        }else if(myType.equals("question")){
            image = new Image(CustomMessageController.class.getResourceAsStream("questionIcon.png"));
            ok.setVisible(false);
        }else if(myType.equals("notify")){
            image = new Image(CustomMessageController.class.getResourceAsStream("notifyIcon.png"));
            yes.setVisible(false);
            no.setVisible(false);
        }
        if(!showButtons){
            yes.setVisible(false);
            no.setVisible(false);
            ok.setVisible(false);
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
    
    @FXML
    public void Yes(ActionEvent event){
        saidYes=true;
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void No(ActionEvent event){
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }
       
}
