package com.chuchu.puzzlegame.Tools;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Sprites.Fireplace;

public class Room2WorldCreator {
    public Room2WorldCreator(World world, TiledMap tiledMap) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (MapLayer objectLayer : ((MapGroupLayer)tiledMap.getLayers().get("Static Objects")).getLayers()) {
            System.out.println(objectLayer.isVisible());
            for (RectangleMapObject object : objectLayer.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rectangle = object.getRectangle();

                if (objectLayer.getName().equals("Fireplace")) {
                    new Fireplace(world, tiledMap, rectangle);
                }
                else {
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    bodyDef.position.set(
                            (rectangle.getX() + rectangle.getWidth() / 2) / PuzzleGame.PPM,
                            (rectangle.getY() + rectangle.getHeight() / 2) / PuzzleGame.PPM
                    );
                    body = world.createBody(bodyDef);

                    polygonShape.setAsBox(
                            rectangle.getWidth() / 2 / PuzzleGame.PPM,
                            rectangle.getHeight() / 2 / PuzzleGame.PPM
                    );
                    fixtureDef.shape = polygonShape;
                    body.createFixture(fixtureDef);
                }
            }
        }
    }
}
