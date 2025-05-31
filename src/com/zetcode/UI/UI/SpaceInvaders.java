package com.zetcode.UI.UI;

import com.zetcode.UI.Domain.Commons;

import javax.swing.JFrame;

public class SpaceInvaders extends JFrame {

    public SpaceInvaders() {
        initUI();
    }

    private void initUI() {
        add(new MainMenu(this));
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
