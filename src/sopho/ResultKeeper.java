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

import java.sql.ResultSet;

//this class is required to keep the result set and pass it to the EditOfeloumenoiController so that the stage can initialize the fields with data.
public class ResultKeeper {
    public static ResultSet rs;
    public static Integer selectedIndex;
    public static boolean multipleResults;
    
    public ResultSet getRes(){
        return rs;
    }
    
    public void setRes(ResultSet results){
        rs=results;
    }
}
