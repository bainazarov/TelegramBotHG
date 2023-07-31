import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class TelegramBotManager implements BotConstants {
    private static boolean isBotActivated = false;
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot(BOT_TOKEN);

        bot.setUpdatesListener(updates -> {

            onUpdate(bot, updates.get(0));

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    static void onUpdate(TelegramBot bot, Update update) {
        if(!isBotActivated) {
            SendMessage request = new SendMessage(CHAT_ID, "bot activated");
            bot.execute(request);
            isBotActivated = true;
        }
        String messageText = update.message().text();
        long chatId = update.message().chat().id();

        if(messageText != null) {
            if(messageText.equalsIgnoreCase("hi")) {
                SendMessage request = new SendMessage(chatId, "hello");
                bot.execute(request);
            } else if (messageText.equalsIgnoreCase("bye")) {
                SendMessage request = new SendMessage(chatId, "goodbye");
                bot.execute(request);
            }
        }
    }
}
