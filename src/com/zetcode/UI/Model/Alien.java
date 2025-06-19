package com.zetcode.UI.Model;

import javax.swing.ImageIcon;
import java.util.Random;

public class Alien extends Sprite {

    private Bomb bomb;
    protected long lastShotTime;
    protected long shotInterval;
    protected Random random;
    public Alien(int x, int y) {

        initAlien(x, y);
        random = new Random();
        resetShotTimer();
    }

    private void initAlien(int x, int y) {

        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);

        var alienImg = "src/resources/images/elien1.png";
        var ii = new ImageIcon(alienImg);

        setImage(ii.getImage());
    }

    public void act(int direction) {

        this.x += direction;
    }

    public Bomb getBomb() {

        return bomb;
    }

    public int getPoints(){
        return 100;
    }

    public void resetShotTimer(){
        shotInterval = (random.nextInt(3)+1)*1000;
        lastShotTime = System.currentTimeMillis();
    }

    public boolean shouldShoot(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastShotTime >= shotInterval){
            resetShotTimer();
            return true;
        }
        return false;
    }

//clase bomba usada solo por el alien
    public class Bomb extends Sprite {

        private boolean destroyed;

        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        private void initBomb(int x, int y) {

            setDestroyed(true);

            this.x = x;
            this.y = y;

            var bombImg = "src/resources/images/bomb1.png";
            var ii = new ImageIcon(bombImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }
    }
}
