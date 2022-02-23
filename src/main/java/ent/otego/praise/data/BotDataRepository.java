package ent.otego.praise.data;

import java.util.List;

public interface BotDataRepository {

    List<TelegramChat> getChatsList();
}
