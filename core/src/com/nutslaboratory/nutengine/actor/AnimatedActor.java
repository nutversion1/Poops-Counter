package com.nutslaboratory.nutengine.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedActor extends BaseActor {
    private Animation animation;
    private float stateTime;
    private boolean paused;

    public AnimatedActor(Animation animation)
    {
        super((TextureRegion) animation.getKeyFrame(0f));

        setAnimation(animation);

        paused = true;
    }

    @Override
    public void act(float delta){
        super.act(delta);

        if(animation != null) {
            if(!paused) {
                stateTime += delta;
            }

            TextureRegion texture = (TextureRegion) animation.getKeyFrame(stateTime);

            setTexture(texture);
        }
    }

    public void setAnimation(Animation animation){
        this.animation = animation;

        TextureRegion texture = (TextureRegion) animation.getKeyFrame(stateTime);
        setTexture(texture);
    }

    public Animation getAnimation(){
        return animation;
    }

    public void startAnimation(){
        paused = false;
    }

    public void stopAnimation(){
        paused = true;
    }

    public void resetAnimation(){
        stateTime = 0;
    }

    public boolean isPaused(){
        return paused;
    }

    public boolean isAnimationFinished(){
        return animation.isAnimationFinished(stateTime);
    }

    public void setStateTime(float stateTime){
        this.stateTime = stateTime;
    }

    public float getStateTime(){
        return stateTime;
    }
}
