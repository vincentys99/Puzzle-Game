package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chuchu.puzzlegame.Screens.Room1;
import com.chuchu.puzzlegame.Tools.TileObjectClickListener;


public class Pillar extends InteractiveTileObject {
    public static boolean first_password = false;
    public static boolean second_password = false;
    public static boolean third_password = false;
    public Pillar(World world, TiledMap map, Rectangle bounds, float unitScale, Stage stage) {
        super(world, map, bounds, unitScale, stage);
        fixture.setUserData(this);
        setCategoryFilter(Room1.DOOR_BIT);

    }

    @Override
    public void onHit() {
        Gdx.app.log("Pillar", "HIT!");
        if( TileObjectClickListener.doorUnlocked) {
            setCategoryFilter(Room1.DESTROYED_BIT);
            onEndContact();
        }
    }


    @Override
    public void onBeginContact() {
        Gdx.app.log("Pillar", "Begin contact");
        this.setName("Pillar");
        addActor();

    }
    @Override
    public void onEndContact() {
        Gdx.app.log("Pillar", "End contact");
    }
}
