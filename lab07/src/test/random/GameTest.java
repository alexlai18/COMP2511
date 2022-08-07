package random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {
    @Test
    void seed4Test() {
        Game game = new Game((long) 4);
        int winCounter = 0;
        for (int i = 0; i < 100; i++) {
            if (game.battle()) {
                winCounter += 1;
            }
        }

        assertEquals(winCounter, 42);
    }

    @Test
    void seed0Test() {
        Game game = new Game((long) 0);
        int winCounter = 0;
        for (int i = 0; i < 100; i++) {
            if (game.battle()) {
                winCounter += 1;
            }
        }

        assertEquals(winCounter, 46);
    }
}