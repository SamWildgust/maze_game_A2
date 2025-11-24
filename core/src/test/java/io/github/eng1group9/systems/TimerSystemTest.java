import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

package io.github.eng1group9.systems;

public class TimerSystemTest {

    @BeforeEach
    public void reset() {
        // Reset elapsedTime before each test
        TimerSystem.elapsedTime = 0f;
    }

    @Test
    public void initialTimeLeftAndClockDisplay() {
        // Given we have a new TimerSystem
        TimerSystem ts = new TimerSystem();
        // When no time has elapsed
        assertEquals(300, TimerSystem.getTimeLeft());
        // Then clock display should show full time
        assertEquals("Time Left: 5:00", TimerSystem.getClockDisplay());
    }

    @Test
    public void addReducesElapsedTime() {
        // Given we have a new TimerSystem
        TimerSystem ts = new TimerSystem();
        // When we add 10 seconds
        ts.add(10f);
        // Then elapsedTime should decrease by 10 seconds
        assertEquals(290, TimerSystem.getTimeLeft());
    }

    @Test
    public void getTimeLeftCalculatesCorrectly() {
        // Given we have a new TimerSystem
        TimerSystem ts = new TimerSystem();
        // When 150 seconds have elapsed
        ts.elapsedTime = 150f;
        // Then time left should be 150 seconds
        assertEquals(150, ts.getTimeLeft());
    }

    @Test
    public void isTimeUpReturnValue() {
        // Given we have a new TimerSystem
        TimerSystem ts = new TimerSystem();
        // When 300 seconds have elapsed
        ts.elapsedTime = 300f;
        // Then isTimeUp should return true
        assertEquals(true, ts.isTimeUp());
        // Else when less than 300 seconds have elapsed
        ts.elapsedTime = 250f;
        // Then isTimeUp should return false
        assertEquals(false, ts.isTimeUp());
    }

    @Test
    public void clockDisplayFormatsCorrectly() {
        // Given we have a new TimerSystem
        TimerSystem ts = new TimerSystem();
        // When time reads 3:20 (300 - 100 = 200 -> 3:20)
        ts.elapsedTime = 100f;
        // Then this should be reflected in the clock display
        assertEquals("Time Left: 3:20", ts.getClockDisplay());
    }
}