package com.zetcode.UI.Model.PowerUp;

import javax.swing.ImageIcon;
import java.awt.Image;

public class PowerUp {
    private PowerUpType type;
    private int x;
    private int y;
    private boolean visible;
    private Image image;

    public PowerUp(PowerUpType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.visible = true;
        loadImage();
    }

    private void loadImage() {
        var ii = new ImageIcon("src/resources/images/shield.png");
        image = ii.getImage();
    }

    public void move() {
        y += 3; // Velocidad de caída del power-up
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PowerUpType getType() {
        return type;
    }
}