package com.nutslaboratory.poopscounter.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.game.BaseGame;
import com.nutslaboratory.nutengine.screen.BaseScreen;
import com.nutslaboratory.nutengine.screen.transition.ScreenTransitionSlideBoth;
import com.nutslaboratory.poopscounter.game.PoopsCounter;
import com.nutslaboratory.poopscounter.util.Asset;

public class AboutScreen extends BaseScreen {
    private Table table1, table2;

    private CreditManager creditManager;

    public AboutScreen(BaseGame game){
        super(game);
    }

    @Override
    public void show(){
        init();
    }

    private void init(){
        //
        BaseActor background = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("about/about_background"));
        addActorToStage(background);

        //
        if(((PoopsCounter)getGame()).isAndroid()) {
            createVersionText();
        }

        //
        creditManager = new CreditManager();
        creditManager.add(createCredit1());
        creditManager.add(createCredit2());
        creditManager.add(createCredit3());
        creditManager.add(createCredit4());
        creditManager.add(createCredit5());
        creditManager.add(createCredit6());

        getStage().addAction(Actions.sequence(
                Actions.delay(0.2f),
                Actions.addAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        creditManager.start();
                        return true;
                    }
                }
        )));

    }

    private void createVersionText() {
        int versionCode = ((PoopsCounter)getGame()).getHandler().getVersionCode();
        String versionName = ((PoopsCounter)getGame()).getHandler().getVersionName();

        /*
        String versionStr = "v "+versionName +
                " (" + versionCode + ")";
        */
        String versionStr = "v "+versionName;

        //
        Label versionText = new Label(versionStr, Asset.instance.skin.gameUISkin, "borders_divide_18");
        versionText.setSize(60, 20);
        versionText.setPosition(330, 140);
        versionText.setAlignment(Align.right);
        versionText.setColor(Color.BROWN);
        addActorToStage(versionText);
        //versionText.debug();

    }

    private Credit createCredit1(){
        Credit credit = new Credit();
        credit.add(createHeadText("Game Design & Programming"));
        credit.add(createText("Nuttapong Phisitbutr"));
        credit.add(createHeadText("Poops Design"));
        credit.add(createText("Warut Pokabarn"));
        credit.add(createHeadText("Art"));
        credit.add(createText("Rapeepan Maliwan"));


        return credit;
    }

    private Credit createCredit2() {
        Credit credit = new Credit();
        credit.add(createHeadText("Music"));
        credit.add(createSmallText("Doobly Doo"));
        credit.add(createSmallText("Voice Over Under"));
        credit.add(createSmallText("Upbeat Forever"));
        credit.add(createSmallText("by Kevin MacLeod (incompetech.com)"));
        credit.add(createSmallText("Licensed under the Creative Commons 3.0:"));
        credit.add(createSmallText("By Attribution license."));

        return credit;
    }

    private Credit createCredit3() {
        Credit credit = new Credit();
        credit.add(createHeadText("Sound"));
        credit.add(createSmallText("From freesound.org:"));
        credit.add(createSmallText("error by fins"));
        credit.add(createSmallText("Beep Space Button by GameAudio"));
        credit.add(createSmallText("water_splash.wav by soundscalpel.com"));
        credit.add(createSmallText("race_countdown1.mp3 by Unnecro"));
        credit.add(createSmallText("button_off.wav by florian_reinke"));
        credit.add(createSmallText("Throwing / Whip Effect by denao270"));
        credit.add(createSmallText("pop4.wav by muel2002"));
        credit.add(createSmallText("Button press.wav by ZvinbergsA"));
        credit.add(createSmallText("Sci-fi button click by dotY21"));
        credit.add(createSmallText("cartoon-throw.wav by copyc4t"));


        return credit;
    }

    private Credit createCredit4() {
        Credit credit = new Credit();
        credit.add(createHeadText("Sound"));
        credit.add(createSmallText("From freesound.org:"));
        credit.add(createSmallText("swosh-22.flac by qubodup"));
        credit.add(createSmallText("blip_2.wav by soundnimja"));
        credit.add(createSmallText("Sniper Scope zoom in by Supakid13"));
        credit.add(createSmallText("Locking a door (no key).wav by grimny"));
        credit.add(createSmallText("Flies by miklovan"));
        credit.add(createSmallText("SyntheticWind-HektorSound.aif by Hektorsound"));
        credit.add(createSmallText("Magic SFX for Games by suntemple"));
        credit.add(createSmallText("Squash by Natty23"));
        credit.add(createSmallText("Game Powerup by josepharaoh99"));
        credit.add(createSmallText("Jump sound or Power Up sound by Cman634"));


        return credit;
    }


    private Credit createCredit5(){
        Credit credit = new Credit();
        credit.add(createHeadText("Game Engine"));
        credit.add(createText("LibGDX"));
        credit.add(createHeadText("Special Thanks To"));
        credit.add(createText("Pinda Phisitbutr"));
        credit.add(createHeadText("Contact"));
        credit.add(createText("nutversion1@hotmail.com"));

        return credit;
    }

    private Credit createCredit6(){
        Credit credit = new Credit();
        credit.add(createText("Thanks for downloading! :)"));

        return credit;
    }

    private Label createHeadText(String string){
        Label text = new Label(string, Asset.instance.skin.gameUISkin, "borders_divide_28");
        text.setColor(Color.RED);
        text.setAlignment(Align.center);
        text.setWidth(300);
        //text.setDebug(true);

        return text;
    }

    private Label createText(String string){
        Label text = new Label(string, Asset.instance.skin.gameUISkin, "borders_divide_23");
        text.setColor(Color.BLACK);
        text.setAlignment(Align.center);
        text.setWidth(300);
        //text.setDebug(true);

        return text;
    }

    private Label createSmallText(String string){
        Label text = new Label(string, Asset.instance.skin.gameUISkin, "borders_divide_18");
        text.setColor(Color.BLACK);
        text.setAlignment(Align.center);
        text.setWidth(300);
        //text.setDebug(true);

        return text;
    }

    @Override
    public  void update(float delta){
        super.update(delta);

        creditManager.update(delta);
    }

    @Override
    public void pressBack(){
        ((PoopsCounter)getGame()).transferScreen(new StartScreen(getGame()), ScreenTransitionSlideBoth.LEFT);
    }



    public class CreditManager {
        private Array<Credit> credits;
        private Credit activeCredit;
        private int currentCreditIndex = 0;

        public CreditManager(){
            credits = new Array<Credit>();
        }

        public void add(Credit credit){
            credits.add(credit);
            credit.reset();
        }

        public void start(){
            activeCredit = credits.get(currentCreditIndex);
            activeCredit.show();
        }

        public void next(){
            currentCreditIndex++;

            start();
        }

        public void reset(){
            for(Credit credit : credits){
                credit.reset();
            }
            currentCreditIndex = 0;
            start();
        }

        public boolean isLast(){
            return credits.peek() == activeCredit;
        }

        public void update(float delta){
            if(activeCredit == null){
                return;
            }

            if(!activeCredit.isAnimationing()){
                if(!activeCredit.isShowing()) {
                    activeCredit.show();
                }else if(!activeCredit.isHiding()) {
                    activeCredit.hide();
                }else if(!isLast()){
                    next();
                }else{
                    reset();

                }
            }
        }
    }

    public class Credit {
        private Array<Label> texts;

        private boolean isShowing;
        private boolean isHiding;

        private int space;
        private int height;
        private int offsetY;

        public Credit(){
            texts = new Array<Label>();
            space = 10;
            height = space;
        }

        public void add(Label text){
            texts.add(text);
            addActorToStage(text);
            height += text.getHeight() + space;
        }

        public void show(){
            //Gdx.app.log("", "show");

            isShowing = true;

            float delay = 0;

            for(Label text : texts){
                text.addAction(Actions.sequence(Actions.delay(delay), Actions.moveTo(105, text.getY(), 1f, Interpolation.bounce)));
                delay += 0.1f;
            }
        }

        public void hide(){
            //Gdx.app.log("", "hide");

            isHiding = true;

            float delay = 0;

            for(Label text : texts){
                if(texts.indexOf(text, true) == 0){
                    delay = 5f;
                }

                text.addAction(Actions.sequence(Actions.delay(delay), Actions.moveTo(-550, text.getY(), 1f, Interpolation.bounce)));
                delay += 0.1f;
            }
        }

        public void reset(){
            isShowing = false;
            isHiding = false;

            offsetY = 0;
            for(Label text : texts){
                offsetY += text.getHeight() + space;
                text.setPosition(550, (380 + height/2) - offsetY);
            }
        }

        public boolean isAnimationing(){
            return texts.peek().hasActions();
        }

        public boolean isShowing(){
            return isShowing;
        }

        public boolean isHiding(){
            return isHiding;
        }
    }
}
