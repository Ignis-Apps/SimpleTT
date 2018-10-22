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
 * Created by Janik on 16.05.2016.
 */
public class Walls {
    private PlayScreen screen;
    private World world;

    public Body leftWall;
    public Body rightWall;

    public Walls(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();

        defineWalls();
    }

    public void defineWalls(){

        // Left Wall
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(-16 / SimpleTableTennis.PPM, -55 / SimpleTableTennis.PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        leftWall = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        ChainShape shape = new ChainShape();

        Vector2[] vertice = new Vector2[2];
        vertice[0] = new Vector2(0, 0).scl(1 / SimpleTableTennis.PPM);
        vertice[1] = new Vector2(0, 4000).scl(1 / SimpleTableTennis.PPM);
        shape.createChain(vertice);

        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = SimpleTableTennis.LEFT_WALL_BIT;
        fixtureDef.filter.maskBits = SimpleTableTennis.BALL_BIT;
        leftWall.createFixture(fixtureDef).setUserData(screen);

        //Right Wall
        bodyDef.position.set((SimpleTableTennis.PLAYSCREEN_WIDHT - 137) / SimpleTableTennis.PPM, -55 / SimpleTableTennis.PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        rightWall = world.createBody(bodyDef);

        shape = new ChainShape();

        vertice[0] = new Vector2(0, 0).scl(1 / SimpleTableTennis.PPM);
        vertice[1] = new Vector2(0, 4000).scl(1 / SimpleTableTennis.PPM);
        shape.createChain(vertice);

        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = SimpleTableTennis.RIGHT_WALL_BIT;
        fixtureDef.filter.maskBits = SimpleTableTennis.BALL_BIT;
        rightWall.createFixture(fixtureDef).setUserData(screen);
    }

}
