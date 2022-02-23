package ent.otego.praise.telegram;

import ent.otego.praise.data.TelegramChat;
import ent.otego.praise.schedule.PraiseSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class PraiseSenderImpl implements PraiseSender {

    private static final String PRAISE_THE_SUN_STICKER_FILE_ID
            = "CAACAgIAAxkBAAMgYhZ1R-vQhP17Vtk19RCFQMIpctAAAnIJAAIItxkCBFPbWMaTR_cjBA";

    private final PraiseTheSunBot bot;

    @Autowired
    public PraiseSenderImpl(PraiseTheSunBot bot) {
        this.bot = bot;
    }

    @Override
    public void praiseTheSun(TelegramChat chat) {
        SendSticker sendPraiseTheSun = SendSticker.builder()
                .chatId(chat.getChatId())
                .sticker(new InputFile(PRAISE_THE_SUN_STICKER_FILE_ID))
                .build();
        try {
            bot.execute(sendPraiseTheSun);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
