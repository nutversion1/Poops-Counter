package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.nutslaboratory.poopscounter.object.SpecialEventButton.ReceivedPlayer;

public class SpecialEventManager {

    private SpecialEvent forcedSpecialEvent = null;

    public static enum SpecialEvent {
        SHIELD(EffectType.GOOD, "shield_event", new Vector2(5f,7f), new Vector2(5f,7f)),
        HINT(EffectType.GOOD, "hint_event", new Vector2(2f,4f), new Vector2(2f,3f)),
        SPIN(EffectType.GOOD, "spin_event", new Vector2(2f,7f), new Vector2(3f,6f)),
        FLIP(EffectType.BAD, "flip_event", new Vector2(3f,8f), new Vector2(3f,5f)),
        LOCK(EffectType.BAD, "lock_event", new Vector2(1f,4f), new Vector2(1f,2f)),
        FOG(EffectType.BAD, "fog_event", new Vector2(3f,6f), new Vector2(3f,4f)),
        MUD(EffectType.BAD, "mud_event", new Vector2(2f,5f), new Vector2(2f,3f)),
        BUG(EffectType.BAD, "bug_event", new Vector2(3f,7f), new Vector2(3f,5f));


        public EffectType effectType;
        public String buttonStyle;
        public Vector2 autoEffectTime;
        public Vector2 manualEffectTime;

        SpecialEvent(EffectType effectType, String buttonStyle, Vector2 autoEffectTime, Vector2 manualEffectTime){
            this.effectType = effectType;
            this.buttonStyle = buttonStyle;
            this.autoEffectTime = autoEffectTime;
            this.manualEffectTime = manualEffectTime;
        }
    }

    public static enum EffectType{
        GOOD, BAD
    }

    private Array<SpecialEvent> specialEvents;

    public static final SpecialEventManager instance = new SpecialEventManager();

    private SpecialEventManager(){
        specialEvents = new Array<SpecialEvent>();

        init();
    }

    private void init(){
        specialEvents.clear();

        if(forcedSpecialEvent == null) {
            specialEvents.add(SpecialEvent.SHIELD);
            specialEvents.add(SpecialEvent.HINT);
            specialEvents.add(SpecialEvent.SPIN);
            specialEvents.add(SpecialEvent.FLIP);
            specialEvents.add(SpecialEvent.LOCK);
            specialEvents.add(SpecialEvent.FOG);
            specialEvents.add(SpecialEvent.MUD);
            specialEvents.add(SpecialEvent.BUG);
        }else{
            for(int i = 0; i < 8; i++){
                specialEvents.add(forcedSpecialEvent);
            }
        }
    }

    public SpecialEventButton createSpecialEventButton(){
        SpecialEvent specialEvent = specialEvents.random();

        SpecialEventButton specialEventButton = new SpecialEventButton(specialEvent, ReceivedPlayer.BOTH);

        return specialEventButton;
    }

    public Array<SpecialEventButton> createSpecialEventButtons(){
        //create effect types
        Array<EffectType> effectTypes = new Array<EffectType>();
        int totalButtons = MathUtils.random(1,3);

        for(int i = 0; i < totalButtons; i++){
            EffectType effectType = null;

            if(forcedSpecialEvent == null) {
                if (MathUtils.random(0, 1) == 0) {
                    effectType = EffectType.GOOD;
                } else {
                    effectType = EffectType.BAD;
                }
            }else{
                effectType = forcedSpecialEvent.effectType;
            }

            effectTypes.add(effectType);
        }

        //Gdx.app.log("", effectTypes+"");


        //create player1 & player2's special event buttons
        Array<SpecialEventButton> player1SpecialEventButtons = getPlayerSpecialEventButtons(getPlayerSpecialEvents(effectTypes), ReceivedPlayer.PLAYER_1);
        Array<SpecialEventButton> player2SpecialEventButtons = getPlayerSpecialEventButtons(getPlayerSpecialEvents(effectTypes), ReceivedPlayer.PLAYER_2);

        //mix player1 & player2's special event buttons to one array
        Array<SpecialEventButton> specialEventButtons = new Array<SpecialEventButton>();
        specialEventButtons.addAll(player1SpecialEventButtons);
        specialEventButtons.addAll(player2SpecialEventButtons);
        specialEventButtons.shuffle();

        return specialEventButtons;
    }

    private  Array<SpecialEventButton> getPlayerSpecialEventButtons(Array<SpecialEvent> specialEvents, ReceivedPlayer receivedPlayer){
        Array<SpecialEventButton> specialEventButtons = new Array<SpecialEventButton>();

        for(SpecialEvent specialEvent : specialEvents){
            SpecialEventButton specialEventButton = new SpecialEventButton(specialEvent, receivedPlayer);
            specialEventButtons.add(specialEventButton);
        }

        return specialEventButtons;
    }

    private  Array<SpecialEvent> getPlayerSpecialEvents(Array<EffectType> effectTypes){
        Array<SpecialEvent> playerSpecialEvents = new Array<SpecialEvent>();

        Array<SpecialEvent> copiedSpecialEvents = new Array<SpecialEvent>(specialEvents);
        copiedSpecialEvents.shuffle();

        for(EffectType effectType : effectTypes){
            for(SpecialEvent specialEvent : copiedSpecialEvents){
                if(effectType == specialEvent.effectType){
                    playerSpecialEvents.add(specialEvent);
                    copiedSpecialEvents.removeValue(specialEvent, true);
                    break;
                }
            }
        }

        return playerSpecialEvents;
    }

    public void setForcedSpecialEvent(SpecialEvent forcedSpecialEvent){
        this.forcedSpecialEvent = forcedSpecialEvent;

        init();
    }

    public SpecialEvent getForcedSpecialEvent(){
        return forcedSpecialEvent;
    }


}
