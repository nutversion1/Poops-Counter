package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.poopscounter.util.Asset;

public abstract class EditWindow extends Table {
    private BaseActor blackBackground;

    protected ImageButton closeButton;
    protected ImageButton decreaseButton;
    protected ImageButton increaseButton;
    protected ImageButton resetButton;

    protected BaseActor punctuateLine;

    protected Label valueLabel;
    protected Label descriptionLabel;

    protected String tag;

    protected boolean isAnimating;

    public EditWindow(){
        //
        pack();

        //
        setSize(480, 800);
        setOrigin(getWidth()/2, getHeight()/2);
        setTransform(true);

        //
        blackBackground = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/black"));
        blackBackground.setSize(getWidth(), getHeight());
        blackBackground.addAction(Actions.alpha(0.7f));
        addActor(blackBackground);

        //
        BaseActor background = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("main_menu/edit_window"));
        background.setPosition(getWidth()/2 - background.getWidth()/2, getHeight()/2 - background.getHeight()/2);
        addActor(background);

        //
        punctuateLine = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("main_menu/punctuate_line"));
        punctuateLine.setPosition(getWidth()/2 - punctuateLine.getWidth()/2, getHeight()/2 - punctuateLine.getHeight()/2 - 50);
        addActor(punctuateLine);

        //
        decreaseButton = new ImageButton(Asset.instance.skin.gameUISkin, "decrease_edit");
        decreaseButton.setPosition((getWidth()/2 - 100) - decreaseButton.getWidth()/2, getHeight()/2 - 10);
        addActor(decreaseButton);
        //decreaseButton.setDebug(true);


        decreaseButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(!decreaseButton.isDisabled()) {
                    AudioManager.instance.playSound(Asset.instance.sound.digitalButton);
                }
                return true;
            }
        });

        //
        increaseButton = new ImageButton(Asset.instance.skin.gameUISkin, "increase_edit");
        increaseButton.setPosition((getWidth()/2 + 100) - increaseButton.getWidth()/2, getHeight()/2 -10);
        addActor(increaseButton);
        //increaseButton.setDebug(true);


        increaseButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(!increaseButton.isDisabled()) {
                    AudioManager.instance.playSound(Asset.instance.sound.digitalButton);
                }
                return true;
            }
        });

        //
        resetButton = new ImageButton(Asset.instance.skin.gameUISkin, "reset_edit");
        resetButton.setPosition(130, 180);
        addActor(resetButton);

        resetButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.mainButton);
                return true;
            }
        });

        //
        closeButton = new ImageButton(Asset.instance.skin.gameUISkin, "close_edit");
        closeButton.setPosition(305, 180);
        addActor(closeButton);

        closeButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.mainButton);
                return true;
            }
        });

        //
        valueLabel = new Label("", Asset.instance.skin.gameUISkin, "white_rabbit_40");
        valueLabel.setSize(160, 50);
        valueLabel.setColor(0, 265, 255, 1);
        valueLabel.setPosition((getWidth()/2) - valueLabel.getWidth()/2, getHeight()/2 - 5);
        valueLabel.setAlignment(Align.center);
        valueLabel.setWrap(true);
        addActor(valueLabel);
        //valueLabel.debug();

        //
        descriptionLabel = new Label("", Asset.instance.skin.gameUISkin, "white_rabbit_20");
        descriptionLabel.setSize(290, 50);
        descriptionLabel.setColor(0, 255, 255, 1);
        descriptionLabel.setPosition((getWidth()/2 ) - descriptionLabel.getWidth()/2, getHeight()/2 - 105);
        descriptionLabel.setAlignment(Align.center);
        descriptionLabel.setWrap(true);
        addActor(descriptionLabel);
        //descriptionLabel.debug();

        //
        show();


    }



    public void decrease(){

    }

    public void increase(){

    }

    public void reset(){

    }

    private void show(){
        isAnimating = true;
        setButtonTouchable(Touchable.disabled);

        addAction(Actions.sequence(
                Actions.parallel(Actions.alpha(0)), Actions.parallel(Actions.alpha(1, 0.3f)),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        isAnimating = false;
                        setButtonTouchable(Touchable.enabled);

                        return true;
                    }
                }
                )
        );
    }

    public void close(){
        isAnimating = true;
        setButtonTouchable(Touchable.disabled);

        addAction(Actions.sequence(
                Actions.parallel(Actions.alpha(0, 0.3f)),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        isAnimating = false;
                        remove();
                        doCloseEvent();
                        return true;
                    }
                }));
    }

    public void doCloseEvent(){

    }

    private void setButtonTouchable(Touchable touchable){
        decreaseButton.setTouchable(touchable);
        increaseButton.setTouchable(touchable);
        resetButton.setTouchable(touchable);
        closeButton.setTouchable(touchable);
    }

    public void setDescription(String description){
        descriptionLabel.setText(description);
    }

    public ImageButton getCloseButton(){
        return closeButton;
    }

    public ImageButton getDecreaseButton(){
        return decreaseButton;
    }

    public ImageButton getIncreaseButton(){
        return increaseButton;
    }

    public ImageButton getResetButton(){
        return resetButton;
    }

    public Label getValueLabel(){
        return valueLabel;
    }

    public Label getDescriptionLabel(){
        return descriptionLabel;
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    public String getTag(){
        return tag;
    }

    public boolean isAnimating(){
        return isAnimating;
    }
}
