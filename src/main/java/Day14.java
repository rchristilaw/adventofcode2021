import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day14 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day14.class.getName());
    private final static String inputFile = "day14.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day14 = new Day14();
//        day14.runPart1();
        day14.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        var lines = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var template = lines.get(0);

        final var insertMap = new HashMap<String, String>();
        lines.subList(2, lines.size())
            .forEach(it -> {
                final var val = it.split(" -> ");
                insertMap.put(val[0], val[1]);
            });


        for (int i = 0; i < 10; i++) {
            var tempArray = template.split("");
            var newStr = new StringBuilder();
            for (int j = 0; j < tempArray.length - 1; j++) {
                newStr.append(template.charAt(j));
                final var currentPattern = template.substring(j, j + 2);
                if (insertMap.containsKey(currentPattern)) {
                    newStr.append(insertMap.get(currentPattern));
                }
            }
            newStr.append(tempArray[tempArray.length - 1]);
//            log.info("TOTAL: " + newStr.length());
            template = newStr.toString();

        }

        var resultArray = template.split("");

        final var letterMap = new HashMap<String, Integer>();
        for (var letter : resultArray) {
            letterMap.put(letter, letterMap.get(letter) == null ? 1 : letterMap.get(letter) + 1);
        }

        for (var l : letterMap.keySet()) {
            log.info("Letter: " + l + " Count: " + letterMap.get(l));
        }

        log.info("?????????????????????????");
    }

    public void runPart2() throws URISyntaxException, IOException {
        var lines = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var template = lines.get(0);

        final var insertMap = new HashMap<String, String>();
        lines.subList(2, lines.size())
            .forEach(it -> {
                final var val = it.split(" -> ");
                insertMap.put(val[0], val[1]);
            });

//        var templateArr = template.split("");
//
//        final var groups = new ArrayList<String>();
//        for (int i = 0; i < templateArr.length - 1; i++) {
//            groups.add(templateArr[i] + templateArr[i + 1]);
//        }
//
//
//        final var letterMap = new HashMap<String, Integer>();
//        addCountToMap(letterMap, template);
//
//        for (var group : groups) {
//            int step = 1;
//            recurse(step, template, insertMap, letterMap);
//        }

        final var letterMap = new HashMap<String, BigDecimal>();
        addCountToMap(letterMap, template);
        recurse(1, template, insertMap, letterMap);

        for (var l : letterMap.keySet()) {
            log.info("Letter: " + l + " Count: " + letterMap.get(l));
        }
    }

    private void recurse(int step, String template, Map<String, String> insertMap, Map<String, BigDecimal> letterMap) {
        if (step > 4) {
            return;
        }
        log.info("STEP: " + step);
        final var groups = new ArrayList<String>();
        for (int i = 0; i < template.length() - 1; i++) {
            groups.add(template.substring(i, i + 2));
        }

        for (var group : groups) {
            for (int i = 0; i < 10; i++) {
                var newStr = new StringBuilder();
                for (int j = 0; j < group.length() - 1; j++) {
                    newStr.append(group.charAt(j));
                    final var currentPattern = group.substring(j, j + 2);
                    if (insertMap.containsKey(currentPattern)) {
                        newStr.append(insertMap.get(currentPattern));
                        addCountToMap(letterMap, insertMap.get(currentPattern));
                    }
                }
                newStr.append(group.charAt(group.length() - 1));
                group = newStr.toString();
            }

            recurse(step + 1, group, insertMap, letterMap);
        }

    }


    private void addCountToMap(Map<String, BigDecimal> letterMap, String result) {
        var resultArray = result.split("");
        for (var letter : resultArray) {
            letterMap.put(letter, letterMap.get(letter) == null
                ? BigDecimal.ONE : letterMap.get(letter).add(BigDecimal.ONE));
        }
    }
}