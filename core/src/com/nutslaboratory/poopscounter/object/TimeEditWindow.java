package com.nutslaboratory.poopscounter.object;


import com.nutslaboratory.poopscounter.util.Utility;

public class TimeEditWindow extends NumberEditWindow {
    public TimeEditWindow(int defaultValue, int value, int minValue, int maxValue, int changeValue){
        super(defaultValue, value, minValue, maxValue, changeValue);

        valueLabel.setText(Utility.convertSecondToMinuteFormat(value));
    }

    @Override
    public void decrease(){
        super.decrease();

        valueLabel.setText(Utility.convertSecondToMinuteFormat(value).toUpperCase());
    }

    @Override
    public void increase(){
        super.increase();

        valueLabel.setText(Utility.convertSecondToMinuteFormat(value).toUpperCase());
    }

    @Override
    public void reset(){
        super.reset();

        valueLabel.setText(Utility.convertSecondToMinuteFormat(value).toUpperCase());
    }



}
