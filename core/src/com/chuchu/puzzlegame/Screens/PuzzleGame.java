package com.chuchu.puzzlegame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class PuzzleGame extends Game {

	public SpriteBatch batch;
	public BitmapFont font;

	public static final int defaultWidth = 1920;
	public static final int defaultHeight = 1080;
	public static final int PPM = 100;

	FreeTypeFontGenerator generator;
	FreeTypeFontParameter parameter;

	public float bgMusicVol;

	@Override
	public void create () {
		batch = new SpriteBatch();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GrechenFuemen-Regular.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 68;
		parameter.color = Color.BLACK;
		parameter.borderColor = Color.WHITE;
		parameter.borderWidth = 2;
		font = generator.generateFont(parameter);

		bgMusicVol = 0.1f;

		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		generator.dispose();
	}
}
