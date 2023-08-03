import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

public class HangmanGame {

    private boolean isGameStarted;
    private String secretWord;
    private StringBuilder revealedWord;
    private TelegramBot bot;

    public HangmanGame(TelegramBot bot) {
        this.bot = bot;
        isGameStarted = false;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void startGame(Message message) {
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
        }
    }

    public void makeGuess(Message message) {
        if (isGameStarted) {
            String guess = message.text().toLowerCase();
            if (guess.matches("[а-яА-ЯёЁ]")) {
                char letter = guess.charAt(0);
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
                        bot.execute(msg);
                        checkIfWordRevealed(message.chat().id());
                    }
                } else {
                    SendMessage msg = new SendMessage(message.chat().id(), "Увы, такой буквы нет");
                    msg.replyToMessageId(message.messageId());
                    bot.execute(msg);
                }
            } else {
                SendMessage msg = new SendMessage(message.chat().id(), "Пожалуйста, введите только одну букву");
                msg.replyToMessageId(message.messageId());
                bot.execute(msg);
            }
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
    }

    public void checkIfWordRevealed(long chatId) {
        if(!revealedWord.toString().contains("*")) {
            SendMessage msg = new SendMessage(chatId, "Вы угадали все буквы. Поздравляю вы победили!");
            bot.execute(msg);
            endGame();
        }
    }

    private String chooseRandomWord() {
        WordProvider wordProvider = new WordProvider();
        return wordProvider.chooseRandomWord();
    }
}
