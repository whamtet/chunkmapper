package com.chunkmapper.ui;

import java.util.Map;

public class NewGameInfo {

    public static enum Difficulty {Peaceful, Easy, Normal, Hard}
    public static enum GameMode {Survival_Mode, Creative_Mode, Hardcore_Mode};

    public final String gameName;

    public final boolean hasCheats, isGaia;
    public final Difficulty difficulty;
    public final GameMode gameMode;

    public NewGameInfo(String gameName, Map<String, String> options) {
        this.gameName = gameName;
        hasCheats = !("false".equals(options.get("cheats")));
        isGaia = "true".equals(options.get("gaia"));
        switch(options.get("difficulty")) {
            case "easy":
                difficulty = Difficulty.Easy;
                break;
            case "normal":
                difficulty = Difficulty.Normal;
                break;
            case "hard":
                difficulty = Difficulty.Hard;
                break;
            default:
                difficulty = Difficulty.Peaceful;
        }
        switch(options.get("gameMode")) {
            case "survival":
                gameMode = GameMode.Survival_Mode;
                break;
            case "hardcore":
                gameMode = GameMode.Hardcore_Mode;
                break;
            default:
                gameMode = GameMode.Creative_Mode;
        }
    }
    public NewGameInfo(String gameName) {
        this.gameName = gameName;
        hasCheats = true;
        isGaia = false;
        difficulty = Difficulty.Peaceful;
        gameMode = GameMode.Creative_Mode;
    }
}
