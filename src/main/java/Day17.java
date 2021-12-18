import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

public class Day17 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day17.class.getName());
    private final static String inputFile = "day17.txt";

    final int x1 = 56;
    final int x2 = 76;
    final int y1 = -162;
    final int y2 = -134;

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day17 = new Day17();
        day17.runPart1();
//        day17.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
//        final var input = readClassPathResource(inputFile);

        int total = 0;
        Integer maxY = null;
        for (int y = -5000; y < 5000; y++) {
            for (int x = 0; x <= x2; x++) {

                Integer result = simulate(x, y);
                if (result != null) {
                    total++;
                    maxY = result;
                    log.info("X: " + x + ", Y: " + y);
                }
            }
        }

        log.info("TOTAL: " + maxY);
        log.info("TOTAL: " + total);
    }

    private Integer simulate(int x0, int y0) {
        int x = 0;
        int y = 0;

        Integer maxY = null;

        while (true) {
            if (isBetween(x1, x2, x) && isBetween(y1, y2, y)) {
                return maxY;
            }
            if (x > x2 || y < y1) {
                return null;
            }

            if (x0 == 0 && !isBetween(x1, x2, x)) {
                return null;
            }

            x += x0;
            if (x0 > 0) {
                x0--;
            }
            y += y0;
            y0--;
            if (maxY == null || y > maxY) {
                maxY = y;
            }
        }
    }

    private boolean isBetween(int i1, int i2, int i) {
        return i >= i1 && i <= i2;
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        var total = 0;
        log.info("TOTAL: " + total);
    }
}