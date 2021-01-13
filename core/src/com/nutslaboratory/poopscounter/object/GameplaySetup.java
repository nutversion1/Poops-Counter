package com.nutslaboratory.poopscounter.object;

public class GameplaySetup {

    public class Opponent{
        public static final String HUMAN = "human";
        public static final String COM = "com";
    }

    public class SpecialEventMode{
        public static final String NO = "n/a";
        public static final String AUTO = "both";
        public static final String MANUAL = "fight";
        public static final String MIX = "mixed";
    }

    public static final int DEFAULT_TOTAL_TIME = 60;
    public static final int DEFAULT_TOTAL_CHARACTER = 8;
    public static final String DEFAULT_OPPONENT = Opponent.HUMAN;
    public static final String DEFAULT_SPECIAL_EVENT_MODE = SpecialEventMode.AUTO;

    public int totalTime;
    public float changeRequiredCharacterTime;
    public int totalCharacter;
    public String specialEventMode;
    public String opponent;



    public  GameplaySetup(){
        totalTime = DEFAULT_TOTAL_TIME;
        totalCharacter = DEFAULT_TOTAL_CHARACTER;
        opponent = DEFAULT_OPPONENT;
        specialEventMode = DEFAULT_SPECIAL_EVENT_MODE;

        changeRequiredCharacterTime = 0.1f;
    }
}
