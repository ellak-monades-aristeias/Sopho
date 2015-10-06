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
package sopho.Eidi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FiltersStatistikaController implements Initializable {

    @FXML
    public BarChart eidiBar;
    @FXML
    public CategoryAxis catAxis;
    @FXML
    public DatePicker fromDate;
    @FXML
    public DatePicker toDate;
    @FXML
    public Button backButton;
    @FXML
    public VBox options;
    
    sopho.DBClass db = new sopho.DBClass();
    
    List<Integer> IDList = new ArrayList<>();
            
    sopho.StageLoader sl = new sopho.StageLoader();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        eidiBar.setLegendVisible(false);
    }    
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Eidi/StatistikaMain.fxml", stage, false, true); //resizable false, utility true
    }
    
    @FXML
    public void ShowStats(ActionEvent e){
        if(fromDate.getValue()==null){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε επιλέξει την ημερομηνία έναρξης των στατιστικών στοιχείων.", "error");
            cm.showAndWait();
        }else if(toDate.getValue()==null){
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Προσοχή!", "Δεν έχετε επιλέξει την ημερομηνία λήξης των στατιστικών στοιχείων.", "error");
            cm.showAndWait();
        }else{
            //we are ready to show statistics.
            
            //now we have to make some conversions.
            
            //hide options and show bar chart
            options.setVisible(false);
            eidiBar.setVisible(true);
            
            LocalDate fromLocalDate = fromDate.getValue();
            Timestamp fromTimestamp = Timestamp.valueOf(fromLocalDate.atStartOfDay());//from the start of the day
            
            LocalDate toLocalDate = toDate.getValue();
            Timestamp toTimeStamp = Timestamp.valueOf(toLocalDate.atTime(23, 59, 59));//till the end of the last day.
            
            setBarChart(fromTimestamp, toTimeStamp);
            
        }
    }  
    
    public void setBarChart(Timestamp fromDate, Timestamp toDate) {
        
        try{
            Connection conn = db.ConnectDB();
            String sql = "SELECT * FROM eidinames";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            rs.last();
            
            int eidiNumber = rs.getRow();
            
            rs.first();
            
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            
            for (int i = 0; i<eidiNumber; i++){
                pst = conn.prepareStatement("SELECT * FROM eidiofeloumenoi WHERE eidos"+(i+1)+"=1 AND date>=? AND date<=?");
                pst.setTimestamp(1, fromDate);
                pst.setTimestamp(2, toDate);
                ResultSet rs2 = pst.executeQuery();
                rs2.last();
                int dothikan = rs2.getRow();
                
                XYChart.Data<String, Integer> data = new XYChart.Data<>(rs.getString("name"), dothikan);
                
                data.nodeProperty().addListener(new ChangeListener<Node>() {
                    @Override 
                    public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                        if (node != null) {
                            displayLabelForData(data);
                        } 
                    }
                });
                

                series.getData().add(data);
                
                rs.next();//move to the next name
                  
            }

        eidiBar.getData().add(series);
        
        } catch (SQLException ex) {
            Logger.getLogger(GeneralStatistikaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** places a text label with a bar's value above a bar node for a given XYChart.Data */
    private void displayLabelForData(XYChart.Data<String, Integer> data) {
        final Node node = data.getNode();
        final Text dataText = new Text(data.getYValue() + "");
        node.parentProperty().addListener(new ChangeListener<Parent>() {
            @Override 
            public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().add(dataText);
            }
        });
        
        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override 
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                dataText.setLayoutX(
                    Math.round(
                        bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
                    )
                );
                dataText.setLayoutY(
                    Math.round(
                        bounds.getMinY() - dataText.prefHeight(-1) * 0.5
                    )
                );
            }
        });
    }    
    
}
