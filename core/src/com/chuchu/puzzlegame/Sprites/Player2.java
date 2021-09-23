package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Screens.Room2;

public class Player2 extends Sprite {

    public World world;
    public Body b2body;
    private TextureRegion characterStand;

    public Player2(World world, Room2 screen) {
        super(screen.getAtlas().findRegion("cc"));
        this.world = world;
        definePlayer();
        characterStand = new TextureRegion(getTexture(), 0, 0, 32, 64);
        setBounds(0, 0, 32 / PuzzleGame.PPM, 64 / PuzzleGame.PPM);
        setRegion(characterStand);
    }

    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getWidth() / 4);
    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(100 / PuzzleGame.PPM, 100 / PuzzleGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16 / PuzzleGame.PPM, 16 / PuzzleGame.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
