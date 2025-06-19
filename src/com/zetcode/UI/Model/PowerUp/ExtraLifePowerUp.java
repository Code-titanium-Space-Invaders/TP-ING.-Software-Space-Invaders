package com.zetcode.UI.Model.PowerUp;

import com.zetcode.UI.Model.Player;
import javax.swing.ImageIcon;

public class ExtraLifePowerUp extends AbstractPowerUp{

    public ExtraLifePowerUp(int x, int y){
        super(PowerUpType.EXTRA_LIFE, x,y);
    }
    @Override
    protected void loadImage() {
        // Temporalmente usando la imagen de la bala hasta tener una imagen específica
        var ii = new ImageIcon("src/resources/images/extraLife.png");
        image = ii.getImage();
    }

    @Override
    public void applyEffect(Player player) {
        if (!active) {
            active = true;
            // Agregar una vida extra al jugador
            player.addExtraLife();
            System.out.println("Vida extra agregada al jugador");
        }
    }

    @Override
    public void removeEffect(Player player) {
        // La vida extra es permanente, no se remueve
        if (active) {
            active = false;
            System.out.println("Vida extra consumida");
        }
    }
}
