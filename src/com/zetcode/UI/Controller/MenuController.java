package com.zetcode.UI.Controller;

import com.zetcode.UI.View.Board;
import com.zetcode.UI.View.MainMenu;

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