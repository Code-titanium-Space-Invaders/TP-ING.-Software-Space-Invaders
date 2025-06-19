package com.zetcode.UI.Model.PowerUp;

import com.zetcode.UI.Model.Player;
import javax.swing.ImageIcon;

public class RapidFirePowerUp extends AbstractPowerUp {
    private static final int SPEED_MULTIPLIER = 2;

    public RapidFirePowerUp(int x, int y){
        super(PowerUpType.RAPID_FIRE, x, y);
    }
    @Override
    protected void loadImage(){
        var ii = new ImageIcon("src/resources/images/Rshot.png");
        image = ii.getImage();
    }
    @Override
    public void applyEffect(Player player) {
        if (!active) {
            active = true;
            System.out.println("Power-up de disparo rápido activado");
            // Guardar la velocidad original y duplicarla
            // La velocidad se manejará en el Board cuando el power-up esté activo
        }
    }

    @Override
    public void removeEffect(Player player) {
        if (active) {
            active = false;
            System.out.println("Power-up de disparo rápido desactivado");
            // La velocidad se restaurará en el Board cuando el power-up se desactive
        }
    }
}
