import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day20 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day20.class.getName());
    private final static String inputFile = "day20.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day20();
//        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var algorithm = input.get(0);

        var imageSize = input.get(3).length() + 50;
        var image = new String[imageSize][imageSize];

        for (String[] arr : image) {
            Arrays.fill(arr, ".");
        }

        int i = 25;
        for (var line : input.subList(2, input.size())) {
            for (int j = 0; j < line.length(); j++) {
                image[i][j + 25] = line.substring(j, j + 1);
            }
            i++;
        }

        var newImage = buildNewImage(image, algorithm, 1);
        newImage = buildNewImage(newImage, algorithm, 2);

        var lightsOn = 0;
        for (int y = 10; y < newImage.length-10; y++) {
            for (int x = 10; x < newImage[0].length-10; x++) {
                if (newImage[y][x].equals("#")) {
                    lightsOn++;
                }
            }
        }

        printImage(newImage);
        log.info("TOTAL: " + lightsOn);
    }

    private String[][] buildNewImage(String[][] image, String algorithm, int round) {
        var newImage = new String[image.length][image.length];

        for (String[] arr : newImage) {
            if (round % 2 == 1) {
                Arrays.fill(arr, "#");
            } else {
                Arrays.fill(arr, ".");
            }
        }

        for (int y = 1; y < image.length - 1; y++) {
            for (int x = 1; x < image[0].length - 1; x++) {
                var binary = new StringBuilder();
                binary.append(translateToBinary(image[y - 1][x - 1]));
                binary.append(translateToBinary(image[y - 1][x]));
                binary.append(translateToBinary(image[y - 1][x + 1]));
                binary.append(translateToBinary(image[y][x - 1]));
                binary.append(translateToBinary(image[y][x]));
                binary.append(translateToBinary(image[y][x + 1]));
                binary.append(translateToBinary(image[y + 1][x - 1]));
                binary.append(translateToBinary(image[y + 1][x]));
                binary.append(translateToBinary(image[y + 1][x + 1]));

                int algIndex = Integer.parseInt(binary.toString(), 2);
                var newVal = algorithm.substring(algIndex, algIndex + 1);
                newImage[y][x] = newVal;
            }
        }

        return newImage;
    }

    private String translateToBinary(String input) {
        if (input.equals("#")) {
            return "1";
        } else {
            return "0";
        }
    }

    private void printImage(String [][] image) {
        var str = "";
        for (int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {
                str += image[y][x];
            }
            str += "\n";
        }
        log.info("Image:\n" + str);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var algorithm = input.get(0);

        var imageSize = input.get(3).length() + 150;
        var image = new String[imageSize][imageSize];

        for (String[] arr : image) {
            Arrays.fill(arr, ".");
        }

        int i = 75;
        for (var line : input.subList(2, input.size())) {
            for (int j = 0; j < line.length(); j++) {
                image[i][j + 75] = line.substring(j, j + 1);
            }
            i++;
        }


        for (int round = 1; round <= 50; round++) {
            image = buildNewImage(image, algorithm, round);
        }

        var lightsOn = 0;
        for (int y = 10; y < image.length-10; y++) {
            for (int x = 10; x < image[0].length-10; x++) {
                if (image[y][x].equals("#")) {
                    lightsOn++;
                }
            }
        }

        printImage(image);
        log.info("TOTAL: " + lightsOn);
    }
}