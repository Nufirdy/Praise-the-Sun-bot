package ent.otego.praise.schedule;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import ent.otego.praise.data.BotDataRepository;
import ent.otego.praise.data.TelegramChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class PraiseSchedulerImpl implements PraiseScheduler {

    private final ScheduledExecutorService scheduledExecutor;

    private final Map<TelegramChat, ScheduledFuture<?>> praises;
    private final Map<TelegramChat, LocalDate> previousPraises;

    private final TheSun theSun;
    private final PraiseSender praiseSender;

    @Autowired
    public PraiseSchedulerImpl(TheSun theSun,
                               BotDataRepository repository,
                               PraiseSender praiseSender) {
        this.theSun = theSun;
        this.praiseSender = praiseSender;

        int cores = Runtime.getRuntime().availableProcessors();
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("praise-scheduler-thread-%d")
                .build();
        scheduledExecutor = Executors.newScheduledThreadPool(cores, threadFactory);

        praises = new ConcurrentHashMap<>();
        previousPraises = new ConcurrentHashMap<>();

        for (TelegramChat chat : repository.getChatsList()) {
            if (chat.getLongitude() != null && chat.getLatitude() != null) {
                schedulePraiseTaskFor(chat);
            }
        }
    }

    private void schedulePraise(TelegramChat chat) {
        ZonedDateTime sunInZenith = theSun.getZenithTime(LocalDate.now(), chat.getLatitude(),
                chat.getLongitude());
        ZonedDateTime now = ZonedDateTime.now();
        LocalDate lastPraise = previousPraises.get(chat);
        if (sunInZenith.isBefore(now)
                || lastPraise != null && lastPraise.isEqual(LocalDate.now())) {
            sunInZenith = theSun.getZenithTime(LocalDate.from(LocalDate.now().plusDays(1)),
                    chat.getLatitude(), chat.getLongitude());
        }

        long secondsUntilZenith = ChronoUnit.SECONDS.between(now, sunInZenith);
        ScheduledFuture<?> praise = scheduledExecutor.schedule(() -> this.praiseTheSun(chat),
                secondsUntilZenith, TimeUnit.SECONDS);
        praises.put(chat, praise);
        log.info("Scheduled praise for {} at {}", chat, sunInZenith);
    }

    private void praiseTheSun(TelegramChat chat) {
        praiseSender.praiseTheSun(chat);
        previousPraises.put(chat, LocalDate.now());
        schedulePraise(chat);
    }

    @Override
    public void schedulePraiseTaskFor(TelegramChat chat) {
        ScheduledFuture<?> praiseTask =
                scheduledExecutor.schedule(() -> schedulePraise(chat), 0, TimeUnit.SECONDS);
        ScheduledFuture<?> prevTask = praises.put(chat, praiseTask);
        if (prevTask != null) {
            prevTask.cancel(true);
            log.info("Rescheduled praise for {}", chat);
        }
    }

    @Override
    public void cancelScheduledPraiseTask(TelegramChat chat) {
        ScheduledFuture<?> praise = praises.remove(chat);
        if (praise != null) {
            praise.cancel(true);
            log.info("Canceled praise for {}", chat);
        }
    }
}
