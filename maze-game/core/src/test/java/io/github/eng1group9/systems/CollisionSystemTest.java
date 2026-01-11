package io.github.eng1group9.systems;

import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CollisionSystem class
 * Testing CollisionSystem.java
 */
public class CollisionSystemTest {
    
    private CollisionSystem collisionSystem; // System under test
    private TiledMap mockMap;
    private MapLayer mockMapLayer; 
    private MapObjects mockMapObjects; 

    @Before
    public void setUp() {
        // Given a new CollisionSystem and mocked variables
        collisionSystem = new CollisionSystem();
        mockMap = mock(TiledMap.class);
        mockMapLayer = mock(MapLayer.class);
        mockMapObjects = mock(MapObjects.class);
        createCollisionLayer();
    }

    // Helper method to set up the collision layer in the mocked map
    private void createCollisionLayer() {
        MapLayers mockLayers = mock(MapLayers.class);
        when(mockMap.getLayers()).thenReturn(mockLayers);
        when(mockLayers.get("Collision")).thenReturn(mockMapLayer);
        when(mockMapLayer.getObjects()).thenReturn(mockMapObjects);
    }

    /**
     * Test that init() correctly creates collision rectangles from map objects.
     * Verifies that multiple RectangleMapObjects are properly extracted and stored.
     */
    @Test
    public void testInitCollisionCreation() {
        
        // Create two different collision rectangles
        RectangleMapObject rectangle1 = new RectangleMapObject(0, 0, 10, 10);
        RectangleMapObject rectangle2 = new RectangleMapObject(20, 20, 15, 15);
        
        // Mock the map objects to return both rectangles
        when(mockMapObjects.iterator()).thenReturn(java.util.List.<MapObject>of(rectangle1, rectangle2).iterator());
        
        collisionSystem.init(mockMap);
        
        // Asserts that both rectangles were added to the collision system
        assertEquals(2, collisionSystem.getWorldCollision().size());
    }

    /**
     * Test that init() correctly handles objects with and without names.
     * Verifies that named objects are included in collision detection.
     */
    @Test
    public void testInitNamedObjects() {
        
        // Create one unnamed and one named rectangle
        RectangleMapObject rectangle1 = new RectangleMapObject(0, 0, 10, 10);
        RectangleMapObject rectangle2 = new RectangleMapObject(20, 20, 15, 15);
        rectangle2.setName("wall");
        when(mockMapObjects.iterator()).thenReturn(java.util.List.<MapObject>of(rectangle1, rectangle2).iterator());
        
        collisionSystem.init(mockMap);
        
        // Assert that both objects were added regardless of name
        assertEquals(2, collisionSystem.getWorldCollision().size());
    }

    /**
     * Test that safeToMove() returns true when there is no collision overlap.
     * Verifies that distant collision objects do not block movement.
     * test that it returns false when there is an overlap.
     */
    @Test
    public void testSafeToMove() {
        
        // Create collision at far position (100, 100)
        RectangleMapObject rectangle = new RectangleMapObject(100, 100, 20, 20);
        when(mockMapObjects.iterator()).thenReturn(java.util.List.<MapObject>of(rectangle).iterator());
        
        collisionSystem.init(mockMap);
        
        // Test that movement is safe when no overlap occurs
        Rectangle hitbox = new Rectangle(0, 0, 16, 16);
        assertTrue(collisionSystem.safeToMove(0, 0, hitbox));
    }

    @Test
    /**
     * Test that safeToMove() returns false when there is a collision overlap.
     * Verifies that overlapping collision objects block movement.
     */
    public void testNotSafeToMove() {
        
        // Create collision at position (5, 5)
        RectangleMapObject rectangle = new RectangleMapObject(0, 0, 10, 10);
        when(mockMapObjects.iterator()).thenReturn(java.util.List.<MapObject>of(rectangle).iterator());
        
        collisionSystem.init(mockMap);
        
        // Test that movement is not safe when overlap occurs
        Rectangle hitbox = new Rectangle(10, 10, 16, 16);
        assertFalse(collisionSystem.safeToMove(0, 0, hitbox));
    }

    /**
     * Test removeCollisionByName() by creating a named rectangle and attempting to remove 
     * a rectangle with a different name.
     * Verifies that the collision list remains unchanged when no matching name is found.
     */
    @Test
    public void testRemoveCollision() {
        // Create one rectangle with a name
        RectangleMapObject rectangle = new RectangleMapObject(0, 0, 10, 10);
        rectangle.setName("wall");    

        when(mockMapObjects.iterator()).thenReturn(java.util.List.<MapObject>of(rectangle).iterator());
        collisionSystem.init(mockMap);

        // Attempt to remove a rectangle with a different name
        collisionSystem.removeCollisionByName("floor");
        // Assert that the collision list still contains the original rectangle
        assertEquals(1, collisionSystem.getWorldCollision().size());
    }

}
