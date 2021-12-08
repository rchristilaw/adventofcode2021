import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Day07 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day07.class.getName());
    private final static String inputFile = "day07.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day7 = new Day07();
        day7.runPart1();
        day7.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);
        final var positions = Arrays.stream(input.split(","))
            .map(it -> Integer.parseInt(it.trim()))
            .sorted()
            .collect(Collectors.toList());

        final var median = positions.get(positions.size() /2);

        int gas = 0;
        for(final var pos : positions) {
            gas = gas + abs(median-pos);
        }

        log.info("TOTAL GAS: " + gas);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);
        final var positions = Arrays.stream(input.split(","))
            .map(it -> Integer.parseInt(it.trim()))
            .collect(Collectors.toList());

        var sum = 0;
        for(final var pos : positions) {
            sum = sum + pos;
        }

        int avg = (sum / (positions.size()));

        int gas = 0;
        for(final var pos : positions) {
            final var distance = abs(avg-pos);
            for (int i = 1; i <= distance; i++) {
                gas = gas + i;
            }
        }

        log.info("TOTAL: " + gas);
    }

}