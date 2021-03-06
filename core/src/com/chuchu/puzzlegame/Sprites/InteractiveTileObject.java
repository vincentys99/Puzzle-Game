package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Screens.Room1;
import com.chuchu.puzzlegame.Screens.Room2;
import com.chuchu.puzzlegame.Tools.TileObjectClickListener;

public abstract class InteractiveTileObject extends Actor {
    protected World world;
    protected TiledMap map;
    public Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected Stage stage;
    protected float unitScale;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds, float unitScale, Stage stage) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;
        this.stage = stage;
        this.unitScale = unitScale;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(
                (bounds.getX() + bounds.getWidth() / 2) / PuzzleGame.PPM * unitScale,
                (bounds.getY() + bounds.getHeight() / 2) / PuzzleGame.PPM * unitScale
        );

        body = world.createBody(bdef);

        shape.setAsBox(
                bounds.getWidth() / 2 / PuzzleGame.PPM * unitScale,
                bounds.getHeight() / 2 / PuzzleGame.PPM * unitScale
        );
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
        EventListener eventListener = new TileObjectClickListener(this);
        this.addListener(eventListener);
    }

    public abstract void onHit();

    public abstract void onBeginContact();

    public abstract void onEndContact();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
    public void addActor() {
        float actorX = bounds.getX() / PuzzleGame.PPM * unitScale;
        float actorY = bounds.getY() / PuzzleGame.PPM * unitScale;
        float actorWidth = bounds.getWidth() / PuzzleGame.PPM * unitScale;
        float actorHeight = bounds.getHeight() / PuzzleGame.PPM * unitScale;
        this.setBounds(actorX, actorY, actorWidth, actorHeight);
        stage.addActor(this);

    }
    public void removeActor() {
        stage.clear();
        removeDialog();
    }

    public void removeDialog() {
//        if (Room2.stageTesting != null) {
//            Room2.showDialogue = false;
//            Room2.stageTesting.clear();
//        }
        if (Room1.stageTesting != null) {
            Room1.showDialogue = false;
            Room1.stageTesting.clear();
        }
    }
}
