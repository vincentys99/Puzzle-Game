package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chuchu.puzzlegame.Screens.Room1;

public class Torch extends InteractiveTileObject {
    private boolean torchOn;
    public Torch(World world, TiledMap map, Rectangle bounds, float unitScale, Stage stage) {
        super(world, map, bounds, unitScale, stage);
        fixture.setUserData(this);

    }
    @Override
    public void onHit() {
        Gdx.app.log("Torch", "HIT!");
        torchOn = !torchOn;
        if(torchOn) {
            Room1.tiledMap.getLayers().get(9).setVisible(true);
            Room1.tiledMap.getLayers().get(8).setVisible(false);
        } else {
            Room1.tiledMap.getLayers().get(8).setVisible(true);
            Room1.tiledMap.getLayers().get(9).setVisible(false);
        }
    }

    @Override
    public void onBeginContact() {
        Gdx.app.log("Torch", "Begin contact");
        this.setName("TorchObject");
        addActor();
    }

    @Override
    public void onEndContact() {
        Gdx.app.log("Torch", "End contact");
        removeActor();
    }
}
