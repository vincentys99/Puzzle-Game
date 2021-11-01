
package com.chuchu.puzzlegame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.PuzzleGame;
import com.badlogic.gdx.graphics.Color;


public class LoadingScreen implements Screen {
    final PuzzleGame game;
    private Stage stage;
    private Texture chuchu_logo;
    private Image background;
    private Image chibi_1;
    private Image chibi_2;
    private Label loadingText;
    private int timer = 20;
    private int loading_duration = 100;

    private float fadeTimeAlpha = 3.5f;
    private boolean rotating = false;
    private Skin skin;
    private final Music chuchu_sound = Gdx.audio.newMusic(Gdx.files.internal(Files.chuchu_logo_sounds));
    public LoadingScreen(final PuzzleGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal(Files.uiskin));

        loadingText = new Label("Loading...", skin);
        chuchu_logo = new Texture(Gdx.files.internal(Files.chuchuLogo));
        background = new Image(new Texture(Gdx.files.internal(Files.grayBackground)));
        chibi_1 = new Image(new Texture(Gdx.files.internal(Files.chibi_1)));
        chibi_2 = new Image(new Texture(Gdx.files.internal(Files.chibi_2)));

        Actor[] actors_ = {chibi_1, chibi_2};
        setup_cc(actors_);
        stage.addActor(background);
        stage.addActor(chibi_1);
        stage.addActor(chibi_2);
        stage.addActor(loadingText);

        chibi_1.setVisible(false);
        chibi_2.setVisible(false);
        loadingText.setVisible(false);
        chuchu_sound.play();
    }

    private void setup_cc(Actor[] actor) {
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Files.customFont));
        parameter = new FreeTypeFontParameter();
        parameter.size = 68;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        BitmapFont font = generator.generateFont(parameter);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        for(Actor ac: actor) {
            ac.setSize((Gdx.graphics.getWidth() * 20) / 100, (Gdx.graphics.getHeight() * 30) / 100);
            ac.setPosition(Gdx.graphics.getWidth() - ac.getWidth(), 0);
        }
        loadingText.setStyle(style);
      //  loadingText.setSize((Gdx.graphics.getWidth() * 10) / 100, (Gdx.graphics.getWidth() * 10) / 100);
        loadingText.setPosition(Gdx.graphics.getWidth() - (actor[0].getWidth() * 2), (actor[0].getHeight() * 20) / 100);


    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        fadeTimeAlpha -= Gdx.graphics.getDeltaTime();
        timer -= Gdx.graphics.getDeltaTime();

        stage.act();
        stage.draw();
        if(fadeTimeAlpha > 0f) {
            game.batch.begin();
            game.batch.setColor(1.0f, 1.0f, 1.0f, fadeTimeAlpha);
            game.batch.draw(chuchu_logo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.batch.end();
        } else {
            if(!loadingText.isVisible())
                loadingText.setVisible(true);
            if(chuchu_sound.isPlaying())
                chuchu_sound.stop();

            if (rotating) {
                loading_duration -= Gdx.graphics.getDeltaTime();
                if (timer <= 0) {
                    stage.getActors().get(1).setVisible(true);
                    stage.getActors().get(2).setVisible(false);
                    timer = 20;
                    rotating = false;
                }
            } else {
                loading_duration -= Gdx.graphics.getDeltaTime();
                if (timer <= 0) {
                    stage.getActors().get(1).setVisible(false);
                    stage.getActors().get(2).setVisible(true);

                    timer = 20;
                    rotating = true;
                }
            }
        }
        if(loading_duration <= 0) {
            this.game.setScreen(new MainMenuScreen(this.game));

            dispose();
        }


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
        chuchu_logo.dispose();
        skin.dispose();
        chuchu_sound.dispose();
        stage.dispose();
    }
}

