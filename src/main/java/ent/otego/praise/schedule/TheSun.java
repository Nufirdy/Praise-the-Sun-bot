package ent.otego.praise.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TheSun {

    LocalDateTime getZenithTime(LocalDate date);
}
