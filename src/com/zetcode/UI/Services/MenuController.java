package com.zetcode.UI.Services;

import com.zetcode.UI.UI.Board;
import com.zetcode.UI.UI.MainMenu;

public class MenuController {
    private MainMenu menu;
    private GameController gameController;

    public MenuController(MainMenu menu, GameController gameController) {
        this.menu = menu;
        this.gameController = gameController;
    }

    public void startNewGame() {
        Board gameBoard = new Board();
        menu.showGameBoard(gameBoard);
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