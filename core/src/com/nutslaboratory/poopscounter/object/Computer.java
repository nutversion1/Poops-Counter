package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.poopscounter.screen.PlayScreen;
import com.nutslaboratory.poopscounter.util.Asset;

public class Computer extends BaseActor{
    public enum ComputerCommand{
        TOUCH_READY_BUTTON,
        TOUCH_REMATCH_BUTTON,
        TOUCH_CHARACTER,
        TOUCH_SPECIAL_EVENT_BUTTON
    }

    private PlayScreen playScreen;
    private boolean isProcessing;

    private Player controlledPlayer;
    private Player opponentPlayer;

    private boolean hasTouchedCharacter = false;

    public Computer(PlayScreen playScreen, Player controlledPlayer){
        super(Asset.instance.texture.gameAtlas.findRegion("play/default"));

        this.playScreen = playScreen;
        this.controlledPlayer = controlledPlayer;

        if(controlledPlayer == playScreen.getPlayer1()){
            opponentPlayer = playScreen.getPlayer2();
        }else{
            opponentPlayer = playScreen.getPlayer1();
        }

        setVisible(false);
    }

    @Override
    public void act(float delta){
        super.act(delta);

        if(isProcessing){
            return;
        }


        switch (playScreen.getcurrentState()){
            case READY:
                //Gdx.app.log("", "ready");
                if(canTouchReadyButton()) {
                    touchReadyButton();
                }
                break;
            case COUNTDOWN:
                //Gdx.app.log("", "countdown");
                break;
            case PLAY:
                //Gdx.app.log("", "play");
                if(canTouchSpecialEventButton()){
                    touchSpecialEventButton();
                }else{
                    touchCharacter();
                }
                break;
            case GAMEOVER:
                //Gdx.app.log("", "gameover");
                if(canTouchRematchButton()){
                    touchRematchButton();
                }
                break;
        }
    }

    private void sendCommand(final ComputerCommand command, float delay){
        isProcessing = true;
        clearActions();

        addAction(Actions.sequence(
                Actions.delay(delay),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        playScreen.onNotify(command, Computer.this);
                        isProcessing = false;
                        clearActions();
                        //Gdx.app.log("", ""+command);
                        return true;
                    }
                })
        );
    }


    private void touchReadyButton(){
        sendCommand(ComputerCommand.TOUCH_READY_BUTTON, 1f);
    }

    private void touchRematchButton(){
        sendCommand(ComputerCommand.TOUCH_REMATCH_BUTTON, 1f);
    }

    private void touchCharacter(){
        //set default delay
        float defaultDelay = MathUtils.random(0.7f, 0.8f);

        //set amount delay
        float amountDelay = controlledPlayer.getTotalCharacter() * 0.01f;

        //set numb delay
        float numbDelay = 0f;
        if(hasTouchedCharacter){
            if (MathUtils.random(1, 100) < 50) {
                numbDelay = MathUtils.random(0.04f, 0.08f);
        }
        }else{
            numbDelay = MathUtils.random(-0.20f, -0.30f);
        }

        //set special event delay
        float specialEventDelay = 0f;

        if (controlledPlayer.getCurrentSpecialEvent() != null) {
            switch (controlledPlayer.getCurrentSpecialEvent()) {
                case SHIELD:
                    specialEventDelay = 0f;
                    break;
                case HINT:
                    specialEventDelay = MathUtils.random(-0.07f, -0.10f);
                    break;
                case SPIN:
                    specialEventDelay = 0f;
                    break;
                case FLIP:
                    specialEventDelay = MathUtils.random(0.07f, 0.10f);
                    break;
                case LOCK:
                    specialEventDelay = 0f;
                    break;
                case FOG:
                    specialEventDelay = MathUtils.random(0.07f, 0.10f);
                    break;
                case MUD:
                    specialEventDelay = MathUtils.random(0.07f, 0.10f);
                    break;
                case BUG:
                    specialEventDelay = MathUtils.random(0.07f, 0.10f);
                    break;
            }
        }
        //Gdx.app.log("", "special: " +specialEventDelay);

        //set total delay
        float totalDelay = defaultDelay + amountDelay + numbDelay + specialEventDelay;
        //set unstable delay
        float unstableDelay = 0.1f;
        //set min delay
        float minDelay = totalDelay - unstableDelay;
        //set max delay
        float maxDelay = totalDelay + unstableDelay;
        //set result delay
        float delay = MathUtils.random(minDelay, maxDelay);

        //notify
        sendCommand(ComputerCommand.TOUCH_CHARACTER, delay);

        /*
        Gdx.app.log("", "default: "+defaultDelay + ", amount: "+amountDelay + ", numb: "+numbDelay + ", special: " +specialEventDelay);
        Gdx.app.log("", "total: "+totalDelay + ", unstable: "+unstableDelay + ", min: " +minDelay + ", max: " +maxDelay + ", delay: " +delay);
        Gdx.app.log("", "");*/

        //
        if(!hasTouchedCharacter){
            hasTouchedCharacter = true;
        }


    }

    private void touchSpecialEventButton(){
        float delay = MathUtils.random(0.2f, 0.4f);
        //Gdx.app.log("", "delay "+delay);

        sendCommand(ComputerCommand.TOUCH_SPECIAL_EVENT_BUTTON, delay);
    }

    public SpecialEventButton pickSpecialEventButton(Array<SpecialEventButton> specialEventButtons){
        SpecialEventButton selectedSpecialEventButton = null;

        Array<SpecialEventButton> copiedSpecialEventButtons = new Array<SpecialEventButton>(specialEventButtons);
        copiedSpecialEventButtons.shuffle();

        for(SpecialEventButton specialEventButton : copiedSpecialEventButtons){
            SpecialEventButton.ReceivedPlayer receivedPlayer = specialEventButton.getReceivedPlayer();
            SpecialEventManager.EffectType effectType = specialEventButton.getSpecialEvent().effectType;

            if(controlledPlayer.getPlayerNum() == 1){
                if((receivedPlayer == SpecialEventButton.ReceivedPlayer.PLAYER_1 && effectType == SpecialEventManager.EffectType.GOOD) ||
                        (receivedPlayer == SpecialEventButton.ReceivedPlayer.PLAYER_2 && effectType == SpecialEventManager.EffectType.BAD)){

                    selectedSpecialEventButton = specialEventButton;
                    break;
                }
            }else{
                if((receivedPlayer == SpecialEventButton.ReceivedPlayer.PLAYER_1 && effectType == SpecialEventManager.EffectType.BAD) ||
                        (receivedPlayer == SpecialEventButton.ReceivedPlayer.PLAYER_2 && effectType == SpecialEventManager.EffectType.GOOD)){

                    selectedSpecialEventButton = specialEventButton;
                    break;
                }
            }



        }

        return selectedSpecialEventButton;
    }

    private boolean canTouchReadyButton(){
        return !controlledPlayer.getPlayerTab().getReadyButton().isChecked();
    }

    private boolean canTouchRematchButton(){
        return !controlledPlayer.getPlayerTab().getRematchButton().isChecked();
    }

    private boolean canTouchSpecialEventButton(){
        return playScreen.getSpecialEventButtons().size > 1 && playScreen.getSpecialEventButtons().first().getState() == SpecialEventButton.State.AWAITING;
    }

    private boolean isLock(){
        return controlledPlayer.getCurrentSpecialEvent() == SpecialEventManager.SpecialEvent.LOCK;
    }

    public Player getControlledPlayer(){
        return controlledPlayer;
    }

    public Player opponentPlayer(){
        return opponentPlayer;
    }

}
