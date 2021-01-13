package com.nutslaboratory.poopscounter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.nutengine.game.BaseGame;
import com.nutslaboratory.nutengine.screen.BaseScreen;
import com.nutslaboratory.nutengine.screen.transition.ScreenTransitionSlideBoth;
import com.nutslaboratory.poopscounter.game.PoopsCounter;
import com.nutslaboratory.poopscounter.util.Asset;

public class QuitScreen extends BaseScreen{

    private BaseActor background;
    private ImageButton yesButton;
    private ImageButton noButton;

    private BaseActor water;

    public QuitScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void show(){
        init();
    }

    public void init() {
        clearStage();

        //
        background = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("quit/quit_background"));
        addActorToStage(background);

        //
        water = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("quit/water"));
        addActorToStage(water);

        water.addAction(Actions.forever(Actions.sequence(
                Actions.moveTo(-480, water.getY(), 10f),
                Actions.moveTo(0, water.getY()))));

        //
        yesButton = new ImageButton(Asset.instance.skin.gameUISkin, "yes");
        addActorToStage(yesButton);
        yesButton.setPosition(127,300);

        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                quitGame();
            }
        });

        yesButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.mainButton);
                return true;
            }
        });

        //
        noButton = new ImageButton(Asset.instance.skin.gameUISkin, "no");
        addActorToStage(noButton);
        noButton.setPosition(252,300);

        noButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((PoopsCounter)getGame()).transferScreen(new StartScreen(getGame()), ScreenTransitionSlideBoth.DOWN);
            }
        });

        noButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.mainButton);
                return true;
            }
        });
    }

    public void quitGame(){
        Gdx.app.exit();
    }

    @Override
    public void pressBack(){
        ((PoopsCounter)getGame()).transferScreen(new StartScreen(getGame()), ScreenTransitionSlideBoth.DOWN);
    }


}
