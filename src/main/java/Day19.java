import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        int match = 0;

        var foundScanners = new HashMap<String, Match>();
        scanners.get(0).coord = new Coord(0, 0,0);
        scanners.get(0).rotation = 0;

        for (int b = 0; b < 5; b ++) {
            for (int s = 1; s < 5; s++) {
                if (b == s) {
                    continue;
                }

                var baseScanner = scanners.get(b);
                if (baseScanner.coord == null) {
                    continue;
                }


                log.info("Scanner: " + s);
                final var scanner = scanners.get(s);
                if (scanner.coord != null) {
                    continue;
                }

                var found = findScannerCoord(scanner, baseScanner);

                if (found != null) {

                    var r = findByName(found.relativeTo, scanners);
                    if (r == null || r.coord == null) {
                        continue;
                    }

                    var name = found.scanner;
                    var balanced = false;
                    var rotScanner = found.relativeTo;
                    var currentRotation = found.rotation;
                    var prevRotation = found.rotation;
                    Coord newCoord = found.coord;
                    while(!balanced) {
                        final var currScan = rotScanner;
                        final var relScan = scanners.stream()
                            .filter(it -> it.name == currScan)
                            .findFirst().orElseThrow();
                        currentRotation = relScan.rotation;

                        if (currentRotation != 0) {
                            newCoord = combineCoord(translateRotation(newCoord, currentRotation), relScan.coord);
                            rotScanner = relScan.relativeTo;
                            prevRotation = relScan.rotation;
                        } else {
                            balanced = true;
                        }
                    }
                    scanner.coord = newCoord;
                    scanner.relativeTo = 0;
                    scanner.rotation = prevRotation;
                }
            }
        }

        for (var sc : scanners) {
            log.info(String.format("Scanner %d: %d, %d, %d", sc.name, sc.coord.x, sc.coord.y, sc.coord.z));
        }
        var total = 0;
        log.info("TOTAL: " + match);
    }

    private Coord combineCoord(Coord c1, Coord c2) {
        return new Coord(c2.x - c1.x, c2.y - c1.y, c2.z - c1.z);
    }

    private Scanner findByName(int name, List<Scanner> scanners) {
        return scanners.stream()
            .filter(it -> it.name == name)
            .findFirst().orElse(null);
    }

    private Match findScannerCoord(Scanner scanner, Scanner baseScanner) {
        final var totalOrientations = scanner.beacons.get(0).size();
        final var targetBeacons = baseScanner.getBeaconsWithRotation(0);

        for (int r = 0; r < totalOrientations; r++) {
            var matchers = new HashMap<String, Integer>();
            for (int t = 0; t < targetBeacons.size(); t++) {
                final var currentBeacons = scanner.getBeaconsWithRotation(r);
                for (int b = 0; b < currentBeacons.size(); b++) {
                    final var current = currentBeacons.get(b);
                    final var currentTarget = targetBeacons.get(t);
                    final var newCoord = new Coord(current, currentTarget);
                    if (matchers.containsKey(newCoord.key)) {
                        final var count = matchers.get(newCoord.key);
                        matchers.put(newCoord.key, count +  1);
                        if (count == 11) {
                            var match = new Match();
                            match.scanner = scanner.name;
                            match.coord = newCoord;
                            match.rotation = r;
                            match.relativeTo = baseScanner.name;
                            return match;
                        }
                    } else {
                        matchers.put(newCoord.key, 1);
                    }
                }
            }
        }
        return null;
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var total = 0;
        log.info("TOTAL: " + total);
    }

    private static class Scanner {
        private int name;
        private Coord coord;
        private int rotation;
        private int relativeTo;
        private List<List<Coord>> beacons = new ArrayList<>();

        private void addBeacons(Coord coord) {
            beacons.add(getPermutations(coord));
        }

        private List<Coord> getBeaconsWithRotation(int i) {
            return beacons.stream()
                .map(it -> it.get(i))
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
        }

        Coord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        Coord(Coord c1, Coord c2) {
            this.x = c1.x + c2.x;
            this.y = c1.y + c2.y;
            this.z = c1.z + c2.z;

            key = this.x + "," + this.y + "," + this.z;
        }
    }

    private static Coord translateRotation(Coord coord, int rotation) {
        var perms = getPermutations(coord);
        return perms.get(rotation);
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

    private static class Match {
        Coord coord;
        String key;
        int rotation;
        int scanner;
        int relativeTo;
    }
}