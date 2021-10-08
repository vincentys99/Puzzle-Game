package com.chuchu.puzzlegame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TileObjectClickListener extends ClickListener {
    private final Actor actor;

    Skin skin;

    public TileObjectClickListener(Actor actor) {
        super(Input.Buttons.RIGHT);
        this.actor = actor;

        skin = new Skin(Gdx.files.internal("uiskin.json"));
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log(actor.getName(), "Player has clicked!");
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
