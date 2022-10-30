package com.github.inc0grepoz.kvad.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final DateFormat DATE_FMT = new SimpleDateFormat("H:m:s");
    private static final String LOG_FMT = "[%s] %s";

    public static void info(String message) {
        message = String.format(LOG_FMT, time(), message);
        System.out.println(message);
    }

    public static void error(String message) {
        message = String.format(LOG_FMT, time(), message);
        System.err.println(message);
    }

    private static String time() {
        Date date = new Date(System.currentTimeMillis());
        String time = DATE_FMT.format(date);
        return time;
    }

}
