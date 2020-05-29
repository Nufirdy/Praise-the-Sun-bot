package ClassicKiryusha;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }



    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            var chatId = update.getMessage().getChatId();
        } else {
            return;
        }

    }

    public String getBotUsername() {
        return "ClassicKiryushaBot";
    }

    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }
}
