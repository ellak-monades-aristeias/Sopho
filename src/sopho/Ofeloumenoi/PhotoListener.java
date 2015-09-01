package sopho.Ofeloumenoi;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
This is a helper class.
We created this class to be able to know when a new photo is taken from the TakePhoto class
When we take a new photo we have to add it to the AddOfeloumenoi ImageView and we have a listener to the AddOfeloumenoiController class that is informed about the new image taken
This is a tricky approach but it works :)
*/
public class PhotoListener {

        static private StringProperty str = new SimpleStringProperty();
        
        public static final String getStr(){
            return str.get();
        }
        
        public static final void setStr(String value){
            str.set(value);
        }
        
        public static StringProperty strProperty(){
            return str;
        }
    }
