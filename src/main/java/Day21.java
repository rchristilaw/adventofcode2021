import lombok.AllArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Day21 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day21.class.getName());
    private final static String inputFile = "day21.txt";

    private static final int PLAYER_1_START = 7;
    private static final int PLAYER_2_START = 8;

    private Map<String, GameResult> cache;

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day21();

        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
//        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        var dice = 0;

        var player1 = new Player(1, 0, PLAYER_1_START, 0);
        var player2 = new Player(2, 0, PLAYER_2_START, 0);

        int winningScore = 0;
        int losingScore = 0;
        while (winningScore == 0) {

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
        for (int i = 1; i <= 3; i++) {
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
//        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        cache = new HashMap<>();
        var result = recursiveRoll(0, PLAYER_1_START, 0, PLAYER_2_START, true, 0, 0);

        log.info("Part 2 Total: \n" + result.winsPlayer1 + "\n" + result.winsPlayer2);

    }

    private GameResult recursiveRoll(int p1Score, int p1Pos, int p2Score, int p2Pos, boolean p1Turn, int rollCount, int roll) {
        var result = new GameResult();


        if (p1Turn) {
            p1Pos = movePlayer(roll, p1Pos);
        } else {
            p2Pos = movePlayer(roll, p2Pos);
        }

        if (rollCount == 3) {
            rollCount = 0;


            if (p1Turn) {
                p1Score += p1Pos;
                if (p1Score >= 21) {
                    result.winsPlayer1 = BigDecimal.ONE;
                    return result;
                }
            } else {
                p2Score += p2Pos;
                if (p2Score >= 21) {
                    result.winsPlayer2 = BigDecimal.ONE;
                    return result;
                }
            }

            p1Turn = !p1Turn;
        }

        rollCount++;

        for (int r = 1; r <= 3; r++) {
            GameResult subResult;

            var key = p1Score + "_" + p1Pos + "_" + p2Score + "_" + p2Pos + "_" + p1Turn + "_" + rollCount+ "_" + r;

            if (cache.get(key) != null) {
                subResult = cache.get(key);
            } else {
                subResult = recursiveRoll(p1Score, p1Pos, p2Score, p2Pos, p1Turn, rollCount, r);
                cache.put(key, subResult);
            }
            result.addResult(subResult);
        }
        return result;
    }

    private int movePlayer(int diceRoll, int position) {
        for (int i = 1; i <= diceRoll; i++) {
            position++;
            if (position > 10) {
                position = 1;
            }
        }
        return position;
    }

    private static class GameResult {
        private BigDecimal winsPlayer1 = BigDecimal.ZERO;
        private BigDecimal winsPlayer2 = BigDecimal.ZERO;

        void addResult(GameResult result) {
            winsPlayer1 = winsPlayer1.add(result.winsPlayer1);
            winsPlayer2 = winsPlayer2.add(result.winsPlayer2);
        }
    }

    @AllArgsConstructor
    private static class Player {
        int name;
        int score;
        int position;
        int rollCount;
    }
}