package com.zetcode.UI;

import com.zetcode.UI.Controller.GameController;
import com.zetcode.UI.Model.Commons;
import com.zetcode.UI.View.MainMenu;

import javax.swing.JFrame;

public class SpaceInvaders extends JFrame {
    private GameController gameController;

    public SpaceInvaders() {
        this.gameController = new GameController(this);
        initUI();
    }

    private void initUI() {
        add(new MainMenu(this, gameController));
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        var ex = new SpaceInvaders();
        ex.setVisible(true);
    }
}