package org.example.vetorrally.model;

/**
 *  A class to represent 2D vectors with custom methods
 */
public class Vector2D {
    private int x;
    private int y;

    /**
     * constructor
     * @param x x value
     * @param y y value
     */
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * get x value
     * @return x value
     */
    public int getX() {
        return x;
    }

    /**
     * set x value
     * @param x x value to be set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * get y value
     * @return y value
     */
    public int getY() {
        return y;
    }
    /**
     * set y value
     * @param y y value to be set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * add a vector to caller object
     * @param other the vector to be added
     */
    public void add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    /**
     * subtract a vector to caller object
     * @param other the vector to be subtracted
     */
    public void subtract(Vector2D other) {
        this.x = this.x - other.x;
        this.y = this.y - other.y;
    }

    /**
     * scalar multiplication of caller object
     * @param scalar scalar factor
     */
    public void multiply(int scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    /**
     * scala division of caller object
     * @param scalar scalar factor
     */
    public void divide(int scalar) {
        if (scalar != 0) {
            this.x /= scalar;
            this.y /= scalar;
        }
    }

    /**
     * to string method for easy output
     * @return string composed of "Vector2D{x=[x value], y=[y value]}"
     */
    @Override
    public String toString() {
        return "Vector2D{" + "x=" + x + ", y=" + y + '}';
    }
}
