package sopho;

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
    }
    
    public void removePrefs(String key) {
        Preferences prefs = Preferences.userNodeForPackage(Sopho.class);
        prefs.remove(key);    
    }
}
