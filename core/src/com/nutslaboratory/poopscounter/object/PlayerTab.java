package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.poopscounter.util.Asset;

public class PlayerTab extends WidgetGroup{
    private ImageButton readyButton;
    private BaseActor countdownLabel;
    private ImageButton rematchButton;
    private BaseActor endLabel;

    private BaseActor winLabel, loseLabel, drawLabel;

    public PlayerTab(){
        super();

        //
        readyButton = new ImageButton(Asset.instance.skin.gameUISkin, "ready");
        readyButton.setPosition(65,60);
        addActor(readyButton);

        readyButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.lightButton);
                return true;
            }
        });

        //
        countdownLabel = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/three_label"));
        countdownLabel.setPosition(100,70);
        countdownLabel.setVisible(false);
        addActor(countdownLabel);

        //
        endLabel = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/default"));
        endLabel.setPosition(50,30);
        endLabel.setVisible(false);
        addActor(endLabel);

        //
        rematchButton = new ImageButton(Asset.instance.skin.gameUISkin, "rematch");
        rematchButton.setPosition(129,25);
        rematchButton.setVisible(false);
        addActor(rematchButton);

        rematchButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.lightButton);
                return true;
            }
        });
    }

    public void showResult(String result){
        if(result == "win") {
            endLabel.setTexture(Asset.instance.texture.gameAtlas.findRegion("play/win_label"));
        }else if(result == "lose") {
            endLabel.setTexture(Asset.instance.texture.gameAtlas.findRegion("play/lose_label"));
        }else if(result == "draw") {
            endLabel.setTexture(Asset.instance.texture.gameAtlas.findRegion("play/draw_label"));
        }

        endLabel.setVisible(true);
    }

    public ImageButton getReadyButton(){
        return readyButton;
    }

    public BaseActor getCountdownLabel(){
        return countdownLabel;
    }

    public ImageButton getRematchButton(){
        return rematchButton;
    }

    public BaseActor getEndLabel(){
        return endLabel;
    }

    public void setCountdownLabel(String label, float delay){
        countdownLabel.setTexture(Asset.instance.texture.gameAtlas.findRegion("play/"+label+"_label"));

        countdownLabel.addAction(Actions.sequence(
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        //AudioManager.instance.play(Asset.instance.sound.countdownReady);
                        return true;
                    }
                },
                Actions.parallel(Actions.visible(false), Actions.alpha(0), Actions.scaleTo(5, 5)),
                Actions.parallel(Actions.visible(true), Actions.alpha(1, 0.5f), Actions.scaleTo(1.0f, 1.0f, 0.5f)),
                Actions.delay(delay),
                Actions.alpha(0f, 0.2f)
        ));
    }

}
