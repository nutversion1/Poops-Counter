package com.nutslaboratory.poopscounter.object;

import com.badlogic.gdx.math.MathUtils;

public class NumberEditWindow extends EditWindow {
    protected int defaultValue;
    protected int value;
    protected int minValue;
    protected int maxValue;
    protected int changeValue;

    public NumberEditWindow(int defaultValue, int value, int minValue, int maxValue, int changeValue){
        super();

        //
        this.defaultValue = defaultValue;
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.changeValue = changeValue;

        //
        valueLabel.setText(Integer.toString(value).toUpperCase());

        //
        setButtonsState();
    }

    @Override
    public void decrease(){
        value = MathUtils.clamp(value - changeValue, minValue, maxValue);
        valueLabel.setText(Integer.toString(value).toUpperCase());

        setButtonsState();
    }

    @Override
    public void increase(){
        value = MathUtils.clamp(value + changeValue, minValue, maxValue);
        valueLabel.setText(Integer.toString(value).toUpperCase());

        setButtonsState();
    }

    @Override
    public void reset(){
        value = defaultValue;
        valueLabel.setText(Integer.toString(value).toUpperCase());

        setButtonsState();
    }

    public int getValue(){
        return value;
    }

    private void setButtonsState(){

        if(value == minValue){
            decreaseButton.setDisabled(true);
        }else{
            decreaseButton.setDisabled(false);
        }

        if(value == maxValue){
            increaseButton.setDisabled(true);
        }else{
            increaseButton.setDisabled(false);
        }
    }
}
