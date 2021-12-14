import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day13 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day13.class.getName());
    private final static String inputFile = "day13.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day13 = new Day13();
        day13.runPart1();
        day13.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        var lines = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        int maxX = 0;
        int maxY = 0;
        final var coords = new ArrayList<Coord>();
        int i = 0;
        while(lines.get(i).length() != 0) {
            final var coordRaw = lines.get(i).split(",");
            final var x = Integer.parseInt(coordRaw[0]);
            final var y = Integer.parseInt(coordRaw[1]);

            maxX = Math.max(x, maxX);
            maxY = Math.max(y, maxY);

            coords.add(new Coord(x, y));
            i++;
        }

        final var grid = new int[maxX + 1][maxY + 1];

        for (var coord : coords) {
            grid[coord.x][coord.y] = 1;
        }

        final var fold = lines.get(i+1);
        int[][] finalGrid;

        if (fold.contains("x=")) {
            final var xLine = Integer.parseInt(fold.replace("fold along x=", ""));
            finalGrid = foldVertical(grid, xLine);
        } else {
            final var yLine = Integer.parseInt(fold.replace("fold along y=", ""));
            finalGrid = foldHorizontal(grid, yLine);
        }

        var total = 0;
        for (int x = 0; x < finalGrid.length; x++) {
            for (int y = 0; y < finalGrid[0].length; y++) {
                if (finalGrid[x][y] == 1) {
                    total++;
                }
            }
        }

        log.info("TOTAL: " + total);
    }

    private int[][] foldVertical(int[][] grid, int foldX) {
        int[][] newGrid = new int[foldX][grid[0].length];
        for (int x = 0; x < foldX; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                newGrid[x][y] = grid[x][y];
            }
        }


        int d = foldX - 1;
        for (int x = foldX + 1; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if(grid[x][y] == 1) {
                    newGrid[d][y] = 1;
                }
            }
            d--;
        }

        return newGrid;
    }

    private int[][] foldHorizontal(int[][] grid, int foldY) {
        int[][] newGrid = new int[grid.length][foldY];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < foldY; y++) {
                newGrid[x][y] = grid[x][y];
            }
        }

        int d = foldY - 1;
        for (int y = foldY + 1; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if(grid[x][y] == 1) {
                    newGrid[x][d] = 1;
                }
            }
            d--;
        }
        return newGrid;
    }

    public void runPart2() throws URISyntaxException, IOException {
        var lines = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        int maxX = 0;
        int maxY = 0;
        final var coords = new ArrayList<Coord>();
        int i = 0;
        while(lines.get(i).length() != 0) {
            final var coordRaw = lines.get(i).split(",");
            final var x = Integer.parseInt(coordRaw[0]);
            final var y = Integer.parseInt(coordRaw[1]);

            maxX = Math.max(x, maxX);
            maxY = Math.max(y, maxY);

            coords.add(new Coord(x, y));
            i++;
        }

        var grid = new int[maxX + 1][maxY + 1];

        for (var coord : coords) {
            grid[coord.x][coord.y] = 1;
        }

        for (var fold : lines.subList(i + 1, lines.size())) {
            if (fold.contains("x=")) {
                final var xLine = Integer.parseInt(fold.replace("fold along x=", ""));
                grid = foldVertical(grid, xLine);
            } else {
                final var yLine = Integer.parseInt(fold.replace("fold along y=", ""));
                grid = foldHorizontal(grid, yLine);
            }
        }

        var total = 0;
        var str = "";
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[x][y] == 1) {
                    total++;
                    str += "#";
                } else {
                    str += " ";
                }
            }
            str += "\n";
        }
        log.info("String: \n" + str);
        log.info("TOTAL: " + total);
    }

    private static class Coord {
        int x;
        int y;

        Coord(int x, int y) {
            this.x = x;
            this.y =y;
        }
    }

    private static class Grid {
        int[][] grid;

        Grid(int x, int y) {
            grid = new int[x][y];
        }
    }
}