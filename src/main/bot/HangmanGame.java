import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.ArrayList;
import java.util.List;


public class HangmanGame {

    private boolean isGameStarted;
    private String secretWord;
    private StringBuilder revealedWord;
    private TelegramBot bot;
    private GameStatistics gameStatistics;
    private Message currentMessage;

    public HangmanGame(TelegramBot bot) {
        this.bot = bot;
        isGameStarted = false;
        PlayerStatisticsDao playerStatisticsDao = new PlayerStatisticsDaoImpl();
        this.gameStatistics = new GameStatistics(playerStatisticsDao);
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    private void revealedWord() {
        revealedWord = new StringBuilder(secretWord);
    }

    public void startGame(Message message) {
        this.currentMessage = message;
        if(!isGameStarted) {
            secretWord = chooseRandomWord();
            revealedWord = new StringBuilder(secretWord.replaceAll(".", "*"));
            SendMessage startMsg = new SendMessage(message.chat().id(),
                    "Я загадал слово. Угадай его, отправляя буквы или целые слова!");
            startMsg.replyToMessageId(message.messageId());
            SendMessage revealMsg = new SendMessage(message.chat().id(), revealedWord.toString());
            revealMsg.replyToMessageId(message.messageId());
            bot.execute(startMsg);
            bot.execute(revealMsg);
            isGameStarted = true;
            gameStatistics.resetStatistics();
            gameStatistics.addPlayer(message.from().firstName());
        }
    }

    public void makeGuess(Message message) {
        if (!isGameStarted) {
            return;
        }

        String guess = message.text().toLowerCase();

        if (guess.length() > 1) {
            handleWordGuess(message, guess);
        } else if (guess.matches("[а-яА-ЯёЁ]")) {
            handleLetterGuess(message, guess.charAt(0));
        } else {
            SendMessage msg = new SendMessage(message.chat().id(), "Пожалуйста, введите только одну букву");
            msg.replyToMessageId(message.messageId());
            bot.execute(msg);
        }
    }

    private void handleWordGuess(Message message, String guess) {
        if (guess.equalsIgnoreCase(secretWord)) {
            revealedWord();
            SendMessage msg = new SendMessage(message.chat().id(),
                    "Поздравляю, вы правильно угадали слово: " + secretWord);
            msg.replyToMessageId(message.messageId());
            gameStatistics.updatePlayerStatisticsWords(message.from().firstName(), true, false);
            bot.execute(msg);
            endGame();
        } else {
            handleWrongWord(message);
        }
    }

    private void handleLetterGuess(Message message, char letter) {
        if (secretWord.toLowerCase().contains(String.valueOf(letter))) {
            boolean hasGuessed = false;
            for (int i = 0; i < secretWord.length(); i++) {
                if (Character.toLowerCase(secretWord.charAt(i)) == letter && revealedWord.charAt(i) == '*') {
                    revealedWord.setCharAt(i, secretWord.charAt(i));
                    hasGuessed = true;
                }
            }
            if (hasGuessed) {
                SendMessage msg = new SendMessage(message.chat().id(),
                        "Вы угадали, такая буква есть в слове:\n" + revealedWord.toString());
                msg.replyToMessageId(message.messageId());
                gameStatistics.updatePlayerStatisticsLetters(message.from().firstName(), true, false);
                bot.execute(msg);
                checkIfWordRevealed(message.chat().id());
            }
        } else {
            SendMessage msg = new SendMessage(message.chat().id(), "Увы, такой буквы нет");
            msg.replyToMessageId(message.messageId());
            gameStatistics.updatePlayerStatisticsLetters(message.from().firstName(), false, true);
            bot.execute(msg);
        }
    }

    public void stopGame(Message message) {
        if(isGameStarted) {
            SendMessage msg = new SendMessage(message.chat().id(), "Игра прервана");
            msg.replyToMessageId(message.messageId());
            bot.execute(msg);
            endGame();
        }
    }

    public void endGame() {
        secretWord = "";
        revealedWord = null;
        isGameStarted = false;

        StringBuilder messageText = new StringBuilder("Игра завершена.\n\n");

        List<String> alreadyDisplayedPlayers = new ArrayList<>();

        for (HangmanGame game : TelegramBotManager.activeGames.values()) {
            String playerName = game.getCurrentMessage().from().firstName();

            if (!alreadyDisplayedPlayers.contains(playerName)) {
                messageText.append(game.getGameStatistics().getStatisticsMessage());
                messageText.append("\n");

                alreadyDisplayedPlayers.add(playerName);
            }
        }

        SendMessage statisticsMsg = new SendMessage(currentMessage.chat().id(), messageText.toString());
        statisticsMsg.replyToMessageId(currentMessage.messageId());
        bot.execute(statisticsMsg);
    }

    private Message getCurrentMessage() {
        return currentMessage;
    }


    public void checkIfWordRevealed(long chatId) {
        if (!revealedWord.toString().contains("*")) {
            StringBuilder messageText = new StringBuilder("Вы угадали все буквы. Поздравляю, вы победили!");


            SendMessage msg = new SendMessage(chatId, messageText.toString());
            bot.execute(msg);
            revealedWord();
            endGame();
        }
    }

    private String chooseRandomWord() {
        WordProvider wordProvider = new WordProvider();
        return wordProvider.chooseRandomWord();
    }

    private void handleWrongWord(Message message) {
        SendMessage msg = new SendMessage(message.chat().id(), "Увы, вы неправильно угадали слово.");
        msg.replyToMessageId(message.messageId());
        gameStatistics.updatePlayerStatisticsWords(message.from().firstName(), false, true);
        bot.execute(msg);
    }

    public GameStatistics getGameStatistics() {
        return gameStatistics;
    }
}
