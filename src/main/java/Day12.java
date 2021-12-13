import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day12 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day12.class.getName());
    private final static String inputFile = "day12.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day12 = new Day12();
        day12.runPart1();
        day12.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        var paths = readClassPathResource(inputFile).lines()
            .map(it -> {
                final var path = it.split("-");
                return new Path(path[0], path[1]);
            })
            .collect(Collectors.toList());

        final var starts = paths.stream()
            .filter(it -> it.isStart)
            .collect(Collectors.toList());

        paths = paths.stream()
            .filter(it -> !it.isStart)
            .collect(Collectors.toList());

        var total = 0;
        for (final var start : starts) {
            final var list = new ArrayList<String>();
            list.add("start");
            list.add(start.getNextCave("start"));
            total += navigate(list, start, paths, new ArrayList<String>());
        }

        log.info("TOTAL: " + total);
    }

    private int navigate(List<String> list, Path currentPath, List<Path> paths, List<String> visitedCaves) {
        final var current = list.get(list.size() - 1);
        if (isSmallCave(current)) {
            if (visitedCaves.contains(current)) {
                log.info("FAILED: " + list);
                return 0;
            } else {
                visitedCaves.add(current);
            }
        }

        if (current.equals("end")) {
            log.info("SUCCESS: " + list);
            return 1;
        }

        final var nextPaths = paths.stream()
            .filter(it -> it.getNextCave(current) != null)
            .collect(Collectors.toList());

        var reachedEnd = 0;
        for (var next : nextPaths) {
            final var newList = new ArrayList<String>(list);
            final var newVisitedCaves = new ArrayList<String>(visitedCaves);
            newList.add(next.getNextCave(current));
            reachedEnd += navigate(newList, next, paths, newVisitedCaves);
        }
        return reachedEnd;
    }

    public void runPart2() throws URISyntaxException, IOException {
        var paths = readClassPathResource(inputFile).lines()
            .map(it -> {
                final var path = it.split("-");
                return new Path(path[0], path[1]);
            })
            .collect(Collectors.toList());

        final var starts = paths.stream()
            .filter(it -> it.isStart)
            .collect(Collectors.toList());

        paths = paths.stream()
            .filter(it -> !it.isStart)
            .collect(Collectors.toList());

        var total = 0;
        for (final var start : starts) {
            final var list = new ArrayList<String>();
            list.add("start");
            list.add(start.getNextCave("start"));
            total += navigate2(list, start, paths, new HashMap<>(
                Map.of(1, new ArrayList<String>(),
                    2, new ArrayList<String>())));
        }

        log.info("TOTAL: " + total);
    }

    //TODO make this reusable between parts
    private int navigate2(List<String> list, Path currentPath, List<Path> paths, Map<Integer, List<String>> visitedCaves) {
        final var current = list.get(list.size() - 1);
        if (isSmallCave(current)) {
            if (visitedCaves.get(1).contains(current)) {
                if (visitedCaves.get(2).size() > 0) {
                    log.info("FAILED: " + list);
                    return 0;
                }
                visitedCaves.get(2).add(current);
            } else if (!visitedCaves.get(1).contains(current)){
                visitedCaves.get(1).add(current);
            }
        }

        if (current.equals("end")) {
            log.info("SUCCESS: " + list);
            return 1;
        }

        final var nextPaths = paths.stream()
            .filter(it -> it.getNextCave(current) != null)
            .collect(Collectors.toList());

        var reachedEnd = 0;
        for (var next : nextPaths) {
            final var newList = new ArrayList<String>(list);
            final var newVisitedCaves = new HashMap<Integer, List<String>>(Map.of(
                1, new ArrayList<>(visitedCaves.get(1)),
                2, new ArrayList<>(visitedCaves.get(2))
            ));
            newList.add(next.getNextCave(current));
            reachedEnd += navigate2(newList, next, paths, newVisitedCaves);
        }
        return reachedEnd;
    }

    private boolean isSmallCave(String cave) {
        if (cave.equals("start") || cave.equals("end")) {
            return false;
        }
        return cave.toLowerCase().equals(cave);
    }

    private static class Path {
        String cave1;
        String cave2;
        boolean isStart;

        Path(String cave1, String cave2) {
            this.cave1 = cave1;
            this.cave2 = cave2;
            this.isStart = cave1.equals("start") || cave2.equals("start");
        }

        boolean isEqual(Path otherPath) {
            return this.cave1.equals(otherPath.cave1) && this.cave2.equals(otherPath.cave2);
        }

        public String getNextCave(String cave) {
            if (cave.equals(cave1)) {
                return cave2;
            } else if (cave.equals(cave2)) {
                return cave1;
            } else {
                return null;
            }
        }
    }
}