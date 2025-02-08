package org.example.vetorrally.view;

import org.example.vetorrally.model.Car;
import org.example.vetorrally.model.TrackElement;
import org.example.vetorrally.model.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * handles rendering of track data and car data
 */
public class Renderer {
    private TrackElement[][] grid;
    private int width;
    private int height;
    private List<Car> cars;
    private List<Vector2D> occupiedSpace;
    private Vector2D projection;

    /**
     * constructor
     * @param grid the track to render
     */
    public Renderer(TrackElement[][] grid) {
        this.grid = grid.clone();
        width = this.grid[0].length;
        height = this.grid.length;
        occupiedSpace = new ArrayList<Vector2D>();
    }

    /**
     * print current track state
     */
    public void printTrack() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                switch (grid[y][x]) {
                    case BOUNDARY:
                        System.out.print(TrackElement.BOUNDARY.getSymbol());
                        break;
                    case START:
                        System.out.print(TrackElement.START.getSymbol());
                        break;
                    case FINISH:
                        System.out.print(TrackElement.FINISH.getSymbol());
                        break;
                    case TRACK:
                        System.out.print(TrackElement.TRACK.getSymbol());
                        break;
                    case OCCUPIED:
                        System.out.print(TrackElement.OCCUPIED.getSymbol());
                        break;
                    case PLAYER:
                        System.out.print(TrackElement.PLAYER.getSymbol());
                        break;
                    case NEXTPROJECTION:
                        System.out.print(TrackElement.NEXTPROJECTION.getSymbol());
                        break;
                }
            }
            System.out.print(y);
            System.out.println();
        }
    }


    /**
     * set a position on the track as occupied
     * @param v Vector2D object of the position to be set as occupied (bot)
     */
    public void setOccupied(Vector2D v){
        if(v.getY() < grid.length && v.getY() >= 0 && v.getX() < grid[0].length && v.getX() >= 0){
            occupiedSpace.add(new Vector2D(v.getX(), v.getY()));
            grid[v.getY()][v.getX()] = TrackElement.OCCUPIED;
        }
    }

    /**
     * set a position on the track as occupied (player)
     * note: does not take Vector2D object for future expansion
     * @param x player x position
     * @param y player y position
     */
    public void setOccupiedByPlayer(int x, int y){
        if(y < grid.length && y >= 0 && x < grid[0].length && x >= 0){
            occupiedSpace.add(new Vector2D(x, y));
            grid[y][x] = TrackElement.PLAYER;
        }
    }

    /**
     * clear all occupied spaces of the track
     */
    public void clearAllOccupiedSpaces(){
        for(Vector2D v : occupiedSpace){
            grid[v.getY()][v.getX()]=TrackElement.TRACK;
        }
        occupiedSpace.clear();
    }

    /**
     * clear a single occupied space (used for bot movement)
     * @param v
     */
    public void clearOcucpiedSpace(Vector2D v){
        int i = occupiedSpaceIndex(v);
        if(i != -1){
            occupiedSpace.remove(i);
            grid[v.getY()][v.getX()]=TrackElement.TRACK;
        }
    }

    /**
     * find the index of a Vector2D object on the occpied list
     * @param v Vector2D object to be found on the occupied list
     * @return index of the object, if not found defaults to -1
     */
    private int occupiedSpaceIndex(Vector2D v){
        for (Vector2D occupied : occupiedSpace) {
            if(occupied.equals(v)){
                return occupiedSpace.indexOf(v); // object found
            }
        }
        return -1; // default
    }

    /**
     * project the player next movement center to facilitate player input
     * @param projCoord Vector2D object of the projection
     */
    public void projectCarCenterPos(Vector2D projCoord){
        clearProjection();
        projection = projCoord;
        int x = projCoord.getX();
        int y = projCoord.getY();

        if(x < grid[0].length && y < grid.length && y >= 0 && x >= 0){
            TrackElement collisionCheck = grid[y][x];
            if(collisionCheck != TrackElement.OCCUPIED && collisionCheck != TrackElement.BOUNDARY) {
                grid[projection.getY()][projection.getX()] = TrackElement.NEXTPROJECTION;
            }
        } else {
          System.out.println("Projection out of bounds");
        }
    }


    /**
     * clear the projection from the rendered track
     */
    private void clearProjection(){
        if(projection != null){
            grid[projection.getY()][projection.getX()]=TrackElement.TRACK;
        }
    }

}
