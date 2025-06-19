package com.zetcode.UI.View;

import com.zetcode.UI.Model.Commons;
import com.zetcode.UI.Model.Alien;
import com.zetcode.UI.Model.Player;
import com.zetcode.UI.Model.PowerUp.*;
import com.zetcode.UI.Model.Shot;
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
import java.util.Random;

public class Board extends JPanel {

    private Dimension d;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;
    private Shot shot2; // Segunda bala para el disparo doble
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
    private static final int POWER_UP_THRESHOLD = 100; // Umbral para dropear power-up
    private Random random = new Random();
    private PowerUp shieldPowerUp;
    private boolean shieldPowerUpActive = false;

    private boolean inGame = true;
    private String explImg = "src/resources/images/explosion1.png";
    private String message = "Game Over";
    private Image backgroundImage;
    private Timer timer;

    private PowerUpStrategy activePowerUp;
    private long powerUpStartTime;
    private boolean powerUpDropped = false; // Nueva variable para controlar si ya se dropeó un power-up en este umbral

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
        shot2 = new Shot();
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
        System.out.println("Oleada " + currentWave + " creada con " + aliens.size() + " enemigos");
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

    public Shot getShot2(){
        return shot2;
    }

    public void setShot2(Shot shot2){
        this.shot2 = shot2;
    }

    public PowerUpStrategy getActivePowerUp(){
        return activePowerUp;
    }

    public void setActivePowerUp(PowerUpStrategy activePowerUp){
        this.activePowerUp = activePowerUp;
    }

    public void setPowerUpDropped(boolean powerUpDropped){
        this.powerUpDropped = powerUpDropped;
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
                // Dibujar el escudo con un efecto de brillo
                g.setColor(new Color(0, 255, 255, 100)); // Color cyan semi-transparente
                g.fillOval(player.getX() - 15,
                        player.getY() - 15,
                        player.getImage().getWidth(null) + 30,
                        player.getImage().getHeight(null) + 30);

                // Dibujar la imagen del escudo
                g.drawImage(player.getShieldImage(),
                        player.getX() - 15,
                        player.getY() - 15,
                        player.getImage().getWidth(null) + 30,
                        player.getImage().getHeight(null) + 30,
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

        if (shot2.isVisible()) {
            g.drawImage(shot2.getImage(), shot2.getX(), shot2.getY(), this);
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
        powerUpDropped = false; // Resetear la bandera de power-up
        activePowerUp = null; // Limpiar el power-up activo
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
        checkWaveProgress();
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

                        // Actualizar puntuación
                        score += SCORE_PER_KILL;
                        System.out.println("Alien eliminado. Nuevo score: " + score);
                        checkPowerUpDrop(alienX, alienY);

                        shot.die();
                        if (shot2.isVisible()) {
                            shot2.die(); // Desactivar la segunda bala también
                        }
                    }
                }
            }

            // Actualizar velocidad de las balas si el power-up de velocidad está activo
            int shotSpeed = 7; // Velocidad base
            if (activePowerUp instanceof RapidFirePowerUp && activePowerUp.isActive()) {
                shotSpeed *= 2; // Duplicar la velocidad
            }

            int y = shot.getY();
            y -= shotSpeed;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // shot2 (segunda bala para disparo doble)
        if (shot2.isVisible()) {
            int shotX = shot2.getX();
            int shotY = shot2.getY();

            for (Alien alien : aliens) {
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot2.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Commons.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Commons.ALIEN_HEIGHT)) {

                        var ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;

                        // Actualizar puntuación
                        score += SCORE_PER_KILL;
                        System.out.println("Alien eliminado por segunda bala. Nuevo score: " + score);
                        checkPowerUpDrop(alienX, alienY);

                        shot2.die();
                        if (shot.isVisible()) {
                            shot.die(); // Desactivar la primera bala también
                        }
                    }
                }
            }

            // Actualizar velocidad de las balas si el power-up de velocidad está activo
            int shotSpeed = 7; // Velocidad base
            if (activePowerUp instanceof RapidFirePowerUp && activePowerUp.isActive()) {
                shotSpeed *= 2; // Duplicar la velocidad
            }

            int y = shot2.getY();
            y -= shotSpeed;

            if (y < 0) {
                shot2.die();
            } else {
                shot2.setY(y);
            }
        }

        // Actualizar power-up activo
        if (activePowerUp != null && activePowerUp instanceof AbstractPowerUp) {
            AbstractPowerUp powerUp = (AbstractPowerUp) activePowerUp;

            // Solo mover si es visible
            if (powerUp.isVisible()) {
                powerUp.move();

                // Verificar colisión con el jugador
                int powerUpX = powerUp.getX();
                int powerUpY = powerUp.getY();
                int playerX = player.getX();
                int playerY = player.getY();

                if (powerUpX >= (playerX)
                        && powerUpX <= (playerX + Commons.PLAYER_WIDTH)
                        && powerUpY >= (playerY)
                        && powerUpY <= (playerY + Commons.PLAYER_HEIGHT)) {

                    System.out.println("Power-up recogido: " + powerUp.getType().getName());
                    activePowerUp.applyEffect(player);
                    powerUp.setVisible(false);
                    if(activePowerUp instanceof AreaBombPowerUp){
                        AreaBombPowerUp areaBomb = (AreaBombPowerUp) activePowerUp;
                        areaBomb.executeAreaBomb(aliens, this);
                        activePowerUp = null;
                        powerUpDropped = false;
                    } else if (activePowerUp instanceof ExtraLifePowerUp) {
                        activePowerUp = null;
                        powerUpDropped = false;
                    } else{// Para power-ups temporales, iniciar el timer
                        if (!(activePowerUp instanceof ShieldPowerUp)) {
                            powerUpStartTime = System.currentTimeMillis();
                        }
                    }
                }

                // Desactivar si sale de la pantalla
                if (powerUpY > Commons.GROUND) {
                    powerUp.setVisible(false);
                    activePowerUp = null;
                    powerUpDropped = false;
                }
            }
        }

        // Verificar duración del power-up (solo para power-ups temporales)
        if (activePowerUp != null && activePowerUp.isActive() && activePowerUp.getDuration() > 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - powerUpStartTime >= activePowerUp.getDuration()) {
                activePowerUp.removeEffect(player);

                // Si es disparo doble, desactivar ambas balas
                if (activePowerUp instanceof DoubleShotPowerUp) {
                    shot.die();
                    shot2.die();
                }

                activePowerUp = null;
                powerUpDropped = false;
            }
        }

        // Si no hay power-up activo, permitir que se cree uno nuevo
        if (activePowerUp == null) {
            powerUpDropped = false;
        }

        //Actualizo aliens
        boardController.handleAlienMovement();

        //Actualizo colisiones
        boardController.handleCollisions();
    }

    private void drawPowerUps(Graphics g) {
        if (activePowerUp != null && activePowerUp instanceof AbstractPowerUp) {
            AbstractPowerUp powerUp = (AbstractPowerUp) activePowerUp;
            System.out.println("Dibujando power-up: " + powerUp.getType().getName() +
                    ", Visible: " + powerUp.isVisible() +
                    ", X: " + powerUp.getX() +
                    ", Y: " + powerUp.getY());
            if (powerUp.isVisible()) {
                g.drawImage(powerUp.getImage(),
                        powerUp.getX(),
                        powerUp.getY(),
                        this);
            }
        }
    }

    private void drawScore(Graphics g) {
        // Dibujar el texto de puntuación
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String scoreText = "Score: " + score;
        g.drawString(scoreText, 20, 30);

        if(player.getExtraLives() > 0){
            String livesText = "Vidas extra: " + player.getExtraLives();
            g.drawString(livesText, 20, 60);
        }
    }

    private void checkPowerUpDrop(int alienX, int alienY) {
        System.out.println("Checking power-up drop. Score: " + score + ", PowerUpDropped: " + powerUpDropped);

        // Solo dropear power-up cuando el score sea exactamente 1000 o múltiplos de 1000
        // y no se haya dropeado ya un power-up en este umbral
        if (score > 0 && score % POWER_UP_THRESHOLD == 0 && !powerUpDropped) {
            System.out.println("Intentando crear power-up...");
            // Seleccionar un power-up aleatorio
            PowerUpType[] types = PowerUpType.values();
            PowerUpType randomType = types[random.nextInt(types.length)];
            System.out.println("Power-up seleccionado: " + randomType.getName());

            // Asegurarnos de que el power-up aparezca en una posición visible
            int powerUpX = alienX;
            int powerUpY = alienY + Commons.ALIEN_HEIGHT; // Posicionarlo justo debajo del alien

            // Crear el power-up específico según el tipo
            switch (randomType) {
                case RAPID_FIRE:
                    activePowerUp = new RapidFirePowerUp(powerUpX, powerUpY);
                    break;
                case SHIELD:
                    activePowerUp = new ShieldPowerUp(powerUpX, powerUpY);
                    break;
                case DOUBLE_SHOT:
                    activePowerUp = new DoubleShotPowerUp(powerUpX, powerUpY);
                    break;
                case EXTRA_LIFE:
                    activePowerUp = new ExtraLifePowerUp(powerUpX, powerUpY);
                    break;
                case AREA_BOMB:
                    activePowerUp = new AreaBombPowerUp(powerUpX,powerUpY);
                    break;
                // Agregar otros casos según sea necesario
            }

            if (activePowerUp != null) {
                if (activePowerUp instanceof AbstractPowerUp) {
                    AbstractPowerUp powerUp = (AbstractPowerUp) activePowerUp;
                    powerUp.setVisible(true);
                    System.out.println("Power-up creado y visible: " + powerUp.getType().getName() +
                            ", X: " + powerUp.getX() +
                            ", Y: " + powerUp.getY());
                }
                powerUpStartTime = System.currentTimeMillis();
                powerUpDropped = true;
                System.out.println("Power-up creado exitosamente: " + randomType.getName() + " en score: " + score);
            }
        } else if (score % POWER_UP_THRESHOLD != 0) {
            // Resetear la bandera cuando pasamos a un nuevo umbral
            powerUpDropped = false;
            System.out.println("Reseteando bandera powerUpDropped");
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
    public void addScore(int points){
        this.score += points;
        System.out.println("score actualizado "+score);
    }
    public void addDeaths(int deaths){
        System.out.println("Antes de agregar muertes: "+this.deaths);
        this.deaths = deaths;
        System.out.println("Despues de agregar "+deaths + "Muertes "+this.deaths);
    }
    public void checkWaveProgress(){
        System.out.println("Verificando progreso de oleada - Muertes: " + deaths + ", AlienPerWave: " + AlienPerWave + ", CurrentWave: " + currentWave + ", Waves: " + Waves);
        if(deaths == AlienPerWave){
            System.out.println("¡Condición cumplida! Pasando a siguiente oleada o terminando juego");
            if(currentWave < Waves){
                currentWave++;
                spawnWave();
                deaths = 0;
                System.out.println("Pasando a la oleada " + currentWave);
            }
            else{
                inGame = false;
                timer.stop();
                message = "Game won!";
                restartButton.setVisible(true);
                System.out.println("¡Juego completado! Todas las oleadas superadas.");
            }
        } else {
            System.out.println("Condición no cumplida - Faltan " + (AlienPerWave - deaths) + " enemigos para pasar de oleada");
        }
    }
    public void forceNextWave() {
        System.out.println("Forzando paso a siguiente oleada - CurrentWave: " + currentWave + ", Waves: " + Waves);
        if(currentWave < Waves){
            currentWave++;
            spawnWave();
            deaths = 0;
            System.out.println("¡Pasando a la oleada " + currentWave + "!");
        }
        else{
            inGame = false;
            timer.stop();
            message = "Game won!";
            restartButton.setVisible(true);
            System.out.println("¡Juego completado! Todas las oleadas superadas.");
        }
    }
}


