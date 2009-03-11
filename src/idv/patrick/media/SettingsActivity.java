package idv.patrick.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getPreferenceManager().setSharedPreferencesName(Preferences.NAME);
        
        addPreferencesFromResource(R.xml.preferences);
    }
    
    /**
     * Starts the PreferencesActivity for the specified user.
     *
     * @param context The application's environment.
     */
    static void show(Context context) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }
}
