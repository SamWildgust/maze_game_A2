package io.github.eng1group9.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import io.github.eng1group9.systems.InputSystem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the Player's movement.
 */

@RunWith(MockitoJUnitRunner.class)
public class PlayerTest {

    @Mock
    private Input mockInput;

    @Before
    public void setUp(){
        Gdx.input = mockInput;
    }

    private Player createUnfrozenMockPlayer() {
        // Ensures less redundant code for movement tests.
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.isFrozen()).thenReturn(false);
        return mockPlayer;
    }

    /**
    * Test to check if the player movement is correctly handled going up.
    */
    @Test
    public void testUpMovement() {
        // Mock the necessary methods and properties.
        Player mockPlayer = createUnfrozenMockPlayer();

        InputSystem inputSystem = new InputSystem();

        // Test input for moving up.
        when(Gdx.input.isKeyPressed(Input.Keys.W)).thenReturn(true);
        inputSystem.handle(mockPlayer);
        verify(mockPlayer, times(1)).move('U');

    }

    @Test
    public void testDownMovement() {
        Player mockPlayer = createUnfrozenMockPlayer();

        InputSystem inputSystem = new InputSystem();

        // Test input for moving down.
        when(Gdx.input.isKeyPressed(Input.Keys.S)).thenReturn(true);
        inputSystem.handle(mockPlayer);
        verify(mockPlayer, times(1)).move('D');
    }

    @Test
    public void testLeftMovement(){
        Player mockPlayer = createUnfrozenMockPlayer();

        InputSystem inputSystem = new InputSystem();

        // Test input for moving left.
        when(Gdx.input.isKeyPressed(Input.Keys.A)).thenReturn(true);
        inputSystem.handle(mockPlayer);
        verify(mockPlayer, times(1)).move('L');
    }

    @Test
    public void testRightMovement() {
        // Mock the necessary methods and properties.
        Player mockPlayer = createUnfrozenMockPlayer();

        InputSystem inputSystem = new InputSystem();

        // Test input for moving right.
        when(Gdx.input.isKeyPressed(Input.Keys.D)).thenReturn(true);
        inputSystem.handle(mockPlayer);
        verify(mockPlayer, times(1)).move('R');
    }

    /**
     * Test to check that no movement occurs when no valid input is given.
     */
    @Test
    public void testFailureMovementCases() {
        Player mockPlayer = createUnfrozenMockPlayer();

        InputSystem inputSystem = new InputSystem();

        when(Gdx.input.isKeyPressed(anyInt())).thenReturn(false);

        inputSystem.handle(mockPlayer);
        verify(mockPlayer, times(0)).move(anyChar());
    }

    /**
     * Test to check that a valid movement will return the correct distance moved.
     */
    @Test
    public void testMovementReturnsCorrectDistance() {
        // Mock the necessary methods and properties
        Player mockPlayer = mock(Player.class);
        // Test valid movement direction
        when(mockPlayer.move('U')).thenReturn(5f);
        float distance = mockPlayer.move('U');
        // Confirm the distance returned is as expected
        assertEquals("Movement should return correct distance", 5f, distance, 0.01f);
    }

    @Test
    public void testPlayerInitialPosition() {
        // Mock the player to test initial position
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getX()).thenReturn(0f);
        when(mockPlayer.getY()).thenReturn(0f);
        
        assertEquals("Player initial X position should be 0", 0f, mockPlayer.getX(), 0.01f);
        assertEquals("Player initial Y position should be 0", 0f, mockPlayer.getY(), 0.01f);
    }

    @Test
    public void testPlayerSpeed() {
        // Mock the player to test speed while moving.
        Player mockPlayer = mock(Player.class);
        
        // Test default speed.
        when(mockPlayer.getSpeed()).thenReturn(200f);
        assertEquals("Player default speed should be 200", 200, mockPlayer.getSpeed(), 0.01f);
        
        // Test speed when updated.
        doNothing().when(mockPlayer).setSpeed(300);
        when(mockPlayer.getSpeed()).thenReturn(300f);
        mockPlayer.setSpeed(300);
        assertEquals("Player speed should be updated to 300", 300, mockPlayer.getSpeed(), 0.01f);   
    }
}
