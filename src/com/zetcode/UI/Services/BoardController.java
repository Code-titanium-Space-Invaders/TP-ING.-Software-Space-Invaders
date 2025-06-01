package com.zetcode.UI.Services;

import com.zetcode.UI.Domain.Alien;
import com.zetcode.UI.Domain.Commons;
import com.zetcode.UI.Domain.Player;
import com.zetcode.UI.Domain.Shot;
import com.zetcode.UI.UI.Board;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Random;

public class BoardController {
    private Board board;
    private Player player;
    private int direction = -1;

    public BoardController(Board board) {
        this.board = board;
        this.player = board.getPlayer();
    }

    public void handlePlayerMovement(int direction) {
        // Implementar lógica de movimiento del jugador
        if(player == null) return;
        player.setDX(direction);
    }
    public void handleKeyPressed(KeyEvent e){
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
             handlePlayerMovement(-2);
            player.setImage(player.getIzq());
        }

        if (key == KeyEvent.VK_RIGHT) {
            handlePlayerMovement(2);
            player.setImage(player.getDer());
        }
    }

    public void handleKeyReleased(KeyEvent e){
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            handlePlayerMovement(0);
            player.setImage(player.getNeutro());
        }
    }

    public void handlePlayerShot() {
        // Implementar lógica de disparo del jugador
        if(!board.getShot().isVisible()){
            Shot shot = new Shot(player.getX(),player.getY());
            board.setShot(shot);
        }
    }

    public void handleAlienMovement() {
        // Implementar lógica de movimiento de aliens
        for (Alien alien : board.getAliens()) {

            int x = alien.getX();

            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {
                direction = -1;
                Iterator<Alien> i1 = board.getAliens().iterator();
                while (i1.hasNext()) {
                    Alien a2 = i1.next();
                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }

            if (x <= Commons.BORDER_LEFT && direction != 1) {
                direction = 1;
                Iterator<Alien> i2 = board.getAliens().iterator();
                while (i2.hasNext()) {
                    Alien a = i2.next();
                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }
        Iterator<Alien> it = board.getAliens().iterator();
        while (it.hasNext()) {

            Alien alien = it.next();

            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                    board.setInGame(false);
                }

                alien.act(direction);
            }
        }
    }

    public void handleCollisions() {
        // Implementar lógica de colisiones
        var generator = new Random();

        for (Alien alien : board.getAliens()) {

            int shot = generator.nextInt(15);
            Alien.Bomb bomb = alien.getBomb();

            if (shot == Commons.CHANCE && alien.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + Commons.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Commons.PLAYER_HEIGHT)) {

                    if (player.hasShield()) {
                        player.setShield(false);
                    } else {
                        var ii = new ImageIcon("src/resources/images/explosion1.png");
                        player.setImage(ii.getImage());
                        player.setDying(true);
                    }
                    bomb.setDestroyed(true);
                }
            }

            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 1);

                if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {

                    bomb.setDestroyed(true);
                }
            }
        }

    }

    public void updateGameState() {
        // Implementar actualización del estado del juego
        if(player!=null){player.act();}
    }
}