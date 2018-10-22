package de.js_labs.simpletabletennis.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.tools.GameManager;
import de.js_labs.simpletabletennis.tools.UiHelper;

/**
 * Created by Janik on 04.06.2016.
 */
public class ShopScreen extends ClickListener implements Screen {
    public static final float SWING_ANIMATION_LENGTH = 300;
    public static float CENTER_Y;
    private SimpleTableTennis game;

    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;

    private Image titleImg;
    private Button buyRemoveAds;
    private Button buyPremium;
    private Button buyPremiumPlus;


    public ShopScreen(SimpleTableTennis game){
        this.game = game;
        this.batch = game.batch;
        game.gameManager.ignoreInput = true;

        viewport = new FitViewport(SimpleTableTennis.MENUSCREEN_WIDHT, SimpleTableTennis.MENUSCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        createComponents();
        swingIn();
    }

    private void createComponents(){
        titleImg = UiHelper.recreate(game.gameManager.pmenu_title_img);
        buyRemoveAds = UiHelper.recreate(game.gameManager.pmenu_remove_ads_btn);
        buyPremium = UiHelper.recreate(game.gameManager.pmenu_premium_btn);
        buyPremiumPlus = UiHelper.recreate(game.gameManager.pmenu_premium_plus_btn);

        CENTER_Y = SimpleTableTennis.MENUSCREEN_HEIGHT / 2 - (buyRemoveAds.getHeight()/2 + titleImg.getHeight()/2);

        titleImg.setPosition(0, CENTER_Y + buyRemoveAds.getHeight() + SWING_ANIMATION_LENGTH);
        buyRemoveAds.setPosition(0, CENTER_Y - SWING_ANIMATION_LENGTH);
        buyPremium.setPosition(buyRemoveAds.getWidth(), CENTER_Y - SWING_ANIMATION_LENGTH);
        buyPremiumPlus.setPosition(buyRemoveAds.getWidth() + buyPremium.getWidth(), CENTER_Y - SWING_ANIMATION_LENGTH);

        titleImg.getColor().a = 0f;
        buyRemoveAds.getColor().a = 0f;
        buyPremium.getColor().a = 0f;
        buyPremiumPlus.getColor().a = 0f;

        titleImg.addListener(this);
        buyRemoveAds.addListener(this);
        buyPremium.addListener(this);
        buyPremiumPlus.addListener(this);

        Gdx.input.setInputProcessor(stage);

        stage.addActor(titleImg);
        stage.addActor(buyRemoveAds);
        stage.addActor(buyPremium);
        stage.addActor(buyPremiumPlus);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if(!game.gameManager.ignoreInput){
            if(Gdx.input.justTouched() && !(buyPremium.isPressed() || buyPremiumPlus.isPressed() || buyRemoveAds.isPressed())){
                game.gameManager.ignoreInput = true;
                swingOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new MenuScreen(game, 0.1f));
                    }
                });
            }
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        titleImg.act(delta);
        buyRemoveAds.act(delta);
        buyPremium.act(delta);
        buyPremiumPlus.act(delta);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void swingIn(){
        titleImg.addAction(Actions.sequence(Actions.parallel(Actions.fadeIn(0.6f), Actions.moveBy(0, - SWING_ANIMATION_LENGTH, 0.6f, Interpolation.exp10Out)), Actions.run(new Runnable() {
            @Override
            public void run() {
                game.gameManager.ignoreInput = false;
            }
        })));
        buyRemoveAds.addAction(Actions.parallel(Actions.fadeIn(0.4f), Actions.moveBy(0, SWING_ANIMATION_LENGTH, 0.4f, Interpolation.exp10Out)));
        buyPremium.addAction(Actions.parallel(Actions.fadeIn(0.7f), Actions.moveBy(0, SWING_ANIMATION_LENGTH, 0.7f, Interpolation.exp10Out)));
        buyPremiumPlus.addAction(Actions.parallel(Actions.fadeIn(1.2f), Actions.moveBy(0, SWING_ANIMATION_LENGTH, 1.2f, Interpolation.exp10Out)));
    }

    private void swingOut(Runnable runnable){
        titleImg.addAction(Actions.sequence(Actions.parallel(Actions.fadeOut(0.4f), Actions.moveBy(0, SWING_ANIMATION_LENGTH, 0.5f, Interpolation.exp5In)), Actions.run(new Runnable() {
            @Override
            public void run() {
                game.gameManager.ignoreInput = false;
            }
        }), Actions.run(runnable)));
        buyRemoveAds.addAction(Actions.parallel(Actions.fadeOut(0.5f), Actions.moveBy(0, - SWING_ANIMATION_LENGTH, 0.5f, Interpolation.exp5In)));
        buyPremium.addAction(Actions.parallel(Actions.fadeOut(0.4f), Actions.moveBy(0, - SWING_ANIMATION_LENGTH, 0.4f, Interpolation.exp5In)));
        buyPremiumPlus.addAction(Actions.parallel(Actions.fadeOut(0.3f), Actions.moveBy(0, - SWING_ANIMATION_LENGTH, 0.3f, Interpolation.exp5In)));
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

    @Override
    public void clicked(InputEvent event, float x, float y) {
        if(!game.gameManager.ignoreInput){
            if(event.getTarget() == buyRemoveAds){
                game.gameManager.ignoreInput = true;
                game.billingHandler.initPurchaseFlow(GameManager.REMOVEADS_SKU);
            }else if(event.getTarget() == buyPremium){
                game.gameManager.ignoreInput = true;
                game.billingHandler.initPurchaseFlow(GameManager.PREMIUM_SKU);
            }else if(event.getTarget() == buyPremiumPlus){
                game.gameManager.ignoreInput = true;
                game.billingHandler.initPurchaseFlow(GameManager.PREMIUM_PLUS_SKU);
            }
        }
    }
}
