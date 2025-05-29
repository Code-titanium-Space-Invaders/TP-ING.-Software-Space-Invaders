package com.zetcode;

import com.zetcode.sprite.Alien;
import com.zetcode.sprite.Player;
import com.zetcode.sprite.Shot;
import com.zetcode.PowerUp.PowerUp;
import com.zetcode.PowerUp.PowerUpType;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;

public class Board extends JPanel {

    private Dimension d;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;
    private JButton restartButton;

    private int direction = -1;
    private int deaths = 0;
    private int Waves = 2;
    private int currentWave = 1;
    private int AlienPerWave = 6;
    private int shieldKillCount = 0;
    private int points = 0; // Puntos actuales
    private static final int MAX_POINTS = 100; // Puntos máximos (100%)
    private static final int POINTS_PER_KILL = 10; // 10% por cada enemigo
    private PowerUp shieldPowerUp;
    private boolean shieldPowerUpActive = false;

    private boolean inGame = true;
    private String explImg = "src/images/explosion1.png";
    private String message = "Game Over";
    private Image backgroundImage ;
    private Timer timer;


    public Board() {

        initBoard();
        gameInit();
        loadBackGroundImage();
        createRestartButton();
    }
    private void loadBackGroundImage() {
        var ii = new ImageIcon("src/images/board2.png");
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
        player = new Player();
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
        // player = new Player();
        // shot = new Shot();
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
            drawPointsBar(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
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

        //   if (deaths == Commons.NUMBER_OF_ALIENS_TO_DESTROY) {

        //        inGame = false;
        //       timer.stop();
        //        message = "Game won!";
        //  }

        // player
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

        player.act();

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

                        // Actualizar puntos
                        points = Math.min(points + POINTS_PER_KILL, MAX_POINTS);

                        // Generar power-up de escudo cada 4 kills
                        if (shieldKillCount >= 4) {
                            System.out.println("Generando power-up de escudo en: " + alienX + ", " + alienY);
                            // Ajustar la posición para que aparezca en el centro del alien
                            int powerUpX = alienX + (Commons.ALIEN_WIDTH / 2);
                            int powerUpY = alienY + (Commons.ALIEN_HEIGHT / 2);
                            shieldPowerUp = new PowerUp(PowerUpType.SHIELD, powerUpX, powerUpY);
                            shieldPowerUpActive = true;
                            shieldKillCount = 0;
                        }

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

        // aliens

        for (Alien alien : aliens) {

            int x = alien.getX();

            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {

                direction = -1;

                Iterator<Alien> i1 = aliens.iterator();

                while (i1.hasNext()) {

                    Alien a2 = i1.next();
                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }

            if (x <= Commons.BORDER_LEFT && direction != 1) {

                direction = 1;

                Iterator<Alien> i2 = aliens.iterator();

                while (i2.hasNext()) {

                    Alien a = i2.next();
                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }

        Iterator<Alien> it = aliens.iterator();

        while (it.hasNext()) {

            Alien alien = it.next();

            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                    restartButton.setVisible(true);
                }

                alien.act(direction);
            }
        }

        // bombs
        var generator = new Random();

        for (Alien alien : aliens) {

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
                        var ii = new ImageIcon(explImg);
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

    private void drawPowerUps(Graphics g) {
        if (shieldPowerUpActive && shieldPowerUp != null && shieldPowerUp.isVisible()) {
           // System.out.println("Dibujando power-up en: " + shieldPowerUp.getX() + ", " + shieldPowerUp.getY());
            g.drawImage(shieldPowerUp.getImage(),
                    shieldPowerUp.getX(),
                    shieldPowerUp.getY(),
                    this);
        }
    }

    private void drawPointsBar(Graphics g) {
        // Dibujar el fondo de la barra
        g.setColor(Color.DARK_GRAY);
        g.fillRect(Commons.BOARD_WIDTH - 210, 20, 200, 20);

        // Dibujar la barra de progreso
        g.setColor(Color.GREEN);
        int barWidth = (points * 200) / MAX_POINTS;
        g.fillRect(Commons.BOARD_WIDTH - 210, 20, barWidth, 20);

        // Dibujar el borde
        g.setColor(Color.WHITE);
        g.drawRect(Commons.BOARD_WIDTH - 210, 20, 200, 20);

        // Dibujar el texto de puntos
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String pointsText = points + "%";
        g.drawString(pointsText, Commons.BOARD_WIDTH - 250, 35);
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

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if (inGame) {

                    if (!shot.isVisible()) {

                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }
}
