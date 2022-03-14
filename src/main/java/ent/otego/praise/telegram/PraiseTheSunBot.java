package ent.otego.praise.telegram;

import ent.otego.praise.data.BotDataRepository;
import ent.otego.praise.data.TelegramChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class PraiseTheSunBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    private final BotDataRepository botDataRepository;

    @Autowired
    public PraiseTheSunBot(BotDataRepository botDataRepository) {
        this.botDataRepository = botDataRepository;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            TelegramChat chat = TelegramChat.builder()
                    .chatId(message.getChatId())
                    .isPrivate(message.getChat().isUserChat())
                    .build();
            botDataRepository.saveChat(chat);
        }
    }
}
