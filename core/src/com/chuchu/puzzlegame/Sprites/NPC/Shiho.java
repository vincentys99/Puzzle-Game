package com.chuchu.puzzlegame.Sprites.NPC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chuchu.puzzlegame.Screens.Room1;
import com.chuchu.puzzlegame.Sprites.InteractiveTileObject;
import com.chuchu.puzzlegame.Tools.TileObjectClickListener;


public class Shiho extends InteractiveTileObject {
    public Shiho(World world, TiledMap map, Rectangle bounds, float unitScale, Stage stage) {
        super(world, map, bounds, unitScale, stage);
        fixture.setUserData(this);

    }

    @Override
    public void onHit() {
    }


    @Override
    public void onBeginContact() {
        this.setName("Shiho");
        addActor();

    }
    @Override
    public void onEndContact() {
    }
}
