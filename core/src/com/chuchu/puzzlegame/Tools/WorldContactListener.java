package com.chuchu.puzzlegame.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.chuchu.puzzlegame.Sprites.InteractiveTileObject;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "collidable" || fixB.getUserData() == "collidable") {
            Fixture edge = fixA.getUserData() == "collidable" ? fixA : fixB;
            Fixture object = edge == fixA ? fixB : fixA;

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onHit();
            }
        }
        else if (fixA.getUserData() == "clickable" || fixB.getUserData() == "clickable") {
            Fixture edge = fixA.getUserData() == "clickable" ? fixA : fixB;
            Fixture object = edge == fixA ? fixB : fixA;

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onBeginContact();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "clickable" || fixB.getUserData() == "clickable") {
            Fixture edge = fixA.getUserData() == "clickable" ? fixA : fixB;
            Fixture object = edge == fixA ? fixB : fixA;

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onEndContact();
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
