package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.Screens.Room1;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

public class Dialogue {
    private Image dialogueBox;
    private TypingLabel dialogue;
    private Skin skin;
    private String text;
    public Dialogue(String text) {
        skin = new Skin(Gdx.files.internal(Files.uiskin));
        this.text = text;
    }
    public void setup_dialogue() {
        dialogueBox = new Image(new Texture(Gdx.files.internal(Files.dialogImg)));
        dialogueBox.setSize(700, 200);
        dialogueBox.setPosition((Gdx.graphics.getWidth() / 2f) - (dialogueBox.getWidth() / 2),0);
        run_dialogue();
    }
    private void run_dialogue() {
        Gdx.input.setInputProcessor(Room1.stageTesting);
        dialogue = new TypingLabel(this.text, skin);
        dialogue.setPosition(dialogueBox.getX() + 22, dialogueBox.getHeight() - 100);
        Room1.stageTesting.addActor(dialogueBox);
        Room1.stageTesting.addActor(dialogue);
        Room1.showDialogue = true;
        Room1.moveable = false;

    }

    public TypingLabel dialogue() {return dialogue;}
    public Image dialogueBox() {return dialogueBox;}
}
