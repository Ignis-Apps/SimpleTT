package de.js_labs.simpletabletennis.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.screens.PlayScreen;

/**
 * Created by Janik on 01.06.2016.
 */
public class Hud extends InputListener implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private SpriteBatch sb;
    private PlayScreen screen;

    private Button hitBtn;
    private Label scoreLabel;

    public Hud(PlayScreen screen){
        this.screen = screen;
        this.sb = screen.game.batch;

        viewport = new FitViewport(SimpleTableTennis.MENUSCREEN_WIDHT, SimpleTableTennis.MENUSCREEN_HEIGHT, new OrthographicCamera());

        stage = new Stage(viewport, sb);

        screen.inputMultiplexer.addProcessor(stage);

        hitBtn = screen.game.gameManager.hud_hit_side;
        hitBtn.setPosition(SimpleTableTennis.MENUSCREEN_WIDHT - hitBtn.getWidth(), 0);
        hitBtn.addListener(this);

        scoreLabel = new Label(Integer.toString(screen.score), new Label.LabelStyle(screen.game.gameManager.font_200, Color.BLACK));
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setPosition(550 / 2 - scoreLabel.getWidth() / 2 + (SimpleTableTennis.MENUSCREEN_WIDHT - 550) + 20, SimpleTableTennis.MENUSCREEN_HEIGHT - scoreLabel.getHeight() - 10);

        stage.addActor(scoreLabel);
        stage.addActor(hitBtn);
    }

    public void setScore(){
        scoreLabel.setText(Integer.toString(screen.score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if(event.getTarget() == hitBtn){
            screen.bat.hit();
        }
        return super.touchDown(event, x, y, pointer, button);
    }
}
