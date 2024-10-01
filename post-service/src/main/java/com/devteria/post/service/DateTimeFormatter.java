package com.devteria.post.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DateTimeFormatter {

    Map<Long, Function<Instant, String>> strategyMap = new LinkedHashMap<>(
    );
    private String formatInSeconds(Instant instant) {
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        return elapseSeconds + " seconds";
    }
    public DateTimeFormatter() {
        strategyMap.put(60L, this::formatInSeconds);
        strategyMap.put(3600L, this::formatInMinutes);
        strategyMap.put(8640L, this::formatInHours);
        strategyMap.put(Long.MAX_VALUE, this::formatInDate);
    }

    private String formatInMinutes(Instant instant) {
        long elapseMinutes = ChronoUnit.MINUTES.between(instant, Instant.now());
        return elapseMinutes + " minutes";
    }

    private String formatInHours(Instant instant) {
        long elapseHours = ChronoUnit.HOURS.between(instant, Instant.now());
        return elapseHours + " hours";
    }

    public String formatInDate(Instant instant) {
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ISO_DATE;
        return localDateTime.format(formatter);
    }
    public String formatDate(Instant instant) {
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
       var strategy = strategyMap.entrySet().stream().filter(
                longFunctionEntry ->
                {
                    return elapseSeconds < longFunctionEntry.getKey();
                }
        ).findFirst().get();
        return strategy.getValue().apply(instant);

    }
}
