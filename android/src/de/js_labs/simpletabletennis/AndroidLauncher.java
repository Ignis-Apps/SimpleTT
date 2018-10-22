package de.js_labs.simpletabletennis;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

	private PlayGamesManager gamesManager;
	private BillingManager billingManager;
	private ExternalManager externalManager;
	private FirebaseManager firebaseManager;

	public SimpleTableTennis game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		game = new SimpleTableTennis();
		initialize(game, config);

		billingManager = new BillingManager(this, game);
		gamesManager = new PlayGamesManager(this, game);
		externalManager = new ExternalManager(this);
		firebaseManager = new FirebaseManager();

		game.setHandler(billingManager, gamesManager, externalManager, firebaseManager);
	}

	@Override
	public void onResume(){
		super.onResume();
		gamesManager.onResume();
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);

        game.gameManager.ignoreInput = false;
		gamesManager.onActivityResult(request, response, data);
	}
}
