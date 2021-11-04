package com.chuchu.puzzlegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.PuzzleGame;
import com.chuchu.puzzlegame.Sprites.Dialogue;
import com.chuchu.puzzlegame.Sprites.Player;
import com.chuchu.puzzlegame.Tools.Room2WorldCreator;
import com.chuchu.puzzlegame.Tools.WorldContactListener;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Room2 implements Screen {
    static TextButton.TextButtonStyle textButtonStyle2;
    private static Image greeting_owner;
    private static Skin defaultSkin;
    private static boolean pauseable = true;
    private static Music greeting;
    //region Variables
    public static Boolean showDialogue = false;
    static PuzzleGame game = null;
    TextureAtlas atlas;
    Skin skin;
    static Music backgroundMusic;
    Sound okSound;
    Sound exitSound;
    Texture transparentBGTexture;

    Viewport viewport;
    OrthographicCamera camera;

    TmxMapLoader mapLoader;
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    float unitScale;

    World world;
    Box2DDebugRenderer b2dr;

    Player player;
    static boolean movable;

    static Stage stageTesting;
    Stage stagePause;
    static Stage stageTapes;
    public static Stage stage;

    public static boolean showTape;

    private enum State
    {
        RUN,
        PAUSE
    }
    private State state;

    public static Music[] tapes;
    //endregion

    public Room2(final PuzzleGame game) {
        Room2.game = game;
        defaultSkin = new Skin(Gdx.files.internal(Files.uiskin));
        greeting = Gdx.audio.newMusic(Gdx.files.internal(Files.tapeWishes.get("Henry")));

        atlas = new TextureAtlas("player/Player3/Testing.pack");
        skin = new Skin(Gdx.files.internal(Files.uiskin));

        transparentBGTexture = new Texture(Gdx.files.internal(Files.transparentBg));

        state = State.RUN;
        textButtonStyle2 = new TextButton.TextButtonStyle();
        textButtonStyle2.font = game.font;
        textButtonStyle2.overFontColor = Color.RED;

        // create music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(Files.room2Music));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(game.bgMusicVol);
        //=----

        // create sounds
        okSound = Gdx.audio.newSound(Gdx.files.internal(Files.ok_sound));
        exitSound = Gdx.audio.newSound(Gdx.files.internal(Files.exit_sound));

        // create cam to follow players
        camera = new OrthographicCamera();

        // create viewport
        viewport = new FitViewport(PuzzleGame.defaultWidth / PuzzleGame.PPM, PuzzleGame.defaultHeight / PuzzleGame.PPM, camera);

        // load tilemap and scale it based on PPM
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load(Files.HinnMap);
        unitScale = 4;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale / PuzzleGame.PPM);

        // set camera
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // create world
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        //region Stage
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        stageTesting = new Stage(new ScreenViewport());
        stagePause = new Stage(new ScreenViewport());
        stageTapes = new Stage(new ScreenViewport());
        //endregion

        // generate world elements (eg. static bodies)
        new Room2WorldCreator(world, tiledMap, unitScale, stage);

        //region Player
        player = new Player(world, atlas, tiledMap);
        movable = true;
        //endregion

        // set world contact listener
        world.setContactListener(new WorldContactListener());

        // set initial camera position
        camera.position.x = player.b2body.getPosition().x;
        camera.position.y = player.b2body.getPosition().y;

        //region Tapes
        tapes = new Music[4];
        int i = 0;
        /*
        for (Music music : tapes) {
            music = Gdx.audio.newMusic(Gdx.files.internal(Files.tapeWishes[i]));
            i++;
        }*/
        showTape = false;
        //endregion
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    private void loadPauseMenu() {
        stagePause.clear();

        Gdx.input.setInputProcessor(stagePause);

        Table table = new Table();
        table.setPosition(Gdx.graphics.getWidth() / PuzzleGame.PPM, Gdx.graphics.getHeight() / PuzzleGame.PPM);
        table.center();
        table.setFillParent(true);

        TextButton.TextButtonStyle textButtonStyle;
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = PuzzleGame.font2;
        textButtonStyle.overFontColor = Color.RED;

        //region Buttons
        TextButton button = new TextButton("Resume", textButtonStyle);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resume();
            }
        });
        table.add(button);
        table.row();

        button = new TextButton("Options", textButtonStyle);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadOptions();
            }
        });
        table.add(button);
        table.row();

        button = new TextButton("Main Menu", textButtonStyle);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.batch.setColor(Color.WHITE);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table.add(button);
        table.row();
        //endregion

        stagePause.addActor(table);
    }

    private void hidePauseMenu() {
        stagePause.clear();

        Gdx.input.setInputProcessor(stage);
    }

    private void loadOptions() {
        stagePause.clear();
        Gdx.input.setInputProcessor(stagePause);

        Table table = new Table();
        table.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        table.center();

        final TextField resolutionWidth = new TextField(String.valueOf(Gdx.graphics.getWidth()), skin);
        final TextField resolutionHeight = new TextField(String.valueOf(Gdx.graphics.getHeight()), skin);
        final Slider volume = new Slider(0, 100, 5, false, skin);
        final Label volumeValueText = new Label(String.valueOf(game.bgMusicVol * 100), skin);
        final TextButton btnToggleFullscreen = new TextButton((Gdx.graphics.isFullscreen()) ? "OFF" : "ON", skin);
        volume.setValue(game.bgMusicVol * 100);
        TextButton apply = new TextButton("Apply", skin);
        TextButton close = new TextButton("Close", skin);
        apply.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (btnToggleFullscreen.getText() == "ON"){
                    Gdx.graphics.setWindowedMode(Integer.parseInt(resolutionWidth.getText()), Integer.parseInt(resolutionHeight.getText()));
                }
                loadPauseMenu();
            }
        });
        close.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadPauseMenu();
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
        Label resolutionWidthText = new Label("Width", skin);
        Label resolutionHeightText = new Label("Height", skin);
        Label volumeText = new Label("Volume", skin);
        Label fullscreenText = new Label("Fullscreen", skin);
        table.add(resolutionWidthText);
        table.add(resolutionWidth);
        table.row();
        table.add(resolutionHeightText);
        table.add(resolutionHeight);
        table.row();
        table.add(volumeText);
        table.add(volume);
        table.add(volumeValueText);
        table.row();
        table.add(fullscreenText);
        table.add(btnToggleFullscreen);
        table.row();
        table.add(apply);
        table.add(close);

        stagePause.addActor(table);
    }
    /*
    public static void SetupTapes() {
        Sound okSound = Gdx.audio.newSound(Gdx.files.internal(Files.ok_sound));
        okSound.play();

        int tapeX = (Gdx.graphics.getWidth() / 2) - ((4 * 160) / 2);
        Gdx.input.setInputProcessor(stageTapes);

        Texture transparentBGTexture = new Texture(Gdx.files.internal("images/ingame-assets/transparent.png"));
        Image transparentBG = new Image(transparentBGTexture);
        transparentBG.setSize(PuzzleGame.defaultWidth, PuzzleGame.defaultHeight);
        transparentBG.setPosition(0, 0);
        stageTapes.addActor(transparentBG);

        for (int i = 0; i < 4; i++) {
            int id = i + 1;
            if (i + 1 > 3) {
                id = i + 1 - 3;
            }

            TextureRegion idleRegion = new TextureRegion(new Texture(Gdx.files.internal("images/ingame-assets/tape_" + Integer.toString(id) + ".png")));
            TextureRegion hoverRegion = new TextureRegion(new Texture(Gdx.files.internal("images/ingame-assets/tape_" + Integer.toString(id) + "Hover.png")));

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
            final ImageButton playButton = new ImageButton(play_style);
            ImageButton pauseButton = new ImageButton(pause_style);

            playButton.setSize(30, 25);
            pauseButton.setSize(30, 25);
            playButton.getImage().setScaling(Scaling.fill);

            playButton.setPosition(tapesButton.getX(), tapesButton.getY() - playButton.getHeight() - 20);
            pauseButton.setPosition(playButton.getX() + playButton.getWidth() + 20, playButton.getY());
            tapes[i] = Gdx.audio.newMusic(Gdx.files.internal(Files.tapeWishes[i]));
            playButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    play_style.up = new TextureRegionDrawable(new TextureRegion(activePlay));
                    pause_style.up = new TextureRegionDrawable(new TextureRegion(idlePause));

                    playTape(playButton, game.bgMusicVol);
                }
            });
            playButton.setName("tape" + (i+1));
            pauseButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    pause_style.up = new TextureRegionDrawable(new TextureRegion(activePause));
                    play_style.up = new TextureRegionDrawable(new TextureRegion(idlePlay));

                    stopTape(playButton);
                }
            });
            stageTapes.addActor(playButton);
            stageTapes.addActor(pauseButton);
            stageTapes.addActor(tapesButton);

            tapeX += tapesButton.getWidth();
        }

        TextButton.TextButtonStyle textButtonStyle;
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = PuzzleGame.font3;
        textButtonStyle.overFontColor = Color.RED;

        TextButton backButton = new TextButton("CLOSE", textButtonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopAllTapes();
                stageTapes.clear();
                Gdx.input.setInputProcessor(stage);

                Sound exitSound = Gdx.audio.newSound(Gdx.files.internal(Files.exit_sound));
                exitSound.play();

                backgroundMusic.play();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(backButton);

        stageTapes.addActor(table);
    }*/
    public static void playTape_(String tape) {
        movable = false;
        TextureRegion owner  = new TextureRegion(new Texture(Gdx.files.internal( Files.tapeOwners.get(tape))));
        greeting_owner = new Image(owner);
        System.out.println(Gdx.graphics.getWidth());
        System.out.println(Gdx.graphics.getHeight());
        greeting_owner.setSize((Gdx.graphics.getWidth() * 20) / 100, (Gdx.graphics.getHeight() * 67) / 100);
        greeting_owner.setPosition(Gdx.graphics.getWidth() - greeting_owner.getWidth(), Gdx.graphics.getHeight() - greeting_owner.getHeight());
        greeting = Gdx.audio.newMusic(Gdx.files.internal(Files.tapeWishes.get(tape)));
        greeting.play();
        showTape = true;
        stageTapes.addActor(greeting_owner);
        Gdx.input.setInputProcessor(stageTapes);


    }
    public static void end_party() {
        pauseable = false;
        movable = false;
        TextButton yes = new TextButton("Yes", textButtonStyle2);
        TextButton no = new TextButton("No", textButtonStyle2);
        yes.setPosition(Gdx.graphics.getWidth() / 2 - yes.getWidth(), Gdx.graphics.getHeight() / 2);
        no.setPosition(Gdx.graphics.getWidth() / 2 + no.getWidth(), Gdx.graphics.getHeight() / 2);
        yes.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked yes");
                try {

                    Desktop.getDesktop().open(new File("test.mp4"));
                    Gdx.app.exit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        no.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(stage);
                movable = true;
                pauseable = true;
                stageTesting.clear();
                stageTapes.clear();
            }
        });
        Image dialogueBox = new Image(new Texture(Gdx.files.internal(Files.dialogImg)));
        dialogueBox.setSize(700, 200);
        dialogueBox.setPosition((Gdx.graphics.getWidth() / 2f) - (dialogueBox.getWidth() / 2),0);
        TypingLabel dialogue = new TypingLabel("Do you want to end the party?", defaultSkin);
        dialogue.setPosition(dialogueBox.getX() + 22, dialogueBox.getHeight() - 100);
        showDialogue = true;

        stageTesting.addActor(dialogueBox);
        stageTesting.addActor(dialogue);
        stageTesting.addActor(yes);
        stageTesting.addActor(no);
        Gdx.input.setInputProcessor(stageTesting);


    }
    private static void playTape(ImageButton button, float volume) {
        try {
            stopAllTapes();

            int tapeID = Integer.parseInt(button.getName().substring(4,5));
            tapeID -= 1;
            tapes[tapeID].setVolume(volume);
            tapes[tapeID].play();

            if (backgroundMusic.isPlaying()) {
                backgroundMusic.pause();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void stopTape(ImageButton button) {
        try {
            int tapeID = Integer.parseInt(button.getName().substring(4,5));
            tapeID -= 1;
            tapes[tapeID].stop();

            backgroundMusic.play();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void stopAllTapes() {
        backgroundMusic.pause();

        for (Music tape : tapes) {
            if (tape != null) {
                if (tape.isPlaying()) {
                    tape.stop();
                }
            }
        }
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && pauseable) {
            if(!movable) {
                movable = true;
                greeting.stop();
                stageTapes.clear();
                showTape = false;
                Gdx.input.setInputProcessor(stage);

            } else {
                switch (state) {
                    case RUN:
                        pause();
                        break;
                    case PAUSE:
                        resume();
                        break;
                }
            }
        }
        else {
            if (movable) {
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

        world.step(1/60f, 6, 2);

        player.update(delta);

        // camera following player2 x and y position
        camera.position.x = player.b2body.getPosition().x;
        camera.position.y = player.b2body.getPosition().y;

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

        //region Object Line
        //b2dr.render(world, camera.combined);
        //endregion

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
        if(showDialogue) {
            stageTesting.act();
            stageTesting.draw();
        }

        stage.act();
        stage.draw();

        if (!movable && pauseable) {
            stagePause.act();
            stagePause.draw();

            game.batch.begin();
            game.batch.draw(transparentBGTexture, 0, 0);
            game.batch.end();
        }

        if (showTape) {
            stageTapes.act();
            stageTapes.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stageTesting.getViewport().update(width, height, true);
        stagePause.getViewport().update(width, height, true);
    }
    @Override
    public void pause() {
        movable = false;
        state = State.PAUSE;

        if (backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
        exitSound.play();

        loadPauseMenu();
        stopAllTapes();
    }

    @Override
    public void resume() {
        movable = true;
        state = State.RUN;

        backgroundMusic.play();
        okSound.play();

        hidePauseMenu();
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
        stagePause.dispose();
    }
}