package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.poopscounter.util.Asset;

public class RulesWindow extends NutScrollPane {
    GameplaySetup gameplaySetup;

    public RulesWindow(GameplaySetup gameplaySetup){
        super(null, Asset.instance.skin.gameUISkin, "rules");

        this.gameplaySetup = gameplaySetup;

        setSize(371,281);

        //play
        Label playHead = new Label("Playing the game",
                Asset.instance.skin.gameUISkin, "droid_sans_23");
        playHead.setColor(Color.YELLOW);

        Label playBody1 = new Label("Tap your finger on a poop that looks similar to the one in the bubble, sending it to your friend's side. " +
                "If you tap on the wrong poop, that poop won't be active for a period of time.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        playBody1.setWrap(true);

        //end
        Label EndHead = new Label("Game End",
                Asset.instance.skin.gameUISkin, "droid_sans_23");
        EndHead.setColor(Color.YELLOW);

        Label endBody1 = new Label("-If all the poops are in one side, that side will lose.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        endBody1.setWrap(true);;

        Label endBody2 = new Label("-When the time's up, the side that has more poops will lose. " +
                "If both sides have the same number of poops, it will result in a draw.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        endBody2.setWrap(true);

        //special events
        Label specialEventsHead = new Label("Special Events",
                Asset.instance.skin.gameUISkin, "droid_sans_23");
        specialEventsHead.setColor(Color.YELLOW);

        Label specialEventsBody1 = new Label("There will be special events during the match. " +
                "The events include ones that aid you and ones that obstruct you.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        specialEventsBody1.setWrap(true);

        Label specialEventsBody2 = new Label("", Asset.instance.skin.gameUISkin, "droid_sans_bold_18");
        specialEventsBody2.setWrap(true);
        Label specialEventsBody3 = new Label("", Asset.instance.skin.gameUISkin, "droid_sans_18");
        specialEventsBody3.setWrap(true);

        if(gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.AUTO)) {
            specialEventsBody2.setText("Special Events Mode: Both");
            specialEventsBody3.setText("When a special event icon appears, both players will receive the same special events.");
        }else if(gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.MANUAL)) {
            specialEventsBody2.setText("Special Events Mode: Fight");
            specialEventsBody3.setText("A set of special event icons will appear, each with an arrow attached. " +
                    "The arrows indicate which player will be affected by the event once that event icon is tapped. " +
                    "Players have to try to tap on the icons that are beneficial to them.");
        }else if(gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.MIX)) {
            specialEventsBody2.setText("Special Events Mode: Mixed");
            specialEventsBody3.setText("This is a mix of Both and Fight Modes. " +
                    "In this Mode, there are special events that affect both players in the same way, and special events that affect players in different ways. " +
                    "There are 2 types of special event icons. " +
                    "The first type is icons without arrows. " +
                    "They will affect both players in the same way. " +
                    "The second type comes with arrows attached. " +
                    "The arrows indicate which player will be affected by the event. " +
                    "Both players need to try obtaining special events that will be beneficial to them by tapping on the 2nd type of icons when they appear.");
        }

        Label specialEventsBody4 = new Label("Each special event icon will affect you in different ways:",
                Asset.instance.skin.gameUISkin, "droid_sans_bold_18");
        specialEventsBody4.setWrap(true);

        SpecialEventButton shieldButton = new SpecialEventButton(SpecialEventManager.SpecialEvent.SHIELD);
        Label shieldDes = new Label("Shield -  You'll be alright even if you tap on the wrong poop. The poop won't be inactive.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        shieldDes.setWrap(true);

        SpecialEventButton hintButton = new SpecialEventButton(SpecialEventManager.SpecialEvent.HINT);
        Label hintDes = new Label("Hint - There will be a hint telling you which poop you need to tap on.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        hintDes.setWrap(true);

        SpecialEventButton spinButton = new SpecialEventButton(SpecialEventManager.SpecialEvent.SPIN);
        Label spinDes = new Label("Spin - When you kick a poop to the other side, it'll spin for a moment, causing confusion to your opponent.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        spinDes.setWrap(true);

        SpecialEventButton filpButton = new SpecialEventButton(SpecialEventManager.SpecialEvent.FLIP);
        Label flipDes = new Label("Flip - The poop in the bubble will appear upside down.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        flipDes.setWrap(true);

        SpecialEventButton lockButton = new SpecialEventButton(SpecialEventManager.SpecialEvent.LOCK);
        Label lockDes = new Label("Lock - You won't be able to touch the screen for a moment.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        lockDes.setWrap(true);

        SpecialEventButton fogButton = new SpecialEventButton(SpecialEventManager.SpecialEvent.FOG);
        Label fogDes = new Label("Fog - A fog will appear causing partial invisibility.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        fogDes.setWrap(true);

        SpecialEventButton mudButton = new SpecialEventButton(SpecialEventManager.SpecialEvent.MUD);
        Label mudDes = new Label("Mud - Mud will be thrown on the screen causing partial invisibility.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        mudDes.setWrap(true);

        SpecialEventButton bugButton = new SpecialEventButton(SpecialEventManager.SpecialEvent.BUG);
        Label bugDes = new Label("Bug - A number of bugs will pass by causing partial invisibility.",
                Asset.instance.skin.gameUISkin, "droid_sans_18");
        bugDes.setWrap(true);


        //table
        Table table = new Table();

        table.add(playHead).colspan(2).padTop(0);
        table.row();
        table.add(playBody1).align(Align.left).width(300).colspan(2);
        table.row();
        table.add(EndHead).colspan(2).padTop(10);
        table.row();
        table.add(endBody1).align(Align.left).width(300).colspan(2);
        table.row();
        table.add(endBody2).align(Align.left).width(300).colspan(2);
        table.row();

        if(!gameplaySetup.specialEventMode.equals(GameplaySetup.SpecialEventMode.NO)) {
            table.add(specialEventsHead).colspan(2).padTop(10);
            table.row();
            table.add(specialEventsBody1).colspan(2).align(Align.left).width(300).padBottom(10);
            table.row();
            table.add(specialEventsBody2).colspan(2).align(Align.left).width(300).padBottom(0);
            table.row();
            table.add(specialEventsBody3).colspan(2).align(Align.left).width(300).padBottom(10);
            table.row();
            table.add(specialEventsBody4).colspan(2).align(Align.left).width(300).padBottom(0);
            table.row();
            table.add(shieldButton).padBottom(10);
            table.add(shieldDes).align(Align.left).width(250).padBottom(10).padLeft(10);
            table.row();
            table.add(hintButton).padBottom(10);
            table.add(hintDes).align(Align.left).width(250).padBottom(10).padLeft(10);
            table.row();
            table.add(spinButton).padBottom(10);
            table.add(spinDes).align(Align.left).width(250).padBottom(10).padLeft(10);
            table.row();
            table.add(filpButton).padBottom(10);
            table.add(flipDes).align(Align.left).width(250).padBottom(10).padLeft(10);
            table.row();
            table.add(lockButton).padBottom(10);
            table.add(lockDes).align(Align.left).width(250).padBottom(10).padLeft(10);
            table.row();
            table.add(fogButton).padBottom(10);
            table.add(fogDes).align(Align.left).width(250).padBottom(10).padLeft(10);
            table.row();
            table.add(mudButton).padBottom(10);
            table.add(mudDes).align(Align.left).width(250).padBottom(10).padLeft(10);
            table.row();
            table.add(bugButton).padBottom(10);
            table.add(bugDes).align(Align.left).width(250).padBottom(10).padLeft(10);
        }

        //table.debug();
        setWidget(table);
        setFadeScrollBars(false);
        setScrollingDisabled(true, false);
        setOverscroll(false, false);
        layout();
    }

    public class SpecialEventButton extends ImageButton{
        public SpecialEventButton(SpecialEventManager.SpecialEvent specialEvent){
            super(Asset.instance.skin.gameUISkin, "special_event_button");

            BaseActor eventIcon = new BaseActor( Asset.instance.skin.gameUISkin.getAtlas().findRegion(specialEvent.buttonStyle+ "_icon"));
            add(eventIcon);

            setTouchable(Touchable.disabled);

        }
    }


}
