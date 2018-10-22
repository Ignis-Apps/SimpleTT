package de.js_labs.simpletabletennis.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Janik on 08.06.2016.
 */
public class UiHelper {

    public static Image createImage(Texture texture){
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return new Image(texture);
    }

    public static Button createButton(Texture texture){
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegionDrawable up = new TextureRegionDrawable(new TextureRegion(texture));
        return new Button(up);
    }

    public static Button createButton(Texture up, Texture down){
        up.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        down.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegionDrawable upD = new TextureRegionDrawable(new TextureRegion(up));
        TextureRegionDrawable downD = new TextureRegionDrawable(new TextureRegion(down));
        return new Button(upD, downD);
    }

    public static Button recreate(Button button){
        return new Button(button.getStyle());
    }

    public static Image recreate(Image image){
        return new Image(image.getDrawable());
    }
}
