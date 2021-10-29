package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Screens.Room1;
import com.chuchu.puzzlegame.Screens.Room2;

public class Player2 extends Sprite {

    public enum State { Standing,
        WalkDown, WalkLeft, WalkRight, WalkUp,
        FaceDown, FaceLeft, FaceRight, FaceUp,
        WalkBottomRight, WalkBottomLeft, WalkTopLeft, WalkTopRight,
        FaceBottomRight, FaceBottomLeft, FaceTopLeft, FaceTopRight }
    private Animation<TextureRegion> playerWalkDown;
    private Animation<TextureRegion> playerWalkLeft;
    private Animation<TextureRegion> playerWalkRight;
    private Animation<TextureRegion> playerWalkUp;
    private Animation<TextureRegion> playerWalkBottomRight;
    private Animation<TextureRegion> playerWalkBottomLeft;
    private Animation<TextureRegion> playerWalkTopLeft;
    private Animation<TextureRegion> playerWalkTopRight;
    public State currentState;
    public State previousState;
    private float stateTimer;

    public World world;
    public Body b2body;
    TextureRegion playerFaceDown;
    TextureRegion playerFaceLeft;
    TextureRegion playerFaceRight;
    TextureRegion playerFaceUp;
    TextureRegion playerFaceBottomRight;
    TextureRegion playerFaceBottomLeft;
    TextureRegion playerFaceTopLeft;
    TextureRegion playerFaceTopRight;

    private final int PlayerWidth = 24;
    private final int PlayerHeight = 48;

    public Player2(World world, TextureAtlas atlas, TiledMap tiledMap) {
        super(atlas.findRegion("WalkBottomLeft"));
        this.world = world;

        currentState = State.Standing;
        previousState = State.Standing;
        stateTimer = 0;

        setAnimations();

        definePlayer(tiledMap);
        playerFaceBottomLeft = new TextureRegion(getTexture(), 0, 0, PlayerWidth, PlayerHeight);
        playerFaceBottomRight = new TextureRegion(getTexture(), 5 * PlayerWidth, 0, PlayerWidth, PlayerHeight);
        playerFaceDown = new TextureRegion(getTexture(), 10 * PlayerWidth, 0, PlayerWidth, PlayerHeight);
        playerFaceLeft = new TextureRegion(getTexture(), 15 * PlayerWidth, 0, PlayerWidth, PlayerHeight);
        playerFaceRight = new TextureRegion(getTexture(), 20 * PlayerWidth, 0, PlayerWidth, PlayerHeight);
        playerFaceTopLeft = new TextureRegion(getTexture(), 25 * PlayerWidth, 0, PlayerWidth, PlayerHeight);
        playerFaceTopRight = new TextureRegion(getTexture(), 30 * PlayerWidth, 0, PlayerWidth, PlayerHeight);
        playerFaceUp = new TextureRegion(getTexture(), 35 * PlayerWidth, 0, PlayerWidth, PlayerHeight);
        setBounds(0, 0, PlayerWidth * 2 / PuzzleGame.PPM, PlayerHeight * 2 / PuzzleGame.PPM);
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
            case WalkBottomLeft:
                region = playerWalkBottomLeft.getKeyFrame(stateTimer, true);
                break;
            case WalkBottomRight:
                region = playerWalkBottomRight.getKeyFrame(stateTimer, true);
                break;
            case WalkTopLeft:
                region = playerWalkTopLeft.getKeyFrame(stateTimer, true);
                break;
            case WalkTopRight:
                region = playerWalkTopRight.getKeyFrame(stateTimer, true);
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
            case FaceBottomLeft:
                region = playerFaceBottomLeft;
                break;
            case FaceBottomRight:
                region = playerFaceBottomRight;
                break;
            case FaceTopLeft:
                region = playerFaceTopLeft;
                break;
            case FaceTopRight:
                region = playerFaceTopRight;
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
        if (b2body.getLinearVelocity().y < 0 && b2body.getLinearVelocity().x < 0)
            return State.WalkBottomLeft;
        else if (b2body.getLinearVelocity().y < 0 && b2body.getLinearVelocity().x > 0)
            return State.WalkBottomRight;
        else if (b2body.getLinearVelocity().y > 0 && b2body.getLinearVelocity().x < 0)
            return State.WalkTopLeft;
        else if (b2body.getLinearVelocity().y > 0 && b2body.getLinearVelocity().x > 0)
            return State.WalkTopRight;
        else if (b2body.getLinearVelocity().y > 0)
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
        else if (previousState == State.WalkBottomLeft)
            return State.FaceBottomLeft;
        else if (previousState == State.WalkBottomRight)
            return State.FaceBottomRight;
        else if (previousState == State.WalkTopLeft)
            return State.FaceTopLeft;
        else if (previousState == State.WalkTopRight)
            return State.FaceTopRight;
        else
            return previousState;
    }

    public void setAnimations() {
        Array<TextureRegion> frames = new Array<>();
        for(int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(getTexture(), i * PlayerWidth, 0, PlayerWidth, PlayerHeight));
        }
        playerWalkBottomLeft = new Animation<>(0.1f, frames);
        frames.clear();

        for(int i = 6; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), i * PlayerWidth, 0, PlayerWidth, PlayerHeight));
        }
        playerWalkBottomRight = new Animation<>(0.1f, frames);
        frames.clear();

        for(int i = 11; i < 15; i++) {
            frames.add(new TextureRegion(getTexture(), i * PlayerWidth, 0, PlayerWidth, PlayerHeight));
        }
        playerWalkDown = new Animation<>(0.1f, frames);
        frames.clear();

        for(int i = 16; i < 20; i++) {
            frames.add(new TextureRegion(getTexture(), i * PlayerWidth, 0, PlayerWidth, PlayerHeight));
        }
        playerWalkLeft = new Animation<>(0.1f, frames);
        frames.clear();

        for(int i = 21; i < 25; i++) {
            frames.add(new TextureRegion(getTexture(), i * PlayerWidth, 0, PlayerWidth, PlayerHeight));
        }
        playerWalkRight = new Animation<>(0.1f, frames);
        frames.clear();

        for(int i = 26; i < 30; i++) {
            frames.add(new TextureRegion(getTexture(), i * PlayerWidth, 0, PlayerWidth, PlayerHeight));
        }
        playerWalkTopLeft = new Animation<>(0.1f, frames);
        frames.clear();

        for(int i = 31; i < 35; i++) {
            frames.add(new TextureRegion(getTexture(), i * PlayerWidth, 0, PlayerWidth, PlayerHeight));
        }
        playerWalkTopRight = new Animation<>(0.1f, frames);
        frames.clear();

        for(int i = 36; i < 40; i++) {
            frames.add(new TextureRegion(getTexture(), i * PlayerWidth, 0, PlayerWidth, PlayerHeight));
        }
        playerWalkUp = new Animation<>(0.1f, frames);
        frames.clear();
    }

    public void definePlayer(TiledMap tiledMap) {
        BodyDef bdef = new BodyDef();

        // set player to center of tile map
        MapProperties prop = tiledMap.getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        int mapPixelWidth = mapWidth * tilePixelWidth;
        int mapPixelHeight = mapHeight * tilePixelHeight;

        bdef.position.set( (mapPixelWidth - PlayerWidth) / PuzzleGame.PPM,  (mapPixelHeight - PlayerWidth) / PuzzleGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        // set shape around player's leg for collision
        CircleShape shape = new CircleShape();
        shape.setRadius((PlayerWidth + (PlayerWidth * 0.5f)) / PuzzleGame.PPM);
        fdef.filter.categoryBits = Room1.PLAYER_BIT;
        fdef.filter.maskBits = Room1.DEFAULT_BIT | Room1.DOOR_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef);

        // set shape outside the collision zone to detect collision
        shape.setRadius((PlayerWidth + (PlayerWidth * 0.5f)) / PuzzleGame.PPM);
        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("collidable");

        // set shape outside player to detect player intractable zone
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(PlayerWidth * 3 / PuzzleGame.PPM, PlayerWidth * 3 / PuzzleGame.PPM);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("clickable");
    }
}
