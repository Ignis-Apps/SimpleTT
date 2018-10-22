package de.js_labs.simpletabletennis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import de.js_labs.simpletabletennis.tools.PlayGamesHandler;

class PlayGamesManager implements PlayGamesHandler {
    private static final String TAG = "js_labs.log";
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    private Activity activity;
    private SimpleTableTennis game;

    private GoogleSignInClient googleSignInClient;

    private AchievementsClient achievementsClient;
    private LeaderboardsClient leaderboardsClient;
    private PlayersClient playersClient;

    private final AccomplishmentsOutbox outbox = new AccomplishmentsOutbox();

    PlayGamesManager(Activity activity, SimpleTableTennis game) {
        this.activity = activity;
        this.game = game;

        googleSignInClient = GoogleSignIn.getClient(activity,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());


    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "onConnected()");

        achievementsClient = Games.getAchievementsClient(activity, googleSignInAccount);
        leaderboardsClient = Games.getLeaderboardsClient(activity, googleSignInAccount);
        playersClient = Games.getPlayersClient(activity, googleSignInAccount);


        // Set the greeting appropriately on main menu
        playersClient.getCurrentPlayer()
                .addOnCompleteListener(new OnCompleteListener<Player>() {
                    @Override
                    public void onComplete(@NonNull Task<Player> task) {
                        String displayName;
                        if (task.isSuccessful()) {
                            displayName = task.getResult().getDisplayName();
                        } else {
                            Exception e = task.getException();
                            displayName = "Error: " + e.getMessage();
                        }
                        Toast.makeText(activity, "Hello, " + displayName, Toast.LENGTH_SHORT).show();
                    }
                });


        // if we have accomplishments to push, push them
        if (!outbox.isEmpty()) {
            pushAccomplishments();
            Toast.makeText(activity, "Uploading Progress ...",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void onDisconnected() {
        Log.d(TAG, "onDisconnected()");

        achievementsClient = null;
        leaderboardsClient = null;
        playersClient = null;
    }

    private void pushAccomplishments() {
        if (!isSignedIn()) {
            Log.d(TAG, "Not signed in! Couldn't upload Progress.");
            return;
        }

        if (outbox.timesPlayed > 0) {
            achievementsClient.increment(activity.getString(R.string.achievement_beginner),
                    outbox.timesPlayed);
            achievementsClient.increment(activity.getString(R.string.achievement_pro),
                    outbox.timesPlayed);
            outbox.timesPlayed = 0;
        }
    }

    private void signInSilently() {

        googleSignInClient.silentSignIn().addOnCompleteListener(activity,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInSilently(): success");
                            onConnected(task.getResult());
                        } else {
                            Log.d(TAG, "signInSilently(): failure", task.getException());
                            onDisconnected();
                            //startSignInIntent();
                        }
                    }
                });
    }

    private void startSignInIntent() {
        activity.startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    @Override
    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(activity) != null;
    }

    @Override
    public void submitHighScore(int score) {
        outbox.timesPlayed++;

        pushAccomplishments();
    }

    @Override
    public void unlockAchievement(String achievementId) {

    }

    @Override
    public void getLeaderboardsActivity() {
        if(!isSignedIn()){
            startSignInIntent();
        } else {
            leaderboardsClient.getAllLeaderboardsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            activity.startActivityForResult(intent, RC_UNUSED);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, e.getMessage(), e);
                        }
                    });
        }
    }

    @Override
    public void getAchievementsActivity() {
        Log.d(TAG, "getAchievementsActivity()");
        if(!isSignedIn()){
            startSignInIntent();
        } else {
            achievementsClient.getAchievementsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            activity.startActivityForResult(intent, RC_UNUSED);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, e.getMessage(), e);
                        }
                    });
        }
    }

    @Override
    public void loadGPGHighScore() {

    }

    @Override
    public void onResume() {
        signInSilently();
    }


    public void onActivityResult(int request, int response, Intent data) {
        if (request == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = "Sign In Error";
                }

                onDisconnected();

                Log.d(TAG, apiException.getMessage(), apiException);

                new AlertDialog.Builder(activity)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }
    }


    private class AccomplishmentsOutbox {
        int timesPlayed = 0;

        boolean isEmpty() {
            return timesPlayed == 0;
        }

    }
}
