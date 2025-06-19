import com.zetcode.UI.Model.Alien;
import com.zetcode.UI.Model.BossAlien;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlienTest {

    @Test
    void testAlienPoints() {
        Alien alien = new Alien(0, 0);
        assertEquals(100, alien.getPoints());
    }

    @Test
    void testBossAlienPoints() {
        BossAlien boss = new BossAlien(0, 0);
        assertEquals(150, boss.getPoints());
    }

    @Test
    void testAlienShouldShoot() throws InterruptedException {
        Alien alien = new Alien(0, 0);
        // Forzar el timer
        alien.lastShotTime = System.currentTimeMillis() - 5000;
        alien.shotInterval = 1000;
        assertTrue(alien.shouldShoot());
    }
}