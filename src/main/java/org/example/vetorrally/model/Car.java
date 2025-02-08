package org.example.vetorrally.model;

/**
 * class used to represent any car on the track, handleing movement and inertia
 */
public class Car {
    private Vector2D position; // actual car position
    private Vector2D speed; // basically the last movement made
    private int id; // id to identifie the car

    /**
     * constructor
     * @param startpos car starting position
     * @param id car identifier
     */
    public Car(Vector2D startpos, int id) {
        position = startpos;
        speed = new Vector2D(0,0);
        this.id = id;
    }

    // getters
    public Vector2D getPosition() {return position;}
    public Vector2D getSpeed() {return speed;}
    public int getId() {return id;}

    /**
     * calculate the next position the car would land in if it made the same previous movement to simulate inertia
     * @return projection of the next position
     */
    public Vector2D getNextPosition(){
        Vector2D nextPos = new Vector2D(position.getX() + speed.getX(), position.getY() + speed.getY());
        return nextPos;
    }

    /**
     * move the car giving a delta from the projected position
     * @param delta Vector2D object with values ranging from 1 and -1 on both y and x
     */
    public void move(Vector2D delta) {
        if(delta.getX() <=1 && delta.getY() <=1 && delta.getX() >=-1 && delta.getY() >=-1) {
            speed.add(delta);
            position.add(speed);
        }
        else{
            throw new RuntimeException("Invalid move");
        }
    }
}
