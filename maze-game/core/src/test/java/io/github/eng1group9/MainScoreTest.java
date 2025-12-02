package io.github.eng1group9;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import io.github.eng1group9.systems.TimerSystem;

/**
 * tests for score calculation in {@link Main}.
 */
public class MainScoreTest {

    @After
    /**
     * this is an after-test method to reset var.s
     * re-initialises elapsed time and longboiBonus
     * @return void
     */
    public void tearDown() {

        //reset static state thus avoid interference
        TimerSystem.elapsedTime = 0f;
        Main.longboiBonus = 0;
    }

    @Before
    /**
     * before-test method to ensure main thus timer is initialised
     * @throws Exception
     * @return void
     */
    public void ensureMainInitialised() throws Exception {

        //force Main initialisation so static TimerSystem is created
        //set TimerSystem.elapsedTime afterwards without resetting it
        Class.forName("io.github.eng1group9.Main");
    }

    @Test
    /**
     * for score without bonuses
     * score is asserted to be just based on time
     * @return void
     */
    public void testCalculateScore_noBonus() {

        //make getTimeLeft() return 5
        TimerSystem.elapsedTime = 295f; //5" remaining
        Main.longboiBonus = 0;

        //assert actual is same as what's expected
        int expected = 5 * 1000 + 0;
        int actual = Main.calculateScore();
        assertEquals("Score should be 1000 * timeLeft with no bonus", expected, actual);
    }

    @Test
    /**
     * check score when bonus is added for longboi
     * @return void
     */
    public void testCalculateScore_withLongboiBonus() {
        
        //make getTimeLeft() return 1
        TimerSystem.elapsedTime = 299f; //1" remaiing
        Main.longboiBonus = 201823;

        //create the expected and assert same as actual
        int expected = 1 * 1000 + 201823;
        int actual = Main.calculateScore();
        assertEquals("Score should include longboi bonus", expected, actual);
    }
}
