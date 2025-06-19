package com.zetcode.UI.Model.PowerUp;

import com.zetcode.UI.Model.Player;

import javax.swing.*;

public class DoubleShotPowerUp extends AbstractPowerUp{

    public DoubleShotPowerUp(int x, int y){
        super(PowerUpType.DOUBLE_SHOT, x, y);
    }

    @Override
    protected void loadImage() {
        // Temporalmente usando la imagen de la bala hasta tener una imagen específica
        var ii = new ImageIcon("src/resources/images/shot1.png");
        image = ii.getImage();
    }

    @Override
    public void applyEffect(Player player) {
        if (!active) {
            active = true;
            System.out.println("Power-up de disparo doble activado");
        }
    }

    @Override
    public void removeEffect(Player player) {
        if (active) {
            active = false;
            System.out.println("Power-up de disparo doble desactivado");
        }
    }
}
