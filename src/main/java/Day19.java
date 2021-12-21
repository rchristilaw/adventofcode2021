import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day19 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day19.class.getName());
    private final static String inputFile = "day19.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day19 = new Day19();
        day19.runPart1();
//        day19.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        final var scanners = new ArrayList<Scanner>();
        var currentScanner = new Scanner();
        for (var line : input) {
            if (line.contains("--- scanner ")) {
                currentScanner.name = Integer.parseInt(line.split(" ")[2]);
            } else if (line.length() == 0) {
                scanners.add(currentScanner);
                currentScanner = new Scanner();
            } else {
                currentScanner.addBeacons(new Coord(line));
            }
        }
        scanners.add(currentScanner);

        scanners.get(0).coord = new Coord(0, 0,0);

        var allMapped = false;
        while (!allMapped) {
            for (int i = 1; i < scanners.size(); i++) {
                if (scanners.get(i).coord != null) {
                    continue;
                }

                scanners.get(i).coord = detectBeacons(scanners.get(i), scanners.get(0));
            }
            allMapped = scanners.stream()
                .noneMatch(it -> it.coord == null);
        }

        for (var sc : scanners) {
            log.info(String.format("Scanner %d: %d, %d, %d", sc.name, sc.coord.x, sc.coord.y, sc.coord.z));
        }

        var totalBeacons = scanners.get(0).beacons.stream()
            .map(it -> it.key)
            .collect(Collectors.toSet());

        log.info("TOTAL: " + totalBeacons.size());

        var maxDistance = 0;

        for (int i = 0; i < scanners.size(); i++) {
            for (int j = 0; j < scanners.size(); j++) {
                var scanner1 = scanners.get(i).coord;
                var scanner2 = scanners.get(j).coord;

                var distance = Math.abs(scanner1.x - scanner2.x) + Math.abs(scanner1.y - scanner2.y) + Math.abs(scanner1.z - scanner2.z);

                maxDistance = Math.max(distance, maxDistance);
            }
        }

        log.info("Max Distance: " + maxDistance);
    }

    private Coord detectBeacons(Scanner scanner, Scanner baseScanner) {
        final var targetBeacons = baseScanner.getBeaconsWithRotation(0);

        for (int r = 0; r < 48; r++) {
            var matchers = new HashMap<String, Integer>();
            for (int t = 0; t < targetBeacons.size(); t++) {
                final var currentBeacons = scanner.getBeaconsWithRotation(r);
                for (int b = 0; b < currentBeacons.size(); b++) {
                    final var current = currentBeacons.get(b);
                    final var currentTarget = targetBeacons.get(t);
                    final var newCoord = combineCoord(current, currentTarget);

                    if (matchers.containsKey(newCoord.key)) {
                        final var count = matchers.get(newCoord.key);
                        matchers.put(newCoord.key, count +  1);
                    } else {
                        matchers.put(newCoord.key, 1);
                    }
                }
            }

            for (var matcher : matchers.entrySet()) {
                if (matcher.getValue() > 11) {
                    final var newCoord = new Coord(matcher.getKey());

                    var newBeacons = new ArrayList<Coord>();
                    for (var beacon : scanner.getBeaconsWithRotation(r)) {
                        newBeacons.add(new Coord(newCoord, beacon));
                    }

                    baseScanner.beacons.addAll(newBeacons);
                    return newCoord;
                }

            }
        }

        return null;
    }

    private Coord combineCoord(Coord c1, Coord c2) {
        return new Coord(c2.x - c1.x, c2.y - c1.y, c2.z - c1.z);
    }


    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var total = 0;
        log.info("TOTAL: " + total);
    }

    private static class Scanner {
        private int name;
        private Coord coord;
        private Set<Coord> beacons = new HashSet<>();

        private void addBeacons(Coord coord) {
            beacons.add(coord);
        }

        private List<Coord> getBeaconsWithRotation(int i) {
            return beacons.stream()
                .map(it -> getPermutations(it).get(i))
                .collect(Collectors.toList());
        }
    }

    private static class Coord {
        private int x;
        private int y;
        private int z;
        private String key;

        Coord(String line) {
            var split = line.split(",");
            this.x = Integer.parseInt(split[0]);
            this.y = Integer.parseInt(split[1]);
            this.z = Integer.parseInt(split[2]);

            key = this.x + "," + this.y + "," + this.z;
        }

        Coord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;

            key = this.x + "," + this.y + "," + this.z;
        }

        Coord(Coord c1, Coord c2) {
            this.x = c1.x + c2.x;
            this.y = c1.y + c2.y;
            this.z = c1.z + c2.z;

            key = this.x + "," + this.y + "," + this.z;
        }
    }

    private static List<Coord> getPermutations(Coord coord) {
        int x = coord.x;
        int y = coord.y;
        int z = coord.z;

        var perms = new ArrayList<Coord>();
        buildPerm(x, y, z, perms); //0-5

        x = coord.x * -1;
        y = coord.y;
        z = coord.z;
        buildPerm(x, y, z, perms); //6-11
        x = coord.x;
        y = coord.y * -1;
        z = coord.z;
        buildPerm(x, y, z, perms);  //12-17
        x = coord.x;
        y = coord.y;
        z = coord.z * -1;
        buildPerm(x, y, z, perms); //18 - 23

        x = coord.x * -1;
        y = coord.y;
        z = coord.z * -1;
        buildPerm(x, y, z, perms); //24 - 29

        x = coord.x * -1;
        y = coord.y * -1;
        z = coord.z;
        buildPerm(x, y, z, perms);//30 - 35
        x = coord.x * -1;
        y = coord.y * -1;
        z = coord.z * -1;
        buildPerm(x, y, z, perms);
        x = coord.x;
        y = coord.y * -1;
        z = coord.z * -1;
        buildPerm(x, y, z, perms);
        return perms;
    }

    private static void buildPerm(int x, int y, int z, List<Coord> perms) {
        perms.add(new Coord(x, y, z));
        perms.add(new Coord(x, z, y));
        perms.add(new Coord(z, y, x));
        perms.add(new Coord(y, x, z));
        perms.add(new Coord(z, x, y));
        perms.add(new Coord(y, z, x));
    }
}