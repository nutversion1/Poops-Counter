package com.nutslaboratory.nutengine.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.nutslaboratory.nutengine.screen.BaseScreen;
import com.nutslaboratory.nutengine.screen.transition.ScreenTransition;

public abstract class BaseGame implements ApplicationListener{
    private int worldWidth;
    private int worldHeight;

    private boolean debugMode = false;

    public BaseGame(int worldWidth, int worldHeight){
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public int getWorldWidth(){
        return worldWidth;
    }

    public int getWorldHeight(){
        return worldHeight;
    }

    private boolean init;
    private BaseScreen currScreen;
    private BaseScreen nextScreen;
    private FrameBuffer currFbo;
    private FrameBuffer nextFbo;
    private SpriteBatch batch;
    private float t;
    private ScreenTransition screenTransition;

    public void setScreen(BaseScreen screen){
        setScreen(screen, null);
    }


    public void setScreen(BaseScreen screen, ScreenTransition screenTransition) {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        if(!init){
            currFbo = new FrameBuffer(Pixmap.Format.RGB888, w, h, false);
            nextFbo = new FrameBuffer(Pixmap.Format.RGB888, w, h, false);
            batch = new SpriteBatch();
            init = true;

            Gdx.app.log("", "init");
        }

        nextScreen = screen;
        nextScreen.show();
        nextScreen.resize(w, h);
        nextScreen.render(0);
        if(currScreen != null) currScreen.pause();
        nextScreen.pause();
        Gdx.input.setInputProcessor(null);
        this.screenTransition = screenTransition;
        t = 0;

    }

    public void render(){
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f/60.0f);
        if(nextScreen == null){
            if(currScreen != null){
                currScreen.render(deltaTime);
            }

        }else{
            float duration = 0;
            if(screenTransition != null){
                duration = screenTransition.getDuration();
            }
            t = Math.min(t+deltaTime, duration);

            if(screenTransition == null || t >= duration){
                if(currScreen != null) {
                    currScreen.hide();
                }
                nextScreen.resume();
                Gdx.input.setInputProcessor(nextScreen.getInputProcessor());

                currScreen = nextScreen;
                nextScreen = null;
                screenTransition = null;
            }else{
                currFbo.begin();
                if(currScreen != null) currScreen.render(deltaTime);
                currFbo.end();

                nextFbo.begin();
                nextScreen.render(deltaTime);
                nextFbo.end();

                float alpha = t / duration;
                screenTransition.render(batch,
                        currFbo.getColorBufferTexture(),
                        nextFbo.getColorBufferTexture(),
                        alpha);


            }

        }
    }


    @Override
    public void resize(int width, int height) {
        if(currScreen != null)
            currScreen.resize(width, height);
        if(nextScreen != null)
            nextScreen.resize(width, height);

    }

    @Override
    public void pause() {
        if(currScreen != null) currScreen.pause();

    }

    @Override
    public void resume() {
        if(currScreen != null) currScreen.resume();

    }


    @Override
    public void dispose() {
        if(currScreen != null) currScreen.hide();
        if(nextScreen != null) nextScreen.hide();

        if(init){
            currFbo.dispose();
            currScreen = null;
            nextFbo.dispose();
            nextScreen = null;
            batch.dispose();
            init = false;
        }
    }

    public void setDebugMode(boolean debugMode){
        this.debugMode = debugMode;
    }

    public boolean getDebugMode(){
        return debugMode;
    }
}


