package com.chuchu.puzzlegame;

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

	public float bgMusicVol;
	public int defaultWidth;
	public int defaultHeight;

	FreeTypeFontGenerator generator;
	FreeTypeFontParameter parameter;

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
		defaultWidth = 1920;
		defaultHeight = 1080;

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
