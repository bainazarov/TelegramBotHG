import java.util.HashMap;
import java.util.Map;

public class GameStatistics {
    private Map<String, PlayerStatistics> playerStatisticsMap;

    public GameStatistics() {
        playerStatisticsMap = new HashMap<>();
    }

    public void updatePlayerStatisticsLetters(String playerName, boolean guessedLetter, boolean unGuessedLetter) {
        PlayerStatistics playerStatistics = playerStatisticsMap.getOrDefault(playerName, new PlayerStatistics());
        if (guessedLetter) {
            playerStatistics.incrementGuessedLetters();
        } else {
            playerStatistics.incrementUnguessedLetters();
        }
        playerStatisticsMap.put(playerName, playerStatistics);
    }
    public void updatePlayerStatisticsWords(String playerName, boolean guessedWord, boolean unGuessedWords) {
        PlayerStatistics playerStatistics = playerStatisticsMap.getOrDefault(playerName, new PlayerStatistics());
        if (guessedWord) {
            playerStatistics.incrementGuessedWords();
        } else {
            playerStatistics.incrementUnguessedWords();
        }
        playerStatisticsMap.put(playerName, playerStatistics);
    }

    public String getStatisticsMessage() {
        StringBuilder statisticsMessage = new StringBuilder();

        for (Map.Entry<String, PlayerStatistics> entry : playerStatisticsMap.entrySet()) {
            String playerName = entry.getKey();
            PlayerStatistics playerStatistics = entry.getValue();

            statisticsMessage.append("Имя игрока: ").append(playerName).append("\n");
            statisticsMessage.append("- Количество угаданных букв: ").append(playerStatistics.getGuessedLetters()).append("\n");
            statisticsMessage.append("- Количество неугаданных букв: ").append(playerStatistics.getUnguessedLetters()).append("\n");
            statisticsMessage.append("- Количество угаданных слов: ").append(playerStatistics.getGuessedWords()).append("\n");
            statisticsMessage.append("- Количество неугаданных слов: ").append(playerStatistics.getUnguessedWords()).append("\n");
            statisticsMessage.append("- Процент успешных попыток: ")
                    .append(playerStatistics.getSuccessRatio()).append("%\n\n");
        }

        return statisticsMessage.toString();
    }

    public class PlayerStatistics {
        private int guessedLetters;
        private int unguessedLetters;
        private int guessedWords;
        private int unguessedWords;

        public PlayerStatistics() {
            guessedLetters = 0;
            unguessedLetters = 0;
            guessedWords = 0;
            unguessedWords = 0;
        }

        public int getGuessedLetters() {
            return guessedLetters;
        }

        public int getUnguessedLetters() {
            return unguessedLetters;
        }

        public int getGuessedWords() {
            return guessedWords;
        }

        public int getUnguessedWords() {
            return unguessedWords;
        }

        public double getSuccessRatio() {
            int totalAttempts = guessedLetters + unguessedLetters + guessedWords + unguessedWords;
            if (totalAttempts != 0) {
                return (double) (guessedLetters + guessedWords) / totalAttempts * 100;
            } else {
                return 0;
            }
        }

        public void incrementGuessedLetters() {
            guessedLetters++;
        }

        public void incrementUnguessedLetters() {
            unguessedLetters++;
        }

        public void incrementGuessedWords() {
            guessedWords++;
        }

        public void incrementUnguessedWords() {
            unguessedWords++;
        }
    }

    public void resetStatistics() {
        playerStatisticsMap.clear();
    }

    public void addPlayer(String playerName) {
        if (!playerStatisticsMap.containsKey(playerName)) {
            playerStatisticsMap.put(playerName, new PlayerStatistics());
        }
    }
}
