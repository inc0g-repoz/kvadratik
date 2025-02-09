package com.github.inc0grepoz.kvad.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final DateFormat DATE_FMT = new SimpleDateFormat("HH:mm:ss");
    private static final String LOG_FMT = "[%s] %s";

    public static void info(Object message) {
        message = String.format(LOG_FMT, time(), message);
        System.out.println(message);
    }

    public static void error(Object message) {
        message = String.format(LOG_FMT, time(), message);
        System.err.println(message);
    }

    private static String time() {
        Date date = new Date(System.currentTimeMillis());
        String time = DATE_FMT.format(date);
        return time;
    }

}
