import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day22 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day22.class.getName());
    private final static String inputFile = "day22.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day22();
        day.runPart1();
//        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var total = 0;
        log.info("TOTAL: " + total);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var total = 0;
        log.info("TOTAL: " + total);
    }
}