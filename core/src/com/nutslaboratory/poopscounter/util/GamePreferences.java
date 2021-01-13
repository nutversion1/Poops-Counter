package com.nutslaboratory.poopscounter.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.nutslaboratory.poopscounter.object.GameplaySetup;

public class GamePreferences {
    public static final String TAG = GamePreferences.class.getName();

    public static final GamePreferences instance = new GamePreferences();

    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public boolean showFpsCounter;

    public int totalTime;
    public int totalCharacter;;
    public String specialEventMode;
    public String opponent;

    private Preferences prefs;

    private GamePreferences(){
        prefs = Gdx.app.getPreferences("gameSetting");
    }


    public void load(){
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.7f), 0.0f, 1.0f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 1.0f), 0.0f, 1.0f);
        showFpsCounter = prefs.getBoolean("showFpsCounter", false);

        totalTime = prefs.getInteger("totalTime", GameplaySetup.DEFAULT_TOTAL_TIME);
        totalCharacter = prefs.getInteger("totalCharacter", GameplaySetup.DEFAULT_TOTAL_CHARACTER);
        specialEventMode = prefs.getString("specialEventMode", GameplaySetup.DEFAULT_SPECIAL_EVENT_MODE);
        opponent = prefs.getString("opponent", GameplaySetup.DEFAULT_OPPONENT);

    }

    public void save(){
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putBoolean("showFpsCounter", showFpsCounter);

        prefs.putInteger("totalTime", totalTime);
        prefs.putInteger("totalCharacter", totalCharacter);
        prefs.putString("specialEventMode", specialEventMode);
        prefs.putString("opponent", opponent);

        prefs.flush();
    }

    public void clear(){
        prefs.clear();
        prefs.flush();
    }
}
