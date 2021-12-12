import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day11 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day11.class.getName());
    private final static String inputFile = "day11.txt";

    private final static List<String> OPENERS = List.of("{", "[", "<", "(");

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day11 = new Day11();
//        day11.runPart1();
        day11.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        var grid = new Grid(readClassPathResource(inputFile).lines()
            .map(it -> Arrays.stream(it.split(""))
                .map(it1 -> new Octopus(Integer.parseInt(it1)))
                .collect(Collectors.toList()))
            .collect(Collectors.toList()));

        var total = 0;

        for (int i = 0; i < 100; i++) {
            int flashes = 0;
            int currentFlashes = 0;

            grid.getAll().forEach(it -> it.level++);

            do {
                var newGrid = clone(grid);
                currentFlashes = 0;
                for (int y = 0; y < 10; y++) {
                    for (int x = 0; x < 10; x++) {
                        final var currentOct = grid.getOct(x, y);
                        if (currentOct.level > 9 && !currentOct.hasFlashed) {
                            currentOct.hasFlashed = true;
                            newGrid.getOct(x, y).hasFlashed = true;

                            increment(newGrid, x, y);
                            currentFlashes++;
                        }
                    }
                }
                flashes = flashes + currentFlashes;
                grid = newGrid;
            } while(currentFlashes != 0);

            grid.getAll().forEach(it -> {
                if (it.level > 9) {
                    it.level = 0;
                }
                it.hasFlashed = false;
            });

            total += flashes;
        }

        log.info("TOTAL: " + total);
    }

    private void increment(Grid grid, int x, int y) {
        if (x > 0) grid.getOct(x - 1, y).level++;
        if (x < 9) grid.getOct(x + 1, y).level++;
        if (y > 0) grid.getOct(x, y - 1).level++;
        if (y < 9) grid.getOct(x, y + 1).level++;
        if (x > 0 && y > 0) grid.getOct(x - 1, y - 1).level++;
        if (x < 9 && y < 9) grid.getOct(x + 1, y + 1).level++;
        if (y > 0 && x < 9) grid.getOct(x + 1, y - 1).level++;
        if (y < 9 && x > 0) grid.getOct(x - 1, y + 1).level++;
    }

    private int flash(Grid grid, int x, int y, boolean inc, int currentFlashes) {
        if (x < 0 || y < 0 || x > 9 || y > 9) {
            return 0;
        }

        final var currentOct = grid.getOct(x, y);
        if (inc) {
            currentOct.level++;
        }

        if (currentOct.hasFlashed || currentOct.level <=  9) {
            return currentFlashes;
        }

        currentOct.hasFlashed = true;
        currentFlashes++;

        currentFlashes += flash(grid, x - 1, y, true, currentFlashes);
        currentFlashes += flash(grid, x + 1, y, true, currentFlashes);
        currentFlashes += flash(grid, x, y - 1, true, currentFlashes);
        currentFlashes += flash(grid, x, y + 1, true, currentFlashes);

        return currentFlashes;
    }

    public void runPart2() throws URISyntaxException, IOException {
        var grid = new Grid(readClassPathResource(inputFile).lines()
            .map(it -> Arrays.stream(it.split(""))
                .map(it1 -> new Octopus(Integer.parseInt(it1)))
                .collect(Collectors.toList()))
            .collect(Collectors.toList()));

        var total = 0;
        int i = 0;
        while (true) {
            i++;
            int flashes = 0;
            int currentFlashes = 0;

            grid.getAll().forEach(it -> it.level++);

            do {
                var newGrid = clone(grid);
                currentFlashes = 0;
                for (int y = 0; y < 10; y++) {
                    for (int x = 0; x < 10; x++) {
                        final var currentOct = grid.getOct(x, y);
                        if (currentOct.level > 9 && !currentOct.hasFlashed) {
                            currentOct.hasFlashed = true;
                            newGrid.getOct(x, y).hasFlashed = true;

                            increment(newGrid, x, y);
                            currentFlashes++;
                        }
                    }
                }
                flashes = flashes + currentFlashes;
                grid = newGrid;
            } while(currentFlashes != 0);

            grid.getAll().forEach(it -> {
                if (it.level > 9) {
                    it.level = 0;
                }
                it.hasFlashed = false;
            });

            if (flashes == 100) {
                log.info("STEP: " + i);
                break;
            }

            total += flashes;
        }

        log.info("TOTAL: " + total);
    }

    public Grid clone(Grid grid) {
        final var newGrid = new ArrayList<List<Octopus>>();
        for (int y = 0; y < 10; y++) {
            final var newRow = new ArrayList<Octopus>();
            for (int x = 0; x < 10; x++) {
                final var copy = grid.getOct(x, y);
                newRow.add(new Octopus(copy.level, copy.hasFlashed));
            }
            newGrid.add(newRow);
        }
        return new Grid(newGrid);
    }

    @AllArgsConstructor
    private static class Grid {
        private final List<List<Octopus>> grid;

        public int getMaxX() {
            return grid.get(0).size();
        }

        public int getMaxY() {
            return grid.size();
        }

        public Octopus getOct(int x, int y) {
            return grid.get(y).get(x);
        }

        public int getValue(int x, int y) {
            return grid.get(x).get(y).getLevel();
        }

        public List<Octopus> getAll() {
            return grid.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        }
    }

    @Getter
    private static class Octopus {
        private int level;
        private boolean hasFlashed;

        Octopus(int level) {
            this.level = level;
            this.hasFlashed = false;
        }
        Octopus(int level, boolean hasFlashed) {
            this.level = level;
            this.hasFlashed = hasFlashed;
        }
    }

}