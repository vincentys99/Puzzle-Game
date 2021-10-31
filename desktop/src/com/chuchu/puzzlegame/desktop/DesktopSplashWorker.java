package com.chuchu.puzzlegame.desktop;

import com.chuchu.puzzlegame.SplashWorker;

import java.awt.*;

public class DesktopSplashWorker implements SplashWorker {
    @Override
    public void closeSpashScreen() {
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            splash.close();
        }
    }
}
