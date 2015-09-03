package sopho;

import java.sql.ResultSet;

//this class is required to keep the result set and pass it to the EditOfeloumenoiController so that the stage can initialize the fields with data.
public class ResultKeeper {
    public static ResultSet rs;
    public static Integer selectedIndex;
    
    public ResultSet getRes(){
        return rs;
    }
    
    public void setRes(ResultSet results){
        rs=results;
    }
}
