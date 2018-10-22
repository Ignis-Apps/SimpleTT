package de.js_labs.simpletabletennis.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.screens.PlayScreen;

/**
 * Created by Janik on 16.05.2016.
 */
public class Ball extends Sprite {
    private PlayScreen screen;
    private World world;
    public Body body;
    private Texture ballTexture;

    public boolean tpToRight;
    public boolean tpToLeft;

    public Ball(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
        ballTexture = new Texture(Gdx.files.internal("images/in_game/ball.png"));

        setBounds(0, 0, 16 / SimpleTableTennis.PPM, 16 / SimpleTableTennis.PPM);
        setRegion(new TextureRegion(ballTexture));

        defineBall(new Vector2(SimpleTableTennis.PLAYSCREEN_WIDHT / SimpleTableTennis.PPM / 2, SimpleTableTennis.PLAYSCREEN_HEIGHT / SimpleTableTennis.PPM * 0.9f));
    }

    private void defineBall(Vector2 position){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position.x, position.y);
        bodyDef.allowSleep = false;
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / SimpleTableTennis.PPM);

        fixtureDef.shape = shape;
        fixtureDef.restitution = 0.99f;
        fixtureDef.filter.categoryBits = SimpleTableTennis.BALL_BIT;
        fixtureDef.filter.maskBits = SimpleTableTennis.BAT_BIT
                | SimpleTableTennis.LEFT_WALL_BIT
                | SimpleTableTennis.RIGHT_WALL_BIT
                | SimpleTableTennis.GROUND_BIT;
        body.createFixture(fixtureDef).setUserData(screen);
    }

    public void update(float dt){
        //Gdx.app.log("Test", "Ball Updating ...");
        if(tpToRight){
            body.setTransform((SimpleTableTennis.PLAYSCREEN_WIDHT - 147) / SimpleTableTennis.PPM, body.getPosition().y, 0);
            tpToRight = false;
        }
        if(tpToLeft){
            body.setTransform(-5 / SimpleTableTennis.PPM, body.getPosition().y, 0);
            tpToLeft = false;
        }
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        //Gdx.app.log("Test", "Bat Updated");
    }
}
