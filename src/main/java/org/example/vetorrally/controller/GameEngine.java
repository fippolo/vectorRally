package org.example.vetorrally.controller;

import org.example.vetorrally.model.*;
import org.example.vetorrally.view.Renderer;

import java.io.IOException;
import java.util.*;

/**
 * main game engine
 */
public class GameEngine {
    private List<Bot> bots;
    private Track gameTrack;
    private Renderer renderer;
    private Player player;
    private AIDirector aidirector;

    /**
     * game engine constructor
     * @param trackPath the absolute path to the track file
     * @throws IOException threw if file is not found or can't be read
     */
    public GameEngine(String trackPath) throws IOException {
        bots = new ArrayList<>();
        startGame(trackPath);
    }

    /**
     * this helper method initializes all objects and launches the main gameloop()
     * @param trackPath the absolute path to the track file
     * @throws IOException threw if file is not found or can't be read
     */
    void startGame(String trackPath) throws IOException {
        gameTrack = new Track(trackPath); // init track
        renderer = new Renderer(gameTrack.getGrid()); // init renderer

        // validate player count
        if(gameTrack.getMaxCar()< gameTrack.getCarQuantity()){ throw new RuntimeException("too many players");}

        //initializing cars(bot and player)
        for(int i = 0; i < gameTrack.getCarQuantity(); i++) {
            if(i == 0) // player car will always have id=0
                player = new Player(gameTrack.getStartLine().get(i),i);
            else
                bots.add(new Bot(gameTrack, gameTrack.getStartLine().get(i), i));
        }
        aidirector = new AIDirector(gameTrack, bots,renderer);
        //launching gameloop
        gameloop();
    }

    //getters
    public List<Bot> getBots() {
        return bots;
    }
    public Track getGameTrack() {
        return gameTrack;
    }
    public Renderer getRenderer() {return renderer;}

    /**
     * uses render object to update all the car position
     */
    private void renderCarsPosition(){
        renderer.clearAllOccupiedSpaces();
        renderer.setOccupiedByPlayer(player.getPosition().getX(), player.getPosition().getY());
        for(Bot bot : bots) {
            renderer.setOccupied(bot.getPosition());
        }
    }

    /**
     * check for player win condition
     * @return true if he has won
     */
    private boolean hasPlayerWon(){
        for(Vector2D f : gameTrack.getFinishLine()){
            if(player.getPosition().getX() == f.getX() && player.getPosition().getY() == f.getY()){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    /**
     * check for player loose condition
     * @return true if he has crashed
     */
    private boolean hasPlayerCrashed(){
        //if out of track
        if(player.getPosition().getY() >= gameTrack.getHeight() || player.getPosition().getX() >= gameTrack.getWidth() )
            return true;
        //if hit boundary
        else return gameTrack.getTrackElement(player.getPosition()) == TrackElement.BOUNDARY;
    }

    /**
     * this is the main game loop, this function puts together
     * all the necessary components in the correct order to run
     * the game correctly, including checking for game over condition
     */
    private void gameloop(){
        boolean done = false;
        while(!done) {
            update();
            player.nextmove();
            if(done = (hasPlayerWon() || hasPlayerCrashed())){
                if(hasPlayerCrashed()){
                    System.out.println("Player crashed");
                } else if (hasPlayerWon()) {
                    System.out.println("Player won");
                }
                //padding
                System.out.println();
                update();
            }

        }

    }

    /**
     * wrapper used in gameloop() that simplifies code for the
     * aforementioned function
     */
    private void update(){
        aidirector.moveBots();
        renderer.projectCarCenterPos(player.getNextPosition());
        this.renderCarsPosition();
        renderer.printTrack();
        System.out.println("player position:             " + player.getPosition());
        System.out.println("player next move projection: " + player.getNextPosition());
    }

}
