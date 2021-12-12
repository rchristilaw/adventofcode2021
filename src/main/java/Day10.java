import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day10 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day10.class.getName());
    private final static String inputFile = "day10.txt";

    private final static List<String> OPENERS = List.of("{", "[", "<", "(");

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day10 = new Day10();
        day10.runPart1();
        day10.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var lines = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var total = 0;
        for (var line : lines) {
            final var targetStack = new Stack<String>();
            final var errorValue = parseChunk(Arrays.stream(line.split(""))
                .filter(it -> it != null && !it.trim().isEmpty())
                .collect(Collectors.toList()), targetStack);
            total = total + getErrorValue(errorValue);
        }

        log.info("TOTAL: " + total);
    }

    private String parseChunk(List<String> chunk, Stack<String> targetStack) {
        final var openChar = chunk.get(0);

        targetStack.push(getMatcher(openChar));
        for (int i = 1; i < chunk.size(); i++) {
            final var currentChar = chunk.get(i);
            if (isOpener(currentChar)) {
                targetStack.push(getMatcher(currentChar));
            } else if (!currentChar.equals(targetStack.pop())) {
                return currentChar;
            }
        }
        return "";
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var lines = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var total = 0;
        var totals = new ArrayList<Long>();
        for (var line : lines) {
            final var targetStack = new Stack<String>();
            final var errorValue = parseChunk(Arrays.stream(line.split(""))
                .filter(it -> it != null && !it.trim().isEmpty())
                .collect(Collectors.toList()), targetStack);

            if (getErrorValue(errorValue) == 0) {
                totals.add(calculateRemainingValue(targetStack));
            }
        }

        final var sortedTotals = totals.stream()
            .sorted()
            .collect(Collectors.toList());

        final var index = (sortedTotals.size()/2);
        log.info("TOTAL: " + sortedTotals.get(index));
    }

    private long calculateRemainingValue(Stack<String> targetStack) {
        long total = 0;
        int size = 5;

        while (targetStack.size() > 0) {
            total = (total * size) + getCloseValue(targetStack.pop());
        }

        return total;
    }

    private boolean isOpener(String val) {
        return OPENERS.contains(val);
    }

    private String getMatcher(String openChar) {
        switch (openChar) {
            case "{":
                return "}";
            case "[":
                return "]";
            case "<":
                return ">";
            case "(":
                return ")";
            default:
                throw new RuntimeException("No matcher");
        }
    }

    private int getErrorValue(String closeChar) {
        switch (closeChar) {
            case "}":
                return 1197;
            case "]":
                return 57;
            case ")":
                return 3;
            case ">":
                return 25137;
            default:
                return 0;
        }
    }

    private int getCloseValue(String closeChar) {
        switch (closeChar) {
            case ")":
                return 1;
            case "]":
                return 2;
            case "}":
                return 3;
            case ">":
                return 4;
            default:
                return 0;
        }
    }

}