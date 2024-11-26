package freezemonsters;

import spriteframework.sprite.BadSprite;
import java.awt.Image;
import javax.swing.*;

public class Slime extends BadSprite {
    private boolean destroyed;

    public Slime(int x, int y) {

        initSlime(x, y);
    }

    private void initSlime(int x, int y) {

        setDestroyed(true);

        this.x = x;
        this.y = y;

        String slimeImg = "images/gosma.png";
        ImageIcon ii = new ImageIcon(slimeImg);
        Image originalImage = ii.getImage();
        Image scaledImage = originalImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    public void setDestroyed(boolean destroyed) {

        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {

        return destroyed;
    }
}
