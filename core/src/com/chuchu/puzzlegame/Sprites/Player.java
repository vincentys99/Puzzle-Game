package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.chuchu.puzzlegame.Screens.PuzzleGame;

public class Player extends Sprite {

    public World world;
    public Body b2body;

    public Player(World world) {
        this.world = world;
        definePlayer();
    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(320 / PuzzleGame.PPM, 320 / PuzzleGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
//        bdef.gravityScale = 0;
        bdef.fixedRotation = true;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(100 / PuzzleGame.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
