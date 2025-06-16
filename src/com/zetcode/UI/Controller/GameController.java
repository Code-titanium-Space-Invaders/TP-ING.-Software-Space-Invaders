package com.zetcode.UI.Controller;

import com.zetcode.UI.SpaceInvaders;
import com.zetcode.UI.View.Board;

public class GameController {
    private SpaceInvaders game;
    private Board board;
    public boolean isPaused = false;

    public GameController(SpaceInvaders game) {
        this.game = game;
    }

    public void startGame() {
        game.setVisible(true);
    }

    public void pauseGame() {
        // Implementar lógica de pausa
        if(board != null && board.getTimer() != null){
            board.getTimer().stop();
            isPaused = false;
            if(board != null) {
                board.repaint();
                System.out.println("Juego pausado");
            }
        }
    }
    public void setBoard (Board board){
        this.board = board;
    }

    public void resumeGame() {
        if (board != null && board.getTimer() != null) {
            board.getTimer().start();
            isPaused = false;
            if (board != null) {
                board.repaint();
                System.out.println("Juego reanudado"); // Debug
            }
        }
    }
    public boolean isPaused() {
        return isPaused;
    }
}