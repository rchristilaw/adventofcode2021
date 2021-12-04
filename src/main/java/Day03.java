import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day03 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day03.class.getName());
    private final static String inputFile = "day03.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day3 = new Day03();
//        day3.runPart1();
        day3.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        final var counters = new ArrayList<Map<String, Integer>>();
        for (int x = 0; x < 12; x++) {
            counters.add(buildCounter());
        }

        input.lines()
            .forEach(it -> {
                    for (int i = 0; i < 12; i++) {
                        final var val = it.charAt(i);
                        if (val == '1') {
                            final var currentCount = counters.get(i).get("ONE");
                            counters.get(i).replace("ONE", currentCount + 1);
                        } else {
                            final var currentCount = counters.get(i).get("ZERO");
                            counters.get(i).replace("ZERO", currentCount + 1);
                        }
                    }
                }
            );

        final var result = new ArrayList<String>();
        final var inverseResult = new ArrayList<String>();
        for (final var counter : counters) {
            result.add(evaluateCounter(counter));
            inverseResult.add(evaluateCounterInverse(counter));
        }

        final var binary = String.join("", result);
        final var inverseBinary = String.join("", inverseResult);

        log.info("Result Binary: " + binary);
        final var decimalResult = Integer.parseInt(binary, 2);
        log.info("Result Decimal: " + decimalResult);

        log.info("Inverse Binary: " + inverseBinary);
        final var inverseDecimalResult = Integer.parseInt(inverseBinary, 2);
        log.info("Inverse Decimal: " + inverseDecimalResult);

        log.info("ANSWER = " + (decimalResult * inverseDecimalResult));
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        var lines = input.lines().collect(Collectors.toList());
        var inverseLines = input.lines().collect(Collectors.toList());

        String binary = null;
        String inverseBinary = null;

        for (int i = 0; i < 12; i++) {
            final var reading =
                getResult(lines, false);

            lines = filterList(lines, i, reading.get(i));

            if (lines.size() == 1) {
                binary = lines.get(0);
                break;
            }
        }

        for (int i = 0; i < 12; i++) {
            final var reading =
                getResult(inverseLines, true);

            inverseLines = filterList(inverseLines, i, reading.get(i));

            if (inverseLines.size() == 1) {
                inverseBinary = inverseLines.get(0);
                break;
            }
        }

        log.info("Result Binary: " + binary);
        final var decimalResult = Integer.parseInt(binary, 2);
        log.info("Result Decimal: " + decimalResult);

        log.info("Inverse Binary: " + inverseBinary);
        final var inverseDecimalResult = Integer.parseInt(inverseBinary, 2);
        log.info("Inverse Decimal: " + inverseDecimalResult);

        log.info("ANSWER = " + (decimalResult * inverseDecimalResult));
    }

    private List<String> getResult(List<String> lines, boolean isInverse) {
        final var counters = new ArrayList<Map<String, Integer>>();
        for (int x = 0; x < 12; x++) {
            counters.add(buildCounter());
        }

        lines.forEach(it -> {
                for (int i = 0; i < 12; i++) {
                    final var val = it.charAt(i);
                    if (val == '1') {
                        final var currentCount = counters.get(i).get("ONE");
                        counters.get(i).replace("ONE", currentCount + 1);
                    } else {
                        final var currentCount = counters.get(i).get("ZERO");
                        counters.get(i).replace("ZERO", currentCount + 1);
                    }
                }
            }
        );

        final var result = new ArrayList<String>();
        for (final var counter : counters) {
            if (isInverse) {
                result.add(evaluateCounterInverse(counter));
            } else {
                result.add(evaluateCounter(counter));
            }
        }

        return result;
    }

    private List<String> filterList(List<String> list, int index, String target) {
        return list.stream()
            .filter(it -> String.valueOf(it.charAt(index)).equals(target))
            .collect(Collectors.toList());
    }

    private Map<String, Integer> buildCounter() {
        final var counter = new HashMap<String, Integer>();
        counter.put("ONE", 0); // horizontal
        counter.put("ZERO", 0); // depth
        return counter;
    }

    private String evaluateCounter(Map<String, Integer> counter) {
        final var one = counter.get("ONE");
        final var zero = counter.get("ZERO");

        if (one >= zero) {
            return "1";
        }
        return "0";
    }

    private String evaluateCounterInverse(Map<String, Integer> counter) {
        final var one = counter.get("ONE");
        final var zero = counter.get("ZERO");

        if (one >= zero) {
            return "0";
        }
        return "1";
    }
}
