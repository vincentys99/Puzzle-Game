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
import com.chuchu.puzzlegame.Screens.Room2;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

public class Door extends InteractiveTileObject {
    private TypingLabel dialogue;
    private Image dialogueBox;
    private static boolean doorUnlocked = false;
    private Skin skin;
    public Door(World world, TiledMap map, Rectangle bounds, float unitScale, Stage stage) {
        super(world, map, bounds, unitScale, stage);
        fixture.setUserData(this);
        skin = new Skin(Gdx.files.internal(Files.uiskin));
        dialogueBox = new Image(new Texture(Gdx.files.internal(Files.dialogImg)));
        dialogueBox.setSize(700, 200);
        dialogueBox.setPosition((Gdx.graphics.getWidth() / 2f) - (dialogueBox.getWidth() / 2),0);
    }

    @Override
    public void onHit() {
        Gdx.app.log("Door", "HIT!");
        if(!doorUnlocked) {
            dialogue = new TypingLabel("The door is locked ...", skin);
            dialogue.setPosition(dialogueBox.getX() + 22, dialogueBox.getHeight() - 100);
            Room1.stageTesting.addActor(dialogueBox);
            Room1.stageTesting.addActor(dialogue);
            Room1.showDialogue = true;
        } else {
            System.out.println("Door is now unlocked");
        }
    }

    @Override
    public void onBeginContact() {
        Gdx.app.log("Door", "Begin contact");
        addActor();
    }

    @Override
    public void onEndContact() {
        Gdx.app.log("Door", "End contact");
        removeActor();
    }
}
