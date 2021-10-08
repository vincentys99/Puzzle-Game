package com.chuchu.puzzlegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.chuchu.puzzlegame.PuzzleGame;

public class MainMenuScreen implements Screen {

    final PuzzleGame game;
    OrthographicCamera camera;
    Sound btnClickSound;
    Music backgroundMusic;
    Texture texture;
    Stage stage;
    TextButtonStyle textButtonStyle;
    TextButton btnStart;
    TextButton btnOption;
    TextButton btnExit;
    TextButton btnTemp;
    Table menuTable;
    Table optionTable;
    Skin defaultSkin;
//    private TextField optionWindow;

    public MainMenuScreen (final PuzzleGame game) {
        this.game = game;

        // Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, PuzzleGame.defaultWidth, PuzzleGame.defaultHeight);

        // Sound & Music
        btnClickSound = Gdx.audio.newSound(Gdx.files.internal("sound/button_click.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/ZEDD x VALORANT Music Theme.ogg"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(game.bgMusicVol);

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
        btnTemp = new TextButton("Enter 2nd Room", textButtonStyle);

        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnClickSound.play(game.bgMusicVol);
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        game.setScreen(new Room1(game));
                        dispose();
                    }
                }, 0.5F);
            }
        });

        btnOption.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnClickSound.play(game.bgMusicVol);
                loadOptions();
            }
        });

        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnClickSound.play(game.bgMusicVol);
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        dispose();
                        Gdx.app.exit();
                    }
                }, 0.5f);
            }
        });

        btnTemp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnClickSound.play(game.bgMusicVol);
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        game.setScreen(new Room2(game));
                        dispose();
                    }
                }, 0.5F);
            }
        });

        // Menu Table
        menuTable = new Table();
        menuTable.add(btnStart);
        menuTable.row();
        menuTable.add(btnTemp);
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
        optionTable.reset();
        final TextField resolutionWidth = new TextField(String.valueOf(Gdx.graphics.getWidth()), defaultSkin);
        final TextField resolutionHeight = new TextField(String.valueOf(Gdx.graphics.getHeight()), defaultSkin);
        final Slider volume = new Slider(0, 100, 5, false, defaultSkin);
        final Label volumeValueText = new Label(String.valueOf(game.bgMusicVol * 100), defaultSkin);
        final TextButton btnToggleFullscreen = new TextButton((Gdx.graphics.isFullscreen()) ? "OFF" : "ON", defaultSkin);
        volume.setValue(game.bgMusicVol * 100);
        TextButton apply = new TextButton("Apply", defaultSkin);
        TextButton close = new TextButton("Close", defaultSkin);
        apply.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (btnToggleFullscreen.getText() == "ON"){
                    Gdx.graphics.setWindowedMode(Integer.parseInt(resolutionWidth.getText()), Integer.parseInt(resolutionHeight.getText()));
                }
                optionTable.reset();
            }
        });
        close.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionTable.reset();
            }
        });
        volume.addListener( new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                volumeValueText.setText(String.valueOf(volume.getValue()));
                backgroundMusic.setVolume(volume.getValue() / 100);
                game.bgMusicVol = volume.getValue() / 100;
            }
        });
        btnToggleFullscreen.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Gdx.graphics.isFullscreen()) {
                    Gdx.graphics.setWindowedMode(PuzzleGame.defaultWidth, PuzzleGame.defaultHeight);
                    btnToggleFullscreen.setText("ON");
                }
                else {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    btnToggleFullscreen.setText("OFF");
                }
                resolutionWidth.setText(String.valueOf(Gdx.graphics.getWidth()));
                resolutionHeight.setText(String.valueOf(Gdx.graphics.getHeight()));
            }
        });
        Label resolutionWidthText = new Label("Width", defaultSkin);
        Label resolutionHeightText = new Label("Height", defaultSkin);
        Label volumeText = new Label("Volume", defaultSkin);
        Label fullscreenText = new Label("Fullscreen", defaultSkin);
        optionTable.add(resolutionWidthText);
        optionTable.add(resolutionWidth);
        optionTable.row();
        optionTable.add(resolutionHeightText);
        optionTable.add(resolutionHeight);
        optionTable.row();
        optionTable.add(volumeText);
        optionTable.add(volume);
        optionTable.add(volumeValueText);
        optionTable.row();
        optionTable.add(fullscreenText);
        optionTable.add(btnToggleFullscreen);
        optionTable.row();
        optionTable.add(apply);
        optionTable.add(close);


        stage.addActor(optionTable);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
//        stage.getCamera().position.set((float)Gdx.graphics.getWidth() / 2, (float)Gdx.graphics.getHeight() / 2, 0);
        optionTable.setX((float) Gdx.graphics.getWidth() / 2);
        optionTable.setY((float) Gdx.graphics.getHeight() / 2);
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
