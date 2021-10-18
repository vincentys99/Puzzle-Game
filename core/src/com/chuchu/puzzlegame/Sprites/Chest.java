package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.Screens.Room1;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

public class Chest extends InteractiveTileObject {
    public Chest(World world, TiledMap map, Rectangle bounds, float unitScale, Stage stage) {
        super(world, map, bounds, unitScale, stage);
        fixture.setUserData(this);
    }

    @Override
    public void onHit() {
        Gdx.app.log("Chest", "Hit contact");
        Room1.showTape = true;
    }

    @Override
    public void onBeginContact() {
        Gdx.app.log("Chest", "Begin contact");
    }

    @Override
    public void onEndContact() {
        Gdx.app.log("Chest", "End contact");
        Room1.showTape = false;
    }
}
