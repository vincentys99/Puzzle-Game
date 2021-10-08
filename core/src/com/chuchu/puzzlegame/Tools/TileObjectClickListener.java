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
import com.chuchu.puzzlegame.Screens.Room2;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

public class TileObjectClickListener extends ClickListener {
    private final Actor actor;

    Skin skin;
    private TypingLabel dialogue;
    private Image dialogueBox;

    public TileObjectClickListener(Actor actor) {
        super(Input.Buttons.RIGHT);
        this.actor = actor;

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        dialogueBox = new Image(new Texture(Gdx.files.internal("dialog.png")));
        dialogueBox.setSize(700, 200);
        dialogueBox.setPosition((Gdx.graphics.getWidth()/2) - (dialogueBox.getWidth() / 2),0);

    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log(actor.getName(), "Player has clicked!");
        dialogue = new TypingLabel("Hello Table!", skin);
        dialogue.setPosition(dialogueBox.getX() + 22, dialogueBox.getHeight() - 100);
        Room2.stageTesting.addActor(dialogueBox);
        Room2.stageTesting.addActor(dialogue);

        Room2.showDialogue = true;
        System.out.println("table boolean is true");
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("images/cursor/cursor02.png")), 0, 0));
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("images/cursor/cursor01.png")), 0, 0));
    }
}
