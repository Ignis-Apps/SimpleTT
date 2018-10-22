package de.js_labs.simpletabletennis.sprites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.screens.PlayScreen;

/**
 * Created by Janik on 31.05.2016.
 */
public class Ground{
    private PlayScreen screen;
    private World world;

    public Body body;

    public Ground(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();

        defineGround();
    }

    private void defineGround(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(2 / SimpleTableTennis.PPM, -50 / SimpleTableTennis.PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        ChainShape shape = new ChainShape();

        Vector2[] vertice = new Vector2[2];
        vertice[0] = new Vector2(0, 0).scl(1 / SimpleTableTennis.PPM);
        vertice[1] = new Vector2(SimpleTableTennis.PLAYSCREEN_WIDHT - 157, 0).scl(1 / SimpleTableTennis.PPM);
        shape.createChain(vertice);

        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = SimpleTableTennis.GROUND_BIT;
        fixtureDef.filter.maskBits = SimpleTableTennis.BALL_BIT;
        body.createFixture(fixtureDef).setUserData(screen);
    }
}
