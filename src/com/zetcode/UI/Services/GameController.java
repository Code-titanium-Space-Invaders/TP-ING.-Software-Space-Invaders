package com.zetcode.UI.Services;

import com.zetcode.UI.UI.SpaceInvaders;

public class GameController {
    private SpaceInvaders game;

    public GameController() {
        this.game = new SpaceInvaders();
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