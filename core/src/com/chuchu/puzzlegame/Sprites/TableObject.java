package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TableObject extends InteractiveTileObject {
    public TableObject(World world, TiledMap map, Rectangle bounds, float unitScale, Stage stage) {
        super(world, map, bounds, unitScale, stage);
        fixture.setUserData(this);
    }

    @Override
    public void onHit() {
        Gdx.app.log("Table", "HIT!");
    }

    @Override
    public void onBeginContact() {
        Gdx.app.log("Table", "Begin contact");
        addActor();
    }

    @Override
    public void onEndContact() {
        Gdx.app.log("Table", "End contact");
        removeActor();
    }
}
