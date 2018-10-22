package de.js_labs.simpletabletennis.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.tools.GameManager;

/**
 * Created by janik on 12.04.2017.
 */

public class GameOverScene implements Disposable {
    private SimpleTableTennis game;
    private GameManager gameManager;

    public Stage stage;
    private Viewport viewport;

    public GameOverScene(SimpleTableTennis game, int score) {
        this.game = game;
        this.gameManager = game.gameManager;

        viewport = new FitViewport(SimpleTableTennis.MENUSCREEN_WIDHT, SimpleTableTennis.MENUSCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        Dialog dialog = new Dialog("Test", new Window.WindowStyle(gameManager.font_80, Color.BLACK, null));
        //dialog.button("Score: " + score);
        dialog.show(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();

    }
}
