package de.js_labs.simpletabletennis.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.tools.GameManager;
import de.js_labs.simpletabletennis.tools.UiHelper;

/**
 * Created by Janik on 16.05.2016.
 */
public class MenuScreen extends ClickListener implements Screen {
    public SimpleTableTennis game;

    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;

    private float cooldown;

    private GameManager gameManager;

    private Label holdLabel;
    private Button shopBtn;
    private Button playGamesBtn;
    private Button shareBtn;
    private Button rateBtn;
    private Button removeAdsBtn;
    private Button languageBtn;
    private Image titleImg;

    public MenuScreen(SimpleTableTennis game){
        this.game = game;
        this.gameManager = game.gameManager;
        this.batch = game.batch;
        cooldown = 2;
        gameManager.ignoreInput = false;

        viewport = new FitViewport(SimpleTableTennis.MENUSCREEN_WIDHT, SimpleTableTennis.MENUSCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        createComponents();
        swingIn();
    }

    public MenuScreen(SimpleTableTennis game, int score){
        this.game = game;
        this.gameManager = game.gameManager;
        this.batch = game.batch;
        cooldown = 2;

        viewport = new FitViewport(SimpleTableTennis.MENUSCREEN_WIDHT, SimpleTableTennis.MENUSCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        createComponents();
        swingIn();

        game.playGamesHandler.submitHighScore(score);
    }

    public MenuScreen(SimpleTableTennis game, float swingInDelay){
        this.game = game;
        this.gameManager = game.gameManager;
        this.batch = game.batch;
        cooldown = 2;

        viewport = new FitViewport(SimpleTableTennis.MENUSCREEN_WIDHT, SimpleTableTennis.MENUSCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        createComponents();
        swingIn(swingInDelay);
    }

    private void createComponents(){
        shopBtn = UiHelper.recreate(gameManager.menu_shop_btn);
        playGamesBtn = UiHelper.recreate(gameManager.menu_playgames_btn);
        shareBtn = UiHelper.recreate(gameManager.menu_share_btn);
        rateBtn = UiHelper.recreate(gameManager.menu_rate_btn);
        removeAdsBtn = UiHelper.recreate(gameManager.menu_removeads_btn);
        languageBtn = UiHelper.recreate(gameManager.menu_language_btn);
        titleImg = UiHelper.recreate(gameManager.menu_title_img);
        holdLabel = new Label(gameManager.strings[GameManager.STRINGID_HOLD_TO_START], new Label.LabelStyle(gameManager.font_80, Color.BLACK));
        holdLabel.getColor().a = 0f;
        holdLabel.setAlignment(Align.center);

        shopBtn.setPosition(SimpleTableTennis.MENUSCREEN_WIDHT / 2 - shopBtn.getWidth() / 2, - shopBtn.getHeight());
        playGamesBtn.setPosition(SimpleTableTennis.MENUSCREEN_WIDHT - playGamesBtn.getWidth(), - playGamesBtn.getHeight());
        shareBtn.setPosition(SimpleTableTennis.MENUSCREEN_WIDHT - playGamesBtn.getWidth() - shareBtn.getWidth(), - shareBtn.getHeight());
        rateBtn.setPosition(SimpleTableTennis.MENUSCREEN_WIDHT - playGamesBtn.getWidth() - shareBtn.getWidth() - rateBtn.getWidth(), - rateBtn.getHeight());
        removeAdsBtn.setPosition(0, - removeAdsBtn.getHeight());
        languageBtn.setPosition(0, SimpleTableTennis.MENUSCREEN_HEIGHT);
        titleImg.setPosition(SimpleTableTennis.MENUSCREEN_WIDHT / 2 - titleImg.getWidth() / 2, SimpleTableTennis.MENUSCREEN_HEIGHT);
        holdLabel.setPosition(SimpleTableTennis.MENUSCREEN_WIDHT / 2 - holdLabel.getWidth() / 2, SimpleTableTennis.MENUSCREEN_HEIGHT / 2 - holdLabel.getHeight() / 2 - 30);

        languageBtn.addListener(this);
        playGamesBtn.addListener(this);
        removeAdsBtn.addListener(this);
        shareBtn.addListener(this);
        rateBtn.addListener(this);
        shopBtn.addListener(this);

        stage.addActor(holdLabel);
        stage.addActor(playGamesBtn);
        stage.addActor(shareBtn);
        stage.addActor(rateBtn);
        stage.addActor(shopBtn);
        stage.addActor(removeAdsBtn);
        stage.addActor(languageBtn);
        stage.addActor(titleImg);

        Gdx.input.setInputProcessor(stage);
    }

    private void recreateComponents(){
        stage.clear();
        createComponents();
    }

    @Override
    public void show() {}

    @Override
    public void clicked(InputEvent event, float x, float y) {
        if(!gameManager.ignoreInput){
            super.clicked(event, x, y);
            if(event.getTarget() == languageBtn){
                swingOut(new Runnable() {
                    @Override
                    public void run() {
                        gameManager.changeLanguage();
                        recreateComponents();
                        swingIn(0.2f);
                    }
                });
            }else if(event.getTarget() == playGamesBtn){
                gameManager.ignoreInput = true;
                game.playGamesHandler.getAchievementsActivity();
            }else if(event.getTarget() == removeAdsBtn){
                gameManager.ignoreInput = true;
                game.billingHandler.initPurchaseFlow(GameManager.REMOVEADS_SKU);
            }else if(event.getTarget() == shopBtn){
                swingOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new ShopScreen(game));
                    }
                });
            }else if(event.getTarget() == rateBtn){
                game.externalHandler.rateApp();
            }else if(event.getTarget() == shareBtn){
                game.externalHandler.shareApp(gameManager.strings[GameManager.STRINGID_SHARE_TEXT], gameManager.strings[GameManager.STRINGID_SHARE_CHOOSE]);
            }
        }
    };

    @Override
    public void render(float dt) {
        update(dt);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playGamesBtn.act(dt);
        shareBtn.act(dt);
        rateBtn.act(dt);
        shopBtn.act(dt);
        removeAdsBtn.act(dt);
        languageBtn.act(dt);
        titleImg.act(dt);
        holdLabel.act(dt);

        stage.draw();
    }

    public void update(float dt){
        if(!gameManager.ignoreInput){
            if(Gdx.input.isTouched() && !(shopBtn.isPressed() || playGamesBtn.isPressed() || shareBtn.isPressed() || rateBtn.isPressed() || removeAdsBtn.isPressed() || languageBtn.isPressed())){
                cooldown -= dt;
                if(cooldown <= 2){
                    holdLabel.setText(gameManager.strings[GameManager.STRINGID_1S]);
                }
                if(cooldown <= 1){
                    swingOut(new Runnable() {
                        @Override
                        public void run() {
                            gameManager.finishLoadingInGameAssets();
                            game.setScreen(new PlayScreen(game));
                        }
                    });
                }
            }else {
                cooldown = 2;
                holdLabel.setText(gameManager.strings[GameManager.STRINGID_HOLD_TO_START]);
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    private void swingOut(Runnable runnable){
        gameManager.ignoreInput = true;

        holdLabel.addAction(Actions.sequence(Actions.delay(0.5f, Actions.sequence(Actions.fadeOut(0.3f))), Actions.run(new Runnable() {
            @Override
            public void run() {
                gameManager.ignoreInput = false;
            }
        }), Actions.run(runnable)));
        shopBtn.addAction(Actions.delay(0f, Actions.moveBy(0, - shopBtn.getHeight(), 0.5f, Interpolation.swingIn)));
        playGamesBtn.addAction(Actions.delay(0.1f, Actions.moveBy(0, - playGamesBtn.getHeight(), 0.5f, Interpolation.swingIn)));
        shareBtn.addAction(Actions.delay(0.2f, Actions.moveBy(0, - shareBtn.getHeight(), 0.5f, Interpolation.swingIn)));
        rateBtn.addAction(Actions.delay(0.3f, Actions.moveBy(0, - rateBtn.getHeight(), 0.5f, Interpolation.swingIn)));
        removeAdsBtn.addAction(Actions.delay(0.2f, Actions.moveBy(0, - removeAdsBtn.getHeight(), 0.5f, Interpolation.swingIn)));
        languageBtn.addAction(Actions.delay(0.2f, Actions.moveBy(0, languageBtn.getHeight(), 0.5f, Interpolation.swingIn)));
        titleImg.addAction(Actions.delay(0f, Actions.moveBy(0, titleImg.getHeight(), 0.5f, Interpolation.swingIn)));
    }

    private void swingIn(){
        gameManager.ignoreInput = true;

        holdLabel.addAction(Actions.delay(1.6f, Actions.fadeIn(0.9f)));
        shopBtn.addAction(Actions.delay(0.8f, Actions.moveBy(0, shopBtn.getHeight(), 0.8f, Interpolation.swingOut)));
        playGamesBtn.addAction(Actions.delay(1f, Actions.moveBy(0, playGamesBtn.getHeight(), 0.8f, Interpolation.swingOut)));
        shareBtn.addAction(Actions.delay(1.2f, Actions.moveBy(0, shareBtn.getHeight(), 0.8f, Interpolation.swingOut)));
        rateBtn.addAction(Actions.delay(1.4f, Actions.sequence(Actions.moveBy(0, rateBtn.getHeight(), 0.8f, Interpolation.swingOut), Actions.run(new Runnable() {
            @Override
            public void run() {
                gameManager.ignoreInput = false;
            }
        }))));
        removeAdsBtn.addAction(Actions.delay(1.2f, Actions.moveBy(0, removeAdsBtn.getHeight(), 0.8f, Interpolation.swingOut)));
        languageBtn.addAction(Actions.delay(1.2f, Actions.moveBy(0, - languageBtn.getHeight(), 0.8f, Interpolation.swingOut)));
        titleImg.addAction(Actions.delay(0.8f, Actions.moveBy(0, - titleImg.getHeight(), 0.8f, Interpolation.swingOut)));
    }

    private void swingIn(float swingInDelay){
        gameManager.ignoreInput = true;

        holdLabel.addAction(Actions.delay(swingInDelay + 0.8f, Actions.sequence(Actions.fadeIn(0.9f), Actions.run(new Runnable() {
            @Override
            public void run() {
                gameManager.ignoreInput = false;
            }
        }))));
        shopBtn.addAction(Actions.delay(swingInDelay, Actions.moveBy(0, shopBtn.getHeight(), 0.8f, Interpolation.swingOut)));
        playGamesBtn.addAction(Actions.delay(swingInDelay + 0.2f, Actions.moveBy(0, playGamesBtn.getHeight(), 0.8f, Interpolation.swingOut)));
        shareBtn.addAction(Actions.delay(swingInDelay + 0.4f, Actions.moveBy(0, shareBtn.getHeight(), 0.8f, Interpolation.swingOut)));
        rateBtn.addAction(Actions.delay(swingInDelay + 0.6f, Actions.sequence(Actions.moveBy(0, rateBtn.getHeight(), 0.8f, Interpolation.swingOut), Actions.run(new Runnable() {
            @Override
            public void run() {
                gameManager.ignoreInput = false;
            }
        }))));
        removeAdsBtn.addAction(Actions.delay(swingInDelay + 0.4f, Actions.moveBy(0, removeAdsBtn.getHeight(), 0.8f, Interpolation.swingOut)));
        languageBtn.addAction(Actions.delay(swingInDelay + 0.4f, Actions.moveBy(0, - languageBtn.getHeight(), 0.8f, Interpolation.swingOut)));
        titleImg.addAction(Actions.delay(swingInDelay, Actions.moveBy(0, - titleImg.getHeight(), 0.8f, Interpolation.swingOut)));
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
        stage.dispose();
    }
}
