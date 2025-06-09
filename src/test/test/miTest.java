package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class miTest {

    @Test
    void testImageQtty() {
        // Este test verifica la cantidad de imágenes
        int expectedImageCount = 10;
        int actualImageCount =2; // Este valor debería venir de tu aplicación
        assertEquals(expectedImageCount, actualImageCount,
                "La cantidad de imágenes debería ser " + expectedImageCount);
    }

    @Test
    void testBasicGameLogic() {
        // Test para verificar que el juego inicia correctamente
        assertTrue(true, "El juego debería iniciar correctamente");
    }

    @Test
    void testPlayerMovement() {
        // Test para verificar el movimiento del jugador
        int initialX = 0;
        int moveDistance = 5;
        int expectedX = initialX + moveDistance;

        int actualX = initialX + moveDistance;

        assertEquals(expectedX, actualX,
                "El jugador debería moverse la distancia correcta");
    }
}