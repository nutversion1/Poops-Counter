package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.nutslaboratory.nutengine.actor.AnimatedActor;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.poopscounter.screen.PlayScreen;
import com.nutslaboratory.poopscounter.util.Asset;

import static com.nutslaboratory.poopscounter.object.SpecialEventManager.SpecialEvent;

public class SpecialEventButton extends Table {

    public static enum ReceivedPlayer{
        BOTH, PLAYER_1, PLAYER_2
    }

    private PlayScreen playScreen;

    private ReceivedPlayer receivedPlayer;

    private SpecialEvent specialEvent;

    private ImageButton background;

    private AnimatedActor upArrow;
    private AnimatedActor downArrow;

    private float startX;
    private float middleX;
    private float endX;

    private boolean isSelect = false;

    public enum SpecialEventButtonCommand{
        SHOW, SELECT, UNSELECT, AWAIT , HIDE, DISCARD
    }

    private boolean isTimesUp = false;

    public enum State{
        SHOWING,
        SELECTING,
        UNSELECTING,
        AWAITING,
        HIDING,
        DISCARDING

    }
    private State currentState;

    private static final float MOVE_TIME = 1f;
    private static final float AWAIT_TIME = 3.2f;
    private static final float ARROW_STATE_TIME = 0.4f;

    public SpecialEventButton(SpecialEvent specialEvent, ReceivedPlayer receivedPlayer){
        this.specialEvent = specialEvent;
        this.receivedPlayer = receivedPlayer;

        setSize(60,60);
        //debug();

        //
        background = createBackground();
        addActor(background);
        background.addAction(Actions.forever(Actions.rotateBy(360, 2f)));

    }

    private ImageButton createBackground(){
        ImageButton background = new ImageButton(Asset.instance.skin.gameUISkin, "special_event_button");
        background.setSize(60,60);
        background.setOrigin(background.getWidth()/2, background.getHeight()/2);
        background.setPosition(getWidth()/2, getHeight()/2, Align.center);
        background.setTransform(true);
        background.setTouchable(Touchable.disabled);

        BaseActor eventIcon = new BaseActor(Asset.instance.skin.gameUISkin.getAtlas().findRegion(specialEvent.buttonStyle+ "_icon"));
        background.add(eventIcon);

        return background;
    }

    private AnimatedActor createDownArrow(){
        downArrow = new AnimatedActor(Asset.instance.animation.specialEventArrowDown);
        downArrow.getAnimation().setPlayMode(PlayMode.LOOP);
        downArrow.setOrigin(downArrow.getWidth() / 2, downArrow.getHeight() / 2);

        downArrow.setPosition(getX(Align.center), getY(Align.center)-42, Align.center);
        downArrow.setStateTime(ARROW_STATE_TIME);
        downArrow.startAnimation();

        return downArrow;
    }

    private AnimatedActor createUpArrow(){
        upArrow = new AnimatedActor(Asset.instance.animation.specialEventArrowUp);
        upArrow.getAnimation().setPlayMode(PlayMode.LOOP);
        upArrow.setOrigin(upArrow.getWidth() / 2, upArrow.getHeight() / 2);

        upArrow.setPosition(getX(Align.center), getY(Align.center)+42, Align.center);
        upArrow.setStateTime(ARROW_STATE_TIME);
        upArrow.startAnimation();

        return upArrow;
    }


    public void createArrows(Group layer){
        if(upArrow == null) {
            if (receivedPlayer == ReceivedPlayer.PLAYER_2 || receivedPlayer == ReceivedPlayer.BOTH) {
                upArrow = createUpArrow();
                layer.addActor(upArrow);
            }
        }
        if(downArrow == null) {
            if (receivedPlayer == ReceivedPlayer.PLAYER_1 || receivedPlayer == ReceivedPlayer.BOTH) {
                downArrow = createDownArrow();
                layer.addActor(downArrow);
            }
        }
    }

    public void removeArrows(){
        if(upArrow != null){
            upArrow.remove();
            upArrow = null;
        }
        if(downArrow != null){
            downArrow.remove();
            downArrow = null;
        }

    }

    public SpecialEvent getSpecialEvent(){
        return specialEvent;
    }

    public ReceivedPlayer getReceivedPlayer(){
        return receivedPlayer;
    }

    public void setStartX(float startX){
        this.startX =  startX;
    }

    public float getStartX(){
        return startX;
    }

    public void setMiddleX(float middleX){
        this.middleX =  middleX;
    }

    public float getMiddleX(){
        return middleX;
    }

    public void setEndX(float endX){
        this.endX =  endX;
    }

    public float getEndX(){
        return endX;
    }

    public boolean isSelect(){
        return isSelect;
    }

    public void setState(State state){
        currentState = state;
        //Gdx.app.log("", "current state: "+currentState);
    }

    public State getState(){
        return currentState;
    }

    public void setPlayScreen(PlayScreen playScreen){
        this.playScreen = playScreen;
    }

    public void showAndSelect(){
        setTouchable(Touchable.disabled);

        setState(State.SHOWING);
        sendCommand(SpecialEventButtonCommand.SHOW);

        addAction(Actions.sequence(
                Actions.moveToAligned(getMiddleX(), 800 / 2, Align.center, MOVE_TIME),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        if(isTimesUp){
                            hide();
                        }else {
                            select();
                        }
                        return true;
                    }
                }
        ));
    }


    public void showAndAwait(){
        setTouchable(Touchable.disabled);

        setState(State.SHOWING);
        sendCommand(SpecialEventButtonCommand.SHOW);

        addAction(Actions.sequence(
                Actions.moveToAligned(getMiddleX(), 800 / 2, Align.center, MOVE_TIME),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        if(isTimesUp){
                            hide();
                        }else {
                            await();
                        }
                        return true;
                    }
                }
        ));
    }

    public void select(){
        isSelect = true;
        background.setChecked(true);
        setTouchable(Touchable.disabled);
        AudioManager.instance.playSound(Asset.instance.sound.specialEventSelect);

        setState(State.SELECTING);

        //command
        sendCommand(SpecialEventButtonCommand.SELECT);

        //actions
        clearActions();

        float effectTime = getEffectTime();
        //Gdx.app.log("", ""+effectTime);

        addAction(Actions.sequence(
                Actions.delay(effectTime),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        unselect();
                        return true;
                    }
                }
        ));

    }

    public void unselect(){
        isSelect = false;
        background.setChecked(false);

        setState(State.UNSELECTING);

        //command
        sendCommand(SpecialEventButtonCommand.UNSELECT);

        //actions
        clearActions();

        addAction(Actions.sequence(
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        hide();
                        return true;
                    }
                }
        ));
    }

    public void await(){
        setTouchable(Touchable.enabled);

        setState(State.AWAITING);

        //command
        sendCommand(SpecialEventButtonCommand.AWAIT);

        //
        clearActions();

        addAction(Actions.sequence(
                Actions.delay(AWAIT_TIME),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        hide();
                        return true;
                    }
                }
        ));
    }

    public void hide(){
        setTouchable(Touchable.disabled);

        setState(State.HIDING);

        //command
        sendCommand(SpecialEventButtonCommand.HIDE);

        //actions
        clearActions();

        addAction(Actions.sequence(
                Actions.moveToAligned(getEndX(), 800 / 2, Align.center, MOVE_TIME),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        discard();
                        return true;
                    }
                }
        ));

    }

    public void discard(){
        remove();

        setState(State.DISCARDING);

        //command
        sendCommand(SpecialEventButtonCommand.DISCARD);
    }

    private void sendCommand(SpecialEventButtonCommand command){
        playScreen.onNotify(command, this);

        //Gdx.app.log("", ""+command);
    }

    public void timesUp(){
        isTimesUp = true;

        //
        if(currentState == State.SELECTING) {
            unselect();
        }
        if(currentState == State.AWAITING){
            hide();
        }
    }

    @Override
    public void act(float delta){
        super.act(delta);

        //Gdx.app.log("", currentState+"");
    }

    public float getEffectTime(){
        Vector2 effectTime = null;
        if(getReceivedPlayer() == SpecialEventButton.ReceivedPlayer.BOTH){
            effectTime = getSpecialEvent().autoEffectTime;
        }else{
            effectTime = getSpecialEvent().manualEffectTime;
        }

        float time = MathUtils.random(effectTime.x, effectTime.y);
        //Gdx.app.log("", "effect time: "+time);

        return time;

    }
}
