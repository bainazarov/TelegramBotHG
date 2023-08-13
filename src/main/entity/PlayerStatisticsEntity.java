import lombok.Data;

@Data
public class PlayerStatisticsEntity {
    private String playerName;
    private int guessedLetters;
    private int unguessedLetters;
    private int guessedWords;
    private int unguessedWords;

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

    public double getSuccessRatio() {
        int totalAttempts = guessedLetters + unguessedLetters + guessedWords + unguessedWords;
        if (totalAttempts != 0) {
            return (double) (guessedLetters + guessedWords) / totalAttempts * 100;
        } else {
            return 0;
        }
    }
}
