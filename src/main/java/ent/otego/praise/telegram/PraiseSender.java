package ent.otego.praise.telegram;

import ent.otego.praise.data.BotDataRepository;
import ent.otego.praise.data.TelegramChat;
import ent.otego.praise.schedule.Praise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class PraiseSender implements Praise {

    private static final String PRAISE_THE_SUN_STICKER_FILE_ID
            = "CAACAgIAAxkBAAMgYhZ1R-vQhP17Vtk19RCFQMIpctAAAnIJAAIItxkCBFPbWMaTR_cjBA";

    private final PraiseTheSunBot bot;
    private final BotDataRepository botDataRepository;

    @Autowired
    public PraiseSender(PraiseTheSunBot bot,
                        BotDataRepository botDataRepository) {
        this.bot = bot;
        this.botDataRepository = botDataRepository;
    }

    @Override
    public void praiseTheSun() {
        for (TelegramChat telegramChat : botDataRepository.getChatsList()) {
            SendSticker sendPraiseTheSun = new SendSticker();
            sendPraiseTheSun.setSticker(new InputFile(PRAISE_THE_SUN_STICKER_FILE_ID));
            sendPraiseTheSun.setChatId(telegramChat.getChatId().toString());
            try {
                bot.execute(sendPraiseTheSun);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
