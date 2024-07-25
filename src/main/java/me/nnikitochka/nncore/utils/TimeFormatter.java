//    ███╗   ██╗ ███╗   ██╗   ███████╗ ██████╗  ██╗ ████████╗ ██╗  ██████╗  ███╗   ██╗
//    ████╗  ██║ ████╗  ██║   ██╔════╝ ██╔══██╗ ██║ ╚══██╔══╝ ██║ ██╔═══██╗ ████╗  ██║
//    ██╔██╗ ██║ ██╔██╗ ██║   █████╗   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██╔██╗ ██║
//    ██║╚██╗██║ ██║╚██╗██║   ██╔══╝   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██║╚██╗██║
//    ██║ ╚████║ ██║ ╚████║   ███████╗ ██████╔╝ ██║    ██║    ██║ ╚██████╔╝ ██║ ╚████║
//    ╚═╝  ╚═══╝ ╚═╝  ╚═══╝   ╚══════╝ ╚═════╝  ╚═╝    ╚═╝    ╚═╝  ╚═════╝  ╚═╝  ╚═══╝
//    Связь с разработчиком в любой соц. сети - @nnikitochka
//    Официальное сообщество https://dsc.gg/nnedition


package me.nnikitochka.nncore.utils;

import me.nnikitochka.nncore.configs.Config;

public class TimeFormatter {
    public String format(Long time) {

        long seconds = time;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;
        long years = months / 12;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;
        days %= 30;
        months %= 12;

        String timeFormat = "";

        if (seconds >= 0)
            timeFormat = seconds + Config.format_seconds + timeFormat;
        if (minutes > 0)
            timeFormat = minutes + Config.format_minutes + timeFormat;
        if (hours > 0)
            timeFormat = hours + Config.format_hours + timeFormat;
        if (days > 0)
            timeFormat = days + Config.format_days + timeFormat;
        if (months > 0)
            timeFormat = months + Config.format_months + timeFormat;
        if (years > 0)
            timeFormat = years + Config.format_years + timeFormat;

        return timeFormat;
    }
}
