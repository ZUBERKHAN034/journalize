package com.journalize.journalize.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.journalize.journalize.cache.AppCache;
import com.journalize.journalize.constants.Constants;
import com.journalize.journalize.dto.weather.WeatherResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class Weatherstack {

    private final RestClient restClient;
    private final AppCache appCache;

    @Value(Constants.WEATHERSTACK_API_KEY)
    private String apiKey;

    public WeatherResponse.Current getWeather(String city) {
        try {
            String url = appCache.get(Constants.WEATHERSTACK_API_URL)
                    .replace(Constants.API_KEY, apiKey)
                    .replace(Constants.CITY, city);

            var response = restClient.get().uri(url).retrieve().toEntity(WeatherResponse.class);

            if (response.getBody() == null) {
                log.error("Weatherstack response missing for city: {}", city);
                return null;
            }

            return response.getBody().getCurrent();

        } catch (Exception ex) {
            log.error("Failed to get weather via Weatherstack", ex);
            return null;
        }

    }
}