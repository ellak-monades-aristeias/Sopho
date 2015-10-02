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

//this is a helper class to be able to lock and unlock editing.

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//we need to ensure that no one has access to the same data we are editing to avoid conflicts caused by saving the data to the db when finished editing
public class LockEdit {
    
    public boolean LockEditing(boolean lock, int id, String tableName) throws SQLException{
        int edit = lock? 1:0;//set 1 to lock and 0 to unlock
        String sql = "UPDATE "+tableName+" SET editing="+edit+" WHERE id=?";
        
        DBClass db = new DBClass();
        Connection conn = db.ConnectDB();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        return pst.executeUpdate()>0;//we are returning true if the number of affected rows is > 0
    }
    
    public boolean CheckLock(int id, String tableName) throws SQLException{

        String sql = "SELECT editing FROM "+tableName+" WHERE id=?";
        
        DBClass db = new DBClass();
        Connection conn = db.ConnectDB();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery(sql);
        rs.last();
        return rs.getInt("editing")==1; // if editing ==1 return true else return false
    }
    
}
