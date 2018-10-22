package de.js_labs.simpletabletennis.sprites;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.TimeUtils;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.screens.PlayScreen;

/**
 * Created by Janik on 31.05.2016.
 */
public class WorldContactListener implements ContactListener {
    private PlayScreen screen;
    private boolean playPingPong = false;

    private long bat_ball_timer = 0;

    private Fixture fixA;
    private Fixture fixB;


    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void endContact(Contact contact) {
        fixA = contact.getFixtureA();
        fixB = contact.getFixtureB();

        int cDev = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDev){
            case SimpleTableTennis.BALL_BIT | SimpleTableTennis.BAT_BIT:
                if(TimeUtils.millis() > bat_ball_timer + 300) {
                    Ball ball = ((PlayScreen) fixA.getUserData()).findBall(fixA.getBody(), fixB.getBody());
                    if(ball == ((PlayScreen) fixA.getUserData()).balls[0])
                        ((PlayScreen) fixA.getUserData()).addScore();
                    screen = ((PlayScreen) fixA.getUserData());
                    playPingPong = true;
                    bat_ball_timer = TimeUtils.millis();
                }
                break;
            case SimpleTableTennis.BALL_BIT | SimpleTableTennis.GROUND_BIT:
                ((PlayScreen) fixA.getUserData()).gameOver();
                break;
        }

        if(playPingPong){
            playPingPong = false;
            screen.game.gameManager.pinp_pong_sound.play();
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        fixA = contact.getFixtureA();
        fixB = contact.getFixtureB();

        int cDev = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDev){
            case SimpleTableTennis.BALL_BIT | SimpleTableTennis.LEFT_WALL_BIT:
                contact.setEnabled(false);
                Ball ball = ((PlayScreen) fixA.getUserData()).findBall(fixA.getBody(), fixB.getBody());
                ball.tpToRight = true;
                break;
            case SimpleTableTennis.BALL_BIT | SimpleTableTennis.RIGHT_WALL_BIT:
                contact.setEnabled(false);
                Ball ball2 = ((PlayScreen) fixA.getUserData()).findBall(fixA.getBody(), fixB.getBody());
                ball2.tpToLeft = true;
                break;
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
