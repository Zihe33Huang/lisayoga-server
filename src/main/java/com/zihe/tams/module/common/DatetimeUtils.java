package com.zihe.tams.module.common;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DatetimeUtils {

    private DatetimeUtils() {
    }

    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    private static final ZoneOffset DEFAULT_ZONE_OFFSET = DEFAULT_ZONE.getRules().getOffset(Instant.EPOCH);

    private static final DateTimeFormatter DEFAULT_FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss");

    public static DateTimeFormatter ofPattern(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE).toLocalDateTime();
    }

    public static Date localDateTime2Date(LocalDateTime datetime) {
        return Date.from(datetime.toInstant(DEFAULT_ZONE_OFFSET));
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE).toLocalDate();
    }

    public static Date str2Date(String datetime, String pattern) {
        DateTimeFormatter formatter = ofPattern(pattern);
        LocalDateTime dt = LocalDateTime.parse(datetime, formatter);
        return localDateTime2Date(dt);
    }

    public static Date str2Date(String datetime, DateTimeFormatter formatter) {
        LocalDateTime dt = LocalDateTime.parse(datetime, formatter);
        return localDateTime2Date(dt);
    }

    public static Date str2Date(String datetime) {
        return str2Date(datetime, DEFAULT_FORMATTER);
    }

    public static LocalDateTime str2LocalDateTime(String datetime, DateTimeFormatter formatter) {
        return LocalDateTime.parse(datetime, formatter);
    }

    public static LocalDateTime str2LocalDateTime(String datetime) {
        return str2LocalDateTime(datetime, DEFAULT_FORMATTER);
    }

    public static String date2Str(Date date, DateTimeFormatter formatter) {
        LocalDateTime dt = toLocalDateTime(date);
        return formatter.format(dt);
    }

    public static String date2Str(Date date, String pattern) {
        DateTimeFormatter formatter = ofPattern(pattern);
        return date2Str(date, formatter);
    }

    public static String date2Str(Date date) {
        return date2Str(date, DEFAULT_FORMATTER);
    }

    public static Date fromUnixTime(long unixTime) {
        return Date.from(Instant.ofEpochSecond(unixTime));
    }

    public static long toUnixTime(Date date) {
        return date.toInstant().getEpochSecond();
    }

    public static LocalDateTime startOfDay(Date date) {
        return LocalDateTime.of(toLocalDate(date), LocalTime.MIN);
    }

    public static LocalDateTime endOfDay(Date date) {
        return LocalDateTime.of(toLocalDate(date), LocalTime.MAX);
    }

    public static Date datePlus(Date date, long amountToAdd, TimeUnit unit) {
        return Date.from(Instant.ofEpochMilli(date.getTime() + unit.toMillis(amountToAdd)));
    }

    public static LocalDateTime plus(Date date, long amountToAdd, TimeUnit unit) {
        LocalDateTime lt = toLocalDateTime(date);
        return lt.plus(unit.toMillis(amountToAdd), ChronoUnit.MILLIS);
    }

}
