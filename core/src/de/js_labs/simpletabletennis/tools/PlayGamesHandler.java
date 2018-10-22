package de.js_labs.simpletabletennis.tools;

public interface PlayGamesHandler {
    boolean isSignedIn();
    void submitHighScore(int score);
    void unlockAchievement(String achievementId);
    void getLeaderboardsActivity();
    void getAchievementsActivity();
    void loadGPGHighScore();

    void onResume();
}
