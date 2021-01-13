package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.nutslaboratory.poopscounter.util.Asset;
import com.nutslaboratory.poopscounter.util.Utility;

public class SetupMatchWindow extends Window {
    private GameplaySetup gameplaySetup;

    private ImageButton resetButton;

    private Label timeLabel;
    private Label timeValueLabel;
    private ImageButton timeEditButton;

    private Label poopsLabel;
    private Label poopsValueLabel;
    private ImageButton poopsEditButton;

    private Label specialEventLabel;
    private Label specialEventValueLabel;
    private ImageButton specialEventEditButton;

    private Label opponentLabel;
    private Label opponentValueLabel;
    private ImageButton opponentEditButton;

    public SetupMatchWindow(GameplaySetup gameplaySetup){
        super("", Asset.instance.skin.gameUISkin, "setup_match");

        this.gameplaySetup = gameplaySetup;

        //
        setMovable(false);

        //
        resetButton = new ImageButton(Asset.instance.skin.gameUISkin, "reset");
        resetButton.setPosition(450, 0);
        addActor(resetButton);

        //
        Table table = new Table();

        //
        timeLabel = new Label("Time:", Asset.instance.skin.gameUISkin);
        timeValueLabel = new Label(Utility.convertSecondToMinuteFormat(gameplaySetup.totalTime), Asset.instance.skin.gameUISkin);
        timeEditButton = new ImageButton(Asset.instance.skin.gameUISkin, "edit");

        poopsLabel = new Label("Poops:", Asset.instance.skin.gameUISkin);
        poopsValueLabel = new Label(""+gameplaySetup.totalCharacter, Asset.instance.skin.gameUISkin);
        poopsEditButton = new ImageButton(Asset.instance.skin.gameUISkin, "edit");

        opponentLabel = new Label("Opponent:", Asset.instance.skin.gameUISkin);
        opponentValueLabel = new Label(gameplaySetup.opponent, Asset.instance.skin.gameUISkin);
        opponentEditButton = new ImageButton(Asset.instance.skin.gameUISkin, "edit");

        specialEventLabel = new Label("Special Event:", Asset.instance.skin.gameUISkin);
        specialEventValueLabel = new Label(gameplaySetup.specialEventMode, Asset.instance.skin.gameUISkin);
        specialEventEditButton = new ImageButton(Asset.instance.skin.gameUISkin, "edit");

        //
        table.top();
        table.add(timeLabel).padTop(80).padBottom(10).padRight(20);
        table.add(timeValueLabel).padTop(80).padBottom(10).padRight(20);
        table.add(timeEditButton).padTop(80).padBottom(10).padRight(20);
        table.row();
        table.add(poopsLabel).padBottom(10).padRight(20);
        table.add(poopsValueLabel).padBottom(10).padRight(20);
        table.add(poopsEditButton).padBottom(10).padRight(20);
        table.row();
        table.add(opponentLabel).padBottom(10).padRight(20);
        table.add(opponentValueLabel).padBottom(10).padRight(20);
        table.add(opponentEditButton).padBottom(10).padRight(20);
        table.row();
        table.add(specialEventLabel).padBottom(10).padRight(20);
        table.add(specialEventValueLabel).padBottom(10).padRight(20);
        table.add(specialEventEditButton).padBottom(10).padRight(20);


        table.setFillParent(true);
        //table.debug();

        addActor(table);
    }

    public void reset(){
        timeValueLabel.setText(Utility.convertSecondToMinuteFormat(gameplaySetup.totalTime));
        poopsValueLabel.setText(""+gameplaySetup.totalCharacter);
        opponentValueLabel.setText(""+gameplaySetup.opponent);
        specialEventValueLabel.setText(""+gameplaySetup.specialEventMode);
    }

    public ImageButton getResetButton(){
        return resetButton;
    }

    public Label getTimeLabel(){
        return timeLabel;
    }

    public Label getTimeValueLabel(){
        return timeValueLabel;
    }

    public ImageButton getTimeEditButton(){
        return  timeEditButton;
    }

    public Label getPoopsLabel(){
        return poopsLabel;
    }

    public Label getPoopsValueLabel(){
        return poopsValueLabel;
    }

    public ImageButton getPoopsEditButton(){
        return  poopsEditButton;
    }

    public Label getSpecialEventLabel(){
        return specialEventLabel;
    }

    public Label getSpecialEventValueLabel(){
        return specialEventValueLabel;
    }

    public ImageButton getSpecialEventEditButton(){
        return  specialEventEditButton;
    }

    public Label getOpponentLabel(){
        return opponentLabel;
    }

    public Label getOpponentValueLabel(){
        return opponentValueLabel;
    }

    public ImageButton getOpponentEditButton(){
        return  opponentEditButton;
    }



}
