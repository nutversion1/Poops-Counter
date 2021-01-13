package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.poopscounter.screen.PlayScreen;
import com.nutslaboratory.poopscounter.util.Asset;

public class CountdownTimer extends BaseActor {

    public static final float READY_DELAY = 0.2f;
    public static final float GO_DELAY = 0.55f;

    public enum CountdownTimerCommand{
        COUNT_THREE,
        COUNT_TWO,
        COUNT_ONE,
        COUNT_GO,
        START
    }

    private PlayScreen playScreen;

    private Action countThreeAction;
    private Action countTwoAction;
    private Action countOneAction;
    private Action countGoAction;
    private Action startAction;

    public CountdownTimer(PlayScreen playScreen){
        super(Asset.instance.texture.gameAtlas.findRegion("play/default"));

        this.playScreen = playScreen;

        setVisible(false);

        //actions
        countThreeAction = new Action() {
            @Override
            public boolean act(float delta) {
                sendCommand(CountdownTimerCommand.COUNT_THREE);
                return true;
            }
        };

        countTwoAction = new Action() {
            @Override
            public boolean act(float delta) {
                sendCommand(CountdownTimerCommand.COUNT_TWO);
                return true;
            }
        };

        countOneAction = new Action() {
            @Override
            public boolean act(float delta) {
                sendCommand(CountdownTimerCommand.COUNT_ONE);
                return true;
            }
        };

        countGoAction = new Action() {
            @Override
            public boolean act(float delta) {
                sendCommand(CountdownTimerCommand.COUNT_GO);
                return true;
            }
        };

        startAction = new Action() {
            @Override
            public boolean act(float delta) {
                sendCommand(CountdownTimerCommand.START);
                return true;
            }
        };
    }


    private void sendCommand(CountdownTimerCommand command){
        playScreen.onNotify(command);

        //Gdx.app.log("", ""+command);
    }

    public void start(){
        addAction(Actions.sequence(
                countThreeAction,
                Actions.delay(0.8f + READY_DELAY),
                countTwoAction,
                Actions.delay(0.8f + READY_DELAY),
                countOneAction,
                Actions.delay(0.8f + READY_DELAY),
                countGoAction,
                Actions.delay(0.8f + GO_DELAY),
                startAction
        ));


    }


}
