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
package sopho;

import insidefx.undecorator.Undecorator;
import insidefx.undecorator.UndecoratorScene;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

//this is a helper class to load the new scene and close the old one
public class StageLoader {
    
    public static boolean fullscreen=false;
    public static String lastStage;
    
    LockEdit le = new LockEdit();
    
    //this method opens new stage and closes the old one
    public void StageLoad(String fxmlName, Stage oldStage, boolean resizable, boolean utilityWindow) throws IOException{
                
        StageLoadNoClose(fxmlName, resizable, utilityWindow);
        
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
        scene.getStylesheets().add( getClass().getResource("Style.css").toExternalForm()); //adding the css for the fxml windows styling*/
        stage.setScene(scene);

        // Set sizes based on client area's sizes
        Undecorator undecorator = scene.getUndecorator();
        stage.setMinWidth(undecorator.getMinWidth());
        stage.setMinHeight(undecorator.getMinHeight());
        stage.setWidth(undecorator.getPrefWidth());
        stage.setHeight(undecorator.getPrefHeight());
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        System.out.println("width" + stage.getWidth());
        System.out.println("height" + stage.getHeight());
        stage.setX((primScreenBounds.getWidth() - stage.getWidth() - 44) / 2); //we use -44 to compensate for the UndecoratorBis that adds 22 pixels at every edge around the stage
        stage.setY((primScreenBounds.getHeight() - stage.getHeight() - 44) / 2);
        stage.setResizable(resizable);
        
        if (undecorator.getMaxWidth() > 0) {
            stage.setMaxWidth(undecorator.getMaxWidth());
        }
        if (undecorator.getMaxHeight() > 0) {
            stage.setMaxHeight(undecorator.getMaxHeight());
        }
        stage.sizeToScene();
        stage.show();
        
        Platform.setImplicitExit(false);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(fxmlName.equals("/sopho/Ofeloumenoi/EditOfeloumenoi.fxml")){
                    //we have to set editing back to 0 else the editing will remain 1 and the next time someone tries to access the record it will be locked.
                    try {
                        le.LockEditing(false, sopho.Ofeloumenoi.EditOfeloumenoiController.selID, "ofeloumenoi");
                    } catch (SQLException ex) {
                        Logger.getLogger(StageLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if(fxmlName.equals("/sopho/EuresiErgasias/EpeksergasiaAtomouPouZita.fxml")){
                    //we have to set editing back to 0 else the editing will remain 1 and the next time someone tries to access the record it will be locked.
                    try {
                        le.LockEditing(false, sopho.EuresiErgasias.EpeksergasiaAtomouPouZitaController.selectedID, "zitounergasia");
                    } catch (SQLException ex) {
                        Logger.getLogger(StageLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if(fxmlName.equals("/sopho/EuresiErgasias/EpeksergasiaThesisErgasias.fxml")){
                    //we have to set editing back to 0 else the editing will remain 1 and the next time someone tries to access the record it will be locked.
                    try {
                        le.LockEditing(false, sopho.EuresiErgasias.EpeksergasiaThesisErgasiasController.selID, "theseisergasias");
                    } catch (SQLException ex) {
                        Logger.getLogger(StageLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if(fxmlName.equals("/sopho/Filoksenoumenoi/EpeksergasiaFiloksenoumenou.fxml")){
                    //we have to set editing back to 0 else the editing will remain 1 and the next time someone tries to access the record it will be locked.
                    try {
                        le.LockEditing(false, sopho.Filoksenoumenoi.EpeksergasiaFiloksenoumenouController.selID, "filoksenoumenoi");
                    } catch (SQLException ex) {
                        Logger.getLogger(StageLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if(fxmlName.equals("/sopho/MathimataStiriksis/EpeksergasiaKathigiti.fxml")){
                    //we have to set editing back to 0 else the editing will remain 1 and the next time someone tries to access the record it will be locked.
                    try {
                        le.LockEditing(false, sopho.MathimataStiriksis.EpeksergasiaKathigitiController.selectedID, "kathigites");
                    } catch (SQLException ex) {
                        Logger.getLogger(StageLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if(fxmlName.equals("/sopho/MathimataStiriksis/EpeksergasiaMathiti.fxml")){
                    //we have to set editing back to 0 else the editing will remain 1 and the next time someone tries to access the record it will be locked.
                    try {
                        le.LockEditing(false, sopho.MathimataStiriksis.EpeksergasiaMathitiController.selectedID, "mathites");
                    } catch (SQLException ex) {
                        Logger.getLogger(StageLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if(fxmlName.equals("/sopho/Vivliothiki/EditVivlio.fxml")){
                    //we have to set editing back to 0 else the editing will remain 1 and the next time someone tries to access the record it will be locked.
                    try {
                        le.LockEditing(false, sopho.Vivliothiki.EditVivlioController.selID, "vivliothiki");
                    } catch (SQLException ex) {
                        Logger.getLogger(StageLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //we have to invoke platform exit; else the app will continue running in the background because we invoked platform.setImplicitExit(false) above.
                Platform.exit();
            }
        });
        
    }
    
}
