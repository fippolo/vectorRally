package org.example.vetorrally.model;
import java.io.IOException;
import java.util.*;

/**
 * bot class that handle bot individual behaviour
 */
public class Bot extends Car {
    private Track track;

    /**
     * constructor
     * @param track the track the bot is going to run on
     * @param startpos bot starting position
     * @param id bot identifier
     */
    public Bot(Track track, Vector2D startpos, int id) {
        super(startpos, id);  // Initialize the Car part of Bot
        this.track = track;
    }

    /**
     * Method to move the bot towards the finish line using BFS
     * @return the next most effecient position to be in according to bfs
     */
    public Vector2D findNextMove() {
        Queue<Vector2D> queue = new LinkedList<>();
        queue.add(getPosition());  // Add the start position to the queue

        boolean[][] visited = new boolean[track.getHeight()][track.getWidth()];
        visited[getPosition().getY()][getPosition().getX()] = true;  // Mark the start position as visited

        // Map to store the previous position for each position (for path tracking)
        Map<Vector2D, Vector2D> previous = new HashMap<>();
        previous.put(getPosition(), null);

        // Possible movement directions: down, up, right, left, and diagonals
        Vector2D[] directions = {
                new Vector2D(0, 1),   // Down
                new Vector2D(0, -1),  // Up
                new Vector2D(1, 0),   // Right
                new Vector2D(-1, 0),  // Left
                new Vector2D(1, 1),   // Down-Right
                new Vector2D(1, -1),  // Up-Right
                new Vector2D(-1, 1),  // Down-Left
                new Vector2D(-1, -1)  // Up-Left
        };

        // Breadth-First Search (BFS) algorithm
        while (!queue.isEmpty()) {
            Vector2D current = queue.poll();  // Get the next position to explore

            for (Vector2D direction : directions) {
                Vector2D newPosition = new Vector2D(current.getX() + direction.getX(), current.getY() + direction.getY());

                // Check if the move is valid and the position has not been visited
                if (isValidMove(newPosition) && !visited[newPosition.getY()][newPosition.getX()]) {
                    visited[newPosition.getY()][newPosition.getX()] = true;  // Mark the new position as visited
                    previous.put(newPosition, current);  // Track the path

                    // Check if the new position is the finish line
                    if (track.getTrackElement(newPosition) == TrackElement.FINISH) {
                        List<Vector2D> path = walkbackPath(newPosition, previous);
                        return path.get(1); // return index 1 because 0 is the current position
                    }

                    // Add the new position to the queue to explore further
                    queue.add(newPosition);
                }
            }
        }

        // If the queue is empty and the finish line was not reached
        System.out.println("a bot could not find a path to the finish line.");
        return null;
    }

    /**
     * Helper method to check if a move is valid
     * @param position Vector2D of a position to be analyzed
     * @return true if the position is valid
     */
    private boolean isValidMove(Vector2D position) {
        return position.getY() >= 0 && position.getX() >= 0 &&
                position.getY() < track.getHeight() && position.getX() < track.getWidth() &&
                track.getTrackElement(position) != TrackElement.BOUNDARY && track.getTrackElement(position) != TrackElement.OCCUPIED;
    }

    /**
     * Method to trace route back to the bor position from finish line and reverse it
     * @param finish Vector2D object the finish line position
     * @param previous BFS node map
     * @return a list of position that takes straight to the finish line
     */
    private List<Vector2D> walkbackPath(Vector2D finish, Map<Vector2D, Vector2D> previous) {
        List<Vector2D> path = new LinkedList<>();
        for (Vector2D at = finish; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * given a Vector2D position it calculate the required move to get there and tries to execute it
     * @param nextPosition the requested position to be in
     */
    public void moveTo(Vector2D nextPosition) {
        nextPosition.subtract(this.getPosition()); //
        Vector2D delta = nextPosition;
        delta.subtract(this.getSpeed()); // factor in speed
        Vector2D debugx = this.getPosition();

        System.out.println(debugx);
        move(delta);  // execute move
    }

}
