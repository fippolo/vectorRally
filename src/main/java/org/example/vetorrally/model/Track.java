package org.example.vetorrally.model;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * representation of the game track inputed from a file
 */
public class Track {
    private TrackElement[][] grid;
    private int width;
    private int height;
    private final List<Vector2D> startLine;
    private final List<Vector2D> finishLine;
    private int carQuantity;

    /**
     * constructor
     * @param filePath file path to txt file containing a track
     * @throws IOException needed to trhow io exception when reading file
     */
    public Track(String filePath) throws IOException {
        startLine = new ArrayList<>();
        finishLine = new ArrayList<>();
        loadTrack(filePath);
    }

    /**
     * load track from file
     * @param filePath the file of track data
     * @throws IOException throws exception if file is missing or unreadable
     */
    private void loadTrack(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                // extracting number of cars
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(line);
                StringBuilder modifiedText = new StringBuilder();
                int lastIndex = 0;
                while (matcher.find()) {
                    String numberStr = matcher.group();
                    int number = Integer.parseInt(numberStr);
                    carQuantity += number;
                    modifiedText.append(line, lastIndex, matcher.start());
                    lastIndex = matcher.end();
                }
                modifiedText.append(line.substring(lastIndex));


                lines.add(modifiedText.toString());
            }
        }
        height = lines.size();
        width = lines.get(0).length();
        grid = new TrackElement[height][width];
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                char c = line.charAt(x);
                switch (c) {
                    case '#':
                        grid[y][x] = TrackElement.BOUNDARY; // Boundary
                        break;
                    case 'S':
                        startLine.add(new Vector2D(x, y));
                        grid[y][x] = TrackElement.START; // Start line
                        break;
                    case 'F':
                        finishLine.add(new Vector2D(x, y));
                        grid[y][x] = TrackElement.FINISH; // Finish line
                        break;
                    default:
                        grid[y][x] = TrackElement.TRACK; // Track path
                        break;
                }
            }
        }
    }

    //getters
    public int getHeight() { return height; }
    public int getWidth() { return width; }
    public List<Vector2D> getStartLine() { return startLine; }
    public List<Vector2D> getFinishLine() { return finishLine; }
    public TrackElement[][] getGrid() {return grid;}

    /**
     * get the maximum amaunt of car that can be placed on the start line
     * @return max amount of cars that can be placed on the start line
     */
    public int getMaxCar(){
        return startLine.size();
    }

    /**
     * get the car quantity specified in the track file
     * @return car quantity specified in the track file
     */
    public int getCarQuantity(){
        return carQuantity;
    }

    /**
     * get the track element at a specified position
     * @param position Vector2D object specifying the position to be analyzed
     * @return TrackElement of the inquired position
     */
    public TrackElement getTrackElement(Vector2D position){
        return grid[position.getY()][position.getX()];
    }


}
