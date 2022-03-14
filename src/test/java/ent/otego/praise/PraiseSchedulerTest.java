package ent.otego.praise;

import ent.otego.praise.schedule.Praise;
import ent.otego.praise.schedule.PraiseScheduler;
import ent.otego.praise.schedule.TheSun;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


class PraiseSchedulerTest {

	@InjectMocks
	PraiseScheduler praiseScheduler;

	@Mock
	Praise praise;

	@Mock
	TheSun theSun;

	@Test
	public void testCorrectScheduling() {

	}
}
