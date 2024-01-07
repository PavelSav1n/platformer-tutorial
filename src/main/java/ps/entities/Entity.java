package ps.entities;

public abstract class Entity {

    protected float x, y; // class that extends this class can use these.

    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
