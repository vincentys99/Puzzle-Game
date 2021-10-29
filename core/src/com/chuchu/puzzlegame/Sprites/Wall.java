package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chuchu.puzzlegame.Screens.Room1;
import com.chuchu.puzzlegame.Tools.TileObjectClickListener;


public class Wall extends InteractiveTileObject {
    public static boolean first_password = false;
    public static boolean second_password = false;
    public static boolean third_password = false;
    public Wall(World world, TiledMap map, Rectangle bounds, float unitScale, Stage stage) {
        super(world, map, bounds, unitScale, stage);
        fixture.setUserData(this);
        setCategoryFilter(Room1.DOOR_BIT);

    }

    @Override
    public void onHit() {
        Gdx.app.log("Wall", "HIT!");
    }



    @Override
    public void onBeginContact() {
        Gdx.app.log("Wall", "Begin contact");
        this.setName("Wall");
        addActor();

    }

    @Override
    public void onEndContact() {
        Gdx.app.log("Wall", "End contact");
    }
}
