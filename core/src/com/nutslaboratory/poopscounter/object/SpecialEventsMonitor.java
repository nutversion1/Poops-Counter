package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.nutslaboratory.nutengine.actor.AnimatedActor;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.poopscounter.util.Asset;
import com.nutslaboratory.poopscounter.util.GamePreferences;

import java.util.Collections;
import java.util.List;

public class SpecialEventsMonitor extends Group implements Disposable{
    private BaseActor lock;
    private BaseActor fog;
    private AnimatedActor shield;
    private BaseActor hint;
    private BaseActor spin;

    private Array<BaseActor> muds = new Array<BaseActor>();
    private Array<AnimatedActor> bugs = new Array<AnimatedActor>();

    private int bugDirection = 0;
    private float bugTimer = 0;
    private boolean bugSpawned;

    private static Array<TextureRegion> mudTextures = new Array<TextureRegion>();

    private long bugSoundId = -1;
    private long fogSoundId = -1;

    public static final float SHIELD_SOUND_VOLUME = 0.8f;
    public static final float HINT_SOUND_VOLUME = 0.7f;
    public static final float SPIN_SOUND_VOLUME = 0.8f;
    public static final float LOCK_SOUND_VOLUME = 1.0f;
    public static final float FOG_SOUND_VOLUME = 1.0f;
    public static final float MUD_SOUND_VOLUME = 0.6f;
    public static final float BUG_SOUND_VOLUME = 0.6f;

    private boolean clean = true;

    public static void setOptions(SpecialEventManager.SpecialEvent specialEvent){
        switch (specialEvent){
            case SHIELD:
                break;
            case HINT:
                break;
            case SPIN:
                break;
            case FLIP:
                break;
            case LOCK:
                break;
            case FOG:
                break;
            case MUD:
                setMudTextures();
                break;
            case BUG:
                break;
        }
    }

    private static void setMudTextures(){
        mudTextures.clear();

        Array<Integer> textureNums = new Array<Integer>();
        for(int i = 1; i <= 8; i++){
            textureNums.add(i);
        }
        textureNums.shuffle();

        int total = MathUtils.random(2,4);
        for(int i = 0; i < total; i++){
            TextureRegion mudTexture = Asset.instance.texture.gameAtlas.findRegion("play/mud"+textureNums.get(i));
            mudTextures.add(mudTexture);
        }

    }

    public SpecialEventsMonitor(){
        super();

        //debug();

    }

    @Override
    public void act(float delta){
        super.act(delta);

        updateBugs(delta);
    }

    public void shield(boolean use) {
        if (use) {
            shield = new AnimatedActor(Asset.instance.animation.shield);
            shield.setPosition(175,80, Align.center);
            shield.startAnimation();
            shield.setVisible(false);
            addActor(shield);
            //shield.debug();

            shield.addAction(Actions.sequence(
                    Actions.parallel(Actions.scaleTo(5f, 5f), Actions.alpha(0.0f), Actions.visible(true)),
                    Actions.parallel(Actions.scaleTo(1, 1, 0.7f), Actions.alpha(1, 0.7f))
            ));

            AudioManager.instance.playSound(Asset.instance.sound.shield, SHIELD_SOUND_VOLUME);

            setClean(false);
        } else {
            shield.addAction(Actions.sequence(
                    Actions.parallel(Actions.scaleTo(0, 0, 0.8f), Actions.alpha(0, 0.8f)),
                    new Action() {
                        @Override
                        public boolean act(float delta) {
                            removeActor(shield);
                            shield = null;
                            setClean(true);
                            return true;
                        }
                    }
                    )
            );
        }
    }

    public void hint(boolean use){
        if(use) {
            hint = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/hint"));
            addActor(hint);
            hint.addAction(Actions.alpha(0));
            //hint.debug();

            setClean(false);
        }else{
            hint.addAction(Actions.sequence(
                    Actions.alpha(0, 0.3f),
                    new Action() {
                        @Override
                        public boolean act(float delta) {
                            removeActor(hint);
                            hint = null;
                            setClean(true);
                            return true;
                        }
                    }
            ));
        }

    }

    public void spin(boolean use){
        if(use) {
            spin = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/spin"));
            spin.setPosition(175,80, Align.center);
            spin.setVisible(false);
            spin.addAction(Actions.forever(Actions.rotateBy(360, 2f)));
            addActor(spin);
            //spin.debug();

            spin.addAction(Actions.sequence(
                    Actions.parallel(Actions.scaleTo(5f, 5f), Actions.alpha(0.0f), Actions.visible(true)),
                    Actions.parallel(Actions.scaleTo(1, 1, 0.7f), Actions.alpha(1, 0.7f))
            ));

            AudioManager.instance.playSound(Asset.instance.sound.spin, SPIN_SOUND_VOLUME);

            setClean(false);
        }else{
            spin.addAction(Actions.sequence(
                    Actions.parallel(Actions.scaleTo(0, 0, 0.8f), Actions.alpha(0, 0.6f)),
                    new Action() {
                        @Override
                        public boolean act(float delta) {
                            removeActor(spin);
                            spin = null;
                            setClean(true);
                            return true;
                        }
                    }
            ));
        }
    }

    public void lock(boolean use){
        if(use) {
            //Gdx.app.log("", "lock");

            lock = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/lock"));
            lock.setPosition(getWidth()/2, getHeight()/2 + 20, Align.center);
            lock.setVisible(false);
            addActor(lock);
            //lock.debug();

            lock.addAction(Actions.sequence(
                    Actions.parallel(Actions.scaleTo(0.0f, 0.0f), Actions.alpha(0.0f), Actions.visible(true)),
                    Actions.parallel(Actions.scaleTo(0.9f, 0.9f, 0.2f), Actions.alpha(1, 0.2f))
            ));

            AudioManager.instance.playSound(Asset.instance.sound.lock, LOCK_SOUND_VOLUME);

            setClean(false);
        }else{
            //Gdx.app.log("", "unlock");

            lock.addAction(Actions.sequence(
                    Actions.alpha(0, 0.2f),
                    new Action() {
                        @Override
                        public boolean act(float delta) {
                            removeActor(lock);
                            lock = null;
                            setClean(true);
                            return true;
                        }
                    }
            ));
        }
    }

    public void fog(boolean use){
        if(use) {
            fog = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/fog"));
            fog.setVisible(false);
            addActor(fog);

            fog.addAction(Actions.sequence(
                    Actions.parallel(Actions.moveTo(-100,0), Actions.alpha(0.0f), Actions.visible(true)),
                    Actions.parallel(Actions.moveBy(-500, 0, 40f), Actions.alpha(0.6f, 1f))
            ));

            fogSoundId = AudioManager.instance.playSound(Asset.instance.sound.fog, FOG_SOUND_VOLUME, true);

            setClean(false);
        }else{
            fog.addAction(Actions.sequence(
                    Actions.alpha(0.0f, 1f),
                    new Action() {
                        @Override
                        public boolean act(float delta) {
                            removeActor(fog);
                            fog = null;

                            AudioManager.instance.stopSound(Asset.instance.sound.fog, fogSoundId);
                            fogSoundId = -1;

                            setClean(true);

                            return true;
                        }
                    }
            ));
        }


    }



    public void mud(boolean use){
        if(use){
            Array<Vector2> positions = new Array<Vector2>();
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 2; j++){
                    Vector2 position = new Vector2(i, j);
                    positions.add(position);
                }
            }
            positions.shuffle();

            for(int i = 0; i < mudTextures.size; i++) {
                TextureRegion mudTexture = mudTextures.get(i);
                BaseActor mud = new BaseActor(mudTexture);

                float startX = (getWidth() - (mud.getWidth()*3))/2;
                float startY = ((getHeight() - (mud.getWidth()*2))/2) + 50;

                float flexibleX = MathUtils.random(-40, 40);
                float flexibleY = MathUtils.random(-40, 40);

                float x = positions.get(i).x;
                x =  startX + (x*mud.getWidth());

                float y = positions.get(i).y;
                y =  startY + (y*mud.getHeight());

                x += flexibleX;
                y += flexibleY;

                mud.setPosition((int)x, (int)y);
                mud.setOrigin(mud.getWidth()/2, mud.getHeight()/2);
                mud.setRotation(MathUtils.random(0,360));
                addActor(mud);
                muds.add(mud);
                //mud.debug();
            }

            AudioManager.instance.playSound(Asset.instance.sound.mud, MUD_SOUND_VOLUME);

            setClean(false);
        }else{
            for(final BaseActor mud : muds) {
                mud.addAction(Actions.sequence(
                        Actions.parallel(Actions.alpha(0, 0.8f)),
                        new Action() {
                            @Override
                            public boolean act(float delta) {
                                removeActor(mud);
                                muds.removeValue(mud, true);
                                if(muds.size == 0){
                                    setClean(true);
                                }
                                return true;
                            }
                        }
                ));
            }

            //muds.clear();
        }

    }

    public void bug(boolean use){
        if(use){
            bugSpawned = true;

            bugDirection = MathUtils.random(0,1);

            setClean(false);
        }else{
            bugSpawned = false;
        }
    }

    public void setHintLocation(float x, float y, int align){
        hint.setPosition(x, y, align);

        hint.addAction(Actions.sequence(
                Actions.parallel(Actions.scaleTo(2f,2f), Actions.alpha(1)),
                Actions.parallel(Actions.scaleTo(1,1,0.3f))
        ));

        AudioManager.instance.playSound(Asset.instance.sound.hint, HINT_SOUND_VOLUME);
    }

    private void spawnBugs(){
        //Gdx.app.log("","spawn");

        //sound
        if(bugs.size == 0) {
            bugSoundId = AudioManager.instance.playSound(Asset.instance.sound.bug, BUG_SOUND_VOLUME, true);
        }

        //
        Array<Integer> pos = new Array<Integer>();
        pos.add(0);
        pos.add(1);
        pos.add(2);
        pos.shuffle();

        int total = 0;
        if(MathUtils.random(1, 100) < 70){
            total = MathUtils.random(1,2);
        }else{
            total = 3;
        }

        for(int i = 0; i < total; i++) {

            Animation bugAnimation = null;

            if(MathUtils.random(0,1) == 0){
                bugAnimation = Asset.instance.animation.fly;
            }else{
                bugAnimation = Asset.instance.animation.cockroach;
            }

            AnimatedActor bug = new AnimatedActor(bugAnimation);

            float startX = 0;
            float startY = 150;

            float flexibleX = MathUtils.random(-50, 50);
            float flexibleY = MathUtils.random(-20, 20);

            float x = 0;
            float y = 0;
            if(bugDirection == 0){
                startX = 0;
                x = (startX-50) - (x*100) - (bug.getWidth()/2);
                bug.addAction(Actions.rotateBy(180));
            }else{
                startX = getWidth();
                x = (startX+50) + (x*100)  + (bug.getWidth()/2);
            }
            y = pos.pop();
            y = (startY-20) + (y*100);

            x += flexibleX;
            y += flexibleY;

            bug.setPosition((int)x, (int)y, Align.center);
            addActor(bug);
            bug.setStateTime(MathUtils.random(0f,2f));
            bug.startAnimation();
            bugs.add(bug);
            //bug.debug();
        }

    }

    private void updateBugs(float delta){
        //
        if(bugSpawned) {
            bugTimer += delta;
            if (bugTimer >= 1f) {
                bugTimer = 0;

                spawnBugs();
            }
        }

        //
        for(AnimatedActor bug : bugs){
            if(bug.isVisible()) {
                if(bugDirection == 0){
                    bug.moveBy(delta * 200, 0);
                    if (bug.getX() > getWidth()) {
                        removeBug(bug);
                    }
                }else {
                    bug.moveBy(delta * -200, 0);
                    if (bug.getX()+bug.getWidth() < getX()) {
                        removeBug(bug);
                    }
                }
            }
        }

        //Gdx.app.log("", ""+bugs.size);
    }

    private void removeBug(AnimatedActor bug){
        removeActor(bug);
        bugs.removeValue(bug, true);
        if(bugs.size == 0){
            AudioManager.instance.stopSound(Asset.instance.sound.bug, bugSoundId);
            bugSoundId = -1;
            setClean(true);
        }
    }

    public void pauseSound(){
        if(bugSoundId != -1) {
            AudioManager.instance.pauseSound(Asset.instance.sound.bug, bugSoundId);
        }
        if(fogSoundId != -1) {
            AudioManager.instance.pauseSound(Asset.instance.sound.fog, fogSoundId);
        }

    }

    public void resumeSound(){
        if(bugSoundId != -1) {
            AudioManager.instance.resumeSound(Asset.instance.sound.bug, bugSoundId);
        }
        if(fogSoundId != -1) {
            AudioManager.instance.resumeSound(Asset.instance.sound.fog, fogSoundId);
        }
    }

    public void muteSound(){
        if(bugSoundId != -1) {
            AudioManager.instance.setVolumeSound(Asset.instance.sound.bug, bugSoundId, 0);
        }
        if(fogSoundId != -1) {
            AudioManager.instance.setVolumeSound(Asset.instance.sound.fog, fogSoundId, 0);
        }
    }

    public void unmuteSound(){
        if(bugSoundId != -1) {
            AudioManager.instance.setVolumeSound(Asset.instance.sound.bug, bugSoundId, BUG_SOUND_VOLUME);
        }
        if(fogSoundId != -1) {
            AudioManager.instance.setVolumeSound(Asset.instance.sound.fog, fogSoundId, FOG_SOUND_VOLUME);
        }
    }

    @Override
    public void dispose() {
        if(bugSoundId != -1) {
            AudioManager.instance.stopSound(Asset.instance.sound.bug, bugSoundId);
            bugSoundId = -1;
        }
        if(fogSoundId != -1) {
            AudioManager.instance.stopSound(Asset.instance.sound.fog, fogSoundId);
            fogSoundId = -1;
        }
    }

    private void setClean(boolean clean){
        this.clean = clean;

        //Gdx.app.log("", "monitor clean: "+clean);
    }

    public boolean isClean(){
        return clean;
    }
}
