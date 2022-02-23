package ent.otego.praise;

import ent.otego.praise.schedule.PraiseSender;
import ent.otego.praise.schedule.PraiseSchedulerImpl;
import ent.otego.praise.schedule.TheSun;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


class PraiseSchedulerTest {

	@InjectMocks
	PraiseSchedulerImpl praiseScheduler;

	@Mock
	PraiseSender praiseSender;

	@Mock
	TheSun theSun;

	@Test
	public void testCorrectScheduling() {

	}
}
