package sopho;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;

//this class is a helper for handling the application preferences file. Saving and loading data from it.
public class PrefsHandler {
    
    public String getPrefs(String prefKey) {
        Preferences prefs = Preferences.userNodeForPackage(Sopho.class);
        String temp = prefs.get(prefKey, null);
        System.out.println("Returning Key = " + temp + "" );
        return temp;
    }
    
    public void setPrefs(String key, String value) {
        Preferences prefs = Preferences.userNodeForPackage(Sopho.class);
        prefs.put(key, value);
        /*Map<String, String> map = new HashMap<String, String>();
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            System.out.println("Setting Key = " + entry.getKey() + ", Value = " + entry.getValue());
            prefs.put(entry.getKey(), entry.getValue());
        }*/     
    }
    
    public void removePrefs(String key, String value) {
        Preferences prefs = Preferences.userNodeForPackage(Sopho.class);
        prefs.put(key, value);
        /*Map<String, String> map = new HashMap<String, String>();
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            System.out.println("Removing Key = " + entry.getKey() + ", Value = " + entry.getValue());
            prefs.remove(entry.getKey());
        }*/     
    }
}
