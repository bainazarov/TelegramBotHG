import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashMap;


public class TelegramBotManager implements BotConstants {
    private TelegramBot bot;
    static HashMap<Long, HangmanGame> activeGames;

    public TelegramBotManager() {
        bot = new TelegramBot(BOT_TOKEN);
        activeGames = new HashMap<>();
    }

    public static void main(String[] args) {
        TelegramBotManager botManager = new TelegramBotManager();
        botManager.startBot();
    }
        public void startBot () {
            bot.setUpdatesListener(updates -> {
                for (Update update : updates) {
                    onUpdate(update);
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            });
        }
    private void onUpdate(Update update) {
        Message message = update.message();

        if (message != null && message.text()!=null) {
            long chatId = message.chat().id();
            String text = message.text();
            if (text.equals("/hangman")) {
                HangmanGame game = activeGames.get(chatId);
                if (game == null) {
                    game = new HangmanGame(bot);
                    activeGames.put(chatId, game);
                }
                game.startGame(message);
            } else if (text.equals("/hangmanstop")) {
                HangmanGame game = activeGames.get(chatId);
                if (game != null) {
                    game.stopGame(message);
                    activeGames.remove(chatId);
                }
            } else {
                HangmanGame game = activeGames.get(chatId);
                if (game != null) {
                    game.makeGuess(message);
                } else {
                    sendInstructions(chatId);
                }
            }
        }
    }

    private void sendInstructions (long chatId) {
        SendMessage instructions = new SendMessage(chatId,
                "Добро пожаловать в игру 'Виселица'!\n\n" +
                "Чтобы начать игру, отправьте команду /hangman\n" +
                "Чтобы прекратить игру, отправьте команду /hangmanstop.");
        bot.execute(instructions);
    }
}
