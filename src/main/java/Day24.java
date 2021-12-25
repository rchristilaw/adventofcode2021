import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day24 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day24.class.getName());
    private final static String inputFile = "day24.txt";

    private int w = 0;
    private int x = 0;
    private int y = 0;
    private int z = 0;

    private int index = 0;

    private int[] inputArr = new int[14];

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day24();
        day.runPart1();
//        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var inputInstructions = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        final var steps = new ArrayList<Step>();
        for (var line : inputInstructions) {
            steps.add(buildStep(line));
        }

        Arrays.fill(inputArr, 9);

        var currentNum = new BigInteger("99999999999999");
        while (currentNum.compareTo(BigInteger.ZERO) > 0) {
            var str = Arrays.asList(currentNum.toString().split(""));

            if (str.contains("0")) {
                continue;
            }

            var ind = 0;
            for (var s : str) {
                inputArr[ind] = Integer.parseInt(s);
                ind++;
            }

            for (var step : steps) {
                processStep(step);
            }

            if (z == 0) {
                log.info("q: " + currentNum);
                break;
            }
            index = 0;
            currentNum = currentNum.subtract(BigInteger.ONE);

            w = 0;
            x = 0;
            y = 0;
            z = 0;
        }


        var total = 0;
        log.info("TOTAL: " + total);
    }


    private void processStep(Step step) {
        if (step.instruction.equals("inp")) {
            switch (step.args.get(0)) {
                case "w":
                    w = inputArr[index];
                    break;
                case "x":
                    x = inputArr[index];
                    break;
                case "y":
                    y = inputArr[index];
                    break;
                case "z":
                    z = inputArr[index];
                    break;
                default:
                    throw new RuntimeException();
            }
            index++;
        } else if (step.instruction.equals("add")) {
            saveVal(step.args.get(0), getArgVal(step.args.get(0)) + getArgVal(step.args.get(1)));
        } else if (step.instruction.equals("mul")) {
            saveVal(step.args.get(0), getArgVal(step.args.get(0)) * getArgVal(step.args.get(1)));
        } else if (step.instruction.equals("div")) {
            saveVal(step.args.get(0), getArgVal(step.args.get(0)) / getArgVal(step.args.get(1)));
        } else if (step.instruction.equals("mod")) {
            saveVal(step.args.get(0), getArgVal(step.args.get(0)) % getArgVal(step.args.get(1)));
        } else if (step.instruction.equals("eql")) {
            saveVal(step.args.get(0), getArgVal(step.args.get(0)) == getArgVal(step.args.get(1)) ? 1 : 0);
        } else {
            throw new RuntimeException();
        }
    }

    private int getArgVal(String val) {
        switch (val) {
            case "w":
                return w;
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
            default:
                return Integer.parseInt(val);
        }
    }

    private void saveVal(String val, int result) {
        switch (val) {
            case "w":
                w = result;
                break;
            case "x":
                x = result;
                break;
            case "y":
                y = result;
                break;
            case "z":
                z = result;
                break;
            default:
                throw new RuntimeException();
        }
    }



    private Step buildStep(String line) {
        var step = new Step();
        var split = Arrays.asList(line.split(" "));

        step.instruction = split.get(0);
        step.args = split.subList(1, split.size());

        return step;

    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var total = 0;
        log.info("TOTAL: " + total);
    }

    private static class Step {
        private String instruction;
        private List<String> args = new ArrayList<>();
    }
}