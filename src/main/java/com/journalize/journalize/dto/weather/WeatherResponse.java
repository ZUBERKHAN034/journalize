package com.journalize.journalize.dto.weather;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherResponse {

    private Current current;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Current {

        private Integer temperature;

        private Integer humidity;

        @JsonProperty("weather_descriptions")
        private List<String> weatherDescriptions;
    }
}