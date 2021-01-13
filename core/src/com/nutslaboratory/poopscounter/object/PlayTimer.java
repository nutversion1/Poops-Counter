package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.poopscounter.screen.PlayScreen;
import com.nutslaboratory.poopscounter.util.Asset;

public class PlayTimer extends BaseActor {

    public enum PlayTimerCommand{
        TICK
    }

    private PlayScreen playScreen;

    private Action tickAction;

    public PlayTimer(PlayScreen playScreen){
        super(Asset.instance.texture.gameAtlas.findRegion("play/default"));

        this.playScreen = playScreen;

        setVisible(false);

        //actions
        tickAction = new Action() {
            @Override
            public boolean act(float delta) {
                sendCommand(PlayTimerCommand.TICK);
                return true;
            }
        };
    }

    public void start(int total){
        addAction(Actions.repeat(total, Actions.sequence(
                Actions.delay(1f),
                tickAction
        )));
    }

    public void stop(){
        clearActions();
    }

    private void sendCommand(PlayTimerCommand command){
        playScreen.onNotify(command);

        //Gdx.app.log("", ""+command);
    }
}
