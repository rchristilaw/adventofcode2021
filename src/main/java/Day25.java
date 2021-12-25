import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day25 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day25.class.getName());
    private final static String inputFile = "day25.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day25();
        day.runPart1();
//        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());


        var grid = new int[input.size()][input.get(0).length()];

        int y1 = 0;
        for (var line : input) {
            int x = 0;
            for (var val : line.split("")) {
                setVal(grid, x, y1, val);
                x++;
            }
            y1++;
        }

        printGrid(grid, 0);


        for (int step = 1; step < 10000; step++) {

            var copyGrid = new int[grid.length][grid[0].length];
            int moves = 0;
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length - 1 ; x++) {
                    if (grid[y][x + 1] == 0 && grid[y][x] == 1) {
                        copyGrid[y][x + 1] = 1;
                        copyGrid[y][x] = 0;
                        moves++;
                    } else if (copyGrid[y][x] == 0) {
                        copyGrid[y][x] = grid[y][x];
                    }
                }

                if (grid[y][0] == 0 && grid[y][grid[0].length - 1] == 1) {
                    copyGrid[y][0] = 1;
                    copyGrid[y][grid[0].length - 1] = 0;
                    moves++;
                } else if (copyGrid[y][grid[0].length - 1] == 0) {
                    copyGrid[y][grid[0].length - 1] = grid[y][grid[0].length - 1];
                }
            }

            grid = copyGrid;
            copyGrid = new int[grid.length][grid[0].length];

            for (int x = 0; x < grid[0].length; x++) {
                for (int y = 0; y < grid.length - 1; y++) {
                    if (grid[y + 1][x] == 0 && grid[y][x] == 2) {
                        copyGrid[y + 1][x] = 2;
                        copyGrid[y][x] = 0;
                        moves++;
                    } else if (copyGrid[y][x] == 0) {
                        copyGrid[y][x] = grid[y][x];
                    }
                }

                if (grid[0][x] == 0 && grid[grid.length - 1][x] == 2) {
                    copyGrid[0][x] = 2;
                    copyGrid[grid.length - 1][x] = 0;
                    moves++;
                } else if (copyGrid[grid.length - 1][x] == 0){
                    copyGrid[grid.length - 1][x] = grid[grid.length - 1][x];
                }
            }

            printGrid(copyGrid, step);

            if (moves == 0) {
                log.info("STEP: " + step);
                break;
            }

            grid = copyGrid;

        }
        var total = 0;
        log.info("TOTAL: " + total);
    }

    private void printGrid(int[][] grid, int step) {
        var str = "";
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                str += grid[y][x];
            }
            str += "\n";
        }

        log.info("Step " + step + ": \n" + str);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var total = 0;
        log.info("TOTAL: " + total);
    }

    void setVal(int[][] grid, int x, int y, String val) {
        if (val.equals(">")) {
            grid[y][x] = 1;
        } else if (val.equals("v")) {
            grid[y][x] = 2;
        } else {
            grid[y][x] = 0;
        }
    }


    private static class Grid {
        int[][] grid;

        Grid(int x, int y) {
            grid = new int[x][y];
        }


    }
}