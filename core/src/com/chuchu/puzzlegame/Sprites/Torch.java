package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chuchu.puzzlegame.Screens.Room1;

public class Torch extends InteractiveTileObject {
    public static boolean torchOn;
    public Torch(World world, TiledMap map, Rectangle bounds, float unitScale, Stage stage) {
        super(world, map, bounds, unitScale, stage);
        fixture.setUserData(this);

    }
    @Override
    public void onHit() {
        Gdx.app.log("Torch", "HIT!");
    }

    @Override
    public void onBeginContact() {
        Gdx.app.log("Torch", "Begin contact");
        this.setName("Torch");
        addActor();
    }

    @Override
    public void onEndContact() {
        Gdx.app.log("Torch", "End contact");
        removeActor();
    }
}
