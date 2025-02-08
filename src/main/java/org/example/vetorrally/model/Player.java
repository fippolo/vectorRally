package org.example.vetorrally.model;

import org.example.vetorrally.controller.InputHandler;


public class Player extends Car {
    private final InputHandler inputHandler;
    private Vector2D lastPos;
    /**
     * player object constructor
     * @param startpos initial player position
     */
    public Player(Vector2D startpos, InputHandler inputHandler) {
        super(startpos);
        lastPos = startpos;
        this.inputHandler = inputHandler;
    }

    /**
     * method to handle player input and execution
     */
    public void nextmove(){
        int x;
        int y;

        //asks next move
        System.out.println("chose your next move (0,0) (0,1) (0,-1)...");

        x = inputHandler.getIntInput("input x : 0 or 1 (right) or -1 (left):");

        y = inputHandler.getIntInput("input y : 0 or 1 (down) or -1 (up):");
        Vector2D delta = new Vector2D(x, y);

        //if non-valid values were inputted, the function calls itself and return
        if(delta.getX() > 1 || delta.getY() > 1 || delta.getX() < -1 || delta.getY() < -1){
            System.out.println("Invalid input. Please enter a value between -1 and 1 for both x and y.");
            nextmove();
            return;
        }

        //if valid values were inputed, the move is executed
        lastPos = this.getPosition();
        move(new Vector2D(x, y));
    }
}
