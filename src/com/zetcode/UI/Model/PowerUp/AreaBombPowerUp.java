package com.zetcode.UI.Model.PowerUp;

import com.zetcode.UI.Model.Player;
import com.zetcode.UI.Model.Alien;
import com.zetcode.UI.View.Board;
import javax.swing.ImageIcon;
import java.util.List;

public class AreaBombPowerUp extends AbstractPowerUp{
    public AreaBombPowerUp(int x, int y){
        super(PowerUpType.AREA_BOMB, x, y);
    }

    @Override
    protected  void loadImage(){
        // Temporalmente usando la imagen de la bala hasta tener una imagen específica
        var ii = new ImageIcon("src/resources/images/AreaBomb.png");
        image = ii.getImage();
    }

    @Override
    public void applyEffect(Player player) {
        if (!active) {
            active = true;
            // La bomba de área se activa inmediatamente
            System.out.println("Bomba de área activada - eliminando todos los enemigos de la wave actual");
        }
    }

    @Override
    public void removeEffect(Player player) {
        // La bomba de área es instantánea, no se remueve
        if (active) {
            active = false;
            System.out.println("Bomba de área completada");
        }
    }

    // Método para ejecutar la bomba de área
    public void executeAreaBomb(List<Alien> aliens, Board board) {
        if (active) {
            int totalAliens = 0;
            int visibleAliens = 0;
            for (Alien alien : aliens) {
                if (alien.isVisible()) {
                    visibleAliens++;
                }
            }
            System.out.println("Total de aliens en la lista: " + totalAliens + ",  aliens visibles");

            int eliminatedCount = 0;
            for(Alien alien : aliens){
                if(alien.isVisible()){
                    alien.setDying(true);
                    eliminatedCount++;
                }
            }
            System.out.println("Bomba de area elminio: "+ eliminatedCount+ " enemigos");
            board.addScore(100);
            System.out.println("Puntos ganados ; 100");
           if(eliminatedCount>0){
               board.forceNextWave();
           }
            active = false;
        }
    }

}
