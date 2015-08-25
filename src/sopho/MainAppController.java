package sopho;

import insidefx.undecorator.Undecorator;
import insidefx.undecorator.UndecoratorScene;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class MainAppController implements Initializable {
    
    double initialX;
    double initialY;
    
    /*@FXML
    private Pane movePane;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimize;
    @FXML
    private Button maximize;*/
    @FXML
    private Button settings;
    
    //the following are functions for handling OnAction events when you click the buttons in the GUI
    
    /*@FXML
    private void QuitApp(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void Minimize(ActionEvent event){
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
        
    }*/
    /*Region root = new Region();
    Stage udstage = (Stage) closeButton.getScene().getWindow();
    Undecorator ud = new Undecorator(udstage, root);
    */
    /*@FXML
    private void Maximize(ActionEvent event){
        /*ud.switchMaximize();*/
        /*Stage appStage = (Stage) maximize.getScene().getWindow();
        appStage.setMaximized(!StageLoader.fullscreen);//using !StageLoader.fullscreen because the window is not maximized at first. We initialize the var as false at the sopho.java
        StageLoader.fullscreen=!StageLoader.fullscreen;*/
    //}
    
    StageLoader sl = new StageLoader(); // instantiating the Stage Loader helper
    
    @FXML
    private void OpenSettings(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("Settings.fxml", oldstage, "MainApp.fxml", false, true); //resizable false, utility true
    }
    
    @FXML
    private void OpenOfeloumenoi(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/AddEditOfeloumenoi.fxml", oldstage, "MainApp.fxml", true, false); //resizable true, utility false
    }
    
    
    @FXML
    private void OpenDikaiologitika(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("Dikaiologitika/dikaiologitika.fxml", oldstage, "MainApp.fxml", true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenEidi(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("Eidi/EidiMain.fxml", oldstage, "MainApp.fxml", true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenApothiki(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("ApothikiAntikeimenon/ApothikiMain.fxml", oldstage, "MainApp.fxml", true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenMathimataStiriksis(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("MathimataStiriksis/MathimataMain.fxml", oldstage, "MainApp.fxml", true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenEuresiErgasias(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("EuresiErgasias/EuresiErgasiasMain.fxml", oldstage, "MainApp.fxml", true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenFiloksenoumenoi(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("Filoksenoumenoi/FiloksenoumenoiMain.fxml", oldstage, "MainApp.fxml", true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenVivliothiki(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("Vivliothiki/VivliothikiMain.fxml", oldstage, "MainApp.fxml", true, false); //resizable true, utility false
    }
    
    @FXML
    private void OpenStatistika(ActionEvent event) throws IOException {
        Stage oldstage = (Stage) settings.getScene().getWindow();
        sl.StageLoad("Statistika/StatistikaMain.fxml", oldstage, "MainApp.fxml", true, false); //resizable true, utility false
    }
    
    // the following is required for moving the undecorated window
    //Mouse mouse = new Mouse();
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        /*
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
                if(!StageLoader.fullscreen){//move only if window is not in fullscreen
                    movePane.getScene().getWindow().setX( t.getScreenX() - mouse.getX() - 14);
                    movePane.getScene().getWindow().setY( t.getScreenY() - mouse.getY() - 14);
                }
            }
        });
        //Move Window Handler Ends*/
    }
  
}
