package com.chuchu.puzzlegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen2 implements Screen {

    final PuzzleGame game;

    Viewport viewport;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    TiledMap tiledMap;

    Texture testImg;
    Rectangle testRectangle;

    public GameScreen2(final PuzzleGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        viewport = new StretchViewport(1920, 1080, camera);

        tiledMap = new TmxMapLoader().load("tilemap/test_map0.tmx");
//        tiledMap = new TmxMapLoader().load("tilemap_test/template.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 5);

        testImg = new Texture(Gdx.files.internal("cc3.png"));

        testRectangle = new Rectangle();
        testRectangle.x = (float)(1920 / 2 - 128 / 2);
        testRectangle.y = (float)(1080 / 2 - 190 / 2);
        testRectangle.width = 128;
        testRectangle.height = 190;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        game.batch.begin();
        game.batch.draw(testImg, testRectangle.x, testRectangle.y);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            camera.translate(-16,0);
            testRectangle.x -= 16;
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)){
            camera.translate(16,0);
            testRectangle.x += 16;
        }
        if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
            camera.translate(0,16);
            testRectangle.y += 16;
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
            camera.translate(0,-16);
            testRectangle.y -= 16;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        tiledMapRenderer.dispose();
        tiledMap.dispose();
        testImg.dispose();
    }
}
