package de.js_labs.simpletabletennis.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.scenes.GameOverScene;
import de.js_labs.simpletabletennis.scenes.Hud;
import de.js_labs.simpletabletennis.scenes.PauseScene;
import de.js_labs.simpletabletennis.sprites.Ball;
import de.js_labs.simpletabletennis.sprites.Bat;
import de.js_labs.simpletabletennis.sprites.Ground;
import de.js_labs.simpletabletennis.sprites.Walls;
import de.js_labs.simpletabletennis.sprites.WorldContactListener;

/**
 * Created by Janik on 16.05.2016.
 */
public class PlayScreen implements Screen {
    public SimpleTableTennis game;

    public OrthographicCamera gameCam;
    private Viewport gamePort;
    private World world;
    private Box2DDebugRenderer b2dr;
    public InputMultiplexer inputMultiplexer;

    public Bat bat;
    public Ball[] balls = new Ball[5];
    public Walls walls;
    public Ground ground;

    public Hud hud;
    public PauseScene pauseScene;
    public GameOverScene gameOverScene;
    public boolean pause = false;

    public int score;

    public PlayScreen(SimpleTableTennis game){
        this.game = game;
        score = 0;

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(SimpleTableTennis.PLAYSCREEN_WIDHT / SimpleTableTennis.PPM, SimpleTableTennis.PLAYSCREEN_HEIGHT / SimpleTableTennis.PPM, gameCam);
        gameCam.setToOrtho(false, SimpleTableTennis.PLAYSCREEN_WIDHT / SimpleTableTennis.PPM, SimpleTableTennis.PLAYSCREEN_HEIGHT / SimpleTableTennis.PPM);

        Box2D.init();

        world = new World(new Vector2(0, -5), true);
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        walls = new Walls(this);
        ground = new Ground(this);
        //balls[0] = new Ball(this);
        bat = new Bat(this);
        hud = new Hud(this);

        game.gameManager.bg_music.play();
        game.gameManager.bg_music.setLooping(true);
        game.gameManager.bg_music.setVolume(0.2f);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {
        update(dt);

        //Gdx.app.log("Test", "Rendering ...");
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        bat.draw(game.batch);
        for (Ball ball: balls){
            if(ball != null){
                ball.draw(game.batch);
            }
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(pause){
            game.batch.setProjectionMatrix(pauseScene.stage.getCamera().combined);
            pauseScene.stage.draw();
        }

        //Gdx.app.log("Test", "Rendered");
    }

    public void update(float dt){
        if(!pause){
            handleInput(dt);
            //Gdx.app.log("Test", "Updateing ...");
            world.step(1/60f, 6, 2);

            bat.update(dt);
            for (Ball ball: balls){
                if(ball != null){
                    ball.update(dt);
                }
            }
            if(score >= 40){
                if(balls[4] == null)
                    balls[4] = new Ball(this);
            }
            if(score >= 30){
                if(balls[3] == null)
                    balls[3] = new Ball(this);
            }
            if(score >= 20){
                if(balls[2] == null)
                    balls[2] = new Ball(this);
            }
            if(score >= 10){
                if(balls[1] == null)
                    balls[1] = new Ball(this);
            }
            //Gdx.app.log("Test", "Updated");
        }else {
            pauseScene.update(dt);
        }
    }

    public void handleInput(float dt){
    //    if(!Gdx.input.isTouched()){
    //        pause();
    //    }
    }

    public void addScore(){
        score++;
        hud.setScore();
    }

    public void gameOver(){
        game.gameManager.bg_music.stop();
        //gameOverScene = new GameOverScene(game, score);
        game.setScreen(new MenuScreen(game, score));
    }

    public Ball findBall(Body A, Body B){
        for (Ball ball : balls){
            if(ball.body == A || ball.body == B){
                return ball;
            }
        }
        return null;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
        pauseScene = new PauseScene(this);
        pause = true;
        game.gameManager.bg_music.pause();

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public World getWorld() {
        return world;
    }
}
