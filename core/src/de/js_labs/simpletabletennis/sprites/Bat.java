package de.js_labs.simpletabletennis.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.screens.PlayScreen;

/**
 * Created by Janik on 16.05.2016.
 */
public class Bat extends Sprite  implements InputProcessor {
    public static final int MAX_DOWN_ANGLE = -10;
    public static final int ROTATION_VELOCITY_ON_HIT = 5;

    private PlayScreen screen;
    private World world;
    private Body body;
    private Texture batTexture;

    private MouseJointDef jointDef;
    private MouseJoint joint;
    private Vector3 tmp;

    private float rotation;
    private float rotationVelocity;

    public Bat(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
        batTexture = new Texture(Gdx.files.internal("images/in_game/bat.png"));

        screen.inputMultiplexer.addProcessor(this);

        rotation = MAX_DOWN_ANGLE;
        rotationVelocity = 0;
        tmp = new Vector3();

        setBounds(0, 0, 100 / SimpleTableTennis.PPM, 8 / SimpleTableTennis.PPM);
        setOrigin(10 / SimpleTableTennis.PPM, 4 / SimpleTableTennis.PPM);
        setRotation(rotation);
        setRegion(new TextureRegion(batTexture));

        defineBat(rotation);
    }

    private void defineBat(float rotation){
        if (body != null){
            world.destroyBody(body);
            body = null;
        }
        screen.gameCam.unproject(tmp.set(Gdx.input.getX(), Gdx.input.getY(), 0));

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(tmp.x, tmp.y);
        bodyDef.angle = (float) (rotation * SimpleTableTennis.DEGREES_TO_RADIANS);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(90, 4).scl(1 / SimpleTableTennis.PPM);
        vertice[1] = new Vector2(-10, 4).scl(1 / SimpleTableTennis.PPM);
        vertice[2] = new Vector2(80, -50).scl(1 / SimpleTableTennis.PPM);
        vertice[3] = new Vector2(0, -50).scl(1 / SimpleTableTennis.PPM);
        shape.set(vertice);

        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = SimpleTableTennis.BAT_BIT;
        body.createFixture(fixtureDef).setUserData(screen);

        jointDef = new MouseJointDef();
        jointDef.bodyA = screen.ground.body;
        jointDef.collideConnected = true;
        jointDef.maxForce = 500;

    }

    public void update(float dt){

        //Gdx.app.log("Test", "Bat Updating ...");
        setPosition(body.getPosition().x, body.getPosition().y);
        rotationVelocity -= dt*20;
        if(rotation > MAX_DOWN_ANGLE || (rotation == MAX_DOWN_ANGLE) && rotationVelocity > 0){
            rotation += rotationVelocity;
        }else {
            rotation = MAX_DOWN_ANGLE;
            rotationVelocity = 0;
        }
        //body.setTransform(new Vector2(position.x, position.y), (float) (rotation * SimpleTableTennis.DEGREES_TO_RADIANS));
        setRotation(rotation);

        //Gdx.app.log("Test", "Bat Updated");
    }

    public void hit(){
        if(rotation <= 0)
            rotationVelocity = ROTATION_VELOCITY_ON_HIT;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(joint != null){
            screen.gameCam.unproject(tmp.set(screenX, screenY, 0));
            joint.setTarget(new Vector2(tmp.x, tmp.y));
        } else {
            screen.gameCam.unproject(tmp.set(screenX, screenY, 0));
            jointDef.bodyB = body;
            jointDef.target.set(tmp.x, tmp.y);
            joint = (MouseJoint) world.createJoint(jointDef);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
