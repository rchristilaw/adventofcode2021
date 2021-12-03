import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day02 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day02.class.getName());
    private final static String inputFile = "day02.txt";

    private final static String DIRECTION_UP = "up";
    private final static String DIRECTION_DOWN = "down";
    private final static String DIRECTION_FORWARD = "forward";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day1 = new Day02();
//        day1.runPart1();
        day1.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        final var coordinates = new HashMap<String, Integer>();
        coordinates.put("X", 0); // horizontal
        coordinates.put("Y", 0); // depth

        input.lines()
            .forEach(it -> {
                    final var command = it.split(" ");
                    final var direction = command[0];
                    final var units = Integer.valueOf(command[1]);

                    final var currentX = coordinates.get("X");
                    final var currentY = coordinates.get("Y");

                    switch (direction) {
                        case DIRECTION_FORWARD:
                            coordinates.replace("X", currentX + units);
                            break;
                        case DIRECTION_DOWN:
                            coordinates.replace("Y", currentY + units);
                            break;
                        case DIRECTION_UP:
                            coordinates.replace("Y", currentY - units);
                            break;
                        default:
                            log.warning("Couldn't find command");
                    }
                }

            );
        log.info("Current Depth: " + coordinates.get("Y"));
        log.info("Current Horizontal: " + coordinates.get("X"));

        log.info("Combined: " + coordinates.get("X") * coordinates.get("Y"));
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        final var coordinates = new HashMap<String, Integer>();
        coordinates.put("X", 0); // horizontal
        coordinates.put("Y", 0); // depth
        coordinates.put("AIM", 0); // depth

        input.lines()
            .forEach(it -> {
                    final var command = it.split(" ");
                    final var direction = command[0];
                    final var units = Integer.valueOf(command[1]);

                    final var currentX = coordinates.get("X");
                    final var currentY = coordinates.get("Y");
                    final var currentAIM = coordinates.get("AIM");

                    switch (direction) {
                        case DIRECTION_FORWARD:
                            coordinates.replace("X", currentX + units);
                            coordinates.replace("Y", currentY + (currentAIM * units));
                            break;
                        case DIRECTION_DOWN:
                            coordinates.replace("AIM", currentAIM + units);
                            break;
                        case DIRECTION_UP:
                            coordinates.replace("AIM", currentAIM - units);
                            break;
                        default:
                            log.warning("Couldn't find command");
                    }
                }

            );
        log.info("Current Depth: " + coordinates.get("Y"));
        log.info("Current Horizontal: " + coordinates.get("X"));

        log.info("Combined: " + coordinates.get("X") * coordinates.get("Y"));
    }


}
