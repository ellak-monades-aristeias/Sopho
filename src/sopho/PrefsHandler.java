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
