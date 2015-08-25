package sopho;

import insidefx.undecorator.Undecorator;
import insidefx.undecorator.UndecoratorScene;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


//this is a helper class to load the new scene and close the old one
public class StageLoader {
    
    public static boolean fullscreen=false;
    
    //this method opens new stage and closes the old one
    public void StageLoad(String fxmlName, Stage oldStage, String currentStageName, boolean resizable, boolean utilityWindow) throws IOException{
        
        StageLoadNoClose(fxmlName, resizable, utilityWindow);
        
        Sopho.lastStageName = currentStageName;
        
        oldStage.close();
    }
    
    //this method is to show new stage without closing the old one
    public void StageLoadNoClose(String fxmlName, boolean resizable, boolean utilityWindow) throws IOException {
               
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));
        Region root = (Region) fxmlLoader.load();
        Stage stage = new Stage();
        
        StageStyle style;
        if(utilityWindow){
            style = StageStyle.UTILITY;
        }else{
            style = StageStyle.UNDECORATED;
        }
        
        UndecoratorScene scene = new UndecoratorScene(stage, style, root, null);
        scene.getStylesheets().add( getClass().getResource("myFonts.css").toExternalForm()); //adding the css for the fxml windows styling*/
        stage.setScene(scene);

        // Set sizes based on client area's sizes
        Undecorator undecorator = scene.getUndecorator();
        stage.setMinWidth(undecorator.getMinWidth());
        stage.setMinHeight(undecorator.getMinHeight());
        stage.setWidth(undecorator.getPrefWidth());
        stage.setHeight(undecorator.getPrefHeight());
        
        stage.setResizable(resizable);
        
        if (undecorator.getMaxWidth() > 0) {
            stage.setMaxWidth(undecorator.getMaxWidth());
        }
        if (undecorator.getMaxHeight() > 0) {
            stage.setMaxHeight(undecorator.getMaxHeight());
        }
        stage.sizeToScene();
        stage.show();
        
    }
    
}
