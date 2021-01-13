package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.nutengine.audio.AudioManager;
import com.nutslaboratory.poopscounter.util.Asset;

public class CharacterMonitor extends Table {
    private BaseActor background;

    private Character character;
    private BaseActor characterImage;

    public CharacterMonitor(){
        setSize(81,81);
        setOrigin(getWidth()/2, getHeight()/2);
        setTransform(true);
        //debug();

        //
        setBackground(new TextureRegionDrawable(Asset.instance.texture.gameAtlas.findRegion("play/character_monitor")));

        //
        characterImage = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/default"));
        characterImage.setSize(55,55);
        characterImage.setOrigin(characterImage.getWidth()/2, characterImage.getHeight()/2);
        characterImage.setPosition(getWidth()/2 - characterImage.getWidth()/2, getHeight()/2 - characterImage.getHeight()/2 + 5);
        addActor(characterImage);
        //characterImage.debug();
    }

    public void setCharacter(Character character){
        this.character = character;

        setCharacterImage(character);

    }


    private void setCharacterImage(Character character){
        TextureRegion requiredCharTexture = (TextureRegion) character.getAnimation().getKeyFrame(0f);
        characterImage.setTexture(requiredCharTexture);
        characterImage.setSize(55,55);
    }

    public void flip(){
        //addAction(Actions.rotateBy(180, 0.5f));

        characterImage.addAction(Actions.rotateBy(180, 0.5f));

        AudioManager.instance.playSound(Asset.instance.sound.flip, 0.6f);
    }





}
