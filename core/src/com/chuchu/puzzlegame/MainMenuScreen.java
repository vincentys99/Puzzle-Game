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
    OrthographicCamera camera;
    Texture texture;
    Stage stage;
    TextButton btnStart;
    TextButton btnOption;
    TextButton btnExit;
    //TextField optionWindow;
    TextButtonStyle textButtonStyle;
    Table menuTable;
    Music backgroundMusic;
    Sound btnClickSound;
    Skin defaultSkin;

    public MainMenuScreen (final PuzzleGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        btnClickSound = Gdx.audio.newSound(Gdx.files.internal("button_click.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("ZEDD x VALORANT Music Theme.ogg"));
        backgroundMusic.setLooping(true);

        texture = new Texture(Gdx.files.internal("valorant_wallpaper_yoru.jpg"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = game.font;
        textButtonStyle.overFontColor = Color.RED;
        this.defaultSkin = new Skin(Gdx.files.internal("uiskin.json"));
        btnStart = new TextButton("Start Game", textButtonStyle);
        btnOption = new TextButton("Option", textButtonStyle);
        btnExit = new TextButton("Exit Game", textButtonStyle);

        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
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

        btnOption.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                btnClickSound.play();
                loadOptions();
            }
        });

        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
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

        menuTable = new Table();
        menuTable.add(btnStart);
        menuTable.row();
        menuTable.add(btnOption);
        menuTable.row();
        menuTable.add(btnExit);
        menuTable.setFillParent(true);
        stage.addActor(menuTable);
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
//        game.font.draw(game.batch, "Welcome to the Puzzle Game!", 100, 150);

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
        menuTable = new Table();
        menuTable.add(resolutionWidthText);
        menuTable.add(resolutionWidth);
        menuTable.row();
        menuTable.add(resolutionHeightText);
        menuTable.add(resolutionHeight);
        menuTable.row();
        menuTable.add(apply);
        menuTable.setX((float)Gdx.graphics.getWidth() / 2);
        menuTable.setY((float)Gdx.graphics.getHeight() / 2);
        stage.addActor(menuTable);

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
        texture.dispose();
        stage.dispose();
        btnClickSound.dispose();
        backgroundMusic.dispose();
    }

}
