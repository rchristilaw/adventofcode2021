import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day01 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day01.class.getName());
    private final static String inputFile = "day01.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day1 = new Day01();
        day1.runPart1();
        day1.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);
        final var depthReadings = input.lines()
            .map(Integer::valueOf)
            .collect(Collectors.toList());

        int increasedReadings = 0;
        for (int i = 0; i < depthReadings.size() - 1; i++) {
            if (depthReadings.get(i) < depthReadings.get(i + 1)) {
                increasedReadings++;
            }
        }

        log.info("Depth increases: " + increasedReadings);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);
        final var depthReadings = input.lines()
            .map(Integer::valueOf)
            .collect(Collectors.toList());

        int increasedReadings = 0;
        for (int i = 0; i < depthReadings.size() - 3; i++) {
            if (getSumOfThreeReadingsAt(i, depthReadings) < getSumOfThreeReadingsAt(i + 1, depthReadings)) {
                increasedReadings++;
            }
        }

        log.info("Bulk Depth increases: " + increasedReadings);
    }

    private int getSumOfThreeReadingsAt(int index, List<Integer> readings) {
        return readings.get(index) + readings.get(index + 1) + readings.get(index + 2);
    }
}
