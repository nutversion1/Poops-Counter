package com.nutslaboratory.nutengine.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nutslaboratory.nutengine.game.BaseGame;

;

public abstract class BaseScreen implements Screen, InputProcessor {
    private BaseGame game;

    private Stage stage;

    private boolean paused;

    private Color backgroundColor;

    private SpriteBatch debugBatch;
    private BitmapFont debugBitmapFont;

    protected Vector2 touchScreenPos;
    protected Vector2 touchStagePos;

    public BaseScreen(BaseGame game){
        this.game = game;

        stage = new Stage(new StretchViewport(game.getWorldWidth(), game.getWorldHeight()));

        paused = false;

        backgroundColor = Color.BLACK;

        touchScreenPos = new Vector2();
        touchStagePos = new Vector2();

        Gdx.input.setInputProcessor(new InputMultiplexer(this, stage));
        Gdx.input.setCatchBackKey(true);

        //debug mode
        if(getGame().getDebugMode()){
            debugBatch = new SpriteBatch();

            debugBitmapFont = new BitmapFont();
            debugBitmapFont.setColor(Color.RED);
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(!paused) {
            update(delta);
        }

        if(getGame().getDebugMode()){
            ((SpriteBatch)stage.getBatch()).totalRenderCalls = 0;
        }

        draw();

        if(getGame().getDebugMode()){
            debugBatch.begin();
            debugBitmapFont.draw(debugBatch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10,15);
            debugBitmapFont.draw(debugBatch, "Render Calls: "+((SpriteBatch)stage.getBatch()).totalRenderCalls, 100,15);
            debugBatch.end();

            //Gdx.app.log("test", ((SpriteBatch) stage.getBatch()).totalRenderCalls+"");
            //Gdx.app.log("test", Gdx.graphics.getFramesPerSecond()+"");
            //Gdx.app.log("test", "stage's actor size: "+stage.getActors().size);
            //Gdx.app.log("", ""+Gdx.app.getJavaHeap());
            //Gdx.app.log("", ""+Gdx.app.getNativeHeap());
        }


    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        //Gdx.app.log("", "resize " + width +","+height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();

        if(debugBatch != null){
            debugBatch.dispose();
        }
        if(debugBitmapFont != null){
            debugBitmapFont.dispose();
        }

        //Gdx.app.log("", "screen dispose");
    }

    public void update(float delta){
        stage.act(delta);
    }

    public void draw(){
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    public BaseGame getGame(){
        return game;
    }

    public Stage getStage(){
        return stage;
    }

    public void clearStage(){
        if(stage != null){
            stage.clear();
        }
    }

    public void addActorToStage(Actor actor){
        stage.addActor(actor);
    }

    public boolean isPaused(){
        return paused;
    }

    public void setPaused(boolean b)
    {
        paused = b;
    }

    public void togglePaused()
    {
        paused = !paused;
    }

    public void setBackgroundColor(Color color){
        backgroundColor = color;
    }

    public void pressBack(){

    }

    public InputProcessor getInputProcessor(){
        return new InputMultiplexer(this, stage);
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.BACK || keycode == Input.Keys.SPACE){
            pressBack();
        }
        else if(keycode == Input.Keys.ENTER) {
            OrthographicCamera camera = (OrthographicCamera) getStage().getCamera();

            if (camera.zoom == 1.0f) {
                camera.zoom = 2.0f;
            } else {
                camera.zoom = 1.0f;
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchScreenPos.set(screenX, screenY);
        touchStagePos = getStage().screenToStageCoordinates(touchScreenPos);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
