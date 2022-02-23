package ent.otego.praise.sun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class SunriseSunsetResponse {

    private Result results;

    private String status;

    @Data
    public static class Result {

        private ZonedDateTime sunrise;

        private ZonedDateTime sunset;

        @JsonProperty("solar_noon")
        private ZonedDateTime solarNoon;

        @JsonProperty("day_length")
        private Integer dayLength;

        @JsonProperty("civil_twilight_begin")
        private ZonedDateTime civilTwilightBegin;

        @JsonProperty("civil_twilight_end")
        private ZonedDateTime civilTwilightEnd;

        @JsonProperty("nautical_twilight_begin")
        private ZonedDateTime nauticalTwilightBegin;

        @JsonProperty("nautical_twilight_end")
        private ZonedDateTime nauticalTwilightEnd;

        @JsonProperty("astronomical_twilight_begin")
        private ZonedDateTime astronomicalTwilightBegin;

        @JsonProperty("astronomical_twilight_end")
        private ZonedDateTime astronomicalTwilightEnd;
    }
}
