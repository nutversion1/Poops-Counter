package com.nutslaboratory.poopscounter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.nutslaboratory.nutengine.actor.AnimatedActor;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.nutengine.game.BaseGame;
import com.nutslaboratory.nutengine.screen.BaseScreen;
import com.nutslaboratory.nutengine.screen.transition.ScreenTransitionSlideBoth;
import com.nutslaboratory.poopscounter.game.PoopsCounter;
import com.nutslaboratory.poopscounter.object.Character;
import com.nutslaboratory.poopscounter.object.Computer;
import com.nutslaboratory.poopscounter.object.CountdownTimer;
import com.nutslaboratory.poopscounter.object.GameplaySetup;
import com.nutslaboratory.poopscounter.object.PauseMenu;
import com.nutslaboratory.poopscounter.object.PlayTimer;
import com.nutslaboratory.poopscounter.object.Player;
import com.nutslaboratory.poopscounter.object.PlayerBar;
import com.nutslaboratory.poopscounter.object.PlayerTab;
import com.nutslaboratory.poopscounter.object.SpecialEventButton;
import com.nutslaboratory.poopscounter.object.SpecialEventManager;
import com.nutslaboratory.poopscounter.object.SpecialEventSpawner;
import com.nutslaboratory.poopscounter.object.SpecialEventsMonitor;
import com.nutslaboratory.poopscounter.util.Asset;
import com.nutslaboratory.poopscounter.util.GamePreferences;
import com.nutslaboratory.poopscounter.util.Utility;

import static com.nutslaboratory.poopscounter.object.SpecialEventManager.SpecialEvent;

public class PlayScreen extends BaseScreen {
    public static final float SPECIAL_EVENT_BUTTON_START_X_LEFT = 0 - 30;
    public static final float SPECIAL_EVENT_BUTTON_START_X_RIGHT = 480 + 30;

    private Player player1;
    private Player player2;
    private Array<Player> players;

    public Group groundLayer;
    private Group foreLayer;
    private Group uiLayer;

    private BaseActor background;
    private BaseActor foreground;
    private BaseActor net;
    private PauseMenu pauseMenu;

    public enum State{
        READY,
        COUNTDOWN,
        PLAY,
        GAMEOVER
    }
    private State currentState;

    private GameplaySetup gameplaySetup;

    private int playTimeLeft;

    private Array<SpecialEventButton> specialEventButtons;

    private CountdownTimer countdownTimer;
    private PlayTimer playTimer;

    private SpecialEventSpawner specialEventSpawner;
    private Computer player2Computer;

    public PlayScreen(BaseGame game, GameplaySetup gameplaySetup){
        super(game);

        this.gameplaySetup = gameplaySetup;
    }

    @Override
    public void show(){
        init();
    }


    public void init(){

        //clear stage
        clearStage();

        //create layers
        groundLayer = new Group();
        addActorToStage(groundLayer);

        foreLayer = new Group();
        addActorToStage(foreLayer);

        uiLayer = new Group();
        addActorToStage(uiLayer);

        //create players
        player1 = new Player(1, gameplaySetup.totalCharacter);
        player2 = new Player(2, gameplaySetup.totalCharacter);

        players = new Array<Player>();
        players.add(player1);
        players.add(player2);

        //create background
        background = createBackground(groundLayer);

        //
        BaseActor netWater = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/net_water"));
        netWater.setPosition(0, getGame().getWorldHeight()/2 - netWater.getHeight()/2);
        groundLayer.addActor(netWater);

        netWater.addAction(Actions.forever(Actions.sequence(
                Actions.moveTo(-480, netWater.getY(), 20f),
                Actions.moveTo(0, netWater.getY()))));

        //create net
        net = createNet(groundLayer);

        //
        BaseActor netFore = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/net_fore"));
        netFore.setPosition(0, getGame().getWorldHeight()/2 - netFore.getHeight()/2);
        netFore.setTouchable(Touchable.disabled);
        foreLayer.addActor(netFore);

        //create characters
        Array<String> characterNames = createCharacterNames();

        player1.setCharacterTable(createCharacters(foreLayer, player1, characterNames));
        player2.setCharacterTable(createCharacters(foreLayer, player2, characterNames));

        //player2.traceCharacterTable();
        //player1.traceCharacterTable();

        //create player bars
        player1.setPlayerBar(createPlayerBar(uiLayer, 1, Utility.convertSecondToMinuteFormat(gameplaySetup.totalTime)));
        player2.setPlayerBar(createPlayerBar(uiLayer, 2, Utility.convertSecondToMinuteFormat(gameplaySetup.totalTime)));

        //create foreround
        foreground = createForeground(uiLayer);

        //create special events monitor
        if(!gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.NO)){
            player1.setSpecialEventsMonitor(createSpecialEventsMonitor(uiLayer, 1));
            player2.setSpecialEventsMonitor(createSpecialEventsMonitor(uiLayer, 2));
        }

        //create player tabs
        player1.setPlayerTab(createPlayerTab(uiLayer, 1));
        player2.setPlayerTab(createPlayerTab(uiLayer, 2));

        //create pause menu
        pauseMenu = createPauseMenu(uiLayer);

        //set music
        AudioManager.instance.playMusic(Asset.instance.music.matchBreak, 0.9f);

        //init settings
        playTimeLeft = gameplaySetup.totalTime;

        specialEventButtons = new Array<SpecialEventButton>();

        currentState = State.READY;

        //
        countdownTimer = new CountdownTimer(this);
        addActorToStage(countdownTimer);

        //
        playTimer = new PlayTimer(this);
        addActorToStage(playTimer);

        //
        if(gameplaySetup.opponent.equals(GameplaySetup.Opponent.COM)){
            player2Computer = new Computer(this, player2);
            addActorToStage(player2Computer);

            /*
            Computer player1Computer = new Computer(this, player1);
            addActorToStage(player1Computer);*/
        }

        //
        if(!gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.NO)){
            specialEventSpawner = new SpecialEventSpawner(this);
            addActorToStage(specialEventSpawner);
        }


    }


    private BaseActor createBackground(Group layer) {
        background = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/play_background"));
        layer.addActor(background);

        return background;
    }

    private BaseActor createForeground(Group layer) {
        foreground = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/play_foreground"));
        foreground.setTouchable(Touchable.disabled);
        layer.addActor(foreground);

        return foreground;
    }

    private BaseActor createNet(Group layer) {
        net = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/net"));
        net.setPosition(0, getGame().getWorldHeight()/2 - net.getHeight()/2);
        layer.addActor(net);

        //
        //net.setVisible(false);

        return net;
    }

    public SpecialEventsMonitor createSpecialEventsMonitor(Group layer, int playerNum){
        SpecialEventsMonitor specialEventsMonitor = new SpecialEventsMonitor();
        specialEventsMonitor.setSize(480, 400);
        specialEventsMonitor.setOrigin(specialEventsMonitor.getWidth()/2, specialEventsMonitor.getHeight()/2);
        specialEventsMonitor.setTouchable(Touchable.disabled);
        //specialEventsMonitor.debug();


        layer.addActor(specialEventsMonitor);

        if(playerNum == 1){
            specialEventsMonitor.setPosition(0,0);
        }else{
            specialEventsMonitor.setPosition(0,400);
            specialEventsMonitor.rotateBy(180);
        }

        return  specialEventsMonitor;
    }

    public PlayerBar createPlayerBar(Group layer, int playerNum, String time) {
        PlayerBar playerBar = new PlayerBar(time);

        if(playerNum == 1){
            playerBar.setPosition(0,0);
        }else{
            playerBar.setPosition(0,700);
            playerBar.setRotation(180);
        }

        layer.addActor(playerBar);

        //
        //playerBar.setVisible(false);

        return playerBar;
    }

    public PlayerTab createPlayerTab(Group layer, final int playerNum) {
        final PlayerTab playerTab = new PlayerTab();
        playerTab.setWidth(400);
        playerTab.setHeight(200);
        playerTab.setOrigin(playerTab.getWidth()/2, playerTab.getHeight()/2);

        if (playerNum == 1){
            playerTab.setPosition(getGame().getWorldWidth()/2 - playerTab.getWidth()/2, 100);
        }else{
            playerTab.setPosition(getGame().getWorldWidth()/2 - playerTab.getWidth()/2, 500);
            playerTab.rotateBy(180);
        }

        layer.addActor(playerTab);

        playerTab.getReadyButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                checkForReady();
            }
        });

        playerTab.getRematchButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                checkForRematch();
            }
        });

        //playerTab.debug();


        return playerTab;
    }

    public PauseMenu createPauseMenu(Group layer){
        final PauseMenu pauseMenu = new PauseMenu();
        pauseMenu.show(false);
        layer.addActor(pauseMenu);

        pauseMenu.getResumeButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                togglePaused();
                pauseMenu.show(false);

                resumeMatch();

            }
        });

        pauseMenu.getRestartButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                togglePaused();
                pauseMenu.show(false);

                //restart game
                restartMatch();
            }
        });

        pauseMenu.getQuitButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                quitMatch();
            }
        });

        pauseMenu.getSoundButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.instance.sound = !pauseMenu.getSoundButton().isChecked();
                GamePreferences.instance.save();

                if(GamePreferences.instance.sound){
                    AudioManager.instance.unmuteSound();

                    player1.unmuteSound();
                    player2.unmuteSound();
                }else{
                    AudioManager.instance.muteSound();

                    player1.muteSound();
                    player2.muteSound();
                }
            }
        });

        pauseMenu.getMusicButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.instance.music = !pauseMenu.getMusicButton().isChecked();
                GamePreferences.instance.save();

                if(GamePreferences.instance.music){
                    AudioManager.instance.unmuteMusic();
                }else{
                    AudioManager.instance.muteMusic();
                }
            }
        });

        return pauseMenu;
    }

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



    public Array<Array<Character>> createCharacters(Group layer, final Player player, Array<String> characterNames){

        //create character sprite
        Array<Character> characters = new Array<Character>();
        for(int i = 0; i < player.getTotalCharacter(); i++){
            String tempCharacterName = characterNames.removeIndex(0);

            final Character tempCharacter = new Character(tempCharacterName,
                    Asset.instance.animation.characters.get(tempCharacterName),
                    player);
            tempCharacter.startAnimation();
            //tempCharacter.debug();
            characters.add(tempCharacter);
            layer.addActor(tempCharacter);

            tempCharacter.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    touchCharacter(tempCharacter);

                    return false;
                }
            });
        }

        //create character sprite table (first set all item to null)
        Array<Array<Character>> characterTable = new Array<Array<Character>>();

        for(int column = 0; column < Player.totalCharacterColumn; column++){
            characterTable.add(new Array<Character>());

            for(int row = 0; row < Player.totalCharacterRow; row++){
                characterTable.get(column).add(null);
            }
        }

        //create empty position table
        Array<Array<Integer>> emptyPositionTable = new Array<Array<Integer>>();

        for(int column = 0; column < Player.totalCharacterColumn; column++){
            for(int row = 0; row < Player.totalCharacterRow; row++){
                Array<Integer> emptyPosition = new Array<Integer>();
                emptyPosition.add(column);
                emptyPosition.add(row);

                emptyPositionTable.add(emptyPosition);
            }
        }

        emptyPositionTable.shuffle();

        //replace empty position with character sprite
        for(int i = 0; i < characters.size; i++){
            Array<Integer> emptyPositionList = emptyPositionTable.get(i);
            int emptyColumn = emptyPositionList.get(0);
            int emptyRow = emptyPositionList.get(1);

            characterTable.get(emptyColumn).set(emptyRow, characters.get(i));

        }

        //set all character's position
        for(int column = 0; column < Player.totalCharacterColumn; column++){
            for(int row = 0; row < Player.totalCharacterRow; row++){
                Character tempCharacter = characterTable.get(column).get(row);

                if(tempCharacter != null){
                    if(player.getPlayerNum() == 1){
                        tempCharacter.setPosition(Player.player1CharacterOffsetX + (column * Player.characterSpaceX),
                                Player.player1CharacterOffsetY - (row * Player.characterSpaceY));
                        //Gdx.app.log("", tempCharacter.getName());
                    }
                    else{
                        tempCharacter.setPosition(Player.player2CharacterOffsetX -(column * Player.characterSpaceX),
                                Player.player2CharacterOffsetY + (row * Player.characterSpaceY));
                        tempCharacter.setRotation(180);
                        //Gdx.app.log("", tempCharacter.getName());
                    }

                    //set character's column & row
                    tempCharacter.setColumn(column);
                    tempCharacter.setRow(row);
                }
            }
        }

        return characterTable;
    }


    public void touchCharacter(final Character touchedCharacter){
        if(isPaused()){
            return;
        }

        if(currentState == State.PLAY) {
            final Player thrownPlayer = touchedCharacter.getCurrentPlayer();

            Player receivedPlayer = null;
            if (thrownPlayer.getPlayerNum() == 1) {
                receivedPlayer = player2;
            } else {
                receivedPlayer = player1;
            }

            //
            if(thrownPlayer.getCurrentSpecialEvent() == SpecialEvent.LOCK){
                return;
            }
            if(touchedCharacter.isUnavailable){
                return;
            }
            if(touchedCharacter.isMoving){
                return;
            }

            //correct touch
            if(touchedCharacter.getName().equals(thrownPlayer.getRequiredCharacter().getName())) {
                //throw
                throwCharacter(touchedCharacter, thrownPlayer, receivedPlayer);

                //check for end game
                if (!checkForEnd()) {
                    //slide character monitor
                    thrownPlayer.getPlayerBar().getCharacterMonitor().addAction(Actions.sequence(
                            Actions.moveBy(0, -70, gameplaySetup.changeRequiredCharacterTime),
                            new Action() {
                                @Override
                                public boolean act(float delta) {
                                    //throw
                                    thrownPlayer.setRequiredCharacter();

                                    //sound
                                    AudioManager.instance.playSound(Asset.instance.sound.pop, 0.2f);

                                    //hint
                                    if (thrownPlayer.getPlayerNum() == 1) {
                                        if (player1.getCurrentSpecialEvent() == SpecialEvent.HINT) {

                                            int x = (Player.player1CharacterOffsetX + (player1.getRequiredCharacter().getColumn() * Player.characterSpaceX))+30;
                                            int y = (Player.player1CharacterOffsetY - (player1.getRequiredCharacter().getRow() * Player.characterSpaceY)+30);
                                            player1.getSpecialEventsMonitor().setHintLocation(x,y, Align.center);
                                        }
                                    } else {
                                        if (player2.getCurrentSpecialEvent() == SpecialEvent.HINT) {

                                            int x = (Player.player1CharacterOffsetX + (player2.getRequiredCharacter().getColumn() * Player.characterSpaceX))+30;
                                            int y = (Player.player1CharacterOffsetY - (player2.getRequiredCharacter().getRow() * Player.characterSpaceY)+30);
                                            player2.getSpecialEventsMonitor().setHintLocation(x,y, Align.center);
                                        }


                                    }


                                    return true;
                                }
                            },
                            Actions.moveBy(0, 70, gameplaySetup.changeRequiredCharacterTime)));



                } else {
                    endGame();

                    thrownPlayer.getPlayerBar().getCharacterMonitor().addAction(Actions.moveBy(0, -70, gameplaySetup.changeRequiredCharacterTime));
                }

            }
            //wrong touch
            else{
                if(thrownPlayer.getCurrentSpecialEvent() != SpecialEvent.SHIELD) {
                    //unavailable character
                    touchedCharacter.unavailable();

                    //play sound
                    AudioManager.instance.playSound(Asset.instance.sound.touchFoul);

                }
            }



        }
    }

    public void throwCharacter(final Character thrownCharacter, Player thrownPlayer, Player receivedPlayer){

        //System.out.println("throw");

        //
        outerloop: for(int column = 0; column < Player.totalCharacterColumn; column++){
            for(int row = 0; row < Player.totalCharacterRow; row++){
                if(thrownPlayer.getCharacterTable().get(column).get(row) == thrownCharacter){
                    thrownPlayer.getCharacterTable().get(column).set(row, null);
                    break outerloop;
                }
            }
        }

        //
        Array<Array<Integer>> emptyPositionTable = new Array<Array<Integer>>();

        for(int column = 0; column < Player.totalCharacterColumn; column++){
            for(int row = 0; row < Player.totalCharacterRow; row++){

                if(receivedPlayer.getCharacterTable().get(column).get(row) == null){
                    Array<Integer> emptyPositions = new Array<Integer>();
                    emptyPositions.add(column);
                    emptyPositions.add(row);

                    emptyPositionTable.add(emptyPositions);
                }
            }
        }

        emptyPositionTable.shuffle();

        int targetColumn = emptyPositionTable.get(0).get(0);
        int targetRow = emptyPositionTable.get(0).get(1);

        //
        receivedPlayer.getCharacterTable().get(targetColumn).set(targetRow, thrownCharacter);


        //
        thrownPlayer.setTotalCharacter(thrownPlayer.getTotalCharacter()-1);
        receivedPlayer.setTotalCharacter(receivedPlayer.getTotalCharacter()+1);

        thrownPlayer.setPoint(thrownPlayer.getPoint()-1);
        receivedPlayer.setPoint(receivedPlayer.getPoint()+1);

        //
        thrownCharacter.setCurrentPlayer(receivedPlayer);


        //
        float destinationXPos = 0;
        float destinationYPos = 0;

        if(receivedPlayer.getPlayerNum() == 1){
            destinationXPos = Player.player1CharacterOffsetX + (targetColumn * Player.characterSpaceX);
            destinationYPos = Player.player1CharacterOffsetY - (targetRow * Player.characterSpaceY);
        }else{
            destinationXPos = Player.player2CharacterOffsetX - (targetColumn * Player.characterSpaceX);
            destinationYPos = Player.player2CharacterOffsetY + (targetRow * Player.characterSpaceY);
        }

        ////move character

        //character is spinning
        if(thrownCharacter.isSpinning){
            thrownCharacter.stopSpin();

            if(thrownCharacter.getCurrentPlayer().getPlayerNum() == 1){
                thrownCharacter.setRotation(180);
            }else{
                thrownCharacter.setRotation(0);
            }
        }

        //player has spin special event
        if(thrownPlayer.getCurrentSpecialEvent() == SpecialEvent.SPIN) {
            thrownCharacter.flyAndSpin((int)destinationXPos, (int)destinationYPos);
        //player has not spin special event
        }else{
            thrownCharacter.fly((int)destinationXPos, (int)destinationYPos);
        }

        //set character's column & row
        thrownCharacter.setColumn(targetColumn);
        thrownCharacter.setRow(targetRow);


    }

    public boolean checkForEnd(){
        boolean isGameEnd = false;

        if (player1.getTotalCharacter() == 0 || player2.getTotalCharacter() == 0){
            isGameEnd = true;
        }

        return isGameEnd;
    }

    public  void endGame(){
        playTimer.stop();

        player1.getPlayerTab().getEndLabel().setVisible(true);
        player2.getPlayerTab().getEndLabel().setVisible(true);

        //time's up
        if(playTimeLeft == 0){
            //player1 won
            if(player1.getTotalCharacter() < player2.getTotalCharacter()){
                player1.getPlayerTab().showResult("win");
                player2.getPlayerTab().showResult("lose");
            }
            //player2 won
            else if(player2.getTotalCharacter() < player1.getTotalCharacter()){
                player1.getPlayerTab().showResult("lose");
                player2.getPlayerTab().showResult("win");
            }
            //draw
            else{
                player1.getPlayerTab().showResult("draw");
                player2.getPlayerTab().showResult("draw");
            }


        }
        //player's character table has empty
        else if (player1.getTotalCharacter() == 0 || player2.getTotalCharacter() == 0){
            //player1 won
            if(player1.getTotalCharacter() == 0){
                player1.getPlayerTab().showResult("win");
                player2.getPlayerTab().showResult("lose");
            }
            //player2 won
            else if(player2.getTotalCharacter() == 0){
                player1.getPlayerTab().showResult("lose");
                player2.getPlayerTab().showResult("win");
            }


        }


        //
        currentState = State.GAMEOVER;

        player1.getPlayerTab().setVisible(true);
        player2.getPlayerTab().setVisible(true);

        player1.getPlayerTab().getRematchButton().addAction(
                Actions.sequence(
                        Actions.delay(0.5f),
                        Actions.visible(true)
                )
        );
        player2.getPlayerTab().getRematchButton().addAction(
                Actions.sequence(
                        Actions.delay(0.5f),
                        Actions.visible(true)
                )
        );

        //set special event buttons
        for(SpecialEventButton specialEventButton : specialEventButtons) {
            specialEventButton.timesUp();
        }

        //stop music
        AudioManager.instance.stopMusic();
    }

    public void checkForReady(){
        //Gdx.app.log("", "check for ready ");

        if(player1.getPlayerTab().getReadyButton().isChecked() && player2.getPlayerTab().getReadyButton().isChecked()){

            player1.getPlayerTab().getReadyButton().setTouchable(Touchable.disabled);
            player2.getPlayerTab().getReadyButton().setTouchable(Touchable.disabled);

            player1.getPlayerTab().getReadyButton().addAction(Actions.sequence(
                    Actions.delay(0.5f),
                    Actions.fadeOut(0.3f)
            ));
            player2.getPlayerTab().getReadyButton().addAction(Actions.sequence(
                    Actions.delay(0.5f),
                    Actions.fadeOut(0.3f),
                    new Action() {
                        @Override
                        public boolean act(float delta) {
                            currentState = State.COUNTDOWN;

                            countdownTimer.start();

                            return true;
                        }
                    }
            ));


        }
    }

    public void checkForRematch(){
        //Gdx.app.log("", "check for rematch ");

        if(player1.getPlayerTab().getRematchButton().isChecked() && player2.getPlayerTab().getRematchButton().isChecked()){

            player1.getPlayerTab().getRematchButton().setTouchable(Touchable.disabled);
            player2.getPlayerTab().getRematchButton().setTouchable(Touchable.disabled);

            player1.getPlayerTab().getRematchButton().addAction(Actions.sequence(
                    Actions.delay(0.5f),
                    Actions.fadeOut(0.3f)
            ));
            player2.getPlayerTab().getRematchButton().addAction(Actions.sequence(
                    Actions.delay(0.5f),
                    Actions.fadeOut(0.3f),
                    new Action() {
                        @Override
                        public boolean act(float delta) {
                            restartMatch();

                            return true;
                        }
                    }
            ));

        }
    }

    @Override
    public void pressBack(){
        togglePaused();
        pauseMenu.show(isPaused());

        if(isPaused()) {
            pauseMatch();
        }else{
            resumeMatch();
        }
    }

    private void pauseMatch(){
        AudioManager.instance.pauseMusic();

        if(!gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.NO)){
            player1.pauseSound();
            player2.pauseSound();
        }
    }

    private void resumeMatch(){
        AudioManager.instance.resumeMusic();

        if(!gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.NO)){
            player1.resumeSound();
            player2.resumeSound();
        }
    }

    private void restartMatch(){
        player1.dispose();
        player2.dispose();

        init();
    }

    private void quitMatch(){
        player1.dispose();
        player2.dispose();

        ((PoopsCounter)getGame()).transferScreen(new MainMenuScreen(getGame()), ScreenTransitionSlideBoth.DOWN);
        AudioManager.instance.playMusic(Asset.instance.music.main, 0.7f);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);

        //
        if(touchStagePos.y <= getGame().getWorldHeight()/2 - net.getHeight()/2) {
            if (player1.touchPointer >= 0) {
                return true;
            }
            player1.touchPointer = pointer;
            //Gdx.app.log("test", "player 1 touch: "+pointer);
        }else if(touchStagePos.y >= getGame().getWorldHeight()/2 + net.getHeight()/2){
            if (player2.touchPointer >= 0) {
                return true;
            }
            player2.touchPointer = pointer;
            //Gdx.app.log("test", "player 2 touch: "+pointer);
        }else{
            //Gdx.app.log("test", "some player touch: "+pointer);
        }

        //disallow human player touch computer's area (if opponent is computer)
        if(gameplaySetup.opponent.equals(GameplaySetup.Opponent.COM)) {
            if(!isPaused()) {
                if (touchStagePos.y >= 430) {
                    return true;
                }
            }
        }
        //Gdx.app.log("", touchStagePos+"");



        return false;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (pointer == player1.touchPointer){
            player1.touchPointer = -1;
            return false;
        }
        if (pointer == player2.touchPointer){
            player2.touchPointer = -1;
            return false;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode){
        super.keyUp(keycode);

        if(keycode == Input.Keys.NUM_0) {
            SpecialEventManager.instance.setForcedSpecialEvent(null);
        }else if(keycode == Input.Keys.NUM_1) {
            SpecialEventManager.instance.setForcedSpecialEvent(SpecialEvent.SHIELD);
        }else if(keycode == Input.Keys.NUM_2) {
            SpecialEventManager.instance.setForcedSpecialEvent(SpecialEvent.HINT);
        }else if(keycode == Input.Keys.NUM_3) {
            SpecialEventManager.instance.setForcedSpecialEvent(SpecialEvent.SPIN);
        }else if(keycode == Input.Keys.NUM_4) {
            SpecialEventManager.instance.setForcedSpecialEvent(SpecialEvent.FLIP);
        }else if(keycode == Input.Keys.NUM_5) {
            SpecialEventManager.instance.setForcedSpecialEvent(SpecialEvent.LOCK);
        }else if(keycode == Input.Keys.NUM_6) {
            SpecialEventManager.instance.setForcedSpecialEvent(SpecialEvent.FOG);
        }else if(keycode == Input.Keys.NUM_7) {
            SpecialEventManager.instance.setForcedSpecialEvent(SpecialEvent.MUD);
        }else if(keycode == Input.Keys.NUM_8) {
            SpecialEventManager.instance.setForcedSpecialEvent(SpecialEvent.BUG);
        }

        Gdx.app.log("","forced special event: "+SpecialEventManager.instance.getForcedSpecialEvent());

        return false;
    }

    public void onNotify(CountdownTimer.CountdownTimerCommand command){
        switch (command){
            case COUNT_THREE:
                AudioManager.instance.stopMusic();
                player1.getPlayerTab().setCountdownLabel("three", CountdownTimer.READY_DELAY);
                player2.getPlayerTab().setCountdownLabel("three", CountdownTimer.READY_DELAY);
                AudioManager.instance.playSound(Asset.instance.sound.countdownReady);
                break;
            case COUNT_TWO:
                player1.getPlayerTab().setCountdownLabel("two", CountdownTimer.READY_DELAY);
                player2.getPlayerTab().setCountdownLabel("two", CountdownTimer.READY_DELAY);
                AudioManager.instance.playSound(Asset.instance.sound.countdownReady);
                break;
            case COUNT_ONE:
                player1.getPlayerTab().setCountdownLabel("one", CountdownTimer.READY_DELAY);
                player2.getPlayerTab().setCountdownLabel("one", CountdownTimer.READY_DELAY);
                AudioManager.instance.playSound(Asset.instance.sound.countdownReady);
                break;
            case COUNT_GO:
                player1.getPlayerTab().setCountdownLabel("go", CountdownTimer.GO_DELAY);
                player2.getPlayerTab().setCountdownLabel("go", CountdownTimer.GO_DELAY);

                AudioManager.instance.playSound(Asset.instance.sound.countdownGo);
                AudioManager.instance.playMusic(Asset.instance.music.match, 1);
                break;
            case START:
                currentState = State.PLAY;

                //slide character monitor
                player1.setRequiredCharacter();
                player1.getPlayerBar().getCharacterMonitor().addAction(Actions.moveBy(0, 70, gameplaySetup.changeRequiredCharacterTime));
                AudioManager.instance.playSound(Asset.instance.sound.pop, 0.2f);

                player2.setRequiredCharacter();
                player2.getPlayerBar().getCharacterMonitor().addAction(Actions.moveBy(0, 70, gameplaySetup.changeRequiredCharacterTime));
                AudioManager.instance.playSound(Asset.instance.sound.pop, 0.2f);

                //
                player1.getPlayerTab().setVisible(false);
                player2.getPlayerTab().setVisible(false);

                //
                playTimer.start(playTimeLeft);
                break;
        }
    }

    public void onNotify(PlayTimer.PlayTimerCommand command){
        switch (command){
            case TICK:
                if(currentState == State.PLAY) {
                    playTimeLeft--;

                    //convert "second to minute:second" text
                    String playTimeLeftString = Utility.convertSecondToMinuteFormat(playTimeLeft);

                    player1.getPlayerBar().setTime(playTimeLeftString);
                    player2.getPlayerBar().setTime(playTimeLeftString);

                    //time's up
                    if (playTimeLeft == 0) {
                        endGame();
                    }
                }
                break;
        }
    }

    public void onNotify(Computer.ComputerCommand command, Computer computer){
        //Gdx.app.log("", message);
        Player controlledPlayer = computer.getControlledPlayer();

        switch (command){
            case TOUCH_READY_BUTTON:
                if(!isPaused()) {
                    controlledPlayer.getPlayerTab().getReadyButton().setChecked(!controlledPlayer.getPlayerTab().getReadyButton().isChecked());
                    AudioManager.instance.playSound(Asset.instance.sound.lightButton);
                }
                break;
            case TOUCH_REMATCH_BUTTON:
                if(!isPaused()) {
                    controlledPlayer.getPlayerTab().getRematchButton().setChecked(!controlledPlayer.getPlayerTab().getRematchButton().isChecked());
                    AudioManager.instance.playSound(Asset.instance.sound.lightButton);
                }
                break;
            case TOUCH_CHARACTER:
                Character selectedCharacter = controlledPlayer.getRequiredCharacter();
                touchCharacter(selectedCharacter);
                break;
            case TOUCH_SPECIAL_EVENT_BUTTON:
                SpecialEventButton selectedSpecialEventButton = computer.pickSpecialEventButton(specialEventButtons);
                if (selectedSpecialEventButton != null) {
                    if (selectedSpecialEventButton.isTouchable()) {
                        selectedSpecialEventButton.select();
                        //Gdx.app.log("", "select special event button: player "+controlledPlayer.getPlayerNum());
                    }
                }
                break;
        }
    }

    public void onNotify(SpecialEventSpawner.SpecialEventSpawnerCommand command){
        switch (command){
            case CREATE_SPECIAL_EVENT_BUTTON:
                if(currentState == State.PLAY) {
                    if (gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.MIX)) {
                        int randomNum = MathUtils.random(100);
                        if (randomNum < 50) {
                            createSpecialEventButton();
                        } else {
                            createSpecialEventButtons();
                        }
                    } else if (gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.AUTO)) {
                        createSpecialEventButton();
                    } else if (gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.MANUAL)) {
                        createSpecialEventButtons();
                    }
                }

                break;
        }
    }

    public void onNotify(SpecialEventButton.SpecialEventButtonCommand command, SpecialEventButton specialEventButton){
        switch (command){
            case SHOW:
                break;
            case SELECT:
                playersReceiveSpecialEvent(specialEventButton);
                if(specialEventButtons.size > 1) {
                    hideUnusedSpecialEventButtons(specialEventButton);
                }
                break;
            case UNSELECT:
                specialEventButton.removeArrows();
                playersRemoveSpecialEvent();
                break;
            case AWAIT:
                specialEventButton.createArrows(uiLayer);
                break;
            case HIDE:
                specialEventButton.removeArrows();
                break;
            case DISCARD:
                specialEventButtons.removeValue(specialEventButton, true);
                break;
        }
    }

    public void createSpecialEventButton(){
        //create button
        final SpecialEventButton specialEventButton = SpecialEventManager.instance.createSpecialEventButton();
        specialEventButton.setPlayScreen(this);
        specialEventButtons.add(specialEventButton);
        groundLayer.addActor(specialEventButton);
        //specialEventButton.debug();

        //set positions
        boolean startLeft = (MathUtils.random(0,1) == 0) ? true : false;

        float startX = 0;
        float middleX = 0;
        float endX = 0;
        if(startLeft){
            startX = SPECIAL_EVENT_BUTTON_START_X_LEFT;
            middleX = getGame().getWorldWidth() / 2;
            endX = SPECIAL_EVENT_BUTTON_START_X_RIGHT;
        }else{
            startX = SPECIAL_EVENT_BUTTON_START_X_RIGHT;
            middleX = getGame().getWorldWidth() / 2;
            endX =  SPECIAL_EVENT_BUTTON_START_X_LEFT;
        }

        specialEventButton.setStartX(startX);
        specialEventButton.setMiddleX(middleX);
        specialEventButton.setEndX(endX);
        specialEventButton.setPosition(specialEventButton.getStartX(), getGame().getWorldHeight() / 2, Align.center);


        //actions
        specialEventButton.showAndSelect();

    }

    public void createSpecialEventButtons(){
        //create buttons
        Array<SpecialEventButton> buttons = SpecialEventManager.instance.createSpecialEventButtons();

        //set buttons
        boolean startLeft = (MathUtils.random(0,1) == 0) ? true : false;

        for (int i = 0; i < buttons.size; i++){
            float startX = 0;
            float middleX = 0;
            float endX = 0;
            if(startLeft) {
                startX = SPECIAL_EVENT_BUTTON_START_X_LEFT - ((buttons.size - i - 1) * 60);
                middleX = ((getGame().getWorldWidth() / 2 - ((buttons.size * 60) / 2)) + 30 + (i * 60));
                endX = SPECIAL_EVENT_BUTTON_START_X_RIGHT + (i * 60);
            }else{
                startX = SPECIAL_EVENT_BUTTON_START_X_RIGHT + (i * 60);
                middleX = ((getGame().getWorldWidth() / 2 - ((buttons.size * 60) / 2)) + 30 + (i * 60));
                endX = SPECIAL_EVENT_BUTTON_START_X_LEFT - ((buttons.size - i - 1) * 60);
            }

            final SpecialEventButton specialEventButton = buttons.get(i);
            specialEventButton.setPlayScreen(this);
            specialEventButton.setStartX(startX);
            specialEventButton.setMiddleX(middleX);
            specialEventButton.setEndX(endX);
            specialEventButton.setPosition(startX, getGame().getWorldHeight() / 2, Align.center);
            groundLayer.addActor(specialEventButton);
            specialEventButtons.add(specialEventButton);
            //specialEventButton.debug();

            //add listener
            specialEventButton.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    specialEventButton.select();

                    return true;
                }
            });

            //actions
            specialEventButton.showAndAwait();
        }
    }

    public void playersReceiveSpecialEvent(SpecialEventButton specialEventButton){
        SpecialEventsMonitor.setOptions(specialEventButton.getSpecialEvent());

        //players receive special event
        switch (specialEventButton.getReceivedPlayer()) {
            case BOTH:
                player1.receiveSpecialEvent(specialEventButton.getSpecialEvent());
                player2.receiveSpecialEvent(specialEventButton.getSpecialEvent());
                break;
            case PLAYER_1:
                player1.receiveSpecialEvent(specialEventButton.getSpecialEvent());
                break;
            case PLAYER_2:
                player2.receiveSpecialEvent(specialEventButton.getSpecialEvent());
                break;
        }
    }

    public void playersRemoveSpecialEvent(){
        //remove special event from player
        for (Player player : players){
            if(player.getCurrentSpecialEvent() != null){
                player.removeSpecialEvent();
            }
        }
    }

    private void hideUnusedSpecialEventButtons(SpecialEventButton specialEventButton){
        //remove unused special event buttons
        for (int i = 0; i < specialEventButtons.size; i++) {
            final SpecialEventButton unusedButton = specialEventButtons.get(i);

            if (unusedButton != specialEventButton) {

                float endX = 0;
                if (unusedButton.getX() < specialEventButton.getX()) {
                    endX = SPECIAL_EVENT_BUTTON_START_X_LEFT - ((specialEventButtons.size - i - 1) * 60);
                } else {
                    endX = SPECIAL_EVENT_BUTTON_START_X_RIGHT + (i * 60);
                }

                unusedButton.setEndX(endX);
                unusedButton.removeArrows();
                unusedButton.hide();
            }
        }

    }

    public Player getPlayer1(){
        return player1;
    }

    public Player getPlayer2(){
        return player2;
    }

    public State getcurrentState(){
        return currentState;
    }

    public Array<SpecialEventButton> getSpecialEventButtons(){
        return specialEventButtons;
    }







}
