import com.zetcode.UI.Model.Player;
import com.zetcode.UI.Model.PowerUp.RapidFirePowerUp;
import com.zetcode.UI.Model.PowerUp.ShieldPowerUp;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PowerUpTest {

    @Test
    void testRapidFirePowerUp() {
        Player player = new Player();
        RapidFirePowerUp powerUp = new RapidFirePowerUp(0, 0);
        assertFalse(powerUp.isActive());
        powerUp.applyEffect(player);
        assertTrue(powerUp.isActive());
        powerUp.removeEffect(player);
        assertFalse(powerUp.isActive());
    }

    @Test
    void testShieldPowerUp() {
        Player player = new Player();
        ShieldPowerUp powerUp = new ShieldPowerUp(0, 0);
        assertFalse(player.hasShield());
        powerUp.applyEffect(player);
        assertTrue(player.hasShield());
        powerUp.removeEffect(player);
        assertFalse(player.hasShield());
    }
}