package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.nutslaboratory.poopscounter.util.Asset;

public class ItemEditWindow extends EditWindow {
    protected String defaultValue;
    protected String value;
    protected Array<String> items;
    protected int currentIndex;

    private Array<String> itemDescriptions;
    private Label itemDescriptionLabel;

    public ItemEditWindow(String defaultValue, Array<String> items, String value, Array<String> itemDescriptions){
        super();

        //
        this.defaultValue = defaultValue;
        this.items = items;
        this.value = value;
        this.currentIndex = items.indexOf(value, false);
        this.itemDescriptions = itemDescriptions;

        //
        value = items.get(currentIndex);
        valueLabel.setText(value.toUpperCase());

        //
        setButtonsState();

        //
        if(itemDescriptions != null) {
            itemDescriptionLabel = new Label(itemDescriptions.get(currentIndex), Asset.instance.skin.gameUISkin, "droid_sans_14");
            itemDescriptionLabel.setSize(290, 50);
            itemDescriptionLabel.setColor(Color.YELLOW);
            itemDescriptionLabel.setPosition((getWidth() / 2) - itemDescriptionLabel.getWidth() / 2, getHeight() / 2 + 40);
            itemDescriptionLabel.setAlignment(Align.center);
            itemDescriptionLabel.setWrap(true);
            addActor(itemDescriptionLabel);
            //itemDescriptionLabel.debug();
        }

    }

    @Override
    public void decrease(){
        currentIndex = MathUtils.clamp(currentIndex - 1, 0, items.size-1);

        value = items.get(currentIndex);
        valueLabel.setText(value.toUpperCase());

        if(itemDescriptions != null) {
            itemDescriptionLabel.setText(itemDescriptions.get(currentIndex));
        }

        setButtonsState();
    }

    @Override
    public void increase(){
        currentIndex = MathUtils.clamp(currentIndex + 1, 0, items.size-1);

        value = items.get(currentIndex);
        valueLabel.setText(value.toUpperCase());

        if(itemDescriptions != null) {
            itemDescriptionLabel.setText(itemDescriptions.get(currentIndex));
        }

        setButtonsState();
    }

    @Override
    public void reset(){
        currentIndex = items.indexOf(defaultValue, false);

        value = items.get(currentIndex);
        valueLabel.setText(value.toUpperCase());

        if(itemDescriptions != null) {
            itemDescriptionLabel.setText(itemDescriptions.get(currentIndex));
        }

        setButtonsState();
    }


    public String getValue(){
        return value;
    }

    private void setButtonsState(){
        if(currentIndex == 0){
            decreaseButton.setDisabled(true);
        }else{
            decreaseButton.setDisabled(false);
        }

        if(currentIndex == items.size-1){
            increaseButton.setDisabled(true);
        }else{
            increaseButton.setDisabled(false);
        }
    }
}
