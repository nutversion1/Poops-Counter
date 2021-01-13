package com.nutslaboratory.nutengine.screen.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

public class ScreenTransitionSlideBoth implements ScreenTransition{

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;

    private static final ScreenTransitionSlideBoth instance = new ScreenTransitionSlideBoth();

    private float duration;
    private int direction;
    private Interpolation easing;


    public static ScreenTransitionSlideBoth init(float duration, int direction, Interpolation easing){
        instance.duration = duration;
        instance.direction = direction;
        instance.easing = easing;

        return instance;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha) {

        if(easing != null){
            alpha = easing.apply(alpha);
        }

        float currScreenWidth = currScreen.getWidth();
        float currScreenHeight = currScreen.getHeight();
        float currScreenX = 0;
        float currScreenY = 0;

        switch(direction){
            case LEFT:
                currScreenX = -currScreenWidth * alpha;
                break;
            case RIGHT:
                currScreenX = currScreenWidth * alpha;
                break;
            case UP:
                currScreenY = currScreenHeight * alpha;
                break;
            case DOWN:
                currScreenY = -currScreenHeight * alpha;
                break;
        }

        float nextScreenWidth = nextScreen.getWidth();
        float nextScreenHeight = nextScreen.getHeight();
        float nextScreenX = 0;
        float nextScreenY = 0;

        switch(direction){
            case LEFT:
                nextScreenX = -nextScreenWidth * alpha;
                nextScreenX += nextScreenWidth;
                break;
            case RIGHT:
                nextScreenX = nextScreenWidth * alpha;
                nextScreenX -= nextScreenWidth;
                break;
            case UP:
                nextScreenY = nextScreenHeight * alpha;
                nextScreenY -= nextScreenHeight;
                break;
            case DOWN:
                nextScreenY = -nextScreenHeight * alpha;
                nextScreenY += nextScreenHeight;
                break;
        }

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(currScreen, currScreenX,currScreenY,0,0,currScreenWidth,currScreenHeight,1,1,0,0,0,
                currScreen.getWidth(),currScreen.getHeight(),
                false,true);
        batch.draw(nextScreen, nextScreenX,nextScreenY,0,0,nextScreenWidth,nextScreenHeight,1,1,0,0,0,
                nextScreen.getWidth(),nextScreen.getHeight(),
                false,true);

        batch.end();

    }

}
