package ent.otego.praise.sun;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

/**
 * <a href="https://sunrise-sunset.org/api">Sunrise Sunset</a>
 */
@FeignClient(name = "sunrise-sunset", url = "https://api.sunrise-sunset.org/json")
public interface SunriseSunsetClient {

    /**
     *
     * @param latitude lat (float): Latitude in decimal degrees. Required.
     * @param longitude lng (float): Longitude in decimal degrees. Required.
     * @param date date (string): Date in YYYY-MM-DD format. Also accepts other date formats and even relative
     *            date formats. If not present, date defaults to current date. Optional.
     * @param formatDate formatted (integer): 0 or 1 (1 is default). Time values in response will be expressed
     *                  following ISO 8601 and day_length will be expressed in seconds. Optional.
     * @return
     */
    @GetMapping()
    SunriseSunsetResponse getSolarTimes(@RequestParam("lat") float latitude,
                                        @RequestParam("lng") float longitude,
                                        @RequestParam("date") LocalDate date,
                                        @RequestParam("formatted") int formatDate);
}
