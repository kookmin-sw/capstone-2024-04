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
    // 입력 날짜 형식: "2024-05-12 13:58:28.534095"
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    // 출력 날짜 형식: "2023-05-16T13:34:56.000"
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static String convertToKsqlTimestamp(String dateStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, INPUT_FORMATTER);
        return dateTime.format(OUTPUT_FORMATTER);
    }

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
