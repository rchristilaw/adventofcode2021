import lombok.Data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Day05 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day05.class.getName());
    private final static String inputFile = "day05.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day5 = new Day05();
        day5.runPart1();
        day5.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        int maxX = 0;
        int maxY = 0;

        final var coordinateList = new ArrayList<Coordinates>();
        for (final var line : input) {
            final var groups = line.split("->");
            final var group1 = groups[0].split(",");
            final var group2 = groups[1].split(",");

            final var coords = new Coordinates();
            coords.setX1(Integer.parseInt(group1[0].trim()));
            coords.setY1(Integer.parseInt(group1[1].trim()));
            coords.setX2(Integer.parseInt(group2[0].trim()));
            coords.setY2(Integer.parseInt(group2[1].trim()));
            coordinateList.add(coords);

            maxX = Math.max(coords.x1, maxX);
            maxX = Math.max(coords.x2, maxX);
            maxY = Math.max(coords.y1, maxY);
            maxY = Math.max(coords.y2, maxY);
        }

        log.info("Max X: " + maxX);
        log.info("Max Y: " + maxY);

        final var grid = new int[maxX + 1][maxY + 1];

        for (final var coord : coordinateList) {

            final var changeX = coord.getX2() - coord.getX1();
            final var changeY = coord.getY2() - coord.getY1();

            if (changeX != 0 && changeY != 0) {
                continue;
            }

            if (changeX != 0) {
                final var startX = changeX > 0 ? coord.getX1() : coord.getX2();
                for (int i = 0; i <= abs(changeX); i++) {
                    grid[startX + i][coord.getY1()]++;
                }
            } else if (changeY != 0) {
                final var startY = changeY > 0 ? coord.getY1() : coord.getY2();
                for (int i = 0; i <= abs(changeY); i++) {
                    grid[coord.getX1()][startY + i]++;
                }
            } else {
                grid[coord.getX1()][coord.getY1()]++;
            }
        }

        var count = 0;
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (grid[i][j] > 1) {
                    count++;
                }
            }
        }

        log.info("TOTAL: " + count);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        int maxX = 0;
        int maxY = 0;

        final var coordinateList = new ArrayList<Coordinates>();
        for (final var line : input) {
            final var groups = line.split("->");
            final var group1 = groups[0].split(",");
            final var group2 = groups[1].split(",");

            final var coords = new Coordinates();
            coords.setX1(Integer.parseInt(group1[0].trim()));
            coords.setY1(Integer.parseInt(group1[1].trim()));
            coords.setX2(Integer.parseInt(group2[0].trim()));
            coords.setY2(Integer.parseInt(group2[1].trim()));
            coordinateList.add(coords);

            maxX = Math.max(coords.x1, maxX);
            maxX = Math.max(coords.x2, maxX);
            maxY = Math.max(coords.y1, maxY);
            maxY = Math.max(coords.y2, maxY);
        }

        log.info("Max X: " + maxX);
        log.info("Max Y: " + maxY);

        final var grid = new int[maxX + 1][maxY + 1];

        for (final var coord : coordinateList) {
            final var changeX = coord.getX2() - coord.getX1();
            final var changeY = coord.getY2() - coord.getY1();
            if (changeX != 0 && changeY != 0) {
                for (int i = 0; i <= abs(changeX); i++) {
                    int currX = changeX > 0 ? coord.getX1() + i : coord.getX1() - i;
                    int currY = changeY > 0 ? coord.getY1() + i : coord.getY1() - i;
                    grid[currX][currY]++;
                }
            } else if (changeX != 0) {
                final var startX = changeX > 0 ? coord.getX1() : coord.getX2();
                for (int i = 0; i <= abs(changeX); i++) {
                    grid[startX + i][coord.getY1()]++;
                }
            } else if (changeY != 0) {
                final var startY = changeY > 0 ? coord.getY1() : coord.getY2();
                for (int i = 0; i <= abs(changeY); i++) {
                    grid[coord.getX1()][startY + i]++;
                }
            } else {
                grid[coord.getX1()][coord.getY1()]++;
            }
        }

        var count = 0;
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (grid[i][j] > 1) {
                    count++;
                }
            }
        }

        log.info("TOTAL: " + count);
    }

    @Data
    private static class Coordinates {
        int x1;
        int y1;
        int x2;
        int y2;
    }
}