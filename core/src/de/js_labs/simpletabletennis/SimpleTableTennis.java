package de.js_labs.simpletabletennis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.js_labs.simpletabletennis.screens.SplashScreen;
import de.js_labs.simpletabletennis.tools.BillingHandler;
import de.js_labs.simpletabletennis.tools.ExternalHandler;
import de.js_labs.simpletabletennis.tools.FirebaseHandler;
import de.js_labs.simpletabletennis.tools.GameManager;
import de.js_labs.simpletabletennis.tools.PlayGamesHandler;

public class SimpleTableTennis extends Game {
	public static final short GROUND_BIT = 1;
	public static final short LEFT_WALL_BIT = 2;
	public static final short RIGHT_WALL_BIT = 4;
	public static final short BAT_BIT = 8;
	public static final short BALL_BIT = 16;

	public static final double DEGREES_TO_RADIANS = (double)(Math.PI/180);

	public static final float PLAYSCREEN_HEIGHT = 300;
	public static float PLAYSCREEN_WIDHT;
	public static float MENUSCREEN_HEIGHT;
	public static final float MENUSCREEN_WIDHT = 1920;
	public static final float PPM = 100;
	private static int log_count = 0;

	public SpriteBatch batch;
	private float screenMultiPlay;
	private float screenMultiMenu;

	public GameManager gameManager;
	public PlayGamesHandler playGamesHandler;
	public BillingHandler billingHandler;
	public ExternalHandler externalHandler;
	public FirebaseHandler firebaseHandler;

	@Override
	public void create () {
		gameManager = new GameManager(this);
		screenMultiPlay = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
		screenMultiMenu = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		MENUSCREEN_HEIGHT = MENUSCREEN_WIDHT * screenMultiMenu;
		PLAYSCREEN_WIDHT = PLAYSCREEN_HEIGHT * screenMultiPlay;
		batch = new SpriteBatch();
		setScreen(new SplashScreen(this));
	}

	public void log(String text){
		log_count++;
		Gdx.app.log("JSL:" + log_count, text);
		firebaseHandler.log("JSL-" + log_count + ": " + text);
	}

	@Override
	public void dispose(){
		batch.dispose();
		gameManager.dispose();
	}

	public void setHandler(BillingHandler billingHandler, PlayGamesHandler playGamesHandler, ExternalHandler externalHandler, FirebaseHandler firebaseHandler){
		this.billingHandler = billingHandler;
		this.playGamesHandler = playGamesHandler;
		this.externalHandler = externalHandler;
		this.firebaseHandler = firebaseHandler;
	}
}
