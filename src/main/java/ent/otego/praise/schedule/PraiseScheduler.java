package ent.otego.praise.schedule;

import ent.otego.praise.data.TelegramChat;

public interface PraiseScheduler {

    void schedulePraiseTaskFor(TelegramChat chat);

    void cancelScheduledPraiseTask(TelegramChat chat);
}
