package com.zetcode.sprite;

import com.zetcode.Commons;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Sprite {

    private int width;
    private Image neutro;
    private Image der;
    private Image izq;

    public Player() {

        initPlayer();
    }

    private void initPlayer() {

        var playerImg = "src/images/neutro.png";
        var playerder = "src/images/derecha.png";
        var playerIzq = "src/images/izquierda.png";
        var ii = new ImageIcon(playerImg);
        var iileft = new ImageIcon(playerIzq);
        var iiright = new ImageIcon(playerder);
        neutro = ii.getImage();
        der = iiright.getImage();
        izq = iileft.getImage();

        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());

        int START_X = 960;
        setX(START_X);

        int START_Y = 960;
        setY(START_Y);
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
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
            setImage(neutro);
        }
    }
}
