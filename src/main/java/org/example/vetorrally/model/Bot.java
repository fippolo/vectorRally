package org.example.vetorrally.model;
import java.util.*;

/**
 * bot class that handle bot individual behaviour
 */
public class Bot extends Car {
    private final Track track;
    private final boolean chasePlayer;
    private Vector2D playerPosition;

    /**
     * constructor
     * @param track the track the bot is going to run on
     * @param startpos bot starting position
     * @param id bot identifier
     */
    public Bot(Track track, Vector2D startpos, int id, boolean chasePlayer) {
        super(startpos);  // Initialize the Car part of Bot
        this.track = track;
        this.chasePlayer = chasePlayer;
    }

    public void updatePlayerPosition(Vector2D playerPos) {
        this.playerPosition = playerPos;
    }

    /**
     * Method to move the bot towards the finish line using BFS
     * @return the next most effecient position to be in according to bfs
     */
    public Vector2D findNextMove() {
        return chasePlayer ? chasePlayerStrategy() : chaseFinishLineStrategy();
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

    private Vector2D chasePlayerStrategy() {
        if (playerPosition == null) {
            return chaseFinishLineStrategy();
        }

        Queue<Vector2D> queue = new LinkedList<>();
        queue.add(getPosition());

        boolean[][] visited = new boolean[track.getHeight()][track.getWidth()];
        visited[getPosition().getY()][getPosition().getX()] = true;

        Map<Vector2D, Vector2D> previous = new HashMap<>();
        previous.put(getPosition(), null);

        Vector2D[] directions = {
                new Vector2D(0, 1), new Vector2D(0, -1),
                new Vector2D(1, 0), new Vector2D(-1, 0),
                new Vector2D(1, 1), new Vector2D(1, -1),
                new Vector2D(-1, 1), new Vector2D(-1, -1)
        };

        while (!queue.isEmpty()) {
            Vector2D current = queue.poll();

            // Found player position
            if (current.getX() == playerPosition.getX() &&
                    current.getY() == playerPosition.getY()) {
                List<Vector2D> path = walkbackPath(current, previous);
                return path.size() > 1 ? path.get(1) : current;
            }

            for (Vector2D direction : directions) {
                Vector2D newPosition = new Vector2D(
                        current.getX() + direction.getX(),
                        current.getY() + direction.getY()
                );

                if (isValidMove(newPosition) &&
                        !visited[newPosition.getY()][newPosition.getX()]) {
                    visited[newPosition.getY()][newPosition.getX()] = true;
                    previous.put(newPosition, current);
                    queue.add(newPosition);
                }
            }
        }

        // Fallback to finish line strategy if player cannot be reached
        return chaseFinishLineStrategy();
    }

    private Vector2D chaseFinishLineStrategy() {
        // Original findNextMove implementation
        Queue<Vector2D> queue = new LinkedList<>();
        queue.add(getPosition());

        boolean[][] visited = new boolean[track.getHeight()][track.getWidth()];
        visited[getPosition().getY()][getPosition().getX()] = true;

        Map<Vector2D, Vector2D> previous = new HashMap<>();
        previous.put(getPosition(), null);

        Vector2D[] directions = {
                new Vector2D(0, 1), new Vector2D(0, -1),
                new Vector2D(1, 0), new Vector2D(-1, 0),
                new Vector2D(1, 1), new Vector2D(1, -1),
                new Vector2D(-1, 1), new Vector2D(-1, -1)
        };

        while (!queue.isEmpty()) {
            Vector2D current = queue.poll();

            for (Vector2D direction : directions) {
                Vector2D newPosition = new Vector2D(
                        current.getX() + direction.getX(),
                        current.getY() + direction.getY()
                );

                if (isValidMove(newPosition) &&
                        !visited[newPosition.getY()][newPosition.getX()]) {
                    visited[newPosition.getY()][newPosition.getX()] = true;
                    previous.put(newPosition, current);

                    if (track.getTrackElement(newPosition) == TrackElement.FINISH) {
                        List<Vector2D> path = walkbackPath(newPosition, previous);
                        return path.size() > 1 ? path.get(1) : newPosition;
                    }

                    queue.add(newPosition);
                }
            }
        }

        System.out.println("Bot could not find path to finish line.");
        return null;
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
