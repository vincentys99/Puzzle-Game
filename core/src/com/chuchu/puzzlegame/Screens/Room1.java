package com.chuchu.puzzlegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Sprites.Player1;

public class Room1 implements Screen {
    private Player1 player;
    private TextureRegion currentFrame;
    final PuzzleGame game;
    private static final int FRAME_COLS = 5, FRAME_ROWS = 1;
    OrthographicCamera camera;
    Music backgroundMusic;
    Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    Texture walkSheet;
    SpriteBatch spriteBatch;

    // A variable for tracking elapsed time for the animation
    float stateTime;
    private float xPos = 0;
    private float yPos = 0;
    public Room1(final PuzzleGame game) {
        this.game = game;
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/CRSED Music Theme.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(game.bgMusicVol);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, PuzzleGame.defaultWidth, PuzzleGame.defaultHeight);

        player = new Player1();
        currentFrame = (TextureRegion) player.walkAnimations[0].getKeyFrame(stateTime, true);

        spriteBatch = new SpriteBatch();

    }

    @Override
    public void show() {
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();
        spriteBatch.begin();
        handleInput();

        spriteBatch.draw(currentFrame, xPos, yPos); // Draw current frame at (50, 50)
        spriteBatch.end();

    }
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            currentFrame = (TextureRegion) player.walkAnimations[0].getKeyFrame(stateTime, true);
            xPos += 1;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            currentFrame = (TextureRegion) player.walkAnimations[1].getKeyFrame(stateTime, true);

            xPos -= 1;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            currentFrame = (TextureRegion) player.walkAnimations[2].getKeyFrame(stateTime, true);
            yPos += 1;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            currentFrame = (TextureRegion) player.walkAnimations[3].getKeyFrame(stateTime, true);
            yPos -= 1;
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
        spriteBatch.dispose();
        walkSheet.dispose();
    }
}
