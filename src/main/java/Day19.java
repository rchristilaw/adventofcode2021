import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

        scanners.get(0).coord = new Coord(0, 0,0);
        scanners.get(0).rotation = 0;

        for (int b = 0; b < scanners.size(); b ++) {
            for (int s = 0; s < scanners.size(); s++) {
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
                    var relativeScanner = found.relativeTo;
                    int currentRotation = 0;
                    Coord newCoord = found.coord;

                    while(!balanced) {
                        final var currScan = relativeScanner;
                        final var relScan = scanners.stream()
                            .filter(it -> it.name == currScan)
                            .findFirst().orElseThrow();
                        currentRotation = relScan.rotation;

                        if (currentRotation != 0) {
                            newCoord = combineCoord(translateRotation(newCoord, currentRotation), relScan.foundCoord);
                            relativeScanner = relScan.relativeTo;

                            var newBeacons = new ArrayList<Coord>();
                            for(var beacon : scanner.copyBeacons) {
                                newBeacons.add(new Coord(translateRotation(beacon, currentRotation), relScan.foundCoord));
                            }
                            scanner.copyBeacons = newBeacons;
                        } else {
                            balanced = true;
                        }
                    }

                    scanner.coord = newCoord;
                    scanner.foundCoord = found.coord;
                    scanner.relativeTo = found.relativeTo;
                    scanner.rotation = found.rotation;

//                    var newBeacons = new ArrayList<Coord>();
//                    for(var beacon : scanner.copyBeacons) {
//                        newBeacons.add(combineCoord(beacon, newCoord));
//                    }
//                    scanner.copyBeacons = newBeacons;

                }
            }
        }

        for (var sc : scanners) {
            log.info(String.format("Scanner %d: %d, %d, %d", sc.name, sc.coord.x, sc.coord.y, sc.coord.z));
        }
        var total = 0;
        findAllBeacons(scanners);
//        log.info("TOTAL: " + match);
    }

    private void findAllBeacons(List<Scanner> scanners) {
        final var map = new HashSet<String>();
        for (var scanner : scanners) {
            for (var beacon : scanner.copyBeacons) {
                var newVal = new Coord(translateRotation(beacon, scanner.rotation), scanner.coord);
                map.add(newVal.key);
            }
        }

        var str = "";
        for (var beacon : map) {
            str += beacon + "\n";
        }
        log.info("Beacons:\n" + str);

        var total = 0;
        log.info("TOTAL: " + map.size());
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
        final var totalOrientations = 48;
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

                            var newBeacons = new ArrayList<Coord>();
                            for (var beacon : currentBeacons) {
                                newBeacons.add(
                                    new Coord(beacon, newCoord));
                            }
                            scanner.copyBeacons = newBeacons;
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
        private Coord foundCoord;
        private int rotation;
        private int relativeTo;
        private List<Coord> beacons = new ArrayList<>();
        private List<Coord> copyBeacons = new ArrayList<>();

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