package io.github.eng1group9.entities;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;

import com.badlogic.gdx.math.Rectangle;

/**
 * tests for the Dean
 * uses minimal constructors creating what class dean only needs
 */
public class DeanTest {

    /**
     * can dean reach the player if visible
     * @throws Exception
     */
    @Test
    public void testReachPlayerVisible() throws Exception {
        Dean dean = new Dean();

        //give dean a reach rectangle
        Rectangle reach = new Rectangle(10, 10, 32, 32);
        Field reachField = Dean.class.getDeclaredField("reachRectangle");
        reachField.setAccessible(true);
        reachField.set(dean, reach);

        //create a player dummy
        Player player = new Player();

        //set its hitbox to overlap dean's reach
        player.setHitbox(new Rectangle(12, 12, 8, 8));

        //ensure player is visible (invisibilityLeft <= 0)
        Field invisField = Player.class.getDeclaredField("invisibilityLeft");
        invisField.setAccessible(true);
        invisField.setFloat(player, 0f);

        assertTrue("Dean should be able to reach overlapping visible player", dean.canReach(player));
    }

    /**
     * can dean reach the invisilbe player
     * @throws Exception
     */
    @Test
    public void testReachPlayerInvisible() throws Exception {

        //create a dummy dean
        Dean dean = new Dean();

        //sets the reach hitbox to size to test
        Rectangle reach = new Rectangle(0, 0, 32, 32);
        Field reachField = Dean.class.getDeclaredField("reachRectangle");
        reachField.setAccessible(true);
        reachField.set(dean, reach);

        //create a dummy player and init. hitbox to be in dean's reach
        Player player = new Player();
        player.setHitbox(new Rectangle(1, 1, 8, 8));

        //set the players invilibility to a non zero val.
        Field invisField = Player.class.getDeclaredField("invisibilityLeft");
        invisField.setAccessible(true);
        invisField.setFloat(player, 5f);

        //the player should not be seen if the both reaches overlap
        assertFalse("Dean should NOT reach invisible player even if overlapping", dean.canReach(player));
    }

    /**
     * tests the reastating of the path of the dean, called at the start of the loop through. 
     * should put values back to 0 and tile width
     * @throws Exception
     */
    @Test
    public void testRestartPath() throws Exception {

        //init. new dummy dean
        Dean dean = new Dean();

        //sets the progress of the walk as midway through
        Field moveNumField = Dean.class.getDeclaredField("moveNum");
        moveNumField.setAccessible(true);
        moveNumField.setInt(dean, 5);

        //set the distance to the next tile to not be some multiple of tile width
        Field nextTileField = Dean.class.getDeclaredField("nextTileDistance");
        nextTileField.setAccessible(true);
        nextTileField.setFloat(dean, 3f);

        dean.restartPath();

        //asserts that the number of moves should be back at 0 and the dist. to next tile should be the 32px tile width
        assertEquals("moveNum should be reset to 0 after restartPath", 0, moveNumField.getInt(dean));
        assertEquals("nextTileDistance should be reset to 32 after restartPath", 32f, nextTileField.getFloat(dean), 0.0001f);
    }

    /**
     * tests reach rectangle can be returned for 
     * @throws Exception
     */
    @Test
    public void testGetReachRectangle() throws Exception {

        //same as before init. a new dummy dean
        Dean dean = new Dean();

        //set the reach
        Rectangle reach = new Rectangle(7, 8, 16, 16);
        Field reachField = Dean.class.getDeclaredField("reachRectangle");
        reachField.setAccessible(true);
        reachField.set(dean, reach);

        Rectangle returned = dean.getReachRectangle();
        assertSame("getReachRectangle should return the stored reach rectangle", reach, returned);
    }

}
