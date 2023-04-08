package ent.otego.praise.data;

import java.util.List;

public interface BotDataRepository {

    List<TelegramChat> getChatsList();

    TelegramChat getByChatId(long telegramChatId);

    void saveOrUpdateChat(TelegramChat chat);

    void delete(TelegramChat chat);

    boolean exists(long telegramChatId);
}
