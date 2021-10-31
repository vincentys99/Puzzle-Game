package com.chuchu.puzzlegame.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.chuchu.puzzlegame.Global.Files;
import com.chuchu.puzzlegame.PuzzleGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Puzzle Game");
		config.setMaximized(true);
		config.setWindowSizeLimits(1024, 576, 3840, 2160);
//		config.setResizable(false);
		config.setWindowIcon(Files.applicationIcon);
		PuzzleGame puzzleg =  new PuzzleGame();
		puzzleg.setSplashWorker(new DesktopSplashWorker());
		new Lwjgl3Application(new PuzzleGame(), config);
	}
}
