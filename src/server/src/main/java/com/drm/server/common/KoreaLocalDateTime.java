package com.drm.server.common;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class KoreaLocalDateTime {

    public static LocalDate stringToLocalDateTime(String date)  {
        LocalDate formDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        return formDate;
    }
}
