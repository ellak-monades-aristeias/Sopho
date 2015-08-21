package sopho;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


//this is a helper class to load the new scene and close the old one
public class StageLoader {
    
    public static boolean fullscreen=false;
    
    //this method opens new stage and closes the old one
    public void StageLoad(String fxmlName, Stage oldStage) throws IOException{
        
        StageLoadNoClose(fxmlName);
                
        oldStage.close();
    }
    
    //this method is to show new stage without closing the old one
    public void StageLoadNoClose(String fxmlName) throws IOException {
        
        Stage stage = new Stage();
        
        stage.initStyle(StageStyle.UNDECORATED);
                
        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));        
        
        Scene scene = new Scene(root);
        
        StageSetup(stage);
        
        scene.getStylesheets().add( getClass().getResource("myFonts.css").toExternalForm()); //adding the css for the fxml windows styling
        
        stage.setScene(scene);
                
        stage.show();
        
    }
     
    // this method is required to set to fullscreen the new stage if the old one was at fullscreen
    public void StageSetup(Stage stage){
        //System.out.println("show fullscreen: "+fullscreen);
        stage.setFullScreen(fullscreen);//if the previous stage was set to fullscreen we need to set the new one also in fullscreen
    }
    
}
