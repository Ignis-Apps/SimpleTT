package de.js_labs.simpletabletennis.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.tools.GameManager;

/**
 * Created by Janik on 09.06.2016.
 */
public class ResultScreen implements Screen {
    private SimpleTableTennis game;
    private GameManager.GameResult result;

    private Stage stage;
    private Viewport viewport;

    public boolean ignoreInput;

    public ResultScreen(SimpleTableTennis game, GameManager.GameResult result){
        this.game = game;
        this.result = result;
        ignoreInput = true;

        viewport = new FitViewport(SimpleTableTennis.MENUSCREEN_WIDHT, SimpleTableTennis.MENUSCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        createComponents();
        swingIn();
    }

    private void createComponents(){

    }

    private void swingIn(){

    }

    private void swingOut(Runnable runnable){

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
