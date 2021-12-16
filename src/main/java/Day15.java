import lombok.Data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Day15 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day15.class.getName());
    private final static String inputFile = "day15.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day15 = new Day15();
        day15.runPart1();
//        day5.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var lines = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        final var grid = new Node[lines.size()][lines.size()];
        int y = 0;
        for (var line : lines) {
            int x = 0;
            for (var cell : line.split("")) {
                grid[x][y] = new Node(Integer.parseInt(cell));
                x++;
            }
            y++;
        }

        final var currentCell = grid[0][0];
        var cost = traverse(grid, 0, 0, null) - currentCell.cost;

        log.info("TOTAL: " + cost);
    }

    private int traverse(Node[][] grid, int x, int y, Direction direction) {

        int up = Integer.MAX_VALUE;
        int down = Integer.MAX_VALUE;
        int left = Integer.MAX_VALUE;
        int right = Integer.MAX_VALUE;

        final var currentNode = grid[x][y];
        if (currentNode.visited) {
            return currentNode.minCost;
        }

        if (x == grid.length - 1 && y == grid.length - 1) {
            return currentNode.cost;
        }

//        if (x > 0 && direction != Direction.RIGHT) {
//            left = traverse(grid, x - 1, y, Direction.LEFT);
//        }

        if (x + 1 < grid.length && direction != Direction.LEFT) {
            right = traverse(grid, x + 1, y, Direction.RIGHT);
        }
//
//        if (y > 0 && direction != Direction.DOWN) {
//            up = traverse(grid, x, y - 1, Direction.UP);
//        }

        if (y + 1 < grid.length && direction != Direction.UP) {
            down = traverse(grid, x, y + 1, Direction.DOWN);
        }

        log.info("CURRENT: " + x + ", " + y);
        currentNode.visited = true;

        int cost = Math.min(down, right) + currentNode.cost;

        currentNode.minCost = cost;
        return cost;
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var count = 0;

        log.info("TOTAL: " + count);
    }

    public static int max(int a, int b, int c, int d) {
        int max = a;

        if (b > max)
            max = b;
        if (c > max)
            max = c;
        if (d > max)
            max = d;

        return max;
    }

    private class Node {
        private int cost;
        private boolean visited;
        private int minCost = 0;

        Node(int cost) {
            this.cost = cost;
        }
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }
}