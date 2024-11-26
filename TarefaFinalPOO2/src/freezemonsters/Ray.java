package freezemonsters;

import spriteframework.sprite.BadSprite;

import java.awt.Image;

import javax.swing.*;

public class Ray extends BadSprite {

    public Ray() {
    }

    public Ray(int x, int y) {

        initRay(x, y);
    }

    private void initRay(int x, int y) {

        String rayImg = "images/ray.png";
        ImageIcon ii = new ImageIcon(rayImg);
        Image originalImage = ii.getImage();
        Image scaledImage = originalImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        setImage(scaledImage);

        int H_SPACE = 6;
        setX(x + H_SPACE);

        int V_SPACE = 1;
        setY(y - V_SPACE);
    }
}