package com.chuchu.puzzlegame.Tools;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Sprites.*;
import com.chuchu.puzzlegame.Sprites.NPC.*;

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
                    switch (objectLayer.getName()) {
                        case "Door":
                            new Door(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "dema":
                            new Dema(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "shiho":
                            new Shiho(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "hinn":
                            new Hinn(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "vince":
                            new Vince(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "henry":
                            new Henry(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "miu":
                            new Miu(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "DoorOpen":
                            new DoorUnlocked(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "Tables":
                            new TableObject(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "Chest":
                            new Chest(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "Torch":
                            new Torch(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "Wall":
                            new Wall(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        case "tapes":
                            new Tape(world, tiledMap, rectangle, unitScale, stage);
                            break;
                        default:
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
                            //body.createFixture(fixtureDef);
                            break;
                    }
                }
            }
        }
    }
}
