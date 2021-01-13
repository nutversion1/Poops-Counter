package com.nutslaboratory.poopscounter.object;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Disposable;
import com.nutslaboratory.nutengine.actor.AnimatedActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.poopscounter.util.Asset;
import com.nutslaboratory.poopscounter.util.GamePreferences;

public class Character extends AnimatedActor implements Disposable {
    private String name;
    private Player currentPlayer;

    private int column;
    private int row;

    public boolean isUnavailable;
    public boolean isMoving;
    public boolean isSpinning;

    private long poopSpinSoundId = -1;

    public static final float POOP_SPIN_SOUND_VOLUME = 0.3f;

    public Character(String name, Animation animation, Player currentPlayer){
        super(animation);

        this.name = name;
        this.currentPlayer = currentPlayer;

        isUnavailable = false;
        isMoving = false;
        isSpinning = false;

        setStateTime(MathUtils.random(0f, 3f));

    }

    public void unavailable(){
        isUnavailable = true;
        stopAnimation();

        addAction(Actions.sequence(
                Actions.color(Color.DARK_GRAY, 0.2f),
                Actions.delay(2f),
                Actions.color(Color.WHITE, 0.2f),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        isUnavailable = false;
                        startAnimation();
                        return true;
                    }
                }));
    }

    public void fly(int destinationXPos, int destinationYPos){
        isMoving = true;

        clearActions();
        toFront();

        addAction(Actions.sequence(
                Actions.moveTo(destinationXPos, destinationYPos, 0.3f),
                Actions.rotateBy(180, 0.1f),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        isMoving = false;
                        return true;
                    }
                }));

        AudioManager.instance.playSound(Asset.instance.sound.throwCharacter, 0.7f);
    }

    public void flyAndSpin(int destinationXPos, int destinationYPos){
        isMoving = true;
        isSpinning = true;

        clearActions();
        toFront();

        addAction(Actions.parallel(
                Actions.sequence(Actions.moveTo(destinationXPos, destinationYPos, 0.3f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        isMoving = false;
                        return true;
                    }
                }),
                Actions.sequence(Actions.repeat(15, Actions.rotateBy(180, 0.3f)), new Action() {
                    @Override
                    public boolean act(float delta) {
                        stopSpin();
                        return true;
                    }
                })));

        AudioManager.instance.playSound(Asset.instance.sound.throwCharacter, 0.7f);
        poopSpinSoundId = AudioManager.instance.playSound(Asset.instance.sound.poopSpin, POOP_SPIN_SOUND_VOLUME, true);
    }

    public void stopSpin(){
        isSpinning = false;

        clearActions();

        AudioManager.instance.stopSound(Asset.instance.sound.poopSpin, poopSpinSoundId);
        poopSpinSoundId = -1;
    }

    public String getName(){
        return  name;
    }

    public Player getCurrentPlayer(){
        return  currentPlayer;
    }

    public  void setCurrentPlayer(Player currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    public void setColumn(int column){
        this.column = column;
    }

    public int getColumn(){
        return column;
    }

    public void setRow(int row){
        this.row = row;
    }

    public int getRow(){
        return row;
    }

    public void pauseSound(){
        if(poopSpinSoundId != -1) {
            AudioManager.instance.pauseSound(Asset.instance.sound.poopSpin, poopSpinSoundId);
        }
    }

    public void resumeSound(){
        if(poopSpinSoundId != -1) {
            AudioManager.instance.resumeSound(Asset.instance.sound.poopSpin, poopSpinSoundId);
        }
    }

    public void muteSound(){
        if(poopSpinSoundId != -1) {
            AudioManager.instance.setVolumeSound(Asset.instance.sound.poopSpin, poopSpinSoundId, 0);
        }

    }

    public void unmuteSound() {
        if (poopSpinSoundId != -1) {
            AudioManager.instance.setVolumeSound(Asset.instance.sound.poopSpin, poopSpinSoundId, POOP_SPIN_SOUND_VOLUME);
        }
    }


    @Override
    public void dispose() {
        if(poopSpinSoundId != -1) {
            AudioManager.instance.stopSound(Asset.instance.sound.poopSpin, poopSpinSoundId);
            poopSpinSoundId = -1;
        }
    }
}
