package io.github.eng1group9.systems;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**Testing TimerSystem.java
 * Unittests for TimerSystem Class
 */
public class TimerSystemTest {

    private TimerSystem timerSystem;

    @Before
    public void setUp() {

        timerSystem = new TimerSystem();
        TimerSystem.elapsedTime = 0; // Reset elapsed time before each test
    }

    @Test
    public void testConstructor() {
        // Verify that a new TimerSystem initializes with 0" elapsed time
        assertEquals(0, TimerSystem.elapsedTime, 0.0f);
    }

    @Test
    /**
     * Test the add method
     * adds a fixed amount of time
     * checks for it being update correctly
     * uses a small error bound for float arithmetic
    */
    public void testAdd() {
        //adding a fixed amount to timer
        timerSystem.add(10.5f);
        assertEquals(10.5f, TimerSystem.elapsedTime, 0.01f);

        //adding more time on top
        timerSystem.add(5.5f);
        assertEquals(16.0f, TimerSystem.elapsedTime, 0.01f);
    }

    @Test
    /**
     * tests addGranually
     * looks for if it adds time over multiple calls not at once
     */
    public void testAddGradually() {
        //test for not immediately adding all time
        timerSystem.addGradually(50.0f);
        assertEquals(0, TimerSystem.elapsedTime, 0.0f);
    }

    @Test
    /**
     * tests getTimeLeft
     * checks for correct time left after various times elapsed
     */
    public void testGetTimeLeft() {
        //should have the full time of 5' at start
        assertEquals(300, TimerSystem.getTimeLeft());

        //after +50"
        timerSystem.add(50.0f);
        assertEquals(250, TimerSystem.getTimeLeft());

        //after +100"
        timerSystem.add(100.0f);
        assertEquals(150, TimerSystem.getTimeLeft());
    }

    @Test
    /**
     * checks the getter for time when at 0"
     * sets time to full then checks
     * checks for 300-t time
     */
    public void testGetTimeLeftAtZero() {
        //test when time reaches 0"
        timerSystem.add(300.0f);
        assertEquals(0, TimerSystem.getTimeLeft());
    }

    @Test
    /**
     * checks the getter for time when negative
     * shoudl eb when t > 300
     * sets time to past time then checks
     */
    public void testGetTimeLeftNegative() {
        timerSystem.add(350.0f);
        assertEquals(-50, TimerSystem.getTimeLeft());
    }


    //checking display of clock, thus time.
    //in m:ss form
    @Test
    /**
     * tests getClockDisplay
     * checks for correct output at set times
     */
    public void testGetClockDisplay() {
        //test initial display 5'
        assertEquals("Time Left: 5:00", TimerSystem.getClockDisplay());

        //test after 60 seconds 4'
        timerSystem.add(60.0f);
        assertEquals("Time Left: 4:00", TimerSystem.getClockDisplay());

        //test with seconds remaining 2'35"
        TimerSystem.elapsedTime = 145.0f; // 300 - 145 = 155" 
        //LEFT IS 2'35"
        assertEquals("Time Left: 2:35", TimerSystem.getClockDisplay());
    }

    @Test
    /**
     * checks for correct time when theres a leading zero in seconds
     * as in m:0s
     * id est 0:06 and not 0:6
     */
    public void testGetClockDisplayWithLeadingZero() {
        //seconds displayed with leading zero
        TimerSystem.elapsedTime = 294.0f; //6" left
        assertEquals("Time Left: 0:06", TimerSystem.getClockDisplay());
    }

    
    @Test
    /**
     * test when >1 call to add is made in sequence
     * tests correct total time added
     */
    public void testMultipleAddCalls() {

        //multiple sequential adds
        timerSystem.add(10.0f);
        timerSystem.add(20.0f);
        timerSystem.add(30.0f);

        //30+20+10 is 60"
        assertEquals(60.0f, TimerSystem.elapsedTime, 0.01f);
        assertEquals(240, TimerSystem.getTimeLeft());
    }
}
