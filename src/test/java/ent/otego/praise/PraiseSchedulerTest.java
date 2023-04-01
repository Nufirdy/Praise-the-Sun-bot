package ent.otego.praise;

import ent.otego.praise.data.BotDataRepository;
import ent.otego.praise.data.TelegramChat;
import ent.otego.praise.schedule.PraiseSchedulerImpl;
import ent.otego.praise.schedule.PraiseSender;
import ent.otego.praise.schedule.TheSun;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PraiseSchedulerTest {

    @InjectMocks
    PraiseSchedulerImpl praiseScheduler;

    @Mock
    PraiseSender praiseSender;

    @Mock
    TheSun theSun;

    @Mock
    BotDataRepository botDataRepository;

    @Test
    public void testPraiseSent() throws InterruptedException {
        when(theSun.getZenithTime(any(), anyFloat(), anyFloat()))
                .thenAnswer(i -> {
                    LocalDate date = i.getArgument(0);
                    return date.atTime(LocalTime.now())
                            .atZone(Clock.systemDefaultZone().getZone())
                            .plus(200, ChronoUnit.MILLIS);
                });
        praiseScheduler.schedulePraiseTaskFor(stubTelegramChat());
        Thread.sleep(300);
        verify(praiseSender).praiseTheSun(any());
    }

    private TelegramChat stubTelegramChat() {
        return TelegramChat.builder()
                .longitude(0f)
                .latitude(0f)
                .build();
    }
}
