package ent.otego.praise.sun;

import ent.otego.praise.schedule.TheSun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class SunTimesSource implements TheSun {

    private static final float DEFAULT_LATITUDE = 59.945073f;
    private static final float DEFAULT_LONGITUDE = 30.313775f;
    private static final ZoneId DEFAULT_TIME_ZONE = Clock.systemDefaultZone().getZone();

    private final SunriseSunsetClient sunriseSunsetClient;

    @Autowired
    public SunTimesSource(SunriseSunsetClient sunriseSunsetClient) {
        this.sunriseSunsetClient = sunriseSunsetClient;
    }

    @Override
    public ZonedDateTime getZenithTime(LocalDate date, float lat, float lon) {
        SunriseSunsetResponse solarTimes = sunriseSunsetClient.getSolarTimes(
                lat,
                lon,
                date,
                0);
        return solarTimes.getResults().getSolarNoon().withZoneSameInstant(DEFAULT_TIME_ZONE);
    }
}
