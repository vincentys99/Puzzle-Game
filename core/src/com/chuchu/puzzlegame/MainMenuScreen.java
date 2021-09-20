package com.chuchu.puzzlegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {

    final PuzzleGame game;
    OrthographicCamera camera;
    Texture texture;
    Stage stage;
    TextButton btnStart;
    TextButton btnOption;
    TextButton btnExit;
    TextButtonStyle textButtonStyle;
    Table menuTable;

    public MainMenuScreen (final PuzzleGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        texture = new Texture(Gdx.files.internal("backgroundImage.jpg"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = game.font;

        btnStart = new TextButton("Start Game", textButtonStyle);
        btnOption = new TextButton("Option", textButtonStyle);
        btnExit = new TextButton("Exit Game", textButtonStyle);

        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                dispose();
                Gdx.app.exit();
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
    }
}
