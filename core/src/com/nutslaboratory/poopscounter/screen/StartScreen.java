package com.nutslaboratory.poopscounter.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.nutengine.game.BaseGame;
import com.nutslaboratory.nutengine.screen.BaseScreen;
import com.nutslaboratory.nutengine.screen.transition.ScreenTransitionSlideBoth;
import com.nutslaboratory.poopscounter.game.PoopsCounter;
import com.nutslaboratory.poopscounter.util.Asset;
import com.nutslaboratory.poopscounter.util.GamePreferences;

public class StartScreen extends BaseScreen {
    private Table table;

    private BaseActor background;
    private ImageButton playButton;
    private ImageButton aboutButton;
    private ImageButton musicButton;
    private ImageButton soundButton;

    public StartScreen(BaseGame game){
        super(game);
    }

    @Override
    public void show(){
        init();
    }

    public void init(){
        clearStage();

        //
        background = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("start/start_background"));
        addActorToStage(background);

        //
        playButton = new ImageButton(Asset.instance.skin.gameUISkin, "play");
        playButton.setPosition(75, 390);
        addActorToStage(playButton);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((PoopsCounter)getGame()).transferScreen(new MainMenuScreen(getGame()), ScreenTransitionSlideBoth.LEFT);
            }
        });

        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.mainButton);
                return true;
            }
        });




        //
        aboutButton = new ImageButton(Asset.instance.skin.gameUISkin, "about");
        aboutButton.setPosition(240, 270);
        addActorToStage(aboutButton);

        aboutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((PoopsCounter)getGame()).transferScreen(new AboutScreen(getGame()), ScreenTransitionSlideBoth.RIGHT);
            }
        });

        aboutButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.mainButton);
                return true;
            }
        });

        //
        musicButton = new ImageButton(Asset.instance.skin.gameUISkin, "music_home");
        musicButton.setChecked(!GamePreferences.instance.music);
        musicButton.setPosition(55, 5);
        addActorToStage(musicButton);

        musicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.instance.music = !musicButton.isChecked();
                GamePreferences.instance.save();

                if(GamePreferences.instance.music){
                    AudioManager.instance.unmuteMusic();
                }else{
                    AudioManager.instance.muteMusic();
                }
            }
        });

        musicButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.electricButton);
                return true;
            }
        });

        //
        soundButton = new ImageButton(Asset.instance.skin.gameUISkin, "sound_home");
        soundButton.setChecked(!GamePreferences.instance.sound);
        soundButton.setPosition(105, 5);
        addActorToStage(soundButton);

        soundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.instance.sound = !soundButton.isChecked();
                GamePreferences.instance.save();

                if(GamePreferences.instance.sound){
                    AudioManager.instance.unmuteSound();
                }else{
                    AudioManager.instance.muteSound();
                }
            }
        });

        soundButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.electricButton, 1, false, true);
                return true;
            }
        });

    }

    @Override
    public void pressBack(){
        ((PoopsCounter)getGame()).transferScreen(new QuitScreen(getGame()), ScreenTransitionSlideBoth.UP);
    }
}
