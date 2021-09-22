package com.chuchu.puzzlegame.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.chuchu.puzzlegame.PuzzleGame;

public class Room2WorldCreator {
    public Room2WorldCreator(World world, TiledMap tiledMap) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (int x = 2; x <= 5; x++){
            for (MapObject object : tiledMap.getLayers().get(x).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rectangle = ((RectangleMapObject)object).getRectangle();

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
