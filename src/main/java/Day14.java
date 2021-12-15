import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
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
            var tempArray =  template.split("");
            var newStr = new StringBuilder();
            for(int j = 0; j < tempArray.length - 1; j++) {
                newStr.append(template.charAt(j));
                final var currentPattern = template.substring(j, j+2);
                if (insertMap.containsKey(currentPattern)) {
                    newStr.append(insertMap.get(currentPattern));
                }
            }
            newStr.append(tempArray[tempArray.length - 1]);
            log.info("TOTAL: " + newStr.length());
            template = newStr.toString();

        }

        var resultArray =  template.split("");

        final var letterMap = new HashMap<String, Integer>();
        for(var letter : resultArray) {
            letterMap.put(letter, letterMap.get(letter) == null ? 1 : letterMap.get(letter) + 1);
        }

        for (var l : letterMap.keySet()) {
            log.info("Letter: " + l + " Count: " + letterMap.get(l));
        }
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

        var tempLL = new LinkedList<>(Arrays.asList(template.split("")));
        for (int i = 0; i < 40; i++) {
            var newLL = new LinkedList<String>();
            for(int j = 0; j < tempLL.size() - 1; j++) {
                newLL.add(tempLL.get(j));
                final var currentPattern = tempLL.get(j) + tempLL.get(j+1);
                if (insertMap.containsKey(currentPattern)) {
                    newLL.add(insertMap.get(currentPattern));
                }
            }
            newLL.add(tempLL.getLast());

            tempLL = new LinkedList<>(newLL);
            log.info("Current Level: " + i);
        }

        final var letterMap = new HashMap<String, Integer>();
        for(var letter : tempLL) {
            letterMap.put(letter, letterMap.get(letter) == null ? 1 : letterMap.get(letter) + 1);
        }

        for (var l : letterMap.keySet()) {
            log.info("Letter: " + l + " Count: " + letterMap.get(l));
        }
    }
}