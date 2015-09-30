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
package sopho.Ofeloumenoi;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class AllagiFotografiasController implements Initializable {
    
    @FXML
    public AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    public void OpenPhotoFile(ActionEvent event) throws FileNotFoundException, SQLException{
        FileChooser fileChooser = new FileChooser();
             
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG);
              
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);

            if(file!=null){
                
                double size = file.length();
                
                if(size>3145728){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Μεγάλο αρχείο...", "Το αρχείο που επιλέξατε είναι μεγαλύτερο από 3 MB. Επιλέξτε μικρότερο αρχείο", "error");
                    cm.showAndWait();
                }else{
                
                    try {

                        //converting the image to bufferedimage 
                        BufferedImage image = ImageIO.read(file);

                        //converting the image to bytearray
                        ByteArrayOutputStream ou = new ByteArrayOutputStream();
                        ImageIO.write(image,"jpeg",ou);
                        byte[] buf = ou.toByteArray();
                        // setup stream for blob
                        ByteArrayInputStream inStream = new ByteArrayInputStream(buf);       

                        String sql = "INSERT INTO images (photoID, image) VALUES (?,?)";

                        sopho.DBClass db = new sopho.DBClass();

                        Connection conn=db.ConnectDB();

                        PreparedStatement pst = conn.prepareStatement(sql);

                        //produce random filename
                        int myRand = randInt(100000000, 999999999);//we use great numbers to reduce the posibility to have 2 identical filenames

                        pst.setString(1, myRand+""); //trick because myRand int cannot be dereferenced
                        pst.setBinaryStream(2, inStream,inStream.available());

                        int flag = pst.executeUpdate();

                        if(flag>0){
                            //finally we call the photoListener class to change the photo at the AddOfeloumenoi Stage.
                            PhotoListener.setStr(myRand+"");
                            Stage stage = (Stage) anchorPane.getScene().getWindow();
                            stage.close();
                        }else{
                            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Πρόβλημα!", "Αδυναμία αποθήκευσης της φωτογραφίας στη βάση δεδομένων", "error");
                            cm.showAndWait();
                        }


                    } catch (IOException ex) {
                        Logger.getLogger(AllagiFotografiasController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }else{//the user didn't choose any file
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν επιλέξατε αρχείο εικόνας. Επιλέξτε κάποιο αρχείο εικόνας ή τραβήξτε μια φωτογραφία από την κάμερα.", "error");
                cm.showAndWait();    
            }
    }
    
    sopho.StageLoader sl = new sopho.StageLoader();
    
    //this is a method to produce random numbers for the photo filename
    public int randInt(int min, int max) {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    
    @FXML
    public void TakePhoto(ActionEvent event) throws IOException{
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/TakePhoto.fxml", stage, false, true); //resizable false, utility true
    }
    
}
