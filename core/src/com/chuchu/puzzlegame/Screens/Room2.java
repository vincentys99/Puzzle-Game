package com.chuchu.puzzlegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Sprites.Player2;
import com.chuchu.puzzlegame.Tools.Room2WorldCreator;
import com.chuchu.puzzlegame.Tools.WorldContactListener;

public class Room2 implements Screen {

    final PuzzleGame game;
    TextureAtlas atlas;
    Music backgroundMusic;

    Viewport viewport;
    OrthographicCamera camera;

    TmxMapLoader mapLoader;
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    World world;
    Box2DDebugRenderer b2dr;

    Player2 player2;

    public Room2(final PuzzleGame game) {
        this.game = game;
        atlas = new TextureAtlas("Player2/Testing.pack");

        // create music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/CRSED Music Theme.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(game.bgMusicVol);

        // create cam to follow players
        camera = new OrthographicCamera();

        // create viewport
        viewport = new FitViewport(PuzzleGame.defaultWidth / PuzzleGame.PPM, PuzzleGame.defaultHeight / PuzzleGame.PPM, camera);

        // load tilemap and scale it based on PPM
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("tilemap_test2/untitled.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PuzzleGame.PPM);

        // set camera
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // create world
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        // generate world elements (eg. static bodies)
        new Room2WorldCreator(world, tiledMap);

        // generate player2
        player2 = new Player2(world, this);

        world.setContactListener(new WorldContactListener());

        // TODO: set player to one of the layers and allow the effect of "user is behind object(s)"

//        tiledMapRenderer.addSprite(player2);
//
//        MapLayer objectLayer = tiledMap.getLayers().get("Player Layer");
//        TextureMapObject tmo = new TextureMapObject(player2);
//        tmo.setX(16 / PuzzleGame.PPM);
//        tmo.setY(16 / PuzzleGame.PPM);
//        objectLayer.getObjects().add(tmo);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            // error when set main menu screen
//            dispose();
//            game.setScreen(new MainMenuScreen(game));
            Gdx.app.exit();
        }

        float x = 0f;
        float y = 0f;
        if (Gdx.input.isKeyPressed(Keys.A)) {
            x -= 2;
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            x += 2;
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            y += 2;
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            y -= 2;
        }
        player2.b2body.setLinearVelocity(x, y);
    }

    public void update(float delta) {
        handleInput(delta);

        world.step(1/60f, 6, 2);

        player2.update(delta);

        // camera following player2 x and y position
        camera.position.x = player2.b2body.getPosition().x;
        camera.position.y = player2.b2body.getPosition().y;

        camera.update();
    }

    @Override
    public void show() {
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        update(delta);

        // clear screen with black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player2.draw(game.batch);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        tiledMap.dispose();
        tiledMapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
