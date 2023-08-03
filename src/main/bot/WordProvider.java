import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordProvider {

    private static List<String> words;

    public WordProvider() {
        words = loadWordsFromFile("hangman_words.txt");
    }

    public static String chooseRandomWord() {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    private List<String> loadWordsFromFile(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}