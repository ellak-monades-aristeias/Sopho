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

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FiltersStatistikaController implements Initializable {
    
    @FXML
    public BarChart ageBar; 
    @FXML
    public BarChart eisodimaBar;
    @FXML
    public BarChart teknaBar;
    @FXML
    public BarChart asfBar;
    @FXML
    public PieChart anergoiPie;
    @FXML
    public PieChart eksartiseisPie;
    @FXML
    public PieChart ethnikotitaPie;
    @FXML
    public PieChart metanastesPie;
    @FXML
    public PieChart romaPie;
    @FXML
    public PieChart oikKatPie;
    @FXML
    public PieChart mellousaMamaPie;
    @FXML
    public PieChart monogoneikiPie;
    @FXML
    public PieChart ameaPie;
    @FXML
    public PieChart xroniosPie;
    @FXML
    public PieChart monaxikoPie;
    @FXML
    public PieChart thimaPie;
    @FXML
    public PieChart spoudastisPie;
    @FXML
    public Button backButton;
    
    sopho.DBClass db = new sopho.DBClass();
    
    List<Integer> IDList = new ArrayList<>();
            
    sopho.StageLoader sl = new sopho.StageLoader();
    
    int age020=0;
    int age2130=0;
    int age3140=0;
    int age4150=0;
    int age5160=0;
    int age6170=0;
    int age7180=0;
    int age8190=0;
    int age91plus=0;
    
    int anergoi=0;
    
    int eis01000=0;
    int eis10012000=0;
    int eis20013000=0;
    int eis30014000=0;
    int eis40015000=0;
    int eis50016000=0;
    int eis6001plus=0;
    
    int eksartiseis=0;
    
    Map<String, Integer> ethnikotita = new HashMap();
    
    int metanastes=0;
    int roma=0;
    
    int agamos=0;
    int eggamos=0;
    int diazeugmenos=0;
    int xiros=0;
    
    int tekna0=0;
    int tekna1=0;
    int tekna2=0;
    int tekna3=0;
    int tekna4=0;
    int tekna5=0;
    int tekna5plus=0;
    
    int mellousaMama=0;
    int monogoneiki=0;
    
    int anasfalistos=0;
    int ika=0;
    int oga=0;
    int oaee=0;
    int etaa=0;
    int eopyy=0;
    int tpdy=0;
    int tapit=0;
    int etap=0;
    int asfallo=0;
    
    int amea=0;
    int xronios=0;
    int monaxiko=0;
    int thima=0;
    int spoudastis=0;
    
    int totalOfeloumenoi=0;
        
    ObservableList<PieChart.Data> anergoiData;
    ObservableList<PieChart.Data> eksartiseisData;
    ObservableList<PieChart.Data> ethnikotitaData;
    ObservableList<PieChart.Data> metanastesData;
    ObservableList<PieChart.Data> romaData;
    ObservableList<PieChart.Data> oikData;
    ObservableList<PieChart.Data> mellousaMamaData;
    ObservableList<PieChart.Data> monogoneikiData;
    ObservableList<PieChart.Data> ameaData;
    ObservableList<PieChart.Data> xroniosData;
    ObservableList<PieChart.Data> monaxikoData;
    ObservableList<PieChart.Data> thimataData;
    ObservableList<PieChart.Data> spoudastesData;
    
    ResultSet rs = sopho.ResultKeeper.rs;
    
    boolean hasData=false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        
        try {
            GetData();
        } catch (SQLException ex) {
            Logger.getLogger(GeneralStatistikaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(hasData){
            anergoiData = anergoiPieData();
            anergoiPie.getData().setAll(anergoiData);
            eksartiseisData = eksartiseisPieData();
            eksartiseisPie.getData().setAll(eksartiseisData);
            ethnikotitaData = ethnikotitaPieData();
            ethnikotitaPie.getData().setAll(ethnikotitaData);
            metanastesData = metanastesPieData();
            metanastesPie.getData().setAll(metanastesData);
            romaData = romaPieData();
            romaPie.getData().setAll(romaData);
            oikData = oikKatPieData();
            oikKatPie.getData().setAll(oikData);
            mellousaMamaData = mellousaMamaPieData();
            mellousaMamaPie.getData().setAll(mellousaMamaData);
            monogoneikiData = monogoneikiPieData();
            monogoneikiPie.getData().setAll(monogoneikiData);
            ameaData = ameaPieData();
            ameaPie.getData().setAll(ameaData);
            xroniosData = xroniosPieData();
            xroniosPie.getData().setAll(xroniosData);
            monaxikoData = monaxikoPieData();
            monaxikoPie.getData().setAll(monaxikoData);
            thimataData = thimaPieData();
            thimaPie.getData().setAll(thimataData);
            spoudastesData = spoudastisPieData();
            spoudastisPie.getData().setAll(spoudastesData);

            ageBarChart();
            ageBar.setLegendVisible(false);
            teknaBarChart();
            teknaBar.setLegendVisible(false);
            eisodimaBarChart();
            eisodimaBar.setLegendVisible(false);
            asfBarChart();
            asfBar.setLegendVisible(false);
        }
    }    
    
    @FXML
    private void GoBack(ActionEvent event) throws IOException{
        Stage stage = (Stage) backButton.getScene().getWindow();
        sl.StageLoad("/sopho/Ofeloumenoi/StatistikaMain.fxml", stage, false, true); //resizable false, utility true
    } 
    
    public void GetData() throws SQLException{
        
        rs.last();
        if(rs.getRow()>0){
            
            boolean firstEth=true;
            
            rs.beforeFirst();
            while(rs.next()){
                                
                totalOfeloumenoi++;
                
                //we have to make some conversions to get age from the birthdate that we have stored at the db
                if(rs.getDate("imGennisis")!=null){
                    Date imGennisis = rs.getDate("imGennisis");
                    LocalDate today = LocalDate.now();
                    LocalDate birthday = imGennisis.toLocalDate();
                    Period p = Period.between(birthday, today);
                    int age = p.getYears();
                    if(age<=20){
                        age020++;
                    }else if(age>20 && age<=30){
                        age2130++;
                    }else if(age>30 && age<=40){
                        age3140++;
                    }else if(age>40 && age<=50){
                        age4150++;
                    }else if(age>50 && age<=60){
                        age5160++;
                    }else if(age>60 && age<=70){
                        age6170++;
                    }else if(age>70 && age<=80){
                        age7180++;
                    }else if(age>80 && age<=90){
                        age8190++;
                    }else{
                        age91plus++;
                    }
                }
                
                anergoi+=rs.getInt("anergos"); // if anergos it will add one else it will add 0 not affecting the total
                
                //we have chosen to store eisodima as String so we have to convert it to int
                
                if(!rs.getString("eisodima").equals("")){//we have to catch the case that the field was not filled.
                    int eis = Integer.parseInt(rs.getString("eisodima"));
                    if(eis>=0 && eis<=1000){
                        eis01000++;
                    }else if(eis>1000 && eis<=2000){
                        eis10012000++;
                    }else if(eis>2000 && eis<=3000){
                        eis20013000++;
                    }else if(eis>3000 && eis<=4000){
                        eis30014000++;
                    }else if(eis>4000 && eis<=5000){
                        eis40015000++;
                    }else if(eis>5000 && eis<=6000){
                        eis50016000++;
                    }else if(eis>6000){
                        eis6001plus++;
                    }
                }
                
                if(!rs.getString("eksartiseis").equals("")) eksartiseis++;
                
                String ethnik = rs.getString("ethnikotita");
                if(!ethnik.equals("")){ //doing the proccess only if the collumn is filled with data

                    if(firstEth){
                        ethnikotita.put(ethnik, 1);
                        firstEth=false;
                        System.out.println("once");
                    }else{
                    
                        //we have to iterate through ethnikotita hashmap and add new ethnikotita if it doesn't exist. Else add to the current number
                        boolean mustadd=true;//we use mustadd to know if the value already exists through searching the map ethnikotita. if exists it is set to false
                        
                        for (Map.Entry<String, Integer> entry : ethnikotita.entrySet()){
                            if(entry.getKey().equalsIgnoreCase(ethnik)){
                                int val = (int) entry.getValue() + 1;
                                System.out.println("val is "+val);
                                entry.setValue(val);
                                mustadd=false;
                                System.out.println("found same " + ethnik);
                                System.out.println("key" + entry.getKey() + " value"+entry.getValue());
                            }
                        }
                        
                        if(mustadd){
                            ethnikotita.put(ethnik, 1);
                            System.out.println("mustadd " + ethnik);
                        }
                    }
                }
                
                metanastes += rs.getInt("metanastis");
                roma += rs.getInt("roma");
                
                if(rs.getInt("oikKatastasi")>=0){//if the user didn't selected anything the value is -1
                    if(rs.getInt("oikKatastasi")==0) agamos++;
                    if(rs.getInt("oikKatastasi")==1) eggamos++;
                    if(rs.getInt("oikKatastasi")==2) diazeugmenos++;
                    if(rs.getInt("oikKatastasi")==3) xiros++;
                }
                
                int tekna =rs.getInt("arithmosTeknon");
                if(tekna==0){
                    tekna0++;
                }else if(tekna==1){
                    tekna1++;
                }else if(tekna==2){
                    tekna2++;
                }else if(tekna==3){
                    tekna3++;
                }else if(tekna==4){
                    tekna4++;
                }else if(tekna==5){
                    tekna5++;
                }else if(tekna>5){
                    tekna5plus++;
                }
                
                mellousaMama += rs.getInt("mellousaMama");
                monogoneiki += rs.getInt("monogoneiki");
                
                int asf = rs.getInt("asfForeas");
                if(asf>=0){//only if the user has selected an option from the dropdown
                    if(asf==0){
                        anasfalistos++;
                    }else if(asf==1){
                        ika++;
                    }else if(asf==2){
                        oga++;
                    }else if(asf==3){
                        oaee++;
                    }else if(asf==4){
                        etaa++;
                    }else if(asf==5){
                        eopyy++;
                    }else if(asf==6){
                        tpdy++;
                    }else if(asf==7){
                        tapit++;
                    }else if(asf==8){
                        etap++;
                    }else if(asf==9){
                        asfallo++;
                    }
                }
                
                amea+=rs.getInt("amea");
                xronios+=rs.getInt("xronios");
                monaxiko+=rs.getInt("monaxikos");
                thima+=rs.getInt("emfiliVia");
                spoudastis+=rs.getInt("spoudastis");
                
            }
            
            hasData=true;
            
        }else{
            sopho.Messages.CustomMessageController cm = new sopho.Messages.CustomMessageController(null, "Ενημέρωση", "Δεν υπάρχουν ωφελούμενοι με τα κριτήρια αυτά για να εξαχθούν στατιστικά. Καταχωρήστε ωφελούμενους προκειμένου να εμφανιστούν τα στατιστικά.", "error");
            cm.showAndWait();
        }
    }
    
    public void ageBarChart() {
        
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        XYChart.Data<String, Integer> data = new XYChart.Data<>("0-20", age020);
        XYChart.Data<String, Integer> data2 = new XYChart.Data<>("21-30", age2130);
        XYChart.Data<String, Integer> data3 = new XYChart.Data<>("31-40", age3140);
        XYChart.Data<String, Integer> data4 = new XYChart.Data<>("41-50", age4150);
        XYChart.Data<String, Integer> data5 = new XYChart.Data<>("51-60", age5160);
        XYChart.Data<String, Integer> data6 = new XYChart.Data<>("61-70", age6170);
        XYChart.Data<String, Integer> data7 = new XYChart.Data<>("71-80", age7180);
        XYChart.Data<String, Integer> data8 = new XYChart.Data<>("81-90", age8190);
        XYChart.Data<String, Integer> data9 = new XYChart.Data<>("90+", age91plus);
        XYChart.Data<String, Integer> data10 = new XYChart.Data<>("Δεν καταγράφηκε", totalOfeloumenoi-age020-age2130-age3140-age4150-age5160-age6170-age7180-age8190-age91plus);
        
        displayLabelForData(data);
        displayLabelForData(data2);
        displayLabelForData(data3);
        displayLabelForData(data4);
        displayLabelForData(data5);
        displayLabelForData(data6);
        displayLabelForData(data7);
        displayLabelForData(data8);
        displayLabelForData(data9);
        displayLabelForData(data10);
        
        series.getData().addAll(data, data2, data3, data4, data5, data6, data7, data8, data9, data10);

        ageBar.getData().add(series);
    }
    
    public void teknaBarChart() {
        
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        XYChart.Data<String, Integer> data = new XYChart.Data<>("0", tekna0);
        XYChart.Data<String, Integer> data2 = new XYChart.Data<>("1", tekna1);
        XYChart.Data<String, Integer> data3 = new XYChart.Data<>("2", tekna2);
        XYChart.Data<String, Integer> data4 = new XYChart.Data<>("3", tekna3);
        XYChart.Data<String, Integer> data5 = new XYChart.Data<>("4", tekna4);
        XYChart.Data<String, Integer> data6 = new XYChart.Data<>("5", tekna5);
        XYChart.Data<String, Integer> data7 = new XYChart.Data<>("5+", tekna5plus);
        
        displayLabelForData(data);
        displayLabelForData(data2);
        displayLabelForData(data3);
        displayLabelForData(data4);
        displayLabelForData(data5);
        displayLabelForData(data6);
        displayLabelForData(data7);
        
        series.getData().addAll(data, data2, data3, data4, data5, data6, data7);

        teknaBar.getData().add(series);
    }
    
    public void eisodimaBarChart(){
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        XYChart.Data<String, Integer> data = new XYChart.Data<>("0-1000", eis01000);
        XYChart.Data<String, Integer> data2 = new XYChart.Data<>("1001-2000", eis10012000);
        XYChart.Data<String, Integer> data3 = new XYChart.Data<>("2001-3000", eis20013000);
        XYChart.Data<String, Integer> data4 = new XYChart.Data<>("3001-4000", eis30014000);
        XYChart.Data<String, Integer> data5 = new XYChart.Data<>("4001-5000", eis40015000);
        XYChart.Data<String, Integer> data6 = new XYChart.Data<>("5001-6000", eis50016000);
        XYChart.Data<String, Integer> data7 = new XYChart.Data<>("6000+", eis6001plus);
        XYChart.Data<String, Integer> data8 = new XYChart.Data<>("Δεν καταγράφηκε", totalOfeloumenoi-eis01000-eis10012000-eis20013000-eis30014000-eis40015000-eis50016000-eis6001plus);
        
        displayLabelForData(data);
        displayLabelForData(data2);
        displayLabelForData(data3);
        displayLabelForData(data4);
        displayLabelForData(data5);
        displayLabelForData(data6);
        displayLabelForData(data7);
        displayLabelForData(data8);
        
        series.getData().addAll(data, data2, data3, data4, data5, data6, data7, data8);

        eisodimaBar.getData().add(series);
    }
    
    public void asfBarChart(){
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        XYChart.Data<String, Integer> data = new XYChart.Data<>("Ανασφάλιστος", anasfalistos);
        XYChart.Data<String, Integer> data2 = new XYChart.Data<>("ΙΚΑ", ika);
        XYChart.Data<String, Integer> data3 = new XYChart.Data<>("ΟΓΑ", oga);
        XYChart.Data<String, Integer> data4 = new XYChart.Data<>("ΟΑΕΕ", oaee);
        XYChart.Data<String, Integer> data5 = new XYChart.Data<>("ΕΤΑΑ", etaa);
        XYChart.Data<String, Integer> data6 = new XYChart.Data<>("ΕΟΠΥΥ", eopyy);
        XYChart.Data<String, Integer> data7 = new XYChart.Data<>("ΤΠΔΥ", tpdy);
        XYChart.Data<String, Integer> data8 = new XYChart.Data<>("ΤΑΠΙΤ", tapit);
        XYChart.Data<String, Integer> data9 = new XYChart.Data<>("ΕΤΑΠ", etap);
        XYChart.Data<String, Integer> data10 = new XYChart.Data<>("ΑΛΛΟ", asfallo);
        XYChart.Data<String, Integer> data11 = new XYChart.Data<>("Δεν καταγράφηκε", (totalOfeloumenoi-anasfalistos-ika-oga-oaee-etaa-eopyy-tpdy-tapit-etap-asfallo));
        
        displayLabelForData(data);
        displayLabelForData(data2);
        displayLabelForData(data3);
        displayLabelForData(data4);
        displayLabelForData(data5);
        displayLabelForData(data6);
        displayLabelForData(data7);
        displayLabelForData(data8);
        displayLabelForData(data9);
        displayLabelForData(data10);
        displayLabelForData(data11);
        
        series.getData().addAll(data, data2, data3, data4, data5, data6, data7, data8, data9, data10, data11);

        asfBar.getData().add(series);
    }
    
    /** places a text label with a bar's value above a bar node for a given XYChart.Data */
    private void displayLabelForData(XYChart.Data<String, Integer> data) {
        data.nodeProperty().addListener(new ChangeListener<Node>() {
            @Override 
            public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                if (node != null) {
                    final Node node2 = data.getNode();
                    final Text dataText = new Text(data.getYValue() + "");
                    node2.parentProperty().addListener(new ChangeListener<Parent>() {
                        @Override 
                        public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                            Group parentGroup = (Group) parent;
                            parentGroup.getChildren().add(dataText);
                        }
                    });

                    node2.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
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
        });
    }
    
    private void displayPieValue(PieChart.Data data) {
        data.nodeProperty().addListener(new ChangeListener<Node>() {
            @Override 
            public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                if (node != null) {
                    final Node node2 = data.getNode();
                    final Text dataText = new Text(data.getPieValue() + "");
                    node2.parentProperty().addListener(new ChangeListener<Parent>() {
                        @Override 
                        public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                            Group parentGroup = (Group) parent;
                            parentGroup.getChildren().add(dataText);
                        }
                    });

                    node2.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
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
        });
    }

    private ObservableList<PieChart.Data> anergoiPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Άνεργοι ("+anergoi+")", anergoi));
        list.add(new PieChart.Data("Εργαζόμενοι ("+(totalOfeloumenoi-anergoi)+")", totalOfeloumenoi - anergoi));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> eksartiseisPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Με εξαρτήσεις ("+eksartiseis+")", eksartiseis));
        list.add(new PieChart.Data("Χωρίς εξαρτήσεις ("+(totalOfeloumenoi-eksartiseis)+")", totalOfeloumenoi - eksartiseis));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> ethnikotitaPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        int hasFilled=0;//we have to know the number of persons that have the field blank

        for (Map.Entry<String, Integer> entry : ethnikotita.entrySet()){
            
            list.add(new PieChart.Data(entry.getKey() + "("+(int) entry.getValue()+")", (int) entry.getValue()));

            hasFilled += (int) entry.getValue();

        }
        
        list.add(new PieChart.Data("Δεν καταγράφηκε ("+(totalOfeloumenoi-hasFilled)+")", totalOfeloumenoi-hasFilled));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> metanastesPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Ναι ("+metanastes+")", metanastes));
        list.add(new PieChart.Data("Όχι ("+(totalOfeloumenoi-metanastes)+")", totalOfeloumenoi - metanastes));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> romaPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Ναι ("+roma+")", roma));
        list.add(new PieChart.Data("Όχι ("+(totalOfeloumenoi-roma)+")", totalOfeloumenoi - roma));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> oikKatPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Άγαμος ("+agamos+")", agamos));
        list.add(new PieChart.Data("Έγγαμος ("+eggamos+")", eggamos));
        list.add(new PieChart.Data("Διαζευγμένος ("+diazeugmenos+")", diazeugmenos));
        list.add(new PieChart.Data("Χήρος ("+xiros+")", xiros));
        list.add(new PieChart.Data("Δεν καταγράφηκε ("+(totalOfeloumenoi-agamos-eggamos-diazeugmenos-xiros)+")", totalOfeloumenoi-agamos-eggamos-diazeugmenos-xiros));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> mellousaMamaPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Ναι ("+mellousaMama+")", mellousaMama));
        list.add(new PieChart.Data("Όχι ("+(totalOfeloumenoi-mellousaMama)+")", totalOfeloumenoi - mellousaMama));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> monogoneikiPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Ναι ("+monogoneiki+")", monogoneiki));
        list.add(new PieChart.Data("Όχι ("+(totalOfeloumenoi-monogoneiki)+")", totalOfeloumenoi - monogoneiki));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> ameaPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Ναι ("+amea+")", amea));
        list.add(new PieChart.Data("Όχι ("+(totalOfeloumenoi-amea)+")", totalOfeloumenoi - amea));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> xroniosPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Ναι ("+xronios+")", xronios));
        list.add(new PieChart.Data("Όχι ("+(totalOfeloumenoi-xronios)+")", totalOfeloumenoi - xronios));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> monaxikoPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Ναι ("+monaxiko+")", monaxiko));
        list.add(new PieChart.Data("Όχι ("+(totalOfeloumenoi-monaxiko)+")", totalOfeloumenoi - monaxiko));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> thimaPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Ναι ("+thima+")", thima));
        list.add(new PieChart.Data("Όχι ("+(totalOfeloumenoi-thima)+")", totalOfeloumenoi - thima));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
    
    private ObservableList<PieChart.Data> spoudastisPieData(){
              
        List<PieChart.Data> list = new ArrayList<>();
        
        list.add(new PieChart.Data("Ναι ("+spoudastis+")", spoudastis));
        list.add(new PieChart.Data("Όχι ("+(totalOfeloumenoi-spoudastis)+")", totalOfeloumenoi - spoudastis));
        
        ObservableList<PieChart.Data> mydata = FXCollections.observableList(list);
        
        return mydata;
    }
}
