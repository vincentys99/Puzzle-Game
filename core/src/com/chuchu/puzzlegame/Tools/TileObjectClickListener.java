package com.chuchu.puzzlegame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.Screens.Room2;
import com.chuchu.puzzlegame.Sprites.InteractiveTileObject;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

public class TileObjectClickListener extends ClickListener {
    private final InteractiveTileObject actor;

    Skin skin;
    private TypingLabel dialogue;
    private Image dialogueBox;

    public TileObjectClickListener(InteractiveTileObject actor) {
        super(Input.Buttons.RIGHT);
        this.actor = actor;

        skin = new Skin(Gdx.files.internal(Files.uiskin));

        dialogueBox = new Image(new Texture(Gdx.files.internal(Files.dialogImg)));
        dialogueBox.setSize(700, 200);
        dialogueBox.setPosition((Gdx.graphics.getWidth() / 2f) - (dialogueBox.getWidth() / 2),0);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log(actor.getName(), "Player has clicked!");

        if (!Room2.showDialogue) {
            dialogue = new TypingLabel("Dialog text...", skin);
            dialogue.setPosition(dialogueBox.getX() + 22, dialogueBox.getHeight() - 100);
            Room2.stageTesting.addActor(dialogueBox);
            Room2.stageTesting.addActor(dialogue);
            Room2.showDialogue = true;
        }
        else {
            actor.removeDialog();
        }
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal(Files.cursorDialog)), 0, 0));
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal(Files.cursorNormalSelect)), 0, 0));
    }
}
