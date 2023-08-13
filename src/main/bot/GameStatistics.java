
import java.util.List;

public class GameStatistics {
    private PlayerStatisticsDao playerStatisticsDao;

    private List<PlayerStatisticsEntity> getAllPlayerStatistics() {
        return playerStatisticsDao.getAllPlayerStatistics();
    }

    private void updatePlayerStatistics(PlayerStatisticsEntity playerStatistics) {
        playerStatisticsDao.updatePlayerStatistics(playerStatistics);
    }

    private PlayerStatisticsEntity getPlayerStatistics(String playerName) {
        return playerStatisticsDao.getPlayerStatistics(playerName);
    }

    private void savePlayerStatistics(PlayerStatisticsEntity playerStatistics) {
        playerStatisticsDao.savePlayerStatistics(playerStatistics);
    }

    public GameStatistics(PlayerStatisticsDao playerStatisticsDao) {
        this.playerStatisticsDao = playerStatisticsDao;
    }

    public void updatePlayerStatisticsLetters(String playerName, boolean guessedLetter, boolean unGuessedLetter) {
        PlayerStatisticsEntity playerStatistics = getPlayerStatistics(playerName);
        if (playerStatistics == null) {
            playerStatistics = new PlayerStatisticsEntity();
            playerStatistics.setPlayerName(playerName);
        }
        if (guessedLetter) {
            playerStatistics.incrementGuessedLetters();
        } else {
            playerStatistics.incrementUnguessedLetters();
        }
        updatePlayerStatistics(playerStatistics);
    }

    public void updatePlayerStatisticsWords(String playerName, boolean guessedWord, boolean unGuessedWord) {
        PlayerStatisticsEntity playerStatistics = getPlayerStatistics(playerName);
        if (playerStatistics == null) {
            playerStatistics = new PlayerStatisticsEntity();
            playerStatistics.setPlayerName(playerName);
        }
        if (guessedWord) {
            playerStatistics.incrementGuessedWords();
        } else {
            playerStatistics.incrementUnguessedWords();
        }
        updatePlayerStatistics(playerStatistics);
    }

    public String getStatisticsMessage() {
        StringBuilder statisticsMessage = new StringBuilder();

        List<PlayerStatisticsEntity> playerStatisticsList = getAllPlayerStatistics();

        for (PlayerStatisticsEntity playerStatistics : playerStatisticsList) {
            statisticsMessage.append("Имя игрока: ").append(playerStatistics.getPlayerName()).append("\n");
            statisticsMessage.append("- Количество угаданных букв: ").append(playerStatistics.getGuessedLetters()).append("\n");
            statisticsMessage.append("- Количество неугаданных букв: ").append(playerStatistics.getUnguessedLetters()).append("\n");
            statisticsMessage.append("- Количество угаданных слов: ").append(playerStatistics.getGuessedWords()).append("\n");
            statisticsMessage.append("- Количество неугаданных слов: ").append(playerStatistics.getUnguessedWords()).append("\n");
            statisticsMessage.append("- Процент успешных попыток: ").append(playerStatistics.getSuccessRatio()).append("%\n\n");
        }

        return statisticsMessage.toString();
    }

    public void resetStatistics() {
        playerStatisticsDao.resetPlayersStatistics();
    }

    public void addPlayer(String playerName) {
        PlayerStatisticsEntity playerStatistics = new PlayerStatisticsEntity();
        playerStatistics.setPlayerName(playerName);
        savePlayerStatistics(playerStatistics);
    }
}