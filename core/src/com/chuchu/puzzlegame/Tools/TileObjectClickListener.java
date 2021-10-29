package com.chuchu.puzzlegame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.Screens.GameOverScreen;
import com.chuchu.puzzlegame.Screens.Room1;
import com.chuchu.puzzlegame.Sprites.Dialogue;
import com.chuchu.puzzlegame.Sprites.Door;
import com.chuchu.puzzlegame.Sprites.InteractiveTileObject;
import com.chuchu.puzzlegame.Sprites.Torch;

public class TileObjectClickListener extends ClickListener {
    private final InteractiveTileObject actor;
    public static boolean doorUnlocked = false;
    private boolean passwordable = false;
    Skin skin;
    private int lock_counter = 0;
    public TileObjectClickListener(InteractiveTileObject actor) {
        super(Input.Buttons.LEFT);
        this.actor = actor;
        skin = new Skin(Gdx.files.internal(Files.uiskin));
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log(actor.getName(), "Player has clicked!");
        System.out.println("CLIKED");
        switch (actor.getName()) {
            case "Chest":
                Room1.showDialogue = true;
                Room1.setup_tapes();
                break;
            case "Torch":
                Torch.torchOn = !Torch.torchOn;
                if (Torch.torchOn) {
                    Room1.tiledMap.getLayers().get(9).setVisible(true);
                    Room1.tiledMap.getLayers().get(8).setVisible(false);
                } else {
                    Room1.tiledMap.getLayers().get(8).setVisible(true);
                    Room1.tiledMap.getLayers().get(9).setVisible(false);
                }
                break;
            case "DoorUnlocked":
            if (Room1.tiledMap.getLayers().get(6).isVisible()) {
                System.out.println("lets fucking switch");
                Room1.switchable = true;
            }
            case "Door":
                if (!doorUnlocked) {
                    if (lock_counter == 0) {
                        Dialogue dialog = new Dialogue("The door is locked.....");
                        dialog.setup_dialogue();
                        dialog.dialogueBox().addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                passwordable = true;
                                Room1.stageTesting.clear();
                                password_check();
                                lock_counter++;

                            }
                        });
                    }
                    if (passwordable) {
                        password_check();
                    }
                }
                break;
        }

    }
    private void password_check() {
            if (!Door.first_password) {
                String[] pass = {"feleng", "Feleng"};
                Room1.setup_passwordfield("Input your first password", pass);
            } else if (!Door.second_password) {
                String[] pass = {"meneng", "Meneng"};
                Room1.setup_passwordfield("Input your second password", pass);
            } else if (!Door.third_password) {
                String[] pass = {"c2", "C2"};
                Room1.setup_passwordfield("Input your third password", pass);
            }
        }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal(Files.cursorHand)), 0, 0));
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal(Files.cursorNormalSelect)), 0, 0));
    }


}
