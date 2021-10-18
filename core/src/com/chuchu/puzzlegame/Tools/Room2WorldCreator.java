package com.chuchu.puzzlegame.Tools;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Sprites.Chest;
import com.chuchu.puzzlegame.Sprites.Door;
import com.chuchu.puzzlegame.Sprites.TableObject;
import com.chuchu.puzzlegame.Sprites.Torch;

public class Room2WorldCreator {
    public Room2WorldCreator(World world, TiledMap tiledMap, float unitScale, Stage stage) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        if (tiledMap.getLayers().get("Static Objects") != null) {
            for (MapLayer objectLayer : ((MapGroupLayer)tiledMap.getLayers().get("Static Objects")).getLayers()) {
                for (RectangleMapObject object : objectLayer.getObjects().getByType(RectangleMapObject.class)) {
                    Rectangle rectangle = object.getRectangle();
                    if (objectLayer.getName().equals("Door")) {
                        new Door(world, tiledMap, rectangle, unitScale, stage);
                    }
                    if (objectLayer.getName().equals("Tables")) {
                        new TableObject(world, tiledMap, rectangle, unitScale, stage);
                    }
                    if (objectLayer.getName().equals("Chest")) {
                        new Chest(world, tiledMap, rectangle, unitScale, stage);
                    }
                    if (objectLayer.getName().equals("Torch")) {
                        new Torch(world, tiledMap, rectangle, unitScale, stage);
                    }
                    else {
                        bodyDef.type = BodyDef.BodyType.StaticBody;
                        bodyDef.position.set(
                                (rectangle.getX() + rectangle.getWidth() / 2) / PuzzleGame.PPM * unitScale,
                                (rectangle.getY() + rectangle.getHeight() / 2) / PuzzleGame.PPM * unitScale
                        );
                        body = world.createBody(bodyDef);

                        polygonShape.setAsBox(
                                rectangle.getWidth() / 2 / PuzzleGame.PPM * unitScale,
                                rectangle.getHeight() / 2 / PuzzleGame.PPM * unitScale
                        );
                        fixtureDef.shape = polygonShape;
                        body.createFixture(fixtureDef);
                    }
                }
            }
        }
    }
}
