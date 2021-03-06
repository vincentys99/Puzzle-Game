package com.chuchu.puzzlegame.Global;

import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;

public class Files {

    //----------------------//
    // Files & Folders path //
    //----------------------//

    //-------//
    // Fonts //
    //-------//

    public static final String customFont = "fonts/GrechenFuemen-Regular.ttf";
    public static final String customFont2 = "fonts/CuteFont-Regular.ttf";

    //--------//
    // Images //
    //--------//
    public static final String chuchuLogo = "images/wallpaper/chuchu.png";
    public static final String grayBackground = "images/wallpaper/gray.png";
    public static final String chibi_1 = "images/wallpaper/chibi_1.png";
    public static final String chibi_2 = "images/wallpaper/chibi_2.png";

    public static final String applicationIcon = "images/icon/icon_transparent.png";
    public static final String mainMenuBg = "images/wallpaper/code geass walpaper.jpg";
    public static final String gameOverBg = "images/wallpaper/over.jpg";
    public static final String transparentBg = "images/ingame-assets/transparent.png";

    public static final String dialogImg = "images/ingame-assets/dialog.png";

    public static final String cursorNormalSelect = "images/cursor/cursor01.png";
    public static final String cursorDialog = "images/cursor/cursor02.png";
    public static final String cursorHand = "images/cursor/cursor03.png";
    public static final String pause_button_off = "images/ingame-assets/pause_button_off.png";
    public static final String pause_button_on = "images/ingame-assets/pause_button_on.png";
    public static final String play_button_off = "images/ingame-assets/play_button_off.png";
    public static final String play_button_on = "images/ingame-assets/play_button_on.png";

    //--------//
    // Musics //
    //--------//
    public static final String mainMenuMusic = "music/Title BGM.wav";
    public static final String horrorMusic = "music/piano_horror2.mp3";
    public static final String room2Music = "music/room2-music.wav";

    public static final String[] tapeMusic = {"sound/feleng.mp3", "sound/meneng.mp3", "music/tape_3.mp3"};
    //--------//
    // Sounds //
    //--------//
    public static final Map<String, String> tapeWishes = new HashMap<String, String>() {
        {
            put("Henry", "sound/henry.mp3");
            put("Vince", "sound/vince.mp3");
            put("Shiho", "sound/shiho.mp3");
            put("Dema", "sound/dema.mp3");
            put("Hinn", "sound/hinn.mp3");
            put("Miu", "sound/miu.mp3");

        }};
    public static final Map<String, String> tapeOwners = new HashMap<String, String>() {
        {
            put("Henry", "images/ingame-assets/henry.jpg");
            put("Hinn", "images/ingame-assets/hinn.jpg");
            put("Dema", "images/ingame-assets/dema.jpg");
            put("Vince", "images/ingame-assets/vince.jpg");
            put("Shiho", "images/ingame-assets/shiho.jpg");
            put("Miu", "images/ingame-assets/miu.jpg");

        }};
    public static final String buttonClickSound = "sound/button_click.mp3";
    public static final String typingSounds = "sound/typing-sound.mp3";
    public static final String chuchu_logo_sounds = "sound/chuchu_audio.wav";
    public static final String ok_sound = "sound/OK_Audio.wav";
    public static final String exit_sound = "sound/Exit_Audio.wav";
    public static final String who_are_you_dialog = "sound/who-are-you-dialog.wav";
    public static final String who_are_you_bgm = "sound/who-are-you-timer.wav";
    public static final String who_are_you_spam = "sound/who-are-you-spam.wav";

    //---------//
    // Tilemap //
    //---------//
    public static final String room1Tilemap = "Tilesmaps/Room1 Tilemap/untitled.tmx";
    public static final String room2Tilemap = "Tilesmaps/Room2 Tilemap/untitled.tmx";
    public static final String DemaMap = "Tilesmaps/map 1/map_1.tmx";
    public static final String HinnMap = "Tilesmaps/map 2/map 2.tmx";


    //--------//
    // Player //
    //--------//
    public static final String Player3 = "player/Player3/Testing.pack"; // 24 x 48


    //--------//
    // uiskin //
    //--------//
    public static final String uiskin = "uiskin/uiskin.json";
    public static final String glassy = "glassy/glassy-ui.json";
}
