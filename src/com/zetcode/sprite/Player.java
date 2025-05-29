package com.zetcode.sprite;

import com.zetcode.Commons;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Sprite {

    private int width;
    private boolean hasShield;
    private Image shieldImage;
    private Image neutro;
    private Image der;
    private Image izq;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        // Cargar imágenes del jugador
        var playerImg = "src/images/neutro.png";
        var playerder = "src/images/derecha.png";
        var playerIzq = "src/images/izquierda.png";

        var ii = new ImageIcon(playerImg);
        var iileft = new ImageIcon(playerIzq);
        var iiright = new ImageIcon(playerder);

        neutro = ii.getImage();
        der = iiright.getImage();
        izq = iileft.getImage();

        width = neutro.getWidth(null);
        setImage(neutro);

        // Cargar imagen del escudo
        var shieldIi = new ImageIcon("src/images/shield.png");
        shieldImage = shieldIi.getImage();

        int START_X = 270;
        setX(START_X);

        // Posicionar el jugador en el suelo
        int START_Y = Commons.GROUND - Commons.PLAYER_HEIGHT;
        setY(START_Y);

        hasShield = false;
    }

    public void act() {
        x += dx;
        if (x <= 2) {
            x = 2;
        }
        if (x >= Commons.BOARD_WIDTH - 2 * width) {
            x = Commons.BOARD_WIDTH - 2 * width;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -2;
            setImage(izq);
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 2;
            setImage(der);
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
            setImage(neutro);
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
            setImage(neutro);
        }
    }

    public boolean hasShield() {
        return hasShield;
    }

    public void setShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public Image getShieldImage() {
        return shieldImage;
    }
}
