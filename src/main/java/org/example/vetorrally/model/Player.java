package org.example.vetorrally.model;

import java.util.Scanner;

public class Player extends Car {
    /**
     * player object constructor
     * @param startpos initial player position
     * @param id player id
     */
    public Player(Vector2D startpos, int id) {
        super(startpos, id);
    }

    /**
     * method to handle player input and execution
     */
    public void nextmove(){
        Scanner scanner = new Scanner(System.in);
        int x = 0;
        int y = 0;
        boolean notvalid = true;

        //asks next move
        System.out.println("chose your next move (0,0) (0,1) (0,-1)...");
        System.out.println("input x 0 or 1 (right) or -1 (left):");
        while(notvalid){
            if (scanner.hasNextInt()) {
                x = scanner.nextInt();
                notvalid = false;
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                if(scanner.hasNextInt())
                    scanner.next(); // Clear the invalid input
            }
        }

        notvalid = true;

        System.out.println("input y 0 or 1 (down) or -1 (up):");
        while(notvalid){
            if (scanner.hasNextInt()) {
                y = scanner.nextInt();
                notvalid = false;
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        Vector2D delta = new Vector2D(x, y);

        //if non valid values were inputed, the fuctions calls itself and return
        if(delta.getX() > 1 || delta.getY() > 1 || delta.getX() < -1 || delta.getY() < -1){
            System.out.println("Invalid input. Please enter a value between -1 and 1 for both x and y.");
            nextmove();
            return;
        }

        //if valid values were inputed, the move is executed
        move(new Vector2D(x, y));
    }
}
