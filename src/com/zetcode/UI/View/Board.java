package com.zetcode.UI.View;

import com.zetcode.UI.Model.Commons;
import com.zetcode.UI.Model.Alien;
import com.zetcode.UI.Model.Player;
import com.zetcode.UI.Model.Shot;
import com.zetcode.UI.Model.PowerUp.PowerUp;
import com.zetcode.UI.Model.PowerUp.PowerUpType;
import com.zetcode.UI.Controller.BoardController;
import com.zetcode.UI.Controller.GameController;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import java.util.Random;

public class Board extends JPanel {

    private Dimension d;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;
    private JButton restartButton;
    private BoardController boardController;
    private GameController gameController;

    private int direction = -1;
    private int deaths = 0;
    private int Waves = 2;
    private int currentWave = 1;
    private int AlienPerWave = 6;
    private int shieldKillCount = 0;
    private int score = 0; // Puntuación actual
    private static final int SCORE_PER_KILL = 100; // Puntos por cada enemigo eliminado
    private static final int POWER_UP_THRESHOLD = 1000; // Umbral para dropear power-up
    private Random random = new Random();
    private PowerUp shieldPowerUp;
    private boolean shieldPowerUpActive = false;

    private boolean inGame = true;
    private String explImg = "src/resources/images/explosion1.png";
    private String message = "Game Over";
    private Image backgroundImage;
    private Timer timer;

    public Board() {
        initBoard();
        loadBackGroundImage();
        createRestartButton();
        this.boardController = new BoardController(this);
    }

    public Timer getTimer() {
        return timer;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private void loadBackGroundImage() {
        var ii = new ImageIcon("src/resources/images/board2.png");
        backgroundImage = ii.getImage();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);

        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();

        gameInit();
    }


    private void gameInit() {
        aliens = new ArrayList<>();
        spawnWave();
        player = Player.getInstance();
        player.reset();
        shot = new Shot();
    }

    private void spawnWave() {
        aliens.clear();
        for (int i = 0; i < Commons.NUMBER_OF_ALIENS_TO_DESTROY; i++) {
            for (int j = 0; j < 3; j++) {
                var alien = new Alien(Commons.ALIEN_INIT_X + 25 * j,
                        Commons.ALIEN_INIT_Y + 30 * i);
                aliens.add(alien);
            }
        }
    }

    public Player getPlayer(){
        return player;
    }

    public Shot getShot(){
        return shot;
    }

    public void setShot(Shot shot){
        this.shot = shot;
    }

    public boolean isInGame() {
        return inGame;
    }

    public List<Alien> getAliens(){
        return aliens;
    }

    private void drawAliens(Graphics g) {

        for (Alien alien : aliens) {

            if (alien.isVisible()) {

                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {

                alien.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);

            if (player.hasShield()) {
                g.drawImage(player.getShieldImage(),
                        player.getX() - 10,
                        player.getY() - 10,
                        player.getImage().getWidth(null) + 20,
                        player.getImage().getHeight(null) + 20,
                        this);
            }
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {

        if (shot.isVisible()) {

            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {

        for (Alien a : aliens) {

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }
    private void createRestartButton() {
        restartButton = new JButton("Restart");
        restartButton.setBounds(Commons.BOARD_WIDTH/2 - 100, Commons.BOARD_HEIGHT/2 + 100, 200, 40);
        restartButton.setVisible(false);
        restartButton.addActionListener( e->restartGame());
        add(restartButton);
        //(Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
        //        Commons.BOARD_HEIGHT/2 + fontMetrics.getHeight()/4);
    }
    private void restartGame() {
        inGame = true;
        deaths = 0;
        currentWave = 1;
        score = 0; // Reiniciar la puntuación
        gameInit();
        timer.start();
        restartButton.setVisible(false);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }


    private void doDrawing(Graphics g) {
        // Dibujar la imagen de fondo
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, d.width, d.height, this);
        } else {
            g.setColor(Color.black);
            g.fillRect(0, 0, d.width, d.height);
        }

        g.setColor(Color.green);

        if (inGame) {
            g.drawLine(0, Commons.GROUND,
                    Commons.BOARD_WIDTH, Commons.GROUND);

            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
            drawPowerUps(g);
            drawScore(g);

            // Manejar el estado de pausa
            if (gameController != null && gameController.isPaused()) {
                drawPauseMessage(g);
            }
        } else {
            if (timer.isRunning()) {
                timer.stop();
            }
            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }
    public void setInGame(boolean inGame){
        this.inGame = inGame;
    }


    private void gameOver(Graphics g) {

        g.setColor(new Color(195,196,195));
        g.fillRect(Commons.BOARD_WIDTH/2-200, Commons.BOARD_HEIGHT/2-100, 400, 200);

        g.setColor(new Color(60, 144, 83));
        g.fillRect(Commons.BOARD_WIDTH/2-180, Commons.BOARD_HEIGHT / 2 - 80, 360, 160);
        g.setColor(Color.black);
        g.drawRect(Commons.BOARD_WIDTH/2-180, Commons.BOARD_HEIGHT / 2 - 80, 360, 160);

        var small = new Font("Tahoma", Font.BOLD, 40);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.black);
        g.setFont(small);
        g.drawString(message,
                (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                Commons.BOARD_HEIGHT/2 + fontMetrics.getHeight()/4);
        restartButton.setVisible(true);
    }

    private void update() {
        if(deaths == AlienPerWave){
            if(currentWave < Waves){
                currentWave++;
                spawnWave();
                deaths = 0;
            }

            else{
                inGame = false;
                timer.stop();
                message = "Game won!";
                restartButton.setVisible(true);
            }
        }
        boardController.updateGameState();


        // shot
        if (shot.isVisible()) {
            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien : aliens) {
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Commons.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Commons.ALIEN_HEIGHT)) {

                        var ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shieldKillCount++;

                        // Actualizar puntuación
                        score += SCORE_PER_KILL;
                        checkPowerUpDrop(alienX, alienY);

                        shot.die();
                    }
                }
            }

            // Restaurar la lógica del disparo
            int y = shot.getY();
            y -= 7;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // Actualizar power-up de escudo
        if (shieldPowerUpActive && shieldPowerUp != null && shieldPowerUp.isVisible()) {
            shieldPowerUp.move();

            // Verificar colisión con el jugador
            int powerUpX = shieldPowerUp.getX();
            int powerUpY = shieldPowerUp.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (powerUpX >= (playerX)
                    && powerUpX <= (playerX + Commons.PLAYER_WIDTH)
                    && powerUpY >= (playerY)
                    && powerUpY <= (playerY + Commons.PLAYER_HEIGHT)) {

                System.out.println("Power-up recogido");
                player.setShield(true);
                shieldPowerUp.setVisible(false);
                shieldPowerUpActive = false;
            }

            // Desactivar si sale de la pantalla
            if (powerUpY > Commons.GROUND) {
                //  System.out.println("Power-up fuera de pantalla");
                shieldPowerUp.setVisible(false);
                shieldPowerUpActive = false;
            }
        }
        //Actualizo aliens
        boardController.handleAlienMovement();

        //Actualizo colisiones
        boardController.handleCollisions();

    }

    private void drawPowerUps(Graphics g) {
        if (shieldPowerUpActive && shieldPowerUp != null && shieldPowerUp.isVisible()) {
            // System.out.println("Dibujando power-up en: " + shieldPowerUp.getX() + ", " + shieldPowerUp.getY());
            g.drawImage(shieldPowerUp.getImage(),
                    shieldPowerUp.getX(),
                    shieldPowerUp.getY(),
                    this);
        }
    }

    private void drawScore(Graphics g) {
        // Dibujar el texto de puntuación
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String scoreText = "Score: " + score;
        g.drawString(scoreText, 20, 30);
    }

    private void checkPowerUpDrop(int alienX, int alienY) {
        if (score >= POWER_UP_THRESHOLD && score % POWER_UP_THRESHOLD == 0) {
            // Seleccionar un power-up aleatorio
            PowerUpType[] types = PowerUpType.values();
            PowerUpType randomType = types[random.nextInt(types.length)];

            // Crear el power-up en la posición del alien eliminado
            shieldPowerUp = new PowerUp(randomType, alienX, alienY);
            shieldPowerUpActive = true;
            shieldKillCount = 0;
        }
    }

    private void drawPauseMessage(Graphics g) {
        // Crear un fondo semi-transparente
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        // Configurar el estilo del texto
        var font = new Font("Arial", Font.BOLD, 80);
        g.setFont(font);

        // Dibujar el texto "PAUSED GAME"
        String message = "PAUSED GAME";
        var fontMetrics = g.getFontMetrics(font);
        int x = (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2;
        int y = Commons.BOARD_HEIGHT / 2 - 50;

        // Agregar un efecto de sombra más pronunciado
        g.setColor(new Color(0, 0, 0, 200));
        g.drawString(message, x + 4, y + 4);

        // Dibujar el texto principal con un color más brillante
        g.setColor(new Color(255, 255, 255, 255));
        g.drawString(message, x, y);
    }

    private void doGameCycle() {

        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            boardController.handleKeyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            boardController.handleKeyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                if (inGame) {
                    boardController.handlePlayerShot();
                }
            }
        }
    }
}

