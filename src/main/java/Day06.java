import lombok.Data;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Day06 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day06.class.getName());
    private final static String inputFile = "day06.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day6 = new Day06();
//        day6.runPart1();
        day6.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);
        final var fishList = Arrays.stream(input.split(","))
            .map(it -> Integer.parseInt(it.trim()))
            .collect(Collectors.toList());

        for (int i = 0; i < 80; i++) {
            int currentSize = fishList.size();
            for (int f = 0; f < currentSize; f++) {
                final var fish = fishList.get(f);
                if (fish == 0) {
                    fishList.set(f, 6);
                    fishList.add(8);
                } else {
                    fishList.set(f, fish - 1);
                }
            }
        }
        log.info("TOTAL after 80 days: " + fishList.size());
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        final var fishMap = new HashMap<>(Map.of(
            0, BigDecimal.ZERO,
            1, BigDecimal.ZERO,
            2, BigDecimal.ZERO,
            3, BigDecimal.ZERO,
            4, BigDecimal.ZERO,
            5, BigDecimal.ZERO,
            6, BigDecimal.ZERO,
            7, BigDecimal.ZERO,
            8, BigDecimal.ZERO
        ));
        Arrays.stream(input.split(","))
            .forEach(it -> {
                final var currentVal = Integer.parseInt(it.trim());
                fishMap.replace(currentVal, fishMap.get(currentVal).add(BigDecimal.ONE));
            });

        for (int i = 0; i < 256; i++) {
            final var toAdd = fishMap.get(0);
            fishMap.put(0, fishMap.get(1));
            fishMap.put(1, fishMap.get(2));
            fishMap.put(2, fishMap.get(3));
            fishMap.put(3, fishMap.get(4));
            fishMap.put(4, fishMap.get(5));
            fishMap.put(5, fishMap.get(6));
            fishMap.put(6, fishMap.get(7).add(toAdd));
            fishMap.put(7, fishMap.get(8));
            fishMap.put(8, toAdd);
        }

        var total = BigDecimal.ZERO;
        for (int i = 0; i <= 8; i++) {
           total = total.add(fishMap.get(i));
        }
        log.info("TOTAL after 256 days: " + total);
    }

}