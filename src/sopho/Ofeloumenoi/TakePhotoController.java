package sopho.Ofeloumenoi;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class TakePhotoController implements Initializable {
       
    @FXML
    public Button takePhoto;
    @FXML
    public ComboBox<WebCamInfo> selCamera;
    @FXML
    public ImageView picture;
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    private BufferedImage grabbedImage;
    private Webcam selWebCam = null;
    private boolean stopCamera = false;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
    
    @FXML
    public void TakePicture(ActionEvent event){
        BufferedImage image = selWebCam.getImage();
        
        
        //produce random filename
        int myRand = randInt(100000000, 999999999);//we use great numbers to reduce the posibility to have 2 identical filenames

        String filename = System.getProperty("user.home")+"/Documents/Sopho/Images/photo" + myRand + ".jpg";

        File theDir = new File(System.getProperty("user.home")+"/Documents/Sopho/Images");
        System.out.println(theDir.toString());
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
        if(SaveImage(image, filename)){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Η φωτογραφία του ωφελούμενου αποθηκεύτηκε επιτυχώς!", "confirm");
            cm.showAndWait();
            String PhotoID = myRand + "";//this is a trick to get myRand because this int cannot be dereferenced and so we could not use toString() method.
            PhotoListener.setStr(PhotoID);
            Stage stage = (Stage) takePhoto.getScene().getWindow();
            closeCamera();//we have to close the camera before exiting
            stage.close();
        }else{
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα...", "Η φωτογραφία του ωφελούμενου δεν μπόρεσε να αποθηκευτεί. Προσπαθήστε και πάλι...", "error");
            cm.showAndWait();
        }
    }
    
    public boolean SaveImage(BufferedImage image, String filename){
        try {
            // save image to JPG file
            ImageIO.write(image, "JPG", new File(filename));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(TakePhotoController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //this is a method to produce random numbers for the photo filename
    public static int randInt(int min, int max) {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();
        int webCamCounter = 0;
        
        for (Webcam webcam : Webcam.getWebcams()) {
            WebCamInfo webCamInfo = new WebCamInfo();
            webCamInfo.setWebCamIndex(webCamCounter);
            webCamInfo.setWebCamName(webcam.getName());
            options.add(webCamInfo);
            webCamCounter++;
        }
        selCamera.setItems(options);
        //selCamera.setPromptText(cameraListPromptText);
        selCamera.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<WebCamInfo>() {

            @Override
            public void changed(ObservableValue<? extends WebCamInfo> arg0, WebCamInfo arg1, WebCamInfo arg2) {
                    if (arg2 != null) {
                            System.out.println("WebCam Index: " + arg2.getWebCamIndex() + ": WebCam Name:" + arg2.getWebCamName());
                            initializeWebCam(arg2.getWebCamIndex());
                    }
            }
        });

    }
    
    protected void initializeWebCam(final int webCamIndex) {

        Task<Void> webCamIntilizer = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                if (selWebCam == null) {
                    selWebCam = Webcam.getWebcams().get(webCamIndex);
                    selWebCam.setViewSize(new Dimension(640, 480)); // set size of image
                    selWebCam.open();
                } else {
                    closeCamera();
                    selWebCam = Webcam.getWebcams().get(webCamIndex);
                    selWebCam.setViewSize(new Dimension(640, 480)); // set size of image
                    selWebCam.open();
                }
                startWebCamStream();
                return null;
            }

        };

        new Thread(webCamIntilizer).start();
    }
    
    private void closeCamera() {
        stopCamera=true;
        if (selWebCam != null) {
            selWebCam.close();
        }
    }
    
    protected void startWebCamStream() {

        stopCamera = false;
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                while (!stopCamera) {
                    try {
                        if ((grabbedImage = selWebCam.getImage()) != null) {

                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    final Image mainiamge = SwingFXUtils
                                        .toFXImage(grabbedImage, null);
                                    imageProperty.set(mainiamge);
                                }
                            });

                            grabbedImage.flush();

                        }
                    } catch (Exception e) {
                            e.printStackTrace();
                    }
                }

                return null;
            }

        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        picture.imageProperty().bind(imageProperty);

    }
    
    // this is a helper class to get the webcam info
    private class WebCamInfo {
        private String webCamName;
        private int webCamIndex;

        public String getWebCamName() {
                return webCamName;
        }

        public void setWebCamName(String webCamName) {
                this.webCamName = webCamName;
        }

        public int getWebCamIndex() {
                return webCamIndex;
        }

        public void setWebCamIndex(int webCamIndex) {
                this.webCamIndex = webCamIndex;
        }
        
        private BufferedImage grabbedImage;
	private Webcam selWebCam = null;
	private boolean stopCamera = false;
	private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();

	private String cameraListPromptText = "Choose Camera";

        @Override
        public String toString() {
                return webCamName;
        }
    }
    
}
