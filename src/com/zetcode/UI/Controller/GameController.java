package com.zetcode.UI.Controller;

import com.zetcode.UI.SpaceInvaders;

public class GameController {
    private SpaceInvaders game;

    public GameController(SpaceInvaders game) {
        this.game = game;
    }

    public void startGame() {
        game.setVisible(true);
    }

    public void pauseGame() {
        // Implementar lógica de pausa
    }

    public void resumeGame() {
        // Implementar lógica de reanudar
    }

    public void endGame() {
        // Implementar lógica de finalizar juego
    }
}