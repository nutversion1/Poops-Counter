package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.poopscounter.util.Asset;
import com.nutslaboratory.poopscounter.util.GamePreferences;

public class PauseMenu extends Table {
    private BaseActor blackBackground;

    private ImageButton resumeButton;
    private ImageButton restartButton;
    private ImageButton quitButton;

    private ImageButton musicButton;
    private ImageButton soundButton;



    public PauseMenu(){
        setSize(480, 800);
        setOrigin(getWidth()/2, getHeight()/2);
        setTransform(true);

        //
        blackBackground = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/black"));
        blackBackground.setSize(getWidth(), getHeight());
        blackBackground.addAction(Actions.alpha(0.7f));
        addActor(blackBackground);

        //
        BaseActor background = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/paused_window"));
        background.setPosition(getWidth()/2 - background.getWidth()/2, getHeight()/2 - background.getHeight()/2);
        addActor(background);

        //
        resumeButton = new ImageButton(Asset.instance.skin.gameUISkin, "resume");

        resumeButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.digitalButton);
                return true;
            }
        });

        //
        restartButton = new ImageButton(Asset.instance.skin.gameUISkin, "restart");

        restartButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.digitalButton);
                return true;
            }
        });

        //
        quitButton = new ImageButton(Asset.instance.skin.gameUISkin, "quit");

        quitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.digitalButton);
                return true;
            }
        });

        //
        soundButton = new ImageButton(Asset.instance.skin.gameUISkin, "sound");
        soundButton.setChecked(!GamePreferences.instance.sound);

        soundButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.digitalButton, 1, false, true);
                return true;
            }
        });

        //
        musicButton = new ImageButton(Asset.instance.skin.gameUISkin, "music");
        musicButton.setChecked(!GamePreferences.instance.music);

        musicButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.digitalButton);
                return true;
            }
        });

        //
        add(resumeButton).colspan(2).padBottom(10).padTop(89);
        row();
        add(restartButton).colspan(2).padBottom(10);
        row();
        add(quitButton).colspan(2).padBottom(40);
        row();
        add(musicButton);
        add(soundButton);
        //debug();



    }

    public void show(boolean b){
        setVisible(b);

        if(b){
            toFront();
        }
    }

    public ImageButton getResumeButton(){
        return  resumeButton;
    }

    public ImageButton getRestartButton(){
        return  restartButton;
    }

    public ImageButton getQuitButton(){
        return  quitButton;
    }

    public ImageButton getMusicButton(){
        return  musicButton;
    }

    public ImageButton getSoundButton(){
        return  soundButton;
    }


}
