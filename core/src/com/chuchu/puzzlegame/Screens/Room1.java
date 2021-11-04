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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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

public class Room1 implements Screen {

    //region Variables
    private static boolean moveDown;
    public static final short DEFAULT_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short DOOR_BIT = 4;
    public static final short DESTROYED_BIT = 8;

    public static boolean showDialogue;
    public static boolean showTape;
    public static Music tape_player;
    public static float timer;
    public static boolean timerBool;
    public static Label timerLabel;
    public static boolean movable;
    public static boolean switchable;
    private int who_counter = 0;
    private final Music who_are_you;
    private static Music who_are_you_bgm;
    private final Music who_are_you_spam;

    static PuzzleGame game = null;
    TextureAtlas atlas;
    Music backgroundMusic;
    Sound okSound;
    Sound exitSound;

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
    public static Stage stagePause;
    private final Skin skin;

    private enum State
    {
        RUN,
        PAUSE,
        RESUME
    }
    private State state;

    private static Texture transparentBGTexture;
    private static Image transparentBG;

    public static Music[] tapes;
    //endregion

    public Room1(final PuzzleGame game) {
        Room1.game = game;
        skin = new Skin(Gdx.files.internal(Files.uiskin));
        timerLabel = new Label("", skin);
        state = State.RUN;
        transparentBGTexture = new Texture(Gdx.files.internal("images/ingame-assets/transparent.png"));
        who_are_you = Gdx.audio.newMusic(Gdx.files.internal(Files.who_are_you_dialog));
        who_are_you_bgm = Gdx.audio.newMusic(Gdx.files.internal(Files.who_are_you_bgm));
        who_are_you_spam = Gdx.audio.newMusic(Gdx.files.internal(Files.who_are_you_spam));

        transparentBG = new Image(transparentBGTexture);
        tapes = new Music[3];

        moveDown = false;
        showDialogue = false;
        showTape = false;
        timer = 60;
        timerBool = false;
        movable = true;
        switchable = false;

        timerLabel.setSize(30, 30);

        timerLabel.setFontScale(2);
        timerLabel.setPosition(Gdx.graphics.getWidth() - timerLabel.getWidth() - 20, Gdx.graphics.getHeight() - timerLabel.getHeight() - 20);
        atlas = new TextureAtlas("secrets/player/Player3/Testing.pack");
        // create music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(Files.horrorMusic));

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(game.bgMusicVol);

        okSound = Gdx.audio.newSound(Gdx.files.internal(Files.ok_sound));
        exitSound = Gdx.audio.newSound(Gdx.files.internal(Files.exit_sound));

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
        stagePause = new Stage(new ScreenViewport());
        // set initial camera position
        camera.position.x = player.b2body.getPosition().x;
        camera.position.y = player.b2body.getPosition().y;
    }

    public static void setup_passwordfield(String text, final String answer, boolean enableCloseBtn) {
        Sound okSound = Gdx.audio.newSound(Gdx.files.internal(Files.ok_sound));
        okSound.play();

        movable = false;
        Gdx.input.setInputProcessor(stageTesting);

        transparentBG.setSize(PuzzleGame.defaultWidth, PuzzleGame.defaultHeight);
        transparentBG.setPosition(0, 0);
        stageTesting.addActor(transparentBG);

        final Skin skin = new Skin(Gdx.files.internal(Files.uiskin));

        TextButton.TextButtonStyle textButtonStyle;
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = PuzzleGame.font3;
        textButtonStyle.overFontColor = Color.RED;

        final TextField textBox = create_textfield(text, skin);

        final TextButton button = new TextButton("SUBMIT", textButtonStyle);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(textBox.getText().toLowerCase().trim().equals(answer)) {
                    stageTesting.clear();
                    if(!Door.first_password) {
                        Door.first_password = true;
                        String newAnswer = "meneng";
                        setup_passwordfield("Input your second password", newAnswer, true);
                    }
                    else if(!Door.second_password) {
                        Door.second_password = true;
                        String newAnswer = "c2";
                        setup_passwordfield("Input your third password", newAnswer, false);
                    }
                    else if (!Door.third_password) {
                        Door.third_password = true;
                        movable = true;
                        tiledMap.getLayers().get(5).setVisible(false);
                        tiledMap.getLayers().get(6).setVisible(true);
                        if(who_are_you_bgm.isPlaying())
                            who_are_you_bgm.stop();
                        TileObjectClickListener.doorUnlocked = true;
                        moveDown = true;
                        timerBool = false;
                        stageTesting.clear();
                    }
                } else {
                    Label wrong_password = new Label("YOU ENTERED THE WRONG PASSWORD!", skin);
                    wrong_password.setPosition(textBox.getX() - ((wrong_password.getWidth() - textBox.getWidth()) / 2), textBox.getY() + textBox.getHeight() + wrong_password.getHeight());
                    stageTesting.addActor(wrong_password);
                }
            }
        });

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        table.add(textBox).width(textBox.getWidth()).height(textBox.getHeight());
        table.row();
        table.add(button).padTop(20);

        if (enableCloseBtn) {
            final TextButton backButton = new TextButton("CLOSE", textButtonStyle);
            backButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    stageTesting.clear();
                    Gdx.input.setInputProcessor(stage);
                    movable = true;

                    Sound exitSound = Gdx.audio.newSound(Gdx.files.internal(Files.exit_sound));
                    exitSound.play();
                }
            });
            table.row();
            table.add(backButton).padTop(20);
        }
        else {
            TextButton.TextButtonStyle textButtonStyle2;
            textButtonStyle2 = new TextButton.TextButtonStyle();
            textButtonStyle2.font = PuzzleGame.font3;
            final TextButton backButton = new TextButton("ESC to close", textButtonStyle2);
            table.row();
            table.add(backButton).padTop(20);
        }

        stageTesting.addActor(table);
        showDialogue = true;

        // add enter event
        stageTesting.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Keys.ENTER) {
                    if(textBox.getText().toLowerCase().trim().equals(answer)) {
                        Sound okSound = Gdx.audio.newSound(Gdx.files.internal(Files.ok_sound));
                        okSound.play();

                        stageTesting.clear();
                        if(!Door.first_password) {
                            Door.first_password = true;
                            String newAnswer = "meneng";
                            setup_passwordfield("Input your second password", newAnswer, true);
                        }
                        else if(!Door.second_password) {
                            Door.second_password = true;
                            String newAnswer = "c2";
                            setup_passwordfield("Input your third password", newAnswer, false);
                        }
                        else if (!Door.third_password) {
                            Door.third_password = true;
                            movable = true;
                            tiledMap.getLayers().get(5).setVisible(false);
                            tiledMap.getLayers().get(6).setVisible(true);
                            if(who_are_you_bgm.isPlaying())
                                who_are_you_bgm.stop();
                            TileObjectClickListener.doorUnlocked = true;
                            moveDown = true;
                            timerBool = false;
                            stageTesting.clear();
                        }
                    } else {
                        Label wrong_password = new Label("YOU ENTERED THE WRONG PASSWORD!", skin);
                        wrong_password.setPosition(textBox.getX() - ((wrong_password.getWidth() - textBox.getWidth()) / 2), textBox.getY() + textBox.getHeight() + wrong_password.getHeight());
                        stageTesting.addActor(wrong_password);
                    }
                }
                return false;
            }
        });
    }

    private static TextField create_textfield(String text, Skin skin) {
        final TextField field = new TextField(text, skin);
        field.setSize(250, 80);
        field.setColor(255, 0, 0 , 255);
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
        field.setAlignment(Align.center);
        return field;
    }

    public static void setup_tapes() {
        Sound okSound = Gdx.audio.newSound(Gdx.files.internal(Files.ok_sound));
        okSound.play();

        int counter = 1;
        int tapeX = (Gdx.graphics.getWidth() / 2) - ((3 * 160) / 2);
        Gdx.input.setInputProcessor(stageTesting);
        transparentBG.setSize(PuzzleGame.defaultWidth, PuzzleGame.defaultHeight);
        transparentBG.setPosition(0, 0);
        stageTesting.addActor(transparentBG);

        for (int i = 0; i < 3; i++) {
            TextureRegion idleRegion = new TextureRegion(new Texture(Gdx.files.internal("images/ingame-assets/tape_" + counter + ".png")));
            TextureRegion hoverRegion = new TextureRegion(new Texture(Gdx.files.internal("images/ingame-assets/tape_" + counter + "Hover.png")));

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
            tapes[i] = Gdx.audio.newMusic(Gdx.files.internal(Files.tapeMusic[i]));
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
            stageTesting.addActor(playButton);
            stageTesting.addActor(pauseButton);
            stageTesting.addActor(tapesButton);

            tapeX += tapesButton.getWidth();
            counter++;
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
                stageTesting.clear();
                Gdx.input.setInputProcessor(stage);
                movable = true;

                Sound exitSound = Gdx.audio.newSound(Gdx.files.internal(Files.exit_sound));
                exitSound.play();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(backButton);

        stageTesting.addActor(table);

        movable = false;
    }

    private static void playTape(ImageButton button, float volume) {
        try {
            stopAllTapes();

            int tapeID = Integer.parseInt(button.getName().substring(4,5));
            tapeID -= 1;
            tapes[tapeID].setVolume(volume);
            tapes[tapeID].play();
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
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void stopAllTapes() {
        for (Music tape : tapes) {
            if (tape != null) {
                if (tape.isPlaying()) {
                    tape.stop();
                }
            }
        }
    }

    private void loadPauseMenu() {
        stagePause.clear();
        Gdx.input.setInputProcessor(stagePause);

        Table tmpTable = new Table();
        tmpTable.setPosition(Gdx.graphics.getWidth() / PuzzleGame.PPM, Gdx.graphics.getHeight() / PuzzleGame.PPM);
        tmpTable.center();
        tmpTable.setFillParent(true);

        TextButton.TextButtonStyle textButtonStyle;
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = PuzzleGame.font2;
        textButtonStyle.overFontColor = Color.RED;

        TextButton tmpButton = new TextButton("Resume", textButtonStyle);
        tmpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                state = State.RESUME;
                resume();
            }
        });
        tmpTable.add(tmpButton);
        tmpTable.row();
        tmpButton = new TextButton("Options", textButtonStyle);
        tmpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadOptions();
            }
        });
        tmpTable.add(tmpButton);
        tmpTable.row();
        tmpButton = new TextButton("Main Menu", textButtonStyle);
        tmpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.batch.setColor(Color.WHITE);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        tmpTable.add(tmpButton);
        tmpTable.row();

        stagePause.addActor(tmpTable);
    }

    private void loadOptions() {
        okSound.play();

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
                exitSound.play();
            }
        });
        close.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadPauseMenu();
                exitSound.play();
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

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            /*
            try {
                Desktop.getDesktop().open(new File("test.mp4"));
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            if (!Door.second_password) {
                switch (state) {
                    case RUN:
                    case RESUME:
                        state = State.PAUSE;
                        pause();
                        break;
                    case PAUSE:
                        state = State.RESUME;
                        resume();
                        break;
                }
            }
            else {
                Sound exitSound = Gdx.audio.newSound(Gdx.files.internal(Files.exit_sound));
                exitSound.play();

                stageTesting.clear();
                stagePause.clear();
                Gdx.input.setInputProcessor(stage);
                if (!movable) {
                    movable = true;
                    if (Door.second_password && !Door.third_password && who_counter == 0) {
                        who_are_you.play();
                        Dialogue dialog = new Dialogue("WHO ARE YOU ? WHO ARE YOU????!");
                        dialog.setup_dialogue();
                        dialog.dialogueBox().addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                stageTesting.clear();
                                Gdx.input.setInputProcessor(Room1.stage);
                                movable = true;
                                timerBool = true;
                            }
                        });
                        who_counter++;
                    }
                }
            }
        } else {
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

        //region Object Line
      //  b2dr.render(world, camera.combined);
        //endregion

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
        if(switchable) {
            Screen b = new Room2(game);
            game.setScreen(b);
            dispose();
        }
        if(timerBool) {
            timer -= Gdx.graphics.getDeltaTime();
            if(backgroundMusic.isPlaying()) {
                backgroundMusic.stop();
            }
            if(!who_are_you_bgm.isPlaying()) {
                who_are_you_bgm.play();
                System.out.println("now playing bgm");
            }

            if(timer <= 30) {
                if(!who_are_you_spam.isPlaying()) {
                    who_are_you_spam.play();
                    System.out.println("Now spamming");
                }
            }
            if(timer <= 0){
                timerBool = false;
                Gdx.input.setInputProcessor(GameOverScreen.stage);
                Screen b = new GameOverScreen(game);
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

        switch (state) {
            case PAUSE:
                stagePause.act();
                stagePause.draw();

                game.batch.begin();
                game.batch.draw(transparentBGTexture, 0, 0);
                game.batch.end();
                break;
            case RUN:
            case RESUME:
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        //stage.getViewport().update(width, height, true);
        stageTesting.getViewport().update(width, height, true);
        stagePause.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        movable = false;

        loadPauseMenu();
        stopAllTapes();

        if (backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
        if (tape_player != null){
            if (tape_player.isPlaying()) {
                tape_player.pause();
            }
        }

        okSound.play();
    }

    @Override
    public void resume() {
        stagePause.clear();
        if (stageTesting.getActors().size > 0) {
            Gdx.input.setInputProcessor(stageTesting);
        }
        else {
            Gdx.input.setInputProcessor(stage);
            movable = true;
        }

        backgroundMusic.play();
        exitSound.play();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        who_are_you.dispose();
        who_are_you_spam.dispose();
        who_are_you_bgm.dispose();
        backgroundMusic.dispose();
        tiledMap.dispose();
        tiledMapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        stage.dispose();
        stageTesting.dispose();
        stagePause.dispose();
        transparentBGTexture.dispose();
    }
}
