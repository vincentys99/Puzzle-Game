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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Sprites.Player2;
import com.chuchu.puzzlegame.Tools.Room2WorldCreator;
import com.chuchu.puzzlegame.Tools.WorldContactListener;

public class Room1 implements Screen {
    public static Boolean showDialogue = false;


    final PuzzleGame game;
    TextureAtlas atlas;
    Music backgroundMusic;

    Viewport viewport;
    OrthographicCamera camera;

    TmxMapLoader mapLoader;
    public static TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    float unitScale;

    World world;
    Box2DDebugRenderer b2dr;

    Player2 player2;
    public static Stage stageTesting;
    public Stage stage;

    public Room1(final PuzzleGame game) {
        this.game = game;
        atlas = new TextureAtlas("player/Player2/Testing.pack");

        // create music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(Files.inGameMusic));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(game.bgMusicVol);

        // create cam to follow players
        camera = new OrthographicCamera();

        // create viewport
        viewport = new FitViewport(PuzzleGame.defaultWidth / PuzzleGame.PPM, PuzzleGame.defaultHeight / PuzzleGame.PPM, camera);

        // load tilemap and scale it based on PPM
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load(Files.DemaMap);
        //tiledMap.getLayers().get(2).setVisible(true);

        System.out.println(tiledMap.toString());
        unitScale = 2;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale / PuzzleGame.PPM);

        // set camera
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // create world
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        // create stage
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // generate world elements (eg. static bodies)
        new Room2WorldCreator(world, tiledMap, unitScale, stage);

        // generate player2
       // player2 = new Player2(this);
        player2 = new Player2(world, atlas, tiledMap);

        world.setContactListener(new WorldContactListener());
        stageTesting = new Stage(new ScreenViewport());


    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
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
        handleInput();

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

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        //=============================================================//
        //  the line below is used to display the lines of the objects //
        //=============================================================//
        b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player2.draw(game.batch);
        game.batch.end();
        if(showDialogue) {
            stageTesting.act();
            stageTesting.draw();
        }

        stage.act();
        stage.draw();

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
        stage.dispose();
    }
}
