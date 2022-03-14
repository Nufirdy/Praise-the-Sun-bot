package ent.otego.praise.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Класс составления последовательного расписания выполнения praise.
 * В теории можно было бы сделать все еще более абстрактно, очистить
 * от конкретики ввод/вывод и оставить только таймер но мне лень :)
 */
@Slf4j
@Service
public class PraiseScheduler {

    private final ScheduledExecutorService scheduledExecutor;

    private ScheduledFuture nextPraise;

    private final TheSun theSun;
    private final Praise praise;

    @Autowired
    public PraiseScheduler(TheSun theSun,
                           Praise praise) {
        scheduledExecutor = Executors.newScheduledThreadPool(1);
        this.theSun = theSun;
        this.praise = praise;

        schedulePraise();
    }

    void schedulePraise() {
        ZonedDateTime sunInZenith = theSun.getZenithTime(LocalDate.now());
        ZonedDateTime now = ZonedDateTime.now();
        if (sunInZenith.isBefore(now) || sunInZenith.equals(now)) {
            sunInZenith = theSun.getZenithTime(LocalDate.from(LocalDate.now().plusDays(1)));
        }
        log.info("Следующее восславление будет {}", sunInZenith.toString());

        long secondsUntilZenith = ChronoUnit.SECONDS.between(now, sunInZenith);
        nextPraise = scheduledExecutor.schedule(this::praiseTheSun, secondsUntilZenith, TimeUnit.SECONDS);
    }

    void praiseTheSun() {
        schedulePraise();
        praise.praiseTheSun();
    }

}
