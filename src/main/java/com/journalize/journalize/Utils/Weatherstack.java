package com.journalize.journalize.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.journalize.journalize.config.AppCacheConfig;
import com.journalize.journalize.constants.Constants;
import com.journalize.journalize.dto.weather.WeatherResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Weatherstack {
    private final WebClient webClient;
    private final AppCacheConfig appCacheConfig;

    // Get 'apiKey' from application.yml
    @Value(Constants.WEATHERSTACK_API_KEY)
    private String apiKey;
    private String url;

    public WeatherResponse.Current getWeather(String city) {
        // Getting 'url' from appCache
        url = appCacheConfig.get(Constants.WEATHERSTACK_API_URL);

        // replace <API_KEY> with apiKey and <CITY> with city
        url = url.replace(Constants.API_KEY, apiKey);
        url = url.replace(Constants.CITY, city);

        var temperatureData = webClient.get().uri(url).retrieve().bodyToMono(WeatherResponse.class).block()
                .getCurrent();

        return temperatureData;
    }
}