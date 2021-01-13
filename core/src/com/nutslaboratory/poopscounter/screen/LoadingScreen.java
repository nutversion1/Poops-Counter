package com.nutslaboratory.poopscounter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.nutslaboratory.nutengine.actor.AnimatedActor;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.nutengine.game.BaseGame;
import com.nutslaboratory.nutengine.screen.BaseScreen;
import com.nutslaboratory.nutengine.screen.transition.ScreenTransition;
import com.nutslaboratory.nutengine.screen.transition.ScreenTransitionSlideBoth;
import com.nutslaboratory.poopscounter.util.Asset;
import com.nutslaboratory.poopscounter.util.GamePreferences;

import static com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;


public class LoadingScreen extends BaseScreen {

    private TextureAtlas loadingTextureAtlas;

    private Sound waterSplashSound;
    private Sound changeScreenSound;

    private AnimatedActor splash;

    public LoadingScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void show(){
        init();
    }

    private void init(){
        loadingTextureAtlas = new TextureAtlas(Gdx.files.internal("images/loading_assets.atlas"));

        //
        waterSplashSound = Gdx.audio.newSound(Gdx.files.internal("sounds/water_splash.ogg"));
        changeScreenSound = Gdx.audio.newSound(Gdx.files.internal("sounds/throwing_whip_effect.ogg"));

        //
        BaseActor background = new BaseActor(loadingTextureAtlas.findRegion("loading_background"));
        addActorToStage(background);

        //
        Array<AtlasRegion> loadingLabelTextures = loadingTextureAtlas.findRegions("loading_label");
        Animation loadingLabelAnimation = new Animation(0.5f, loadingLabelTextures, Animation.PlayMode.LOOP);

        AnimatedActor loadingLabel = new AnimatedActor(loadingLabelAnimation);
        loadingLabel.setPosition(getGame().getWorldWidth()/2 - loadingLabel.getWidth()/2, getGame().getWorldHeight()/2 - loadingLabel.getHeight()/2 - 20);
        addActorToStage(loadingLabel);
        loadingLabel.startAnimation();

        //
        BaseActor water = new BaseActor(new TextureRegion(loadingTextureAtlas.findRegion("water")));
        water.setPosition(0,-30);
        addActorToStage(water);

        water.addAction(Actions.forever(Actions.sequence(
                Actions.moveTo(-480, water.getY(), 10f),
                Actions.moveTo(0, water.getY()))));

        //
        BaseActor poop = new BaseActor(loadingTextureAtlas.findRegion("m03"));
        poop.setSize(55,55);
        poop.setPosition(437, 700);
        addActorToStage(poop);


        poop.addAction(Actions.forever(Actions.sequence(
                Actions.delay(1f),
                Actions.moveTo(poop.getX(), 0, 1.5f),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        splash.startAnimation();
                        splash.resetAnimation();
                        return true;
                    }
                },
                Actions.moveTo(poop.getX(), 700),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        AudioManager.instance.playSound(waterSplashSound);
                        return true;
                    }
                },
                Actions.delay(0.8f),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        if (Asset.instance.getAssetManager().update()) {
                            finishLoading();
                        }

                        return true;
                    }
                })
        ));

        //
        Array<AtlasRegion> splashTextures = loadingTextureAtlas.findRegions("splash");
        Animation splashAnimation = new Animation(0.08f, splashTextures, Animation.PlayMode.NORMAL);

        splash = new AnimatedActor(splashAnimation);
        splash.setPosition(395,0);
        addActorToStage(splash);
        splash.setVisible(false);

        //
        BaseActor foreground = new BaseActor(loadingTextureAtlas.findRegion("loading_foreground"));
        addActorToStage(foreground);

        //
        Asset.instance.loadAssets();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        //
        if(splash.isPaused() || splash.isAnimationFinished()){
            splash.setVisible(false);
        }else{
            splash.setVisible(true);
        }

        //loading
        Asset.instance.getAssetManager().update();

        //still loading
        //Gdx.app.log("", ""+Asset.instance.getAssetManager().getProgress());


    }

    private void finishLoading(){
        Gdx.app.log("", "finish loading");

        Asset.instance.getAssets();

        ScreenTransition transition = ScreenTransitionSlideBoth.init(0.7f, ScreenTransitionSlideBoth.UP, Interpolation.pow4);
        getGame().setScreen(new StartScreen(getGame()), transition);

        AudioManager.instance.playSound(changeScreenSound);
        AudioManager.instance.playMusic(Asset.instance.music.main, 0.7f);
    }


    @Override
    public void dispose(){
        super.dispose();

        loadingTextureAtlas.dispose();
        waterSplashSound.dispose();
        changeScreenSound.dispose();
    }






}
