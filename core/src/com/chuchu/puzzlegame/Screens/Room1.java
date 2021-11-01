package com.chuchu.puzzlegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Sprites.Dialogue;
import com.chuchu.puzzlegame.Sprites.Door;
import com.chuchu.puzzlegame.Sprites.Player;
import com.chuchu.puzzlegame.Tools.Room2WorldCreator;
import com.chuchu.puzzlegame.Tools.TileObjectClickListener;
import com.chuchu.puzzlegame.Tools.WorldContactListener;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Room1 implements Screen {
    private static boolean moveDown = false;
    public static final short DEFAULT_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short DOOR_BIT = 4;
    public static final short DESTROYED_BIT = 8;

    public static Boolean showDialogue = false;
    public static Boolean showTape = false;
    public static Music tape_player;
    public static float timer = 60;
    public static Boolean timerBool = false;
    public static Label timerLabel;
    public static boolean moveable = true;
    public static boolean switchable = false;
    private int who_counter = 0;
    FileHandle logFile = Gdx.files.local("log.txt");
    ///public static Music tape_2 = Gdx.audio.newMusic(Gdx.files.internal("music/promise.mp3"));
    //public static Music tape_3 = Gdx.audio.newMusic(Gdx.files.internal("music/summer.mp3"));
    final PuzzleGame game;
    private  static PuzzleGame static_game;
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

    Player player;
    public static Stage stage;
    public static Stage stageTesting;
    private Skin skin;

    public Room1(final PuzzleGame game) {
        this.game = game;
        static_game = game;
        skin = new Skin(Gdx.files.internal(Files.uiskin));
        this.timerLabel = new Label("", skin);

        timerLabel.setSize(30, 30);

        timerLabel.setFontScale(2);
        timerLabel.setPosition(Gdx.graphics.getWidth() - timerLabel.getWidth() - 20, Gdx.graphics.getHeight() - timerLabel.getHeight() - 20);
        logFile.writeString("before", true);
        atlas = new TextureAtlas("player/Player3/Testing.pack");
        logFile.writeString("yes", true);
        // create music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(Files.horrorMusic));

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(game.bgMusicVol);
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
        player = new Player(world, atlas, tiledMap);

        world.setContactListener(new WorldContactListener());
        stageTesting = new Stage(new ScreenViewport());
        // set initial camera position
        camera.position.x = player.b2body.getPosition().x;
        camera.position.y = player.b2body.getPosition().y;
    }

    public static void setup_passwordfield(String text, final String[] passwordText) {
        Gdx.input.setInputProcessor(stageTesting);
        Image transparentBG = new Image(new Texture(Gdx.files.internal("images/ingame-assets/transparent.png")));
        transparentBG.setSize(1920, 1080);
        transparentBG.setPosition(0, 0);
        stageTesting.addActor(transparentBG);
        moveable = false;
        final Skin skin = new Skin(Gdx.files.internal(Files.uiskin));

        final TextField password = create_textfield(text, 0, 0, skin);
        TextButton enter = new TextButton("Enter", skin);
        enter.setPosition(password.getX() + password.getWidth(), password.getY());
        enter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(password.getText().equals(passwordText[0]) || password.getText().equals(passwordText[1])) {
                    stageTesting.clear();
                    if(!Door.first_password) {
                        Door.first_password = true;
                        String[] pass = {"meneng", "Meneng"};
                        setup_passwordfield("Input your second password", pass);
                    }
                    else if(!Door.second_password) {
                        Door.second_password = true;
                        String[] pass = {"C2", "c2"};
                        Room1.setup_passwordfield("Input your third password", pass);
                    }
                    else if (!Door.third_password) {
                        Door.third_password = true;
                        moveable = true;
                        tiledMap.getLayers().get(5).setVisible(false);
                        tiledMap.getLayers().get(6).setVisible(true);
                        Label bitch = new Label("DOOR IS NOW UNLOCKED SON OF A BITCH", skin);
                        TileObjectClickListener.doorUnlocked = true;
                        moveDown = true;
                        bitch.setSize(400, 400);
                        timerBool = false;
                        stageTesting.clear();
                        bitch.setPosition((Gdx.graphics.getWidth() / 2) - (bitch.getWidth() / 2),Gdx.graphics.getHeight() / 2);
                        stageTesting.addActor(bitch);
                    }
                } else {
                    Label wrong_password = new Label("YOU ENTERED THE WRONG PASSWORD!", skin);
                    wrong_password.setSize(30, 30);
                    wrong_password.setPosition(password.getX(), password.getY() + password.getHeight() + wrong_password.getHeight());
                    stageTesting.addActor(wrong_password);
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
        field.setPosition((Gdx.graphics.getWidth() / 2f) - (field.getWidth() / 2),Gdx.graphics.getHeight() / 2f);
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
        int tapeX = Gdx.graphics.getWidth() / 4;
        Gdx.input.setInputProcessor(stageTesting);
        Image transparentBG = new Image(new Texture(Gdx.files.internal("images/ingame-assets/transparent.png")));
        transparentBG.setSize(1920, 1080);
        transparentBG.setPosition(0, 0);
        stageTesting.addActor(transparentBG);

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
            final TextureRegion idlePause = new TextureRegion(new Texture(Gdx.files.internal(Files.pause_button_off)));
            final TextureRegion activePause = new TextureRegion(new Texture(Gdx.files.internal(Files.pause_button_on)));
            final TextureRegion idlePlay = new TextureRegion(new Texture(Gdx.files.internal(Files.play_button_off)));
            final TextureRegion activePlay = new TextureRegion(new Texture(Gdx.files.internal(Files.play_button_on)));

            final ImageButton.ImageButtonStyle pause_style = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle play_style = new ImageButton.ImageButtonStyle();
            pause_style.up = new TextureRegionDrawable(new TextureRegion(idlePause));
            play_style.up = new TextureRegionDrawable(new TextureRegion(idlePlay));
            ImageButton playButton = new ImageButton(play_style);
            ImageButton pauseButton = new ImageButton(pause_style);

            playButton.setSize(30, 25);
            pauseButton.setSize(30, 25);
            playButton.getImage().setScaling(Scaling.fill);

            playButton.setPosition(tapesButton.getX(), tapesButton.getY() - playButton.getHeight() - 20);
            pauseButton.setPosition(playButton.getX() + playButton.getWidth() + 20, playButton.getY());
            tape_player = Gdx.audio.newMusic(Gdx.files.internal(Files.tapeMusic[i]));
            final boolean[] is_playing = {false};
            playButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(!is_playing[0]) {
                        play_style.up = new TextureRegionDrawable(new TextureRegion(activePlay));
                        pause_style.up = new TextureRegionDrawable(new TextureRegion(idlePause));
                        tape_player.play();
                        is_playing[0] = true;
                    }

                }
            });
            pauseButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(is_playing[0]) {
                        pause_style.up = new TextureRegionDrawable(new TextureRegion(activePause));
                        play_style.up = new TextureRegionDrawable(new TextureRegion(idlePlay));
                        tape_player.stop();
                        is_playing[0] = false;
                    }
                }
            });
            stageTesting.addActor(playButton);
            stageTesting.addActor(pauseButton);
            stageTesting.addActor(tapesButton);

            tapeX += tapesButton.getWidth();
            counter++;
        }
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            /*
            try {
                Desktop.getDesktop().open(new File("test.mp4"));
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            stageTesting.clear();
            Gdx.input.setInputProcessor(stage);
            if (!moveable) {

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
                player.b2body.setLinearVelocity(x, y);
            }
        }
    }

    public void update(float delta) {
        handleInput();
        if(moveDown)
        {
            System.out.println("Shit");
            player.b2body.setLinearVelocity(player.getX(), player.getY() - 10);
            moveDown = false;
        }
        world.step(1/60f, 6, 2);

        player.update(delta);

        // camera following player2 x and y position
        float lerp = 8f;
        Vector3 position = camera.position;
        position.x += (player.b2body.getPosition().x - position.x) * lerp * delta;
        position.y += (player.b2body.getPosition().y - position.y) * lerp * delta;

        camera.update();
    }

    @Override
    public void show() {
        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(Actions.fadeOut(1f));
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
        player.draw(game.batch);
        game.batch.end();
        if(switchable) {
            Screen b = new Room2(this.game);
            game.setScreen(b);
            dispose();
        }
        if(timerBool) {
            timer -= Gdx.graphics.getDeltaTime();
            if(timer <= 0){
                timerBool = false;
                Screen b = new GameOverScreen(this.game);
                dispose();

                game.setScreen(b);
            }
            else if(timer <= 11) {
                timerLabel.setColor(255, 0, 0, 255);
            }
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
        //stage.getViewport().update(width, height, true);
        stageTesting.getViewport().update(width, height, true);
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
        stageTesting.dispose();

    }
}
