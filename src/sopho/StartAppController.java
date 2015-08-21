package sopho;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StartAppController implements Initializable {
    
    @FXML
    public Button login;
    @FXML
    public PasswordField pass;
    @FXML
    public Button closeButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }
    
    public void start(Stage primaryStage) throws Exception {
        login.requestFocus();
    }
    
    Connection conn=null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DBClass db = new DBClass();
    
    StageLoader sl = new StageLoader();
  
    @FXML
    private void QuitApp(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void Login(ActionEvent a){
        conn=db.ConnectDB();
        
        System.out.println("the pass is: "+pass.getText());
        
        MD5 md5 = new MD5();
        String encryptedPass = md5.getMD5(pass.getText());
        
        String sql = "SELECT pass FROM users WHERE pass=?";
        
        try {
            
            pst = conn.prepareStatement(sql);
            pst.setString(1, encryptedPass);
            System.out.println("the query is:" + pst.toString());
            rs = pst.executeQuery();
            
            //now we will check if the query returned a result
            
            rs.last(); //i go to the last line of the result to find out the number of the line
            
            
            if(rs.getRow()>0){    
                //login successful. Goto to mainApp
                Stage stage = (Stage) login.getScene().getWindow();
                sl.StageLoad("MainApp.fxml", stage);
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Προσοχή!");
                alert.setHeaderText("Λάθος κωδικός πρόσβασης");
                alert.setContentText("Προσπαθήστε και πάλι...");
                alert.showAndWait();                
            }
            
            
        }catch (SQLException | IOException e){
            System.out.println("Πρόβλημα κατά την εγγραφή!" + e);
        }
    }
    
    
    
}
