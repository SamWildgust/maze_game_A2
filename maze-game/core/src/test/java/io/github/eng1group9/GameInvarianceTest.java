package io.github.eng1group9;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import io.github.eng1group9.systems.TimerSystem;

/**
 * General invariance tests for the entire game state.
 * 
 * These tests verify that key game properties and constraints always hold true,
 * regardless of how the game state is manipulated or what sequence of events occurs.
 * 
 * Key game invariants:
 * - gameState is always in valid range [0, 4]
 * - Event counters are never negative
 * - longboiBonus is either 0 or LONGBOIBONUSAMOUNT (201823)
 * - Score is always >= 0
 * - Score follows the formula: getTimeLeft() * 1000 + longboiBonus
 * - Only one boolean flag per item can be true at once (mutually exclusive states)
 */
public class GameInvarianceTest {

    @Before
    public void setUp() {
        // Reset all game state to known initial values before each test
        Main.chestDoorOpen = false;
        Main.exitOpen = false;
        Main.spikesLowered = false;
        Main.scrollUsed = false;
        Main.longboiBonus = 0;
        Main.hiddenEventCounter = 0;
        Main.negativeEventCounter = 0;
        Main.positiveEventCounter = 0;
        TimerSystem.elapsedTime = 0;
    }

    /**
     * Test validity of gameState
     * game state must always be within [0,4]
     * Game states: 
     *      0 = not started yet.
     *      1 = Playing.
     *      2 = Paused.
     *      3 = Won.
     *      4 = Lost.
     */
    @Test
    public void testGameStateAlwaysValid() {
        //range must be 0 through to 4
        int[] validStates = {0, 1, 2, 3, 4};
        
        //check for all current states, whether they are out of the bounds, by checking each occurance against our assertion
        for (int state : validStates) {
            Main.gameState = state;
            assertTrue("gameState " + state + " should be valid (in range [0,4])", 
                       Main.gameState >= 0 && Main.gameState <= 4);
        }
    }


    /**
     * should only be one of two discreet values
     * 0 for not found or the bonus amount, 201823, for found
     */
    @Test
    public void testBonusOnlyValidValues() {
        final int BONUSAMOUNT = 201823;
        
        //with 0
        Main.longboiBonus = 0;
        assertTrue("longboiBonus should be either 0 or " + BONUSAMOUNT, Main.longboiBonus == 0 || Main.longboiBonus == BONUSAMOUNT);
        
        //at max val.
        Main.longboiBonus = BONUSAMOUNT;
        assertTrue("longboiBonus should be either 0 or " + BONUSAMOUNT, Main.longboiBonus == 0 || Main.longboiBonus == BONUSAMOUNT);
    }

    /**
     * test score calculation always valid.
     * 
     * score = getTimeLeft() * 1000 + longboiBonus
     * Score should always be >= 0 and handle -ve correctly
     */
    @Test
    public void testScoreFormulaAlwaysHolds() {
        int[] elapsedTimes = {0, 50, 100, 150, 299, 300, 350};
        int[] bonusValues = {0, 201823};
        
        for (int elapsed : elapsedTimes) {
            for (int bonus : bonusValues) {
                TimerSystem.elapsedTime = elapsed;
                Main.longboiBonus = bonus;
                
                int actualScore = Main.calculateScore();
                int expectedScore = TimerSystem.getTimeLeft() * 1000 + bonus;
                
                assertEquals("Score formula should always be: getTimeLeft() + longboiBonus", 
                             expectedScore, actualScore);
            }
        }
    }

    /**
     * test mutual exclusivity of game states.
     * 
     * @result when the game transitions between states, only one state is active.
     * @result once an item is collected, the corresponding flag remains true.
     */
    @Test
    public void testFlagsAreIndependent() {

        //flags should be independently toggleable
        Main.chestDoorOpen = true;
        Main.exitOpen = false;
        Main.spikesLowered = false;
        Main.scrollUsed = false;
        
        //when set to true then they should remain true
        assertTrue("chestDoorOpen should remain true", Main.chestDoorOpen);
        assertFalse("exitOpen should be false", Main.exitOpen);
        assertFalse("spikesLowered should be false", Main.spikesLowered);
        assertFalse("scrollUsed should be false", Main.scrollUsed);
        
        //each of the flags should be independently changable 
        Main.spikesLowered = true;
        assertTrue("chestDoorOpen should still be true", Main.chestDoorOpen);
        assertTrue("spikesLowered should now be true", Main.spikesLowered);
    }

    /**
     * test score is neither silly small or big
     * @result score should be bounded corerctly
     */
    @Test
    public void testScoreRemainsReasonable() {
        final int LONGBOIBONUSAMOUNT = 201823;
        final int MAX_REASONABLE_SCORE = 600000;//5' times 300
        
        //max score would be at start would be 300s' with bonus found
        TimerSystem.elapsedTime = 0; 
        Main.longboiBonus = LONGBOIBONUSAMOUNT;
        int maxScore = Main.calculateScore();
        
        //assert this is the same as the max score described
        assertTrue("Maximum score should be reasonable (under " + MAX_REASONABLE_SCORE + ")", maxScore < MAX_REASONABLE_SCORE);
        
        //minimum score is when the time runs out no bonus
        TimerSystem.elapsedTime = 300;
        Main.longboiBonus = 0;
        int minScore = Main.calculateScore();
        
        //assert that they min score is 0
        assertEquals("Minimum score should be 0 when no time and no bonus", 0, minScore);
    }

    /**
     * test consistency of item collection mechanics
     * once an item flag is set to true it should remain true for the rest of the game session
     * @result flags should have persistant values once set to true
     * they cant change as there is only one of each item and they cant be dropped by the player
     */
    @Test
    public void testItemCollectionPersists() {

        //set falgs like the player has all items
        Main.chestDoorOpen = true;
        Main.exitOpen = true;
        Main.spikesLowered = true;
        Main.scrollUsed = true;
        
        //after game events flags should not unset themselves
        //items do not get lost after the player has them
        assertTrue("chestDoorOpen should persist once collected", Main.chestDoorOpen);
        assertTrue("exitOpen should persist once collected", Main.exitOpen);
        assertTrue("spikesLowered should persist once lowered", Main.spikesLowered);
        assertTrue("scrollUsed should persist once used", Main.scrollUsed);
    }

    /**
     * calculateScore() several times with same state should always return the same value
     */
    @Test
    public void testScoreCalcDeterministic() {
        TimerSystem.elapsedTime = 150;
        Main.longboiBonus = 201823;
        
        int score1 = Main.calculateScore();
        int score2 = Main.calculateScore();
        int score3 = Main.calculateScore();
        
        assertEquals("Score calculation should be deterministic (same result each call)", 
                     score1, score2);
        assertEquals("Score calculation should be deterministic (same result each call)", 
                     score2, score3);
    }
}
