package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chuchu.puzzlegame.Screens.Room1;

public class Tape extends InteractiveTileObject{
    public Tape(World world, TiledMap map, Rectangle bounds, float unitScale, Stage stage) {
        super(world, map, bounds, unitScale, stage);
        fixture.setUserData(this);
    }

    @Override
    public void onHit() {
        Gdx.app.log("Tape", "Hit contact");
    }

    @Override
    public void onBeginContact() {
        Gdx.app.log("Tape", "Begin contact");
        this.setName("Tape");
        addActor();
    }

    @Override
    public void onEndContact() {
        Gdx.app.log("Tape", "End contact");
        removeActor();
    }
}
