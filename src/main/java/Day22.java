import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Day22 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day22.class.getName());
    private final static String inputFile = "day22.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day22();
//        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var gridSize = 0;
        var steps = new ArrayList<Step>();
        for (var line : input) {
            var step = buildStep(line);
            gridSize = Math.max(step.getMaxVal(), gridSize);
            steps.add(step);
        }

        int[][][] grid = new int[101][101][101];

        for (var step : steps) {
            if (!step.isWithinRange(50)) {
                continue;
            }

            for (int x = step.x.c1 + 50; x <= step.x.c2 + 50; x++) {
                for (int y = step.y.c1 + 50; y <= step.y.c2 + 50; y++) {
                    for (int z = step.z.c1 + 50; z <= step.z.c2 + 50; z++) {
                            grid[x][y][z] = step.on ? 1 : 0;
                    }
                }
            }
        }

        var total = 0;
        for (int x = 0; x < 101; x++) {
            for (int y =0; y < 101; y++) {
                for (int z = 0; z < 101; z++) {
                    if (grid[x][y][z] == 1) {
                        total++;
                    }
                }
            }
        }

        log.info("TOTAL: " + total);
    }

    private Step buildStep(String line) {
        var step = new Step();
        var split = line.split(" ");

        step.on = split[0].equals("on");

        var rawCoords = split[1].split(",");

        var x = rawCoords[0].substring(2).split("\\.\\.");
        var y = rawCoords[1].substring(2).split("\\.\\.");
        var z = rawCoords[2].substring(2).split("\\.\\.");


        step.x = new Coord(Integer.parseInt(x[0]), Integer.parseInt(x[1]));
        step.y = new Coord(Integer.parseInt(y[0]), Integer.parseInt(y[1]));
        step.z = new Coord(Integer.parseInt(z[0]), Integer.parseInt(z[1]));

        return step;
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var gridSize = 0;
        var steps = new ArrayList<Step>();
        for (var line : input) {
            var step = buildStep(line);
            gridSize = Math.max(step.getMaxVal(), gridSize);
            steps.add(step);
        }

        byte[][] gridPosXPosY = new byte[gridSize][gridSize];
//        int[][][] gridPosXNegY = new int[gridSize][gridSize][gridSize];
//        int[][][] gridNegXPosY = new int[gridSize][gridSize][gridSize];
//        int[][][] gridNegXNegY = new int[gridSize][gridSize][gridSize];

        for (var step : steps) {
            if (!step.isWithinRange(50)) {
                continue;
            }

            for (int x = step.x.c1 + gridSize; x <= step.x.c2 + gridSize; x++) {
                for (int y = step.y.c1 + gridSize; y <= step.y.c2 + gridSize; y++) {
                    for (int z = step.z.c1 + gridSize; z <= step.z.c2 + gridSize; z++) {
//                        grid[x][y][z] = step.on ? 1 : 0;
                    }
                }
            }
        }

        var total = 0;
        for (int x = 0; x < gridSize * 2; x++) {
            for (int y =0; y < gridSize * 2; y++) {
                for (int z = 0; z < gridSize * 2; z++) {
//                    if (grid[x][y][z] == 1) {
//                        total++;
//                    }
                }
            }
        }

        log.info("TOTAL: " + total);
    }

    private static class Cube {
        int x;
        int y;
        int z;
        boolean on;
    }

    private static class Step {
        private boolean on;
        private Coord x;
        private Coord y;
        private Coord z;

        boolean isWithinRange(int range) {
            return x.maxAbs() <= range && y.maxAbs() <= range && z.maxAbs() <= range;
        }

        int getMaxVal() {
            return Math.max(Math.max(x.maxAbs(), y.maxAbs()), z.maxAbs());
        }
    }

    private static class Coord {
        int c1;
        int c2;

        Coord(int c1, int c2) {
            this.c1 = Math.min(c1, c2);
            this.c2 = Math.max(c1, c2);

        }

        int maxAbs() {
            return Math.max(abs(c1), abs(c2));
        }
    }
}