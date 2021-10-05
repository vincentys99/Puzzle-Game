package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Screens.Room2;

public class Player2 extends Sprite {

    public enum State { Standing, WalkDown, WalkLeft, WalkRight, WalkUp, FaceDown, FaceLeft, FaceRight, FaceUp };
    private final String[] walkImages = new String[] { "WalkDown", "WalkLeft", "WalkRight", "WalkUp" };
    private Animation<TextureRegion> playerWalkDown;
    private Animation<TextureRegion> playerWalkLeft;
    private Animation<TextureRegion> playerWalkRight;
    private Animation<TextureRegion> playerWalkUp;
    public State currentState;
    public State previousState;
    private float stateTimer;

    public World world;
    public Body b2body;
    TextureRegion playerFaceDown;
    TextureRegion playerFaceLeft;
    TextureRegion playerFaceRight;
    TextureRegion playerFaceUp;

    public Player2(World world, Room2 screen) {
        super(screen.getAtlas().findRegion("WalkDown"));
        this.world = world;

        currentState = State.Standing;
        previousState = State.Standing;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 32));
        }
        playerWalkDown = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 6; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 32));
        }
        playerWalkLeft = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 11; i < 15; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 32));
        }
        playerWalkRight = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 16; i < 20; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 32));
        }
        playerWalkUp = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        definePlayer();
        playerFaceDown = new TextureRegion(getTexture(), 0, 0, 16, 32);
        playerFaceLeft = new TextureRegion(getTexture(), 5 * 16, 0, 16, 32);
        playerFaceRight = new TextureRegion(getTexture(), 10 * 16, 0, 16, 32);
        playerFaceUp = new TextureRegion(getTexture(), 15 * 16, 0, 16, 32);
        setBounds(0, 0, 16 * 2 / PuzzleGame.PPM, 32 * 2 / PuzzleGame.PPM);
        setRegion(playerFaceDown);
    }

    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getWidth() / 4);
        setRegion(getFrame(delta));
    }

    public TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case WalkDown:
                region = playerWalkDown.getKeyFrame(stateTimer, true);
                break;
            case WalkLeft:
                region = playerWalkLeft.getKeyFrame(stateTimer, true);
                break;
            case WalkRight:
                region = playerWalkRight.getKeyFrame(stateTimer, true);
                break;
            case WalkUp:
                region = playerWalkUp.getKeyFrame(stateTimer, true);
                break;
//            case FaceDown:
//                region = playerFaceDown; //already set as default
//                break;
            case FaceLeft:
                region = playerFaceLeft;
                break;
            case FaceRight:
                region = playerFaceRight;
                break;
            case FaceUp:
                region = playerFaceUp;
                break;
            default:
                region = playerFaceDown;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (b2body.getLinearVelocity().y > 0)
            return State.WalkUp;
        else if (b2body.getLinearVelocity().y < 0)
            return State.WalkDown;
        else if (b2body.getLinearVelocity().x > 0)
            return State.WalkRight;
        else if (b2body.getLinearVelocity().x < 0)
            return State.WalkLeft;
        else if (previousState == State.WalkUp)
            return State.FaceUp;
        else if (previousState == State.WalkDown)
            return State.FaceDown;
        else if (previousState == State.WalkRight)
            return State.FaceRight;
        else if (previousState == State.WalkLeft)
            return State.FaceLeft;
        else
            return previousState;
    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(100 / PuzzleGame.PPM, 100 / PuzzleGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / PuzzleGame.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape edge = new EdgeShape();
        edge.set(new Vector2(-2 / PuzzleGame.PPM, 7 / PuzzleGame.PPM), new Vector2(2 / PuzzleGame.PPM, 7 / PuzzleGame.PPM));
        fdef.shape = edge;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("edge");
    }
}
