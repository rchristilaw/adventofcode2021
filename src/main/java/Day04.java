import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day04 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day04.class.getName());
    private final static String inputFile = "day04.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day4 = new Day04();
//        day4.runPart1();
        day4.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        final var callNumbers = Arrays.asList(input.get(0).split(","));

        final var cards = buildCards(input);

        final var numbersCalled = new ArrayList<Integer>();
        for (var number : callNumbers) {
            numbersCalled.add(Integer.parseInt(number));

            final var winners = checkForWinner(cards, numbersCalled);
            if (!winners.isEmpty()) {
                log.info("Winning Number: " + number);

                final var winner = winners.get(0);

                log.info(Arrays.toString(winner[0]) + "\n"
                    + Arrays.toString(winner[1]) + "\n"
                    + Arrays.toString(winner[2]) + "\n"
                    + Arrays.toString(winner[3]) + "\n"
                    + Arrays.toString(winner[4]) + "\n");

                final var unselectedCount = countUnselected(winner, numbersCalled);
                log.info("Unselected count: " + unselectedCount);

                log.info("RESULT: " + (Integer.parseInt(number) * unselectedCount));
                break;
            }
        }
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        final var callNumbers = Arrays.asList(input.get(0).split(","));

        final var cards = buildCards(input);

        final var numbersCalled = new ArrayList<Integer>();
        for (var number : callNumbers) {
            final var currentNumber = Integer.parseInt(number);
            numbersCalled.add(currentNumber);


            final var winners = checkForWinner(cards, numbersCalled);

            if (cards.size() == 1 && winners.size() == 1) {
                log.info("LOSER FOUND");

                final var unselectedCount = countUnselected(cards.get(0), numbersCalled);
                log.info("Unselected count: " + unselectedCount);

                log.info("RESULT: " + (currentNumber * unselectedCount));
                break;
            } else if (!winners.isEmpty()) {
                cards.removeAll(winners);
            }
        }
    }

    private List<Integer[][]> checkForWinner(List<Integer[][]> cards, List<Integer> callNumbers) {
        final var winners = new ArrayList<Integer[][]>();
        for(final var card : cards) {
            var sumOfUnselected = 0;
            boolean winner = false;
            for (int i = 0; i < 5; i++) {
                int y = 0;
                int x = 0;
                for (int j = 0; j < 5; j++) {

                    if (callNumbers.contains(card[i][j])) {
                        x++;
                    }

                    if (callNumbers.contains(card[j][i])) {
                        y++;
                    }
                }
                if (x == 5 || y == 5) {
                    log.info("WINNER FOUND. Sum of Unselected Winner: " + sumOfUnselected);
                    winners.add(card);
                    break;
                }
            }
        }
        return winners;
    }

    private int countUnselected(Integer[][] card, List<Integer> callNumbers) {
        var sumOfUnselected = 0;
        for (int i = 0; i < 5; i++) {
            int y = 0;
            int x = 0;
            for (int j = 0; j < 5; j++) {
                if (!callNumbers.contains(card[i][j])) {
                    sumOfUnselected = sumOfUnselected + card[i][j];
                }
            }
        }

        return sumOfUnselected;
    }

    private List<Integer[][]> buildCards(List<String> input) {
        final var cards = new ArrayList<Integer[][]>();

        final var cardLines = new ArrayList<String>();
        for (int line = 2; line < input.size(); line++) {

            if (input.get(line).length() > 2) {
                cardLines.add(input.get(line));
            }

            if (cardLines.size() == 5) {
                cards.add(buildCard(cardLines));
                cardLines.clear();
            }
        }
        return cards;
    }

    private Integer[][] buildCard(List<String> lines) {
        final var card = new Integer[5][5];

        int lineNo = 0;

        for (final var lineStr : lines) {
            final var line = lineStr.strip().split("\\s+");
            log.info(Arrays.toString(line));

            for (int i = 0; i < 5; i++) {
                card[lineNo][i] = Integer.parseInt(line[i]);
//                line
            }
            lineNo++;
        }

        return card;
    }

}