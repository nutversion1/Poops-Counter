package com.nutslaboratory.poopscounter.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.nutengine.game.BaseGame;
import com.nutslaboratory.nutengine.screen.BaseScreen;
import com.nutslaboratory.nutengine.screen.transition.ScreenTransitionSlideBoth;
import com.nutslaboratory.poopscounter.game.PoopsCounter;
import com.nutslaboratory.poopscounter.object.EditWindow;
import com.nutslaboratory.poopscounter.object.GameplaySetup;
import com.nutslaboratory.poopscounter.object.ItemEditWindow;
import com.nutslaboratory.poopscounter.object.NumberEditWindow;
import com.nutslaboratory.poopscounter.object.TimeEditWindow;
import com.nutslaboratory.poopscounter.util.Asset;
import com.nutslaboratory.poopscounter.util.GamePreferences;
import com.nutslaboratory.poopscounter.util.Utility;

public class MainMenuScreen extends BaseScreen {
    private GameplaySetup gameplaySetup;

    private BaseActor background;
    private BaseActor foreground;

    private SetupWindow timeSetupWindow;
    private SetupWindow poopsSetupWindow;
    private SetupWindow opponentSetupWindow;
    private SetupWindow specialEventSetupWindow;

    private ImageButton resetButton;

    private ImageButton startButton;

    private EditWindow editWindow;

    public enum State{
        MAIN,
        EDIT
    }

    public State currentState;

    public MainMenuScreen(BaseGame game){
        super(game);
    }

    @Override
    public void show(){
        init();
    }

    public void init(){
        //
        gameplaySetup = new GameplaySetup();

        loadSettings();

        //
        background = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("main_menu/main_menu_background"));
        addActorToStage(background);

        //
        timeSetupWindow = new SetupWindow("time_label", Utility.convertSecondToMinuteFormat(gameplaySetup.totalTime));
        timeSetupWindow.setPosition(0, 560);
        addActorToStage(timeSetupWindow);

        poopsSetupWindow = new SetupWindow("poops_label", ""+gameplaySetup.totalCharacter);
        poopsSetupWindow.setPosition(0, 460);
        addActorToStage(poopsSetupWindow);

        opponentSetupWindow = new SetupWindow("opponent_label", ""+gameplaySetup.opponent);
        opponentSetupWindow.setPosition(0, 360);
        addActorToStage(opponentSetupWindow);

        specialEventSetupWindow = new SetupWindow("special_event_label", gameplaySetup.specialEventMode);
        specialEventSetupWindow.setPosition(0, 260);
        addActorToStage(specialEventSetupWindow);

        //
        timeSetupWindow.getEditButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(currentState == State.MAIN) {
                    editWindow = createTimeEditWindow(timeSetupWindow.getValueLabel(),
                            GameplaySetup.DEFAULT_TOTAL_TIME,
                            gameplaySetup.totalTime, 30, 300, 30,
                            "Time for each match");
                }
            }
        });

        poopsSetupWindow.getEditButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(currentState == State.MAIN) {
                    editWindow = createPoopsEditWindow(poopsSetupWindow.getValueLabel(),
                            GameplaySetup.DEFAULT_TOTAL_CHARACTER,
                            gameplaySetup.totalCharacter, 5, 12, 1,
                            "Total poops");
                }
            }
        });

        opponentSetupWindow.getEditButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                    if(currentState == State.MAIN) {
                        Array<String> items = new Array<String>();
                        items.add(GameplaySetup.Opponent.HUMAN);
                        items.add(GameplaySetup.Opponent.COM);


                        editWindow = createOpponentEditWindow(opponentSetupWindow.getValueLabel(),
                                GameplaySetup.DEFAULT_OPPONENT,
                                items, gameplaySetup.opponent,
                                "Your opponent");
                    }
            }
        });

        specialEventSetupWindow.getEditButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(currentState == State.MAIN) {
                    Array<String> items = new Array<String>();
                    items.add(GameplaySetup.SpecialEventMode.NO);
                    items.add(GameplaySetup.SpecialEventMode.AUTO);
                    items.add(GameplaySetup.SpecialEventMode.MANUAL);
                    items.add(GameplaySetup.SpecialEventMode.MIX);

                    Array<String> itemDescriptions = new Array<String>();
                    itemDescriptions.add("There is no special events");
                    itemDescriptions.add("Both players receive the same special events");
                    itemDescriptions.add("Both players need to fight to obtain special events that will be beneficial to them");
                    itemDescriptions.add("This is a mix of Both and Fight modes");

                    editWindow = createSpecialEventEditWindow(specialEventSetupWindow.getValueLabel(),
                            GameplaySetup.DEFAULT_SPECIAL_EVENT_MODE,
                            items,
                            itemDescriptions,
                            gameplaySetup.specialEventMode,
                            "Special events occurred during the match can aid or obstruct you");
                }
            }
        });


        //
        resetButton = new ImageButton(Asset.instance.skin.gameUISkin, "reset_setup");
        resetButton.setPosition(390, 80);
        addActorToStage(resetButton);

        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(currentState == State.MAIN) {
                    resetSettings();
                }
            }
        });

        resetButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.mainButton);
                return true;
            }
        });

        //
        startButton = new ImageButton(Asset.instance.skin.gameUISkin, "start");
        startButton.setPosition(getGame().getWorldWidth()/2 - startButton.getWidth()/2, 5);
        addActorToStage(startButton);

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(currentState == State.MAIN) {
                    ((PoopsCounter)getGame()).transferScreen(new RulesScreen(getGame(), gameplaySetup), ScreenTransitionSlideBoth.LEFT);
                }
            }
        });

        startButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.mainButton);
                return true;
            }
        });

        //
        foreground = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("main_menu/main_menu_foreground"));
        foreground.setTouchable(Touchable.disabled);
        addActorToStage(foreground);

        //
        currentState = State.MAIN;
    }

    private void loadSettings() {
        gameplaySetup.totalTime = GamePreferences.instance.totalTime;
        gameplaySetup.totalCharacter = GamePreferences.instance.totalCharacter;
        gameplaySetup.opponent = GamePreferences.instance.opponent;
        gameplaySetup.specialEventMode = GamePreferences.instance.specialEventMode;
    }

    private void resetSettings(){
        gameplaySetup.totalTime = GameplaySetup.DEFAULT_TOTAL_TIME;
        gameplaySetup.totalCharacter = GameplaySetup.DEFAULT_TOTAL_CHARACTER;
        gameplaySetup.opponent = GameplaySetup.DEFAULT_OPPONENT;
        gameplaySetup.specialEventMode = GameplaySetup.DEFAULT_SPECIAL_EVENT_MODE;

        GamePreferences.instance.totalTime = gameplaySetup.totalTime;
        GamePreferences.instance.totalCharacter = gameplaySetup.totalCharacter;
        GamePreferences.instance.opponent = gameplaySetup.opponent;
        GamePreferences.instance.specialEventMode = gameplaySetup.specialEventMode;
        GamePreferences.instance.save();


        timeSetupWindow.getValueLabel().setText(""+Utility.convertSecondToMinuteFormat(gameplaySetup.totalTime).toUpperCase());
        poopsSetupWindow.getValueLabel().setText(Integer.toString(gameplaySetup.totalCharacter).toUpperCase());
        opponentSetupWindow.getValueLabel().setText(gameplaySetup.opponent.toUpperCase());
        specialEventSetupWindow.getValueLabel().setText(gameplaySetup.specialEventMode.toUpperCase());

    }




    public EditWindow createTimeEditWindow(final Label label, int defaultValue, int value, int minValue, int maxValue, int changeValue, String description){
        final TimeEditWindow newEditWindow = new TimeEditWindow(defaultValue, value, minValue, maxValue, changeValue){

            @Override
            public void close(){
                super.close();

                gameplaySetup.totalTime = value;
                label.setText(Utility.convertSecondToMinuteFormat(gameplaySetup.totalTime));

                GamePreferences.instance.totalTime = gameplaySetup.totalTime;
                GamePreferences.instance.save();
            }

            @Override
            public void doCloseEvent() {
                editWindow = null;
                currentState = State.MAIN;
            }
        };
        newEditWindow.setDescription(description);
        newEditWindow.setTag("time");
        addActorToStage(newEditWindow);

        //
        newEditWindow.getCloseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.close();
            }
        });

        //
        newEditWindow.getDecreaseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.decrease();
            }
        });

        //
        newEditWindow.getIncreaseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.increase();
            }
        });

        //
        newEditWindow.getResetButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.reset();
            }
        });

        //
        currentState = State.EDIT;

        return newEditWindow;
    }

    public EditWindow createPoopsEditWindow(final Label label, int defaultValue, int value, int minValue, int maxValue, int changeValue, String description){
        final NumberEditWindow newEditWindow = new NumberEditWindow(defaultValue, value, minValue, maxValue, changeValue){

            @Override
            public void close(){
                super.close();

                gameplaySetup.totalCharacter =  value;
                label.setText(""+gameplaySetup.totalCharacter);

                GamePreferences.instance.totalCharacter = gameplaySetup.totalCharacter;
                GamePreferences.instance.save();
            }

            @Override
            public void doCloseEvent() {
                editWindow = null;
                currentState = State.MAIN;
            }
        };
        newEditWindow.setDescription(description);
        newEditWindow.setPosition(getGame().getWorldWidth()/2 - newEditWindow.getWidth()/2, getGame().getWorldHeight()/2 - newEditWindow.getHeight()/2);
        newEditWindow.setTag("poops");
        addActorToStage(newEditWindow);

        //
        newEditWindow.getCloseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.close();
            }
        });

        //
        newEditWindow.getDecreaseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.decrease();
            }
        });

        //
        newEditWindow.getIncreaseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.increase();
            }
        });

        //
        newEditWindow.getResetButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.reset();
            }
        });

        //
        currentState = State.EDIT;

        return newEditWindow;
    }

    public EditWindow createOpponentEditWindow(final Label label, String defaultValue, Array<String> items, String value, String description){
        final ItemEditWindow newEditWindow = new ItemEditWindow(defaultValue, items, value, null){

            @Override
            public void close(){
                super.close();

                gameplaySetup.opponent = value;
                label.setText(gameplaySetup.opponent.toUpperCase());

                GamePreferences.instance.opponent = gameplaySetup.opponent;
                GamePreferences.instance.save();
            }

            @Override
            public void doCloseEvent() {
                editWindow = null;
                currentState = State.MAIN;
            }
        };
        newEditWindow.setDescription(description);
        newEditWindow.setPosition(getGame().getWorldWidth()/2 - newEditWindow.getWidth()/2, getGame().getWorldHeight()/2 - newEditWindow.getHeight()/2);
        newEditWindow.setTag("opponent");
        addActorToStage(newEditWindow);

        //
        newEditWindow.getCloseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.close();
            }
        });

        //
        newEditWindow.getDecreaseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.decrease();
            }
        });

        //
        newEditWindow.getIncreaseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.increase();
            }
        });

        //
        newEditWindow.getResetButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.reset();
            }
        });

        //
        currentState = State.EDIT;

        return newEditWindow;
    }

    public EditWindow createSpecialEventEditWindow(final Label label, String defaultValue, Array<String> items, Array<String> itemDescriptions, String value, String description){
        final ItemEditWindow newEditWindow = new ItemEditWindow(defaultValue, items, value, itemDescriptions){

            @Override
            public void close(){
                super.close();

                gameplaySetup.specialEventMode = value;
                label.setText(gameplaySetup.specialEventMode.toUpperCase());

                GamePreferences.instance.specialEventMode = gameplaySetup.specialEventMode;
                GamePreferences.instance.save();
            }

            @Override
            public void doCloseEvent() {
                editWindow = null;
                currentState = State.MAIN;
            }
        };
        newEditWindow.setDescription(description);
        newEditWindow.setPosition(getGame().getWorldWidth()/2 - newEditWindow.getWidth()/2, getGame().getWorldHeight()/2 - newEditWindow.getHeight()/2);
        newEditWindow.setTag("special_event");
        addActorToStage(newEditWindow);

        //
        newEditWindow.getCloseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.close();
            }
        });

        //
        newEditWindow.getDecreaseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.decrease();
            }
        });

        //
        newEditWindow.getIncreaseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.increase();
            }
        });

        //
        newEditWindow.getResetButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newEditWindow.reset();
            }
        });

        //
        currentState = State.EDIT;

        return newEditWindow;
    }

    @Override
    public void pressBack()
    {
        if(editWindow != null){
            if(!editWindow.isAnimating()){
                editWindow.close();
            }
        }else {
            ((PoopsCounter)getGame()).transferScreen(new StartScreen(getGame()), ScreenTransitionSlideBoth.RIGHT);
        }
    }

    public class SetupWindow extends Table{
        private Label valueLabel;
        private ImageButton editButton;

        public SetupWindow(String name, String value){

            //
            BaseActor background = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("main_menu/setup_window"));
            background.setPosition(0, 0);
            addActor(background);

            //
            BaseActor logo = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("main_menu/"+name));
            logo.setPosition(55, -8);
            addActor(logo);

            //
            valueLabel = new Label(value.toUpperCase(), Asset.instance.skin.gameUISkin, "white_rabbit_25");
            valueLabel.setColor(0, 255, 255, 1);
            valueLabel.setPosition(263, 17);
            valueLabel.setWidth(50);
            valueLabel.setAlignment(Align.center);
            addActor(valueLabel);
            //valueLabel.debug();


            //
            editButton = new ImageButton(Asset.instance.skin.gameUISkin, "edit");
            editButton.setPosition(370, 10);
            addActor(editButton);

            editButton.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    AudioManager.instance.playSound(Asset.instance.sound.mainButton);
                    return true;
                }
            });
        }

        public Label getValueLabel(){
            return valueLabel;
        }

        public ImageButton getEditButton(){
            return editButton;
        }

    }
}
