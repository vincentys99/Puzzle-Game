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
import com.chuchu.puzzlegame.Screens.LoadingScreen;
import com.chuchu.puzzlegame.Screens.MainMenuScreen;

public class PuzzleGame extends Game {
	public SpriteBatch batch;
	public static BitmapFont font;
	public static BitmapFont font2;
	public static BitmapFont font3;

	public static final int defaultWidth = Constants.defaultX;
	public static final int defaultHeight = Constants.defaultY;
	public static final float PPM = 100; // Pixel per Meter

	FreeTypeFontGenerator generator;
	FreeTypeFontGenerator generator2;

	FreeTypeFontParameter parameter;
	FreeTypeFontParameter parameter2;

	public float bgMusicVol = 1f;


	@Override
	public void create () {
		batch = new SpriteBatch();

		parameter = new FreeTypeFontParameter();
		parameter.size = 68;
		parameter.color = Color.BLACK;
		parameter.borderColor = Color.WHITE;
		parameter.borderWidth = 2;

		parameter2 = new FreeTypeFontParameter();
		parameter2.size = 32;
		parameter2.color = Color.BLACK;
		parameter2.borderColor = Color.WHITE;
		parameter2.borderWidth = 2;

		generator = new FreeTypeFontGenerator(Gdx.files.internal(Files.customFont));
		generator2 = new FreeTypeFontGenerator(Gdx.files.internal(Files.customFont2));

		font = generator.generateFont(parameter);
		font2 = generator2.generateFont(parameter);
		font3 = generator2.generateFont(parameter2);

		Pixmap pixmap = new Pixmap(Gdx.files.internal(Files.cursorNormalSelect));
		Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
		Gdx.graphics.setCursor(cursor);
		pixmap.dispose();

		this.setScreen(new LoadingScreen(this));
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
