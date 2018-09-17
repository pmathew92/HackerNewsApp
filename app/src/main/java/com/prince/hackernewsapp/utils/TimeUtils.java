package com.prince.hackernewsapp.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static String timeConverter(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder stringBuilder = new StringBuilder();
        if (days > 0) {
            stringBuilder.append(days);
            stringBuilder.append(days > 1 ? " days " : " day ");
        }
        if (hours > 0) {
            stringBuilder.append(hours);
            stringBuilder.append(hours > 1 ? " hours " : " hour ");
        }
        if (minutes > 0) {
            stringBuilder.append(minutes);
            stringBuilder.append(minutes > 1 ? " minutes " : " minute ");
        }
        if (seconds > 0) {
            stringBuilder.append(seconds);
            stringBuilder.append(seconds > 1 ? " seconds" : " second");
        }
        return (stringBuilder.toString());
    }
}
