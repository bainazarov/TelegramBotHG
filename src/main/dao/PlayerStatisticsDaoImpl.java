import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerStatisticsDaoImpl implements PlayerStatisticsDao {

    @Override
    public void savePlayerStatistics(PlayerStatisticsEntity playerStatistics) {
        String sql = "INSERT INTO player_statistics (player_name, guessed_letters, unguessed_letters, guessed_words, unguessed_words) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, playerStatistics.getPlayerName());
            statement.setInt(2, playerStatistics.getGuessedLetters());
            statement.setInt(3, playerStatistics.getUnguessedLetters());
            statement.setInt(4, playerStatistics.getGuessedWords());
            statement.setInt(5, playerStatistics.getUnguessedWords());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerStatisticsEntity getPlayerStatistics(String playerName) {
        String sql = "SELECT * FROM player_statistics WHERE player_name = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    PlayerStatisticsEntity playerStatistics = new PlayerStatisticsEntity();
                    playerStatistics.setPlayerName(resultSet.getString("player_name"));
                    playerStatistics.setGuessedLetters(resultSet.getInt("guessed_letters"));
                    playerStatistics.setUnguessedLetters(resultSet.getInt("unguessed_letters"));
                    playerStatistics.setGuessedWords(resultSet.getInt("guessed_words"));
                    playerStatistics.setUnguessedWords(resultSet.getInt("unguessed_words"));
                    return playerStatistics;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<PlayerStatisticsEntity> getAllPlayerStatistics() {
        String sql = "SELECT * FROM player_statistics";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<PlayerStatisticsEntity> playerStatisticsList = new ArrayList<>();
            while (resultSet.next()) {
                PlayerStatisticsEntity playerStatistics = new PlayerStatisticsEntity();
                playerStatistics.setPlayerName(resultSet.getString("player_name"));
                playerStatistics.setGuessedLetters(resultSet.getInt("guessed_letters"));
                playerStatistics.setUnguessedLetters(resultSet.getInt("unguessed_letters"));
                playerStatistics.setGuessedWords(resultSet.getInt("guessed_words"));
                playerStatistics.setUnguessedWords(resultSet.getInt("unguessed_words"));
                playerStatisticsList.add(playerStatistics);
            }
            return playerStatisticsList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetPlayersStatistics() {
        String sql = "DELETE FROM player_statistics";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePlayerStatistics(PlayerStatisticsEntity playerStatistics) {
        String sql = "UPDATE player_statistics SET guessed_letters = ?, unguessed_letters = ?, guessed_words = ?, unguessed_words = ? WHERE player_name = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerStatistics.getGuessedLetters());
            statement.setInt(2, playerStatistics.getUnguessedLetters());
            statement.setInt(3, playerStatistics.getGuessedWords());
            statement.setInt(4, playerStatistics.getUnguessedWords());
            statement.setString(5, playerStatistics.getPlayerName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}