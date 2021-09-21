package com.chuchu.puzzlegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen2 implements Screen {

    final PuzzleGame game;

    OrthographicCamera camera;
    OrthogonalTiledMapRenderer renderer;
    TiledMap map;

    public GameScreen2(final PuzzleGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        map = new TmxMapLoader().load("tilemap/template.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,1/16f);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        renderer.setView(camera);
        renderer.render();
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
        renderer.dispose();
        map.dispose();
    }
}
