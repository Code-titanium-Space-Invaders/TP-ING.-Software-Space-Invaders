import com.zetcode.UI.Model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testExtraLife() {
        Player player = new Player();
        assertFalse(player.hasExtraLife());
        player.addExtraLife();
        assertTrue(player.hasExtraLife());
        player.consumeExtraLife();
        assertFalse(player.hasExtraLife());
    }

    @Test
    void testShield() {
        Player player = new Player();
        assertFalse(player.hasShield());
        player.setShield(true);
        assertTrue(player.hasShield());
        player.setShield(false);
        assertFalse(player.hasShield());
    }
}