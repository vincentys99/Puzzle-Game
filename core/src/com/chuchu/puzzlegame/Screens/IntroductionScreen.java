package com.chuchu.puzzlegame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.PuzzleGame;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class IntroductionScreen implements Screen {
    private final PuzzleGame game;
    private Stage stage;
    private OrthographicCamera camera;
    private Skin defaultSkin;
    private Image dialogueBox;
    private int counter = 0;
    private Sound ok_sound;
    private final String[] lines = {"{EVENT=Start}    Once upon a time there was a young beautiful girl who lost her memory and ended up trapped in a small room.",
            "{EVENT=Start}She is very confused and doesn't know where she is.",
            "{EVENT=Start}Then she found a chest in that small room...",
            "{EVENT=Start}She asked herself 'What is inside that chest?'"};
    public IntroductionScreen(PuzzleGame game) {
        this.game = game;
        defaultSkin = new Skin(Gdx.files.internal(Files.uiskin));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, PuzzleGame.defaultWidth, PuzzleGame.defaultHeight);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        load_intro();

        ok_sound = Gdx.audio.newSound(Gdx.files.internal(Files.ok_sound));
    }
    private void load_intro() {
        create_dialogue(lines[counter], 100);
    }
    private void create_dialogue(String text, int limit) {
        final Music typing_sound = Gdx.audio.newMusic(Gdx.files.internal(Files.typingSounds));
        typing_sound.setVolume(game.bgMusicVol);
        if(text.length() >= limit)
            text = text_limiter(text, limit);
        dialogueBox = new Image(new Texture(Gdx.files.internal(Files.dialogImg)));
        dialogueBox.setSize(700, 200);
        dialogueBox.setPosition((Gdx.graphics.getWidth() / 2f) - (dialogueBox.getWidth() / 2),0);

        TypingLabel dialogue = new TypingLabel(text, defaultSkin);
        dialogue.setPosition(dialogueBox.getX() + 22, dialogueBox.getHeight() - 100);
        dialogueBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                typing_sound.stop();
                ok_sound.play((game.bgMusicVol < 0.5f) ? 0.5f : game.bgMusicVol);
                counter ++;
                stage.clear();
                if(counter <= lines.length - 1) {
                    create_dialogue(lines[counter], 100);
                }
                else
                    switchScreen(game, new Room1(game));
            }
        });
        dialogue.setTypingListener(new TypingAdapter() {
            public void event(String event) {
                typing_sound.play();
            }
            public void end() {
                typing_sound.stop();
            }
        });

        stage.addActor(dialogueBox);
        stage.addActor(dialogue);
    }
    private String text_limiter(String text, int limit) {
        int counter = 0;
        String result = "";
        for(char c: text.toCharArray()) {
            if(counter >= limit) {
                result += "\n";
                counter = 0;
            }
            result += c;
            counter += 1;
        }
        return result;
    }
    public void switchScreen(final Game game, final Screen newScreen){
        stage.getRoot().getColor().a = 1;
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.fadeOut(0.1f));
        sequenceAction.addAction(run(new Runnable() {
            @Override
            public void run() {
                game.setScreen(newScreen);
            }
        }));
        stage.getRoot().addAction(sequenceAction);
    }
    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        //game.batch.draw(texture, 0, 0);
        game.batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
//        stage.getCamera().position.set((float)Gdx.graphics.getWidth() / 2, (float)Gdx.graphics.getHeight() / 2, 0);
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void hide() {

    }
}
