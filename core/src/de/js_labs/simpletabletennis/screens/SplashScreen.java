package de.js_labs.simpletabletennis.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import de.js_labs.simpletabletennis.SimpleTableTennis;

public class SplashScreen implements Screen {

	private Image jsLabsImage;
	private Stage stage;
	private SimpleTableTennis game;

	public SplashScreen(SimpleTableTennis game){
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();

		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		stage = new Stage();

		/* Load splash image */
		jsLabsImage = new Image(new Texture(
				Gdx.files.internal("images/js_labs_logo.png")));

		game.gameManager.loadMenuAssetsAsyc();
		game.gameManager.loadInGameAssetsAsyc();

		/* Set the splash image in the center of the screen */
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		jsLabsImage.setPosition((width - jsLabsImage.getWidth()) / 2,
				(height - jsLabsImage.getHeight()) / 2);

		/* Fade in the image and then swing it down */
		jsLabsImage.getColor().a = 0f;
		jsLabsImage.addAction(Actions.delay(0.4f, Actions.sequence(Actions.fadeIn(0.3f), Actions.delay(0.8f, Actions.moveBy(0,
						-(height - jsLabsImage.getHeight() / 2), 0.8f,
						Interpolation.swingIn)), Actions.run(new Runnable() {
			@Override
			public void run() {

				game.gameManager.finishLoadingMenu();
				/* Show main menu after swing out */
				game.setScreen(new MenuScreen(game));
			}
		}))));

		stage.addActor(jsLabsImage);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}