package com.chuchu.puzzlegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Sprites.Dialogue;
import com.chuchu.puzzlegame.Sprites.Door;
import com.chuchu.puzzlegame.Sprites.Player2;
import com.chuchu.puzzlegame.Tools.Room2WorldCreator;
import com.chuchu.puzzlegame.Tools.WorldContactListener;

import java.io.IOException;

public class Room1 implements Screen {
    public static Boolean showDialogue = false;
    public static Boolean showTape = false;
    public static Music tape_1;
    public static float timer = 60;
    public static Boolean timerBool = false;
    public static Label timerLabel;
    public static boolean moveable = true;
    private int who_counter = 0;
    FileHandle logFile = Gdx.files.local("log.txt");
    ///public static Music tape_2 = Gdx.audio.newMusic(Gdx.files.internal("music/promise.mp3"));
    //public static Music tape_3 = Gdx.audio.newMusic(Gdx.files.internal("music/summer.mp3"));
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
    public static Stage stage;
    public static Stage stageTesting;
    private Skin skin;
    public Room1(final PuzzleGame game) {
        this.game = game;
        skin = new Skin(Gdx.files.internal(Files.uiskin));
        this.timerLabel = new Label("", skin);

        timerLabel.setSize(400, 400);

        timerLabel.setFontScale(2);
        timerLabel.setPosition(Gdx.graphics.getWidth() - timerLabel.getWidth(), 0);
        atlas = new TextureAtlas("player/Player3/Testing.pack");


        // create music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(Files.horrorMusic));

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(game.bgMusicVol);

        tape_1 = Gdx.audio.newMusic(Gdx.files.internal(Files.inGameMusic));

        // create cam to follow players
        camera = new OrthographicCamera();
        // create viewport
        viewport = new FitViewport(PuzzleGame.defaultWidth / PuzzleGame.PPM, PuzzleGame.defaultHeight / PuzzleGame.PPM, camera);

        // load tilemap and scale it based on PPM
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load(Files.DemaMap);

        unitScale = 4;
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

        player2 = new Player2(world, atlas, tiledMap);

        world.setContactListener(new WorldContactListener());
        stageTesting = new Stage(new ScreenViewport());
        // set initial camera position
        camera.position.x = player2.b2body.getPosition().x;
        camera.position.y = player2.b2body.getPosition().y;
    }

    public static void setup_passwordfield(String text, final String passwordText, final String password_level) {
        Gdx.input.setInputProcessor(stageTesting);
        moveable = false;
        final Skin skin = new Skin(Gdx.files.internal(Files.uiskin));

        final TextField password = create_textfield(text, 0, 0, skin);
        TextButton enter = new TextButton("Enter", skin);
        enter.setPosition(password.getX() + password.getWidth() + 100, password.getY());
        enter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Checking if " + password.getText() + " is equals to " + passwordText);
                if(password.getText().equals(passwordText)) {
                    System.out.println("Correct password for " + passwordText + password_level);
                    if(!Door.first_password) {
                        Door.first_password = true;
                        setup_passwordfield("Input your second password", "meneng", "Level_1");
                    }
                    else if(!Door.second_password) {
                        Door.second_password = true;
                        Room1.setup_passwordfield("Input your third password", "C2", "Level_2");
                    }
                    else if (!Door.third_password) {
                        Door.third_password = true;
                        Label bitch = new Label("DOOR IS NOW UNLOCKED SON OF A BITCH", skin);
                        bitch.setSize(400, 400);
                        stageTesting.clear();
                        bitch.setPosition((Gdx.graphics.getWidth() / 2) - (bitch.getWidth() / 2),Gdx.graphics.getHeight() / 2);
                        stageTesting.addActor(bitch);
                    }
                }
            }
        });
        stageTesting.addActor(enter);
        stageTesting.addActor(password);
        showDialogue = true;
    }

    private static TextField create_textfield(String text, int x, int y, Skin skin) {

        final TextField field = new TextField(text, skin);
        field.setSize(250, 80);
        field.setColor(255, 0, 0 , 255);
        field.setPosition((Gdx.graphics.getWidth() / 2) - (field.getWidth() / 2),Gdx.graphics.getHeight() / 2);
        field.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                field.setColor(0, 255, 0 , 255);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                field.setColor(255, 0, 0 , 255);
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                field.setText("");
            }
        });
        return field;
    }
    public static void setup_tapes() {
        int counter = 1;
        int tapeX = 280;
        Gdx.input.setInputProcessor(stageTesting);
       // System.out.println(stageTesting.getWidth() + "==" + stageTesting.getHeight());
        //stageTesting.getViewport().update((int)stageTesting.getWidth(), (int)stageTesting.getHeight());
        Image transparentBG = new Image(new Texture(Gdx.files.internal("images/ingame-assets/transparent.png")));
        transparentBG.setSize(1920, 1080);
        transparentBG.setPosition(0, 0);

       // stageTesting.addActor(transparentBG);
        for (int i = 0; i < 3; i++) {
            TextureRegion idleRegion = new TextureRegion(new Texture(Gdx.files.internal("images/ingame-assets/tape_" + Integer.toString(counter) + ".png")));
            TextureRegion hoverRegion = new TextureRegion(new Texture(Gdx.files.internal("images/ingame-assets/tape_" + Integer.toString(counter) + "Hover.png")));

            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            style.imageUp = new TextureRegionDrawable(new TextureRegion(idleRegion));
            style.imageOver = new TextureRegionDrawable(new TextureRegion(hoverRegion));
            ImageButton tapesButton = new ImageButton(style);
            tapesButton.setPosition(tapeX, 300);
            tapesButton.setSize(160, 60);
            tapesButton.getImage().setScaling(Scaling.fill);
            TextureRegion idlePause = new TextureRegion(new Texture(Gdx.files.internal(Files.pause_button_off)));
            TextureRegion activePause = new TextureRegion(new Texture(Gdx.files.internal(Files.pause_button_on)));
            TextureRegion idlePlay = new TextureRegion(new Texture(Gdx.files.internal(Files.play_button_off)));
            TextureRegion activePlay = new TextureRegion(new Texture(Gdx.files.internal(Files.play_button_on)));

            ImageButton.ImageButtonStyle pause_style = new ImageButton.ImageButtonStyle();
            ImageButton.ImageButtonStyle play_style = new ImageButton.ImageButtonStyle();
            pause_style.up = new TextureRegionDrawable(new TextureRegion(idlePause));
            play_style.up = new TextureRegionDrawable(new TextureRegion(idlePlay));
            play_style.over = new TextureRegionDrawable(new TextureRegion(idlePause));
            ImageButton playButton = new ImageButton(play_style);
            // ImageButton pauseButton = new ImageButton(pause_style);

            playButton.setSize(30, 25);
            // pauseButton.setSize(30, 25);
            playButton.getImage().setScaling(Scaling.stretch);

            playButton.setPosition(tapeX, tapesButton.getY() - playButton.getHeight() - 20);
            //pauseButton.setPosition(playButton.getX() - playButton.getWidth() - 20, playButton.getY());
            tapesButton.addListener(new ClickListener() {
                boolean bol;

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    bol = !bol;
                    if (bol)
                        tape_1.play();
                    else
                        tape_1.stop();
                }
            });
            stageTesting.addActor(tapesButton);
            stageTesting.addActor(playButton);
            //stageTesting.addActor(pauseButton);
            tapeX += 180;
            counter++;
        }
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            if (!moveable) {
                stageTesting.clear();
                Gdx.input.setInputProcessor(stage);
                moveable = true;
                if (Door.second_password && !Door.third_password && who_counter == 0) {
                    System.out.println("Dildo");
                    Dialogue dialog = new Dialogue("WHO ARE YOU ? WHERE IS YOUR DILDO?!!");
                    dialog.setup_dialogue();

                    dialog.dialogueBox().addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            System.out.println("yes i clicked ur ass");
                            stageTesting.clear();
                            Gdx.input.setInputProcessor(Room1.stage);
                            moveable = true;
                            timerBool = true;
                        }
                    });
                    who_counter++;
                }
            }
            //Gdx.app.exit();
        } else {
            if (moveable) {
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
        }
    }

    public void update(float delta) {
        handleInput();

        world.step(1/60f, 6, 2);

        player2.update(delta);

        // camera following player2 x and y position
        float lerp = 8f;
        Vector3 position = camera.position;
        position.x += (player2.b2body.getPosition().x - position.x) * lerp * delta;
        position.y += (player2.b2body.getPosition().y - position.y) * lerp * delta;

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
        if(timerBool) {
            timer -= Gdx.graphics.getDeltaTime();
            timerLabel.setText(String.valueOf((int)timer));

            stageTesting.addActor(timerLabel);

            if(!showDialogue) {
                stageTesting.act();
                stageTesting.draw();
            }
        }
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
