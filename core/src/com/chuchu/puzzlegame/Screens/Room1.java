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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Sprites.Player1;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Room1 implements Screen {
    private Player1 player;
    private TextureRegion currentFrame;
    final PuzzleGame game;
    private TypingLabel dialogue;
    private Image dialogueBox;
    private static final int FRAME_COLS = 5, FRAME_ROWS = 1;
    OrthographicCamera camera;
    Music backgroundMusic;
    Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    Texture walkSheet;
    SpriteBatch spriteBatch;
    Skin defaultSkin;
    Stage stage;

    // A variable for tracking elapsed time for the animation
    float stateTime;
    private float xPos = 0;
    private float yPos = 0;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public Room1(final PuzzleGame game) {
        this.game = game;
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("untitled.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(Files.inGameMusic));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(game.bgMusicVol);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, PuzzleGame.defaultWidth, PuzzleGame.defaultHeight);

        player = new Player1();
        currentFrame = (TextureRegion) player.walkAnimations[0].getKeyFrame(stateTime, true);

        spriteBatch = new SpriteBatch();
        defaultSkin = new Skin(Gdx.files.internal(Files.uiskin));
        stage = new Stage(new ScreenViewport());

        dialogueBox = new Image(new Texture(Gdx.files.internal(Files.dialogImg)));
        dialogueBox.setSize(700, 200);
        dialogueBox.setPosition((Gdx.graphics.getWidth()/2) - (dialogueBox.getWidth() / 2),0);
        dialogue = new TypingLabel("Once upon a time in the village of motherfuckers a guy named Dema married an elephant", defaultSkin);
        dialogue.setPosition(dialogueBox.getX() + 22, dialogueBox.getHeight() - 100);
        stage.addActor(dialogueBox);
        stage.addActor(dialogue);

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
        stage.act();
        stage.draw();
        renderer.render();
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
