package com.zetcode.UI.Services;

import com.zetcode.UI.UI.MainMenu;

public class MenuController {
    private MainMenu menu;
    private GameController gameController;

    public MenuController(MainMenu menu) {
        this.menu = menu;
        this.gameController = new GameController();
    }

    public void startNewGame() {
        gameController.startGame();
    }

    public void showHighScores() {
        // Implementar lógica para mostrar puntuaciones altas
    }

    public void showOptions() {
        // Implementar lógica para mostrar opciones
    }

    public void exitGame() {
        System.exit(0);
    }
}