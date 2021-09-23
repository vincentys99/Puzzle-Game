package com.chuchu.puzzlegame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player2  {
    public Animation[] walkAnimations;
    private Texture walkRightTexture, walkLeftTexture, walkUpTexture, walkDownTexture;
    private Animation<TextureRegion> walkRight, walkLeft, walkUp, walkDown;
    private final String[] walkImages;
    private final Texture[] walkTextures;
    private SpriteBatch spriteBatch;
    private static final int FRAME_COLS = 5, FRAME_ROWS = 1;
    private float xPos, yPos;
    public Player2() {
        walkImages = new String[] {"walkRight.png", "walkLeft.png", "walkUp.png", "walkDown.png"};
        walkTextures = new Texture[] {walkRightTexture, walkLeftTexture, walkUpTexture, walkDownTexture};
        walkAnimations = new Animation[] {walkRight, walkLeft, walkUp, walkDown};
        setup_directory();
        setup_animation();

    }
    private void setup_directory() {
        int counter = 0;
        for(String imageDir: walkImages) {
            walkTextures[counter] = new Texture(Gdx.files.internal("Player/" + imageDir));
            System.out.println(imageDir);
            counter++;
        }
    }

    private void setup_animation() {
        for(int i = 0; i < 4; i ++) {
            TextureRegion[][] tmp = TextureRegion.split(walkTextures[i],
                    walkTextures[i].getWidth() / FRAME_COLS,
                    walkTextures[i].getHeight() / FRAME_ROWS);

            // Place the regions into a 1D array in the correct order, starting from the top
            // left, going across first. The Animation constructor requires a 1D array.
            TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
            int index = 0;
            for (int a = 0; a < FRAME_ROWS; a++) {
                for (int j = 0; j < FRAME_COLS; j++) {
                    walkFrames[index++] = tmp[a][j];
                }
            }
            walkAnimations[i] = new Animation<TextureRegion>(0.1f, walkFrames);

            // Instantiate a SpriteBatch for drawing and reset the elapsed animation
            // time to 0
        }

    }

}
