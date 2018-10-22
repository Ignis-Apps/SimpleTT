package de.js_labs.simpletabletennis;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import de.js_labs.simpletabletennis.tools.ExternalHandler;

/**
 * Created by janik on 09.04.2017.
 */

public class ExternalManager implements ExternalHandler {
    private Activity mainActivity;

    ExternalManager(Activity activity) {
        mainActivity = activity;
    }

    @Override
    public void shareApp(String text, String chooserText) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, text);
        mainActivity.startActivity(Intent.createChooser(i, chooserText));
    }

    @Override
    public void rateApp() {
        try {
            mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=de.js_labs.simpletabletennis.android")));
        } catch (android.content.ActivityNotFoundException anfe) {
            mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=de.js_labs.simpletabletennis.android")));
        }
    }
}
