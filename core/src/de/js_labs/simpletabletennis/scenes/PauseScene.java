package de.js_labs.simpletabletennis.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.screens.PlayScreen;

/**
 * Created by Janik on 28.11.2016.
 */

public class PauseScene implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private SpriteBatch sb;
    private PlayScreen screen;
    private float resumeCooldown;

    private Label resumeLabel;
    private Image black_bg;

    public PauseScene(PlayScreen screen){
        this.screen = screen;
        this.sb = screen.game.batch;

        resumeCooldown = 2;

        viewport = new FitViewport(SimpleTableTennis.MENUSCREEN_WIDHT, SimpleTableTennis.MENUSCREEN_HEIGHT, new OrthographicCamera());

        stage = new Stage(viewport, sb);

        Gdx.input.setInputProcessor(stage);

        resumeLabel = new Label(screen.game.gameManager.strings[screen.game.gameManager.STRINGID_HOLD_TO_RESUME], new Label.LabelStyle(screen.game.gameManager.font_80, Color.BLACK));
        resumeLabel.setAlignment(Align.center);
        resumeLabel.setColor(Color.BLUE);
        resumeLabel.setPosition(SimpleTableTennis.MENUSCREEN_WIDHT / 2 - resumeLabel.getWidth() / 2, SimpleTableTennis.MENUSCREEN_HEIGHT / 2 - resumeLabel.getHeight() / 2);

        black_bg = screen.game.gameManager.black_bg;

        stage.addActor(black_bg);
        stage.addActor(resumeLabel);
    }

    public void update(float dt){
        if(Gdx.input.isTouched()){
            resumeCooldown -= dt;
            if(resumeCooldown < 2){
                resumeLabel.setStyle(new Label.LabelStyle(screen.game.gameManager.font_200, Color.BLACK));
                resumeLabel.setText(Integer.toString(2));
            }
            if(resumeCooldown < 1){
                resumeLabel.setStyle(new Label.LabelStyle(screen.game.gameManager.font_200, Color.BLACK));
                resumeLabel.setText(Integer.toString(1));
            }
            if(resumeCooldown <= 0){
                dispose();
                screen.pause = false;
                screen.game.gameManager.bg_music.play();
                Gdx.input.setInputProcessor(screen.hud.stage);
            }
        }else {
            resumeCooldown = 2;
            resumeLabel.setStyle(new Label.LabelStyle(screen.game.gameManager.font_80, Color.BLACK));
            resumeLabel.setText(screen.game.gameManager.strings[screen.game.gameManager.STRINGID_HOLD_TO_RESUME]);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
