package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.nutslaboratory.nutengine.actor.BaseActor;
import com.nutslaboratory.poopscounter.util.Asset;

public class PlayerBar extends Table{

    private BaseActor background;

    private Label pointLabel;
    private Label timeLabel;

    private CharacterMonitor characterMonitor;

    public PlayerBar(String time) {

        //
        pack();
        setSize(480, 100);
        setOrigin(getWidth()/2, getHeight()/2);
        setTransform(true);

        //
        characterMonitor = new CharacterMonitor();
        characterMonitor.pack();
        characterMonitor.setOrigin(characterMonitor.getWidth()/2, characterMonitor.getHeight()/2);
        characterMonitor.setPosition(198, -50);
        characterMonitor.setTransform(true);
        addActor(characterMonitor);

        //
        background = new BaseActor(Asset.instance.texture.gameAtlas.findRegion("play/player_bar"));
        addActor(background);

        //
        pointLabel = new Label("0", Asset.instance.skin.gameUISkin, "ds_digital_36");
        pointLabel.setWidth(50);
        pointLabel.setPosition(67, 32);
        pointLabel.setColor(Color.RED);
        pointLabel.setAlignment(Align.center);
        addActor(pointLabel);
        //pointLabel.debug();


        //
        timeLabel = new Label(time, Asset.instance.skin.gameUISkin, "ds_digital_36");
        timeLabel.setWidth(50);
        timeLabel.setPosition(361, 32);
        timeLabel.setColor(Color.GREEN);
        timeLabel.setAlignment(Align.center);
        addActor(timeLabel);
        //timeLabel.debug();


    }

    public void setPoint(int point){
        String pointStr = "";
        if(point > 0){
            pointStr = "+";
        }
        pointStr += point;

        pointLabel.setText(pointStr);

    }

    public void setTime(String time){
        timeLabel.setText(time);
    }

    public CharacterMonitor getCharacterMonitor(){
        return characterMonitor;
    }

}
