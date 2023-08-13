import java.util.List;

public interface PlayerStatisticsDao {
    void savePlayerStatistics(PlayerStatisticsEntity playerStatistics);
    PlayerStatisticsEntity getPlayerStatistics(String playerName);
    List<PlayerStatisticsEntity> getAllPlayerStatistics();
    void resetPlayersStatistics();
    void updatePlayerStatistics(PlayerStatisticsEntity playerStatistics);
}
