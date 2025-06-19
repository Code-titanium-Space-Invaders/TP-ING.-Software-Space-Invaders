package com.zetcode.UI.Model;

import javax.swing.ImageIcon;
import java.util.Random;

public class BossAlien extends Alien {

    public BossAlien(int x, int y) {
        super(x, y);
        loadBossImage();
    }

    private void loadBossImage() {
        // Cargar una imagen diferente para el boss alien
        // Por ahora usaremos la misma imagen pero puedes cambiarla
        var ii = new ImageIcon("src/resources/images/bossAlien.png");
        setImage(ii.getImage());
    }
    @Override
        public int getPoints() {
            return 150; // Dar 150 puntos en lugar de 100
        }
    @Override
    public void resetShotTimer(){
        shotInterval = (random.nextInt(15) + 5) * 100; // 500-2000 ms
        lastShotTime = System.currentTimeMillis();
    }
}
