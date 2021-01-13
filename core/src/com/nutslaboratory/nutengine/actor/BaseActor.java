package com.nutslaboratory.nutengine.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BaseActor extends Actor {
    private TextureRegion region = new TextureRegion();

    public BaseActor(TextureRegion t){
        region = new TextureRegion();

        setTexture(t);
    }

    public void setTexture(TextureRegion t)
    {
        region.setRegion(t);

        setWidth(t.getRegionWidth());
        setHeight(t.getRegionHeight());

        setOrigin(getWidth()/2, getHeight()/2);
    }

    @Override
    public void act(float delta){
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        if(isVisible()) {
            batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }

        batch.setColor(color.r, color.g, color.b, 1f);
    }

    protected void sizeChanged () {
        setOrigin(getWidth()/2, getHeight()/2);
    }


}
