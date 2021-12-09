import lombok.Getter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Day08 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day08.class.getName());
    private final static String inputFile = "day08.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day8 = new Day08();
//        day8.runPart1();
        day8.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        final var dataList = new ArrayList<Data>();

        input.lines().forEach(it -> dataList.add(new Data(it)));

        var total = 0L;
        for (var data : dataList) {
            final var count = data.output.stream()
                .filter(it -> it.length() == 2 || it.length() == 3 || it.length() == 4 || it.length() == 7 )
                .count();
            total = total + count;
        }

        log.info("TOTAL: " + total);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);
        final var dataList = new ArrayList<Data>();
        input.lines().forEach(it -> dataList.add(new Data(it)));

        var total = 0;
        for (var data : dataList) {
            StringBuilder lineTotal = new StringBuilder();
            for (var outVal : data.output) {
                lineTotal.append(mapNumbers(data.input, outVal));
            }
            final var lineVal = Integer.parseInt(lineTotal.toString());
            log.info(lineVal + " " + lineTotal.toString());
            total = total + lineVal;
        }

        log.info("TOTAL: " + total);
    }

    private int mapNumbers(List<String> input, String currentVal) {
        if (currentVal.length() == 2) {
            return 1;
        }
        if (currentVal.length() == 3) {
            return 7;
        }
        if (currentVal.length() == 4) {
            return 4;
        }
        if (currentVal.length() == 7) {
            return 8;
        }
        if (currentVal.length() == 6) {
            return decodeValueOfSix(input, currentVal);
        }
        if (currentVal.length() == 5) {
            return decodeValueOfFive(input, currentVal);
        }
        throw new RuntimeException("Shouldn't be possible to get here");
    }

    private int decodeValueOfSix(List<String> input, String currentVal) {
        final var one = input.stream()
            .filter(it -> it.length() == 2)
            .findFirst().orElse(null);

        if (one != null) {
            if (!split(currentVal).containsAll(split(one))) {
                return 6;
            }
        }

        final var four = input.stream()
            .filter(it -> it.length() == 4)
            .findFirst().orElse(null);

        if (four != null) {
            if (split(currentVal).containsAll(split(four))) {
                return 9;
            } else {
                return 0;
            }
        }

        throw new RuntimeException("Can't get here");
    }

    private int decodeValueOfFive(List<String> input, String currentVal) {
        final var one = input.stream()
            .filter(it -> it.length() == 2)
            .findFirst().orElse(null);

        if (one != null) {
            if (split(currentVal).containsAll(split(one))) {
                return 3;
            }
        }

        final var four = input.stream()
            .filter(it -> it.length() == 4)
            .findFirst().orElse(null);

        if (four != null) {
            if (getOverlap(split(four), split(currentVal)) == 3) {
                return 5;
            } else {
                return 2;
            }
        }

        throw new RuntimeException("Can't get here");
    }


    private List<String> split(String string) {
        return Arrays.stream(string.split("")).collect(Collectors.toList());
    }

    private int getOverlap(List<String> list1, List<String> list2) {
        var s1 = new HashSet<String>(list1);
        var s2 = new HashSet<String>(list2);
        s1.retainAll(s2);
        return s1.size();
    }

    @Getter
    private static class Data {

        private final List<String> input = new ArrayList<>();
        private final List<String> output = new ArrayList<>();

        Data(String line) {
            final var data = line.split("\\|");
            Arrays.stream(data[0].split("\\s+"))
                .forEach(it -> {
                    if (it != null && it.trim().length() > 0) {
                        input.add(it.trim());
                    }
                });

            Arrays.stream(data[1].split("\\s+"))
                .forEach(it -> {
                    if (it != null && it.trim().length() > 0) {
                        output.add(it.trim());
                    }
                });
        }
    }


}