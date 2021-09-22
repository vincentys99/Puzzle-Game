package com.chuchu.puzzlegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.chuchu.puzzlegame.PuzzleGame;

public class Room1 implements Screen {
    final PuzzleGame game;

    OrthographicCamera camera;
    Music backgroundMusic;

    public Room1(final PuzzleGame game) {
        this.game = game;

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("CRSED Music Theme.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(game.bgMusicVol);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, PuzzleGame.defaultWidth, PuzzleGame.defaultHeight);
    }

    @Override
    public void show() {
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome, Player!", 100, 150);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundMusic.dispose();
    }
}
