package ent.otego.praise.schedule;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public interface TheSun {

    ZonedDateTime getZenithTime(LocalDate date, float lat, float lon);
}
