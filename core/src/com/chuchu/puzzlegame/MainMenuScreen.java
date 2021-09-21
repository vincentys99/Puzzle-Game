package com.chuchu.puzzlegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
public class MainMenuScreen implements Screen {

    final PuzzleGame game;
    private OrthographicCamera camera;
    private Sound btnClickSound;
    private Music backgroundMusic;
    private Texture texture;
    private Stage stage;
    private TextButtonStyle textButtonStyle;
    private TextButton btnStart;
    private TextButton btnOption;
    private TextButton btnExit;
    private Table menuTable;
    private Table optionTable;
    private Skin defaultSkin;
//    private TextField optionWindow;

    public MainMenuScreen (final PuzzleGame game) {
        this.game = game;

        // Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        // Sound & Music
        btnClickSound = Gdx.audio.newSound(Gdx.files.internal("button_click.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("ZEDD x VALORANT Music Theme.ogg"));
        backgroundMusic.setLooping(true);

        // Image
        texture = new Texture(Gdx.files.internal("valorant_wallpaper_yoru.jpg"));


        // Skin
        defaultSkin = new Skin(Gdx.files.internal("uiskin.json"));

        // Stage for buttons
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Buttons
        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = game.font;
        textButtonStyle.overFontColor = Color.RED;

        btnStart = new TextButton("Start Game", textButtonStyle);
        btnOption = new TextButton("Option", textButtonStyle);
        btnExit = new TextButton("Exit Game", textButtonStyle);

        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnClickSound.play();
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game));
                        dispose();
                    }
                }, 0.5F);
            }
        });

        btnOption.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnClickSound.play();
                loadOptions();
            }
        });

        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnClickSound.play();
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        dispose();
                        Gdx.app.exit();
                    }
                }, 0.5f);
            }
        });

        // Menu Table
        menuTable = new Table();
        menuTable.add(btnStart);
        menuTable.row();
        menuTable.add(btnOption);
        menuTable.row();
        menuTable.add(btnExit);
        menuTable.setFillParent(true);
        stage.addActor(menuTable);

        // Option Table
        optionTable = new Table();
        optionTable.setX((float)Gdx.graphics.getWidth() / 2);
        optionTable.setY((float)Gdx.graphics.getHeight() / 2);
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
        game.batch.draw(texture, 0, 0);
        game.batch.end();

        stage.act();
        stage.draw();
    }

    private void loadOptions() {
        final TextField resolutionWidth = new TextField("1920", defaultSkin);
        final TextField resolutionHeight = new TextField("1080", defaultSkin);
        TextButton apply = new TextButton("Apply", defaultSkin);
        apply.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setWindowedMode(Integer.parseInt(resolutionWidth.getText()), Integer.parseInt(resolutionHeight.getText()));
            }
        });
        Label resolutionWidthText = new Label("Width", defaultSkin);
        Label resolutionHeightText = new Label("Height", defaultSkin);
        optionTable.add(resolutionWidthText);
        optionTable.add(resolutionWidth);
        optionTable.row();
        optionTable.add(resolutionHeightText);
        optionTable.add(resolutionHeight);
        optionTable.row();
        optionTable.add(apply);

        stage.addActor(optionTable);

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        stage.getCamera().position.set((float)Gdx.graphics.getWidth() / 2, (float)Gdx.graphics.getHeight() / 2, 0);
//        System.out.println(optionTable.getX());
//        System.out.println(Gdx.graphics.getWidth());
//        System.out.println((optionTable.getX() / Gdx.graphics.getWidth()) * 100);
//        System.out.println((optionTable.getY() / Gdx.graphics.getHeight()) * 100);
//        System.out.println((((float) 50 / 100) * Gdx.graphics.getWidth()));
//        System.out.println((optionTable.getY() / Gdx.graphics.getHeight()) * 100);
        optionTable.setX(((float) 50 / 100) * Gdx.graphics.getWidth());
        optionTable.setY(((float) 50 / 100) * Gdx.graphics.getHeight());
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
        btnClickSound.dispose();
        backgroundMusic.dispose();
        texture.dispose();
        stage.dispose();
        defaultSkin.dispose();
    }
}
