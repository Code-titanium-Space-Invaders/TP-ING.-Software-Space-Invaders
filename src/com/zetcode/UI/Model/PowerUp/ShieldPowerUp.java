package com.zetcode.UI.Model.PowerUp;

import com.zetcode.UI.Model.Player;
import javax.swing.ImageIcon;

public class ShieldPowerUp extends AbstractPowerUp{

    public ShieldPowerUp(int x, int y){
        super(PowerUpType.SHIELD, x, y);
    }

    @Override
    protected void loadImage() {
        var ii = new ImageIcon("src/resources/images/shield.png");
        image = ii.getImage();
    }

    @Override
    public void applyEffect(Player player) {
        if (!active) {
            active = true;
            player.setShield(true);
            System.out.println("Escudo activado");
        }
    }

    @Override
    public void removeEffect(Player player) {
        if (active) {
            active = false;
            player.setShield(false);
            System.out.println("Escudo desactivado");
        }
    }
}
