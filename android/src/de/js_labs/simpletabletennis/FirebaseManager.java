package de.js_labs.simpletabletennis;

import com.crashlytics.android.Crashlytics;
import de.js_labs.simpletabletennis.tools.FirebaseHandler;

public class FirebaseManager implements FirebaseHandler {
    @Override
    public void log(String log) {
        Crashlytics.log(log);
    }

    @Override
    public void reportCrash(Throwable exception) {
        Crashlytics.logException(exception);
    }
}
