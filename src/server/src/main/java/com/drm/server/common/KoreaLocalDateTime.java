package com.drm.server.common;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class KoreaLocalDateTime {

    public static LocalDate stringToLocalDateTime(String date)  {
        LocalDate formDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        return formDate;
    }
    public static LocalDateTime datTimeListToLocalDateTime(List<Integer> dateTimeList){
        int year = dateTimeList.get(0);
        int month = dateTimeList.get(1);
        int day = dateTimeList.get(2);
        int hour = dateTimeList.get(3);
        int minute = dateTimeList.get(4);
        int second = dateTimeList.get(5);
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        return localDateTime;
    }
}
