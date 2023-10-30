package com.mpcs.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final String messageTemplate = "[%s][%s] %s: %s\n";

    public static void log(String message) {
        send_message(stackWalker.getCallerClass(),"INFO", message);
    }

    public static void debug(String message) {
        send_message(stackWalker.getCallerClass(),"DEBUG", message);
    }

    public static void warn(String message) {
        send_message(stackWalker.getCallerClass(),"WARN", message);
    }

    public static void error(String message) {
        send_message(stackWalker.getCallerClass(),"ERROR", message);
    }


    private static void send_message(Class caller, String type, String message) {
        System.out.printf(messageTemplate, dateFormat.format(new Date()), caller.getSimpleName(), type, message);

    }
}
