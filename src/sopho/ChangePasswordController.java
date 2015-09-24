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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChangePasswordController implements Initializable {
    
    @FXML
    public TextField password;
    @FXML
    public TextField password2;
    @FXML
    public TextField passwordOld;
    
    int selID;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}  
    DBClass db = new DBClass();
    StageLoader sl = new StageLoader();
    
    @FXML
    public void Save(ActionEvent event) throws IOException {
        if(CheckOldPass(passwordOld.getText())){
            if(password.getText().equals(password2.getText())){
                if(ChangePass(password.getText())){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Τέλεια!", "Ο κωδικός έχει αλλάξει. Στο εξής θα χρησιμοποιείτε το νέο κωδικό που ορίσατε για την πρόσβαση στην εφαρμογή!", "confirm");
                    cm.showAndWait();
                    Stage stage = (Stage) password.getScene().getWindow();
                    sl.StageLoad("Settings.fxml", stage, false, true);  //resizable false, utility true
                }
            }else{
                sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Ο κωδικός που έχετε εισάγει στο πεδίο της επιβεβαίωσης κωδικού δεν συμφωνεί με τον κωδικό που εισάγατε στο πεδίο του νέου κωδικού. Οι δύο αυτοί κωδικοί θα πρέπει να είναι ίδιοι.", "error");
                cm.showAndWait();
            }
        }else{
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Ο παλιός κωδικός πρόσβασης που έχετε εισάγει δεν είναι σωστός!", "error");
            cm.showAndWait();
        }
    }
    
    public boolean CheckOldPass(String pass){
        Connection conn=null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        conn=db.ConnectDB();
        
        MD5 md5 = new MD5();
        String encryptedPass = md5.getMD5(pass);
        
        String sql = "SELECT * FROM users WHERE pass=?";
        
        try {
            
            pst = conn.prepareStatement(sql);
            pst.setString(1, encryptedPass);
            System.out.println("the query is:" + pst.toString());
            rs = pst.executeQuery();
            
            //now we will check if the query returned a result
            
            rs.last(); //i go to the last line of the result to find out the number of the line
            
            if( rs.getRow()>0){
                selID=rs.getInt("id");
                return true;
            }else{
                return false;
            }
            
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
            String encryptedPass = md5.getMD5(password.getText());
            
            String sql = "UPDATE users SET pass=? WHERE id=?";

            try {

                pst = conn.prepareStatement(sql);
                pst.setString(1, encryptedPass);
                pst.setInt(2, selID);
                System.out.println("the query is:" + pst.toString());
                int flag = pst.executeUpdate(); //the flag is the number of affected rows
                
                //we check if everything was fine through the flag integer.
                
                if (flag>0){
                    System.out.println("Successful update of password");
                    return true;
                }
                else if(flag==0){
                    sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Ο κωδικός δεν μπόρεσε να εισαχθεί στη βάση. Προσπαθήστε και πάλι!", "error");
                    cm.showAndWait();
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
