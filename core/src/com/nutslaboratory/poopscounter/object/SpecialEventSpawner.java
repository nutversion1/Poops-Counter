package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.screen.BaseScreen;
import com.nutslaboratory.poopscounter.screen.PlayScreen;
import com.nutslaboratory.poopscounter.util.Asset;

public class SpecialEventSpawner extends BaseActor {

    public enum SpecialEventSpawnerCommand{
        CREATE_SPECIAL_EVENT_BUTTON
    }

    private PlayScreen playScreen;
    private boolean isProcessing;

    public SpecialEventSpawner(PlayScreen playScreen){
        super(Asset.instance.texture.gameAtlas.findRegion("play/default"));

        this.playScreen = playScreen;

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
                break;
            case COUNTDOWN:
                //Gdx.app.log("", "countdown");
                break;
            case PLAY:
                //Gdx.app.log("", "play");
                if(playScreen.getSpecialEventButtons().size == 0 &&
                        playScreen.getPlayer1().getSpecialEventsMonitor().isClean() &&
                        playScreen.getPlayer2().getSpecialEventsMonitor().isClean()){
                    float spawnDelay = MathUtils.random(1f,8f);
                    sendCommand(SpecialEventSpawnerCommand.CREATE_SPECIAL_EVENT_BUTTON, spawnDelay);
                    //Gdx.app.log("", spawnDelay+"");
                }
                break;
            case GAMEOVER:
                //Gdx.app.log("", "gameover");

                break;
        }
    }

    private void sendCommand(final SpecialEventSpawnerCommand command, float delay){
        isProcessing = true;
        clearActions();

        addAction(Actions.sequence(
                Actions.delay(delay),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        playScreen.onNotify(command);
                        isProcessing = false;
                        clearActions();
                        //Gdx.app.log("", ""+command);
                        return true;
                    }
                })
        );
    }
}
