package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import static com.nutslaboratory.poopscounter.object.SpecialEventManager.SpecialEvent;

public class Player implements Disposable{
    private static int PLAYER_SIZE = 60;
    private static int START_Y = 400;
    private static int OFFSET_X = 35;;
    private static int OFFSET_Y = 40;

    public static int player1CharacterOffsetX = 0 + OFFSET_X;
    public static int player1CharacterOffsetY = START_Y - OFFSET_Y - PLAYER_SIZE;
    public static int player2CharacterOffsetX = 480 - OFFSET_X - PLAYER_SIZE;
    public static int player2CharacterOffsetY = START_Y + OFFSET_Y;
    public static int characterSpaceX = 70;
    public static int characterSpaceY = 65;

    public static int totalCharacterColumn = 6;
    public static int totalCharacterRow = 4;

    private int playerNum;
    private int totalCharacter;
    private int point;

    private Array<Array<Character>> characterTable;

    private Character requiredCharacter;

    private PlayerBar playerBar;
    private PlayerTab playerTab;
    private SpecialEventsMonitor specialEventsMonitor;

    private SpecialEvent currentSpecialEvent;

    public int touchPointer;

    public Player(int playerNum, int totalCharacter) {
        this.playerNum = playerNum;
        this.totalCharacter = totalCharacter;

        point = 0;

        currentSpecialEvent = null;

        touchPointer = -1;
    }

    public void traceCharacterTable() {
        //player 2
        if (playerNum == 2) {
            System.out.println("*************** player " + playerNum + " ***************");
            for (int row = totalCharacterRow - 1; row >= 0; row--) {
                for (int column = totalCharacterColumn - 1; column >= 0; column--) {
                    if (characterTable.get(column).get(row) == null) {
                        System.out.print("____");
                    } else {
                        System.out.print(characterTable.get(column).get(row).getName());
                    }

                    System.out.print("    ");
                }
                System.out.println();
            }
            System.out.println("*************** player " + playerNum + " ***************");
        }
        //player 1
        else if (playerNum == 1) {
            System.out.println("*************** player " + playerNum + " ***************");
            for (int row = 0; row < totalCharacterRow; row++) {
                for (int column = 0; column < totalCharacterColumn; column++) {
                    if (characterTable.get(column).get(row) == null) {
                        System.out.print("____");
                    } else {
                        System.out.print(characterTable.get(column).get(row).getName());
                    }
                    System.out.print("    ");
                }
                System.out.println();
            }
            System.out.println("*************** player " + playerNum + " ***************");
        }
    }



    public int getPlayerNum(){
        return playerNum;
    }

    public void setTotalCharacter(int totalCharacter){
        this.totalCharacter = totalCharacter;
    }

    public int getTotalCharacter(){
        return  totalCharacter;
    }

    public void setPoint(int point){
        this.point = point;

        playerBar.setPoint(point);
    }

    public int getPoint(){
        return  point;
    }

    public void setCharacterTable(Array<Array<Character>> characterTable) {
        this.characterTable = characterTable;
    }

    public Array<Array<Character>> getCharacterTable(){
        return characterTable;
    }

    public void setRequiredCharacter(){
        ////set required character
        Array<Character> possibleRequiredCharacters = new Array<com.nutslaboratory.poopscounter.object.Character>();

        for(int column = 0; column < totalCharacterColumn; column++){
            for(int row = 0; row < totalCharacterRow; row++){
                Character tempCharacter = characterTable.get(column).get(row);

                if(tempCharacter != null){
                    possibleRequiredCharacters.add(tempCharacter);
                }
            }
        }

        //
        requiredCharacter = possibleRequiredCharacters.random();
        //Gdx.app.log("", requiredCharacter.getName());

        //
        playerBar.getCharacterMonitor().setCharacter(requiredCharacter);


    }

    public Character getRequiredCharacter(){
        return requiredCharacter;
    }


    public void setPlayerBar(PlayerBar playerBar){
        this.playerBar = playerBar;
    }

    public PlayerBar getPlayerBar(){
        return  playerBar;
    }

    public void setPlayerTab(PlayerTab playerTab){
        this.playerTab = playerTab;
    }

    public PlayerTab getPlayerTab(){
        return  playerTab;
    }

    public void setSpecialEventsMonitor(SpecialEventsMonitor specialEventsMonitor){
        this.specialEventsMonitor = specialEventsMonitor;


    }

    public SpecialEventsMonitor getSpecialEventsMonitor(){
        return  specialEventsMonitor;
    }


    public SpecialEvent getCurrentSpecialEvent(){
        return currentSpecialEvent;
    }

    public void receiveSpecialEvent(SpecialEvent specialEvent){
        currentSpecialEvent = specialEvent;

        switch (currentSpecialEvent){
            case FLIP:
                playerBar.getCharacterMonitor().flip();
                break;
            case LOCK:
                specialEventsMonitor.lock(true);
                break;
            case FOG:
                specialEventsMonitor.fog(true);
                break;
            case MUD:
                specialEventsMonitor.mud(true);
                break;
            case BUG:
                specialEventsMonitor.bug(true);
                break;
            case SHIELD:
                specialEventsMonitor.shield(true);
                break;
            case HINT:
                specialEventsMonitor.hint(true);

                if(playerNum == 1){
                    int x = (Player.player1CharacterOffsetX + (requiredCharacter.getColumn() * Player.characterSpaceX))+30;
                    int y = (Player.player1CharacterOffsetY - (requiredCharacter.getRow() * Player.characterSpaceY)+30);
                    specialEventsMonitor.setHintLocation(x,y, Align.center);
                }else{
                    int x = (Player.player1CharacterOffsetX + (requiredCharacter.getColumn() * Player.characterSpaceX))+30;
                    int y = (Player.player1CharacterOffsetY - (requiredCharacter.getRow() * Player.characterSpaceY)+30);
                    specialEventsMonitor.setHintLocation(x,y, Align.center);
                }

                break;
            case SPIN:
                specialEventsMonitor.spin(true);
                break;
        }
    }

    public void removeSpecialEvent(){
        switch (currentSpecialEvent){
            case FLIP:
                playerBar.getCharacterMonitor().flip();
                break;
            case LOCK:
                specialEventsMonitor.lock(false);
                break;
            case FOG:
                specialEventsMonitor.fog(false);
                break;
            case MUD:
                specialEventsMonitor.mud(false);
                break;
            case BUG:
                specialEventsMonitor.bug(false);
                break;
            case SHIELD:
                specialEventsMonitor.shield(false);
                break;
            case HINT:
                specialEventsMonitor.hint(false);
                break;
            case SPIN:
                specialEventsMonitor.spin(false);
                break;
        }

        currentSpecialEvent = null;
    }

    public void pauseSound(){
        //pause special events monitor
        if(specialEventsMonitor != null) {
            specialEventsMonitor.pauseSound();
        }

        //pause characters
        for(int column = 0; column < Player.totalCharacterColumn; column++) {
            for (int row = 0; row < Player.totalCharacterRow; row++) {
                Character tempCharacter = characterTable.get(column).get(row);

                if (tempCharacter != null) {
                    tempCharacter.pauseSound();
                }
            }
        }
    }

    public void resumeSound(){
        //resume special events monitor
        if(specialEventsMonitor != null) {
            specialEventsMonitor.resumeSound();
        }

        //resume characters
        for(int column = 0; column < Player.totalCharacterColumn; column++) {
            for (int row = 0; row < Player.totalCharacterRow; row++) {
                Character tempCharacter = characterTable.get(column).get(row);

                if (tempCharacter != null) {
                    tempCharacter.resumeSound();
                }
            }
        }
    }

    public void muteSound(){
        if(specialEventsMonitor != null) {
            specialEventsMonitor.muteSound();
        }

        for(int column = 0; column < Player.totalCharacterColumn; column++) {
            for (int row = 0; row < Player.totalCharacterRow; row++) {
                Character tempCharacter = characterTable.get(column).get(row);

                if (tempCharacter != null) {
                    tempCharacter.muteSound();
                }
            }
        }
    }

    public void unmuteSound(){
        if(specialEventsMonitor != null) {
            specialEventsMonitor.unmuteSound();
        }

        for(int column = 0; column < Player.totalCharacterColumn; column++) {
            for (int row = 0; row < Player.totalCharacterRow; row++) {
                Character tempCharacter = characterTable.get(column).get(row);

                if (tempCharacter != null) {
                    tempCharacter.unmuteSound();
                }
            }
        }
    }


    @Override
    public void dispose() {
        //dispose special events monitor
        if(specialEventsMonitor != null) {
            specialEventsMonitor.dispose();
        }

        //dispose characters
        for(int column = 0; column < Player.totalCharacterColumn; column++) {
            for (int row = 0; row < Player.totalCharacterRow; row++) {
                Character tempCharacter = characterTable.get(column).get(row);

                if (tempCharacter != null) {
                    tempCharacter.dispose();
                }
            }
        }
    }
}
