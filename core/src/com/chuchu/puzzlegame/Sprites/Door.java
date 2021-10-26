package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.chuchu.puzzlegame.Screens.Room1;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;


public class Door extends InteractiveTileObject {
    public static boolean first_password = false;
    public static boolean second_password = false;
    public static boolean third_password = false;
    public Door(World world, TiledMap map, Rectangle bounds, float unitScale, Stage stage) {
        super(world, map, bounds, unitScale, stage);
        fixture.setUserData(this);

    }

    @Override
    public void onHit() {
        Gdx.app.log("Door", "HIT!");
        this.setName("Door");
    }

    @Override
    public void onBeginContact() {
        Gdx.app.log("Door", "Begin contact");
        this.setName("Door");
        addActor();
    }

    @Override
    public void onEndContact() {
        Gdx.app.log("Door", "End contact");
    }
}
