package game;

/**
 * This interface is for game objects that need to be updated during each frame.
 * Classes that implement this interface should define behavior for their update logic of things such as
 * movement, animation, or state changes.
 */
public interface Updatable {

    /**
     * Updates the object's state for the current frame.
     * It is called once per game loop iteration.
     */
    public void update();
    
}
