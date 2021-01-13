package com.nutslaboratory.poopscounter.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.nutengine.game.BaseGame;
import com.nutslaboratory.nutengine.screen.BaseScreen;
import com.nutslaboratory.nutengine.screen.transition.ScreenTransitionSlideBoth;
import com.nutslaboratory.poopscounter.game.PoopsCounter;
import com.nutslaboratory.poopscounter.object.GameplaySetup;
import com.nutslaboratory.poopscounter.object.RulesWindow;
import com.nutslaboratory.poopscounter.util.Asset;

public class RulesScreen extends BaseScreen {
    private GameplaySetup gameplaySetup;

    private BaseActor background;
    private ExampleMatchWindow exampleMatchWindow;
    private RulesWindow rulesWindow;
    private ImageButton okButton;

    public RulesScreen(final BaseGame game, final GameplaySetup gameplaySetup){
        super(game);

        this.gameplaySetup = gameplaySetup;
    }

    @Override
    public void show(){
        init();
    }

    private void init(){
        //
        background = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("rules/rules_background"));
        addActorToStage(background);

        //
        exampleMatchWindow = new ExampleMatchWindow(gameplaySetup);
        exampleMatchWindow.setPosition((int)(getGame().getWorldWidth()/2 - exampleMatchWindow.getWidth()/2) + 2,
                (int) (getGame().getWorldHeight()/2 - exampleMatchWindow.getHeight()/2 + 140));
        addActorToStage(exampleMatchWindow);

        //
        rulesWindow = new RulesWindow(gameplaySetup);
        rulesWindow.setPosition((int) (getGame().getWorldWidth()/2 - rulesWindow.getWidth()/2) + 3,
                (int) (getGame().getWorldHeight()/2 - rulesWindow.getHeight()/2 - 170));
        addActorToStage(rulesWindow);

        //
        okButton = new ImageButton(Asset.instance.skin.gameUISkin, "ok");
        okButton.setPosition(getGame().getWorldWidth()/2 - okButton.getWidth()/2, 5);
        addActorToStage(okButton);

        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((PoopsCounter)getGame()).transferScreen(new PlayScreen(getGame(), gameplaySetup), ScreenTransitionSlideBoth.LEFT);
            }
        });

        okButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                AudioManager.instance.playSound(Asset.instance.sound.mainButton);
                return true;
            }
        });
    }


    @Override
    public void pressBack(){
        ((PoopsCounter)getGame()).transferScreen(new MainMenuScreen(getGame()), ScreenTransitionSlideBoth.RIGHT);
    }

    public class ExampleMatchWindow extends Table {
        private GameplaySetup gameplaySetup;

        private BaseActor background;

        public ExampleMatchWindow(GameplaySetup gameplaySetup){
            this.gameplaySetup = gameplaySetup;

            setSize(362, 229);

            //
            background = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("rules/example_match_window"));
            addActor(background);

            /*
            //
            Label player1Time = new Label(Utility.convertSecondToMinuteFormat(gameplaySetup.totalTime), AssetLoader.instance.skin.gameUISkin);
            player1Time.setPosition(160,-5);
            addActor(player1Time);

            Label player2Time = new Label(Utility.convertSecondToMinuteFormat(gameplaySetup.totalTime), AssetLoader.instance.skin.gameUISkin);
            player2Time.setPosition(10,360);
            addActor(player2Time);
            */

            /*
            //
            Array<String> characterNames = createCharacterNames();

            createCharacters(1, characterNames);
            //createCharacters(2, characterNames);
            */

            //
            if(!gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.NO)){
                BaseActor autoSpecialEventTag = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("rules/auto_special_event_tag"));
                autoSpecialEventTag.setPosition(158, 128);
                addActor(autoSpecialEventTag);

                BaseActor manualSpecialEventTag = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("rules/manual_special_event_tag"));
                manualSpecialEventTag.setPosition(158, 128);
                addActor(manualSpecialEventTag);

                if(gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.AUTO)){
                    manualSpecialEventTag.setVisible(false);
                }
                else if(gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.MANUAL)){
                    autoSpecialEventTag.setVisible(false);
                }
                else if(gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.MIX)){
                    manualSpecialEventTag.addAction(Actions.alpha(0f));

                    autoSpecialEventTag.addAction(Actions.forever(Actions.sequence(
                            Actions.delay(5f),
                            Actions.alpha(0f, 1f),
                            Actions.delay(5f),
                            Actions.alpha(1f, 1f))));

                    manualSpecialEventTag.addAction(Actions.forever(Actions.sequence(
                            Actions.delay(5f),
                            Actions.alpha(1f, 1f),
                            Actions.delay(5f),
                            Actions.alpha(0f, 1f))));


                }
            }

        }

        /*
        private Array<String> createCharacterNames(){
            Array<String> characterNames = new Array<String>();

            for(int i = 0; i < 24; i++){
                String tempCharacterName = "";

                if(i < 10){
                    tempCharacterName = "m_0"+i;
                }
                else{
                    tempCharacterName = "m_"+i;
                }

                characterNames.add(tempCharacterName);

                characterNames.shuffle();

                //Gdx.app.log("", tempCharacterName);
            }

            return characterNames;
        }

        public Array<Array<Character>> createCharacters(int playerNum, Array<String> characterNames){
            String s[][] = {{"#", "-", "#", "-", "-", "-"},
                            {"-", "#", "-", "-", "#", "#"},
                            {"#", "#", "-", "#", "-", "-"},
                            {"#", "-", "-", "#", "#", "#"}};


            int characterCounter = 0;

            for(int row = 0; row < s.length; row++){
                for(int column = 0; column < s[row].length; column++) {

                    if(s[row][column] == "-"){
                        String characterName = characterNames.removeIndex(0);

                        BaseActor character = new BaseActor();
                        character.setTexture((TextureRegion) Asset.instance.animation.characters.get(characterName).getKeyFrame(0f));
                        character.sizeBy(-32f);
                        addActor(character);

                        if(playerNum == 1){
                            character.setPosition(80 + (column * 35), 140 - (row * 30));
                            //character.setRotation(-90);
                        }else{
                            character.setPosition(265 + (row * 40), 100 + (column * 40));
                            character.setRotation(90);
                        }

                        characterCounter++;
                        if(characterCounter == gameplaySetup.totalCharacter){
                            return null;
                        }
                    }


                }

            }

            return  null;
        }*/
    }
}
