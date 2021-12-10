import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day09 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day09.class.getName());
    private final static String inputFile = "day09.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day9 = new Day09();
        day9.runPart1();
        day9.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var grid = new Grid(readClassPathResource(inputFile).lines()
            .map(it -> Arrays.stream(it.split(""))
                .map(Integer::parseInt)
                .collect(Collectors.toList()))
            .collect(Collectors.toList()));

        var total = 0;

        final var maxX = grid.getMaxX();
        final var maxY = grid.getMaxY();

        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {

                final var currentVal = grid.getValue(x, y);

                if (y != 0 && currentVal >= grid.getValue(x, y - 1)) {
                    continue;
                }

                if (x != 0 && currentVal >= grid.getValue(x - 1, y)) {
                    continue;
                }

                if (x != maxX - 1 && currentVal >= grid.getValue(x + 1, y)) {
                    continue;
                }

                if (y != maxY - 1 && currentVal >= grid.getValue(x, y + 1)) {
                    continue;
                }
                total = total + 1;
            }
        }
        log.info("TOTAL: " + total);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var grid = new Grid(readClassPathResource(inputFile).lines()
            .map(it -> Arrays.stream(it.split(""))
                .map(Integer::parseInt)
                .collect(Collectors.toList()))
            .collect(Collectors.toList()));

        final var basins = new ArrayList<Basin>();

        final var maxX = grid.getMaxX();
        final var maxY = grid.getMaxY();

        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {

                final var currentVal = grid.getValue(x, y);

                // UP
                if (y != 0 && currentVal >= grid.getValue(x, y - 1)) {
                    continue;
                }

                // LEFT
                if (x != 0 && currentVal >= grid.getValue(x - 1, y)) {
                    continue;
                }

                // RIGHT
                if (x != maxX - 1 && currentVal >= grid.getValue(x + 1, y)) {
                    continue;
                }

                // DOWN
                if (y != maxY - 1 && currentVal >= grid.getValue(x, y + 1)) {
                    continue;
                }

                final var basin = new Basin(x, y, currentVal);

                traverse(basin, grid, x, y, null);

                basins.add(basin);
            }
        }

        final var top3 = basins.stream()
            .map(it -> it.getSize())
            .sorted()
            .collect(Collectors.toList());

        final var factor = top3.get(basins.size() -1) * top3.get(basins.size() -2) * top3.get(basins.size() -3);
        log.info(String.format("%d x %d x %d", top3.get(basins.size() -1), top3.get(basins.size() -2), top3.get(basins.size() -3)));

        log.info("TOTAL: " + factor);

        //2175255
    }

    private void traverse(Basin basin, Grid grid, int x, int y, Direction direction) {
        if (x > 0 && direction != Direction.RIGHT)
            traverseLeft(basin, grid, x - 1, y);
        if (x < grid.getMaxX() - 1 && direction != Direction.LEFT)
            traverseRight(basin, grid, x + 1, y);
        if (y > 0 && direction != Direction.DOWN)
            traverseUp(basin, grid, x, y - 1);
        if (y < grid.getMaxY() - 1 && direction != Direction.UP)
            traverseDown(basin, grid, x, y + 1);
    }

    private void traverseLeft(Basin basin, Grid grid, int x, int y) {
        if (grid.getValue(x + 1, y) + 1 == grid.getValue(x, y)) {
            if (basin.addCoord(x, y)) {
                return;
            }
            traverse(basin, grid, x, y, Direction.LEFT);
        }
    }

    private void traverseRight(Basin basin, Grid grid, int x, int y) {
        if (grid.getValue(x - 1, y) + 1 == grid.getValue(x, y)) {
            if (basin.addCoord(x, y)) {
                return;
            }
            traverse(basin, grid, x, y, Direction.RIGHT);
        }
    }

    private void traverseUp(Basin basin, Grid grid, int x, int y) {
        if (grid.getValue(x, y + 1) + 1 == grid.getValue(x, y)) {
            if (basin.addCoord(x, y)) {
                return;
            }
            traverse(basin, grid, x, y, Direction.UP);
        }
    }

    private void traverseDown(Basin basin, Grid grid, int x, int y) {
        if (grid.getValue(x, y - 1) + 1 == grid.getValue(x, y)) {
            if (basin.addCoord(x, y)) {
                return;
            }
            traverse(basin, grid, x, y, Direction.DOWN);
        }
    }

    @AllArgsConstructor
    private static class Grid {
        private final List<List<Integer>> grid;

        public int getMaxX() {
            return grid.get(0).size();
        }

        public int getMaxY() {
            return grid.size();
        }

        public int getValue(int x, int y) {
            return grid.get(x).get(y);
        }
    }

    @Getter
    private static class Basin {
        private final int x;
        private final int y;
        private final int val;

        private int size = 0;
        private final Map<Integer, List<Integer>> basin = new HashMap<>();

        Basin(int x, int y, int val) {
            this.x = x;
            this.y = y;
            this.val = val;

            this.basin.put(y, new ArrayList<Integer>(List.of(x)));
            size++;
        }

        public boolean addCoord(int x, int y) {
            var row = basin.get(y);

            if (row == null) {
                basin.put(y, new ArrayList<Integer>(List.of(x)));
                size++;
            } else if (!row.contains(x)) {
                row.add(x);
                basin.put(y, row);
                size++;
            } else {
                return true;
            }
            return false;
        }

        public int getSize() {
            return size;
//            var fullList = new ArrayList<Integer>();
//            basin.values()
//                .forEach(fullList::addAll);
//            return fullList.size();
        }
    }

    private enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT;
    }


}