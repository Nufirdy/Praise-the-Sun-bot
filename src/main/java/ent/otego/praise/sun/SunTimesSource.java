package ent.otego.praise.sun;

import ent.otego.praise.schedule.TheSun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class SunTimesSource implements TheSun {

    private static final float DEFAULT_LATITUDE = 59.945073f;
    private static final float DEFAULT_LONGITUDE = 30.313775f;

    private SunriseSunsetClient sunriseSunsetClient;

    @Autowired
    public SunTimesSource(SunriseSunsetClient sunriseSunsetClient) {
        this.sunriseSunsetClient = sunriseSunsetClient;
    }

    @Override
    public LocalDateTime getZenithTime(LocalDate date) {
        SunriseSunsetResponse solarTimes = sunriseSunsetClient.getSolarTimes(
                DEFAULT_LATITUDE,
                DEFAULT_LONGITUDE,
                date,
                0);
        return solarTimes.getResults().getSolarNoon().toLocalDateTime();
    }
}
