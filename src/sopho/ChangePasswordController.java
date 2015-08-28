package sopho;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChangePasswordController implements Initializable {

    @FXML
    public TextField password;
    @FXML
    public TextField password2;
    @FXML
    public TextField passwordOld;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    DBClass db = new DBClass();
    StageLoader sl = new StageLoader();
    
    @FXML
    public void Save(ActionEvent event) throws IOException {
        if(CheckOldPass(passwordOld.getText())){
            if(password.getText().equals(password2.getText())){
                if(ChangePass(password.getText())){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Επιτυχία!");
                    alert.setHeaderText("Ο κωδικός πρόσβασης έχει αλλάξει");
                    alert.setContentText("Στο εξής θα χρησιμοποιείτε τον νέο κωδικό για την πρόσβαση στην εφαρμογή");
                    alert.showAndWait();
                    Stage stage = (Stage) password.getScene().getWindow();
                    sl.StageLoad("Settings.fxml", stage, false, true);  //resizable false, utility true
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Προσοχή!");
                alert.setHeaderText("Ασυμφωνία κωδικών");
                alert.setContentText("Ο νέος κωδικός πρόσβασης δεν ταιριάζει με τον κωδικό πρόσβασης που εισάγατε στο πεδίο της επιβεβαίωσης!");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Προσοχή!");
            alert.setHeaderText("Λάθος κωδικός");
            alert.setContentText("Ο παλιός κωδικός πρόσβασης που έχετε εισάγει δεν είναι σωστός");
            alert.showAndWait();
        }
    }
    
    public boolean CheckOldPass(String pass){
        Connection conn=null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        conn=db.ConnectDB();
        
        MD5 md5 = new MD5();
        String encryptedPass = md5.getMD5(pass);
        
        String sql = "SELECT pass FROM users WHERE pass=?";
        
        try {
            
            pst = conn.prepareStatement(sql);
            pst.setString(1, encryptedPass);
            System.out.println("the query is:" + pst.toString());
            rs = pst.executeQuery();
            
            //now we will check if the query returned a result
            
            rs.last(); //i go to the last line of the result to find out the number of the line
            
            return rs.getRow()>0;
            
        }catch (SQLException e){
            System.out.println("Πρόβλημα κατά την εγγραφή!" + e);
            return false;
        }
    }
    
    public boolean ChangePass(String pass){
        
        Connection conn=null;
        PreparedStatement pst = null;
        
        conn=db.ConnectDB();
        
            System.out.println("the pass is: "+password.getText());

            MD5 md5 = new MD5();
            String encryptedPassOld = md5.getMD5(passwordOld.getText());
            String encryptedPass = md5.getMD5(password.getText());
            
            String sql = "UPDATE users SET pass=? WHERE pass='"+encryptedPassOld+"'";

            try {

                pst = conn.prepareStatement(sql);
                pst.setString(1, encryptedPass);
                System.out.println("the query is:" + pst.toString());
                int flag = pst.executeUpdate(); //the flag is the number of affected rows
                
                //we check if everything was fine through the flag integer.
                
                if (flag>0){
                    System.out.println("Successful update of password");
                    return true;
                }
                else if(flag==0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Σφάλμα!");
                    alert.setHeaderText("Ο κωδικός δεν μπόρεσε να εισαχθεί στη βάση");
                    alert.setContentText("Προσπαθήστε και πάλι να εισάγετε τον κωδικό ή επικοινωνήστε με την τεχνική υποστήριξη.");
                    alert.showAndWait();
                    return false;
                }

            }catch (SQLException e){
                
                System.out.println("Πρόβλημα κατά την εισαγωγή του κωδικού στη βάση!" + e);
                return false;
                
            }  
        return false;
    }
    
    @FXML
    public void Cancel(ActionEvent event) throws IOException{
        Stage stage = (Stage) password.getScene().getWindow();
        sl.StageLoad("Settings.fxml", stage, true, false); //resizable true, utility false
    }
}
