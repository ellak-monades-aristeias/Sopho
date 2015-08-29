package sopho;

import java.sql.ResultSet;

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
