import com.zetcode.UI.View.Board;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardIntegrationTest {

    @Test
    void testAddScoreAndWaveProgress() {
        Board board = new Board();
        int initialScore = board.getScore();
        board.addScore(500);
        assertEquals(initialScore + 500, board.getScore());
    }

    @Test
    void testForceNextWave() {
        Board board = new Board();
        int initialWave = board.getCurrentWave();
        board.forceNextWave();
        assertEquals(initialWave + 1, board.getCurrentWave());
    }
}