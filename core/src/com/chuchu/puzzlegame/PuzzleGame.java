package com.chuchu.puzzlegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.chuchu.puzzlegame.Global.Constants;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.Screens.MainMenuScreen;

public class PuzzleGame extends Game {

	public SpriteBatch batch;
	public static BitmapFont font;

	public static final int defaultWidth = Constants.defaultX;
	public static final int defaultHeight = Constants.defaultY;
	public static final float PPM = 100; // Pixel per Meter

	FreeTypeFontGenerator generator;
	FreeTypeFontParameter parameter;

	public float bgMusicVol = 0.1f;

	@Override
	public void create () {
		batch = new SpriteBatch();
		generator = new FreeTypeFontGenerator(Gdx.files.internal(Files.customFont));
		parameter = new FreeTypeFontParameter();
		parameter.size = 68;
		parameter.color = Color.BLACK;
		parameter.borderColor = Color.WHITE;
		parameter.borderWidth = 2;
		font = generator.generateFont(parameter);

		Pixmap pixmap = new Pixmap(Gdx.files.internal(Files.cursorNormalSelect));
		Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
		Gdx.graphics.setCursor(cursor);
		pixmap.dispose();

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
