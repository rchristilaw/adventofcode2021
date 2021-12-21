import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day21 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day21.class.getName());
    private final static String inputFile = "day21.txt";

    private static final int PLAYER_1_START = 7;
    private static final int PLAYER_2_START = 8;

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day21();

        day.runPart1();
//        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
//        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var dice = 0;

        var player1 = new Player(1, 0, PLAYER_1_START, 0);
        var player2 = new Player(2, 0, PLAYER_2_START, 0);

        int winningScore = 0;
        int losingScore = 0;
        while(winningScore == 0) {

            dice = roll(player1, dice);

            if (player1.score >= 1000) {
                winningScore = player1.score;
                losingScore = player2.score;
                continue;
            }

            dice = roll(player2, dice);
            if (player2.score >= 1000) {
                winningScore = player2.score;
                losingScore = player1.score;
            }
        }


        var totalDiceRolls = player1.rollCount + player2.rollCount;
        var total = losingScore * totalDiceRolls;
        log.info("Part 1 Total: " + total);
    }

    private int roll(Player player, int dice) {
        int totalMove = 0;
        for (int i = 1; i<=3; i++) {
            player.rollCount++;
            dice++;
            if (dice > 100) {
                dice = 1;
            }
            totalMove += dice;
        }

        var newPosition = player.position;
        for (int i = 1; i <= totalMove; i++) {
            newPosition++;
            if (newPosition > 10) {
                newPosition = 1;
            }
        }
        player.position = newPosition;
        player.score += player.position;
        return dice;
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var total = 0;
        log.info("TOTAL: " + total);
    }

    @AllArgsConstructor
    private static class Player {
        int name;
        int score;
        int position;
        int rollCount;
    }
}