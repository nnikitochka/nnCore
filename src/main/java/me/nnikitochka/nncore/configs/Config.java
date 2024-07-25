//    ███╗   ██╗ ███╗   ██╗   ███████╗ ██████╗  ██╗ ████████╗ ██╗  ██████╗  ███╗   ██╗
//    ████╗  ██║ ████╗  ██║   ██╔════╝ ██╔══██╗ ██║ ╚══██╔══╝ ██║ ██╔═══██╗ ████╗  ██║
//    ██╔██╗ ██║ ██╔██╗ ██║   █████╗   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██╔██╗ ██║
//    ██║╚██╗██║ ██║╚██╗██║   ██╔══╝   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██║╚██╗██║
//    ██║ ╚████║ ██║ ╚████║   ███████╗ ██████╔╝ ██║    ██║    ██║ ╚██████╔╝ ██║ ╚████║
//    ╚═╝  ╚═══╝ ╚═╝  ╚═══╝   ╚══════╝ ╚═════╝  ╚═╝    ╚═╝    ╚═╝  ╚═════╝  ╚═╝  ╚═══╝
//    Связь с разработчиком в любой соц. сети - @nnikitochka
//    Официальное сообщество https://dsc.gg/nnedition


package me.nnikitochka.nncore.configs;

import me.nnikitochka.nncore.utils.AbstractConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config extends AbstractConfig {
    public Config(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public boolean is_debug_enabled;
    public String time_format;

    public static String format_seconds;
    public static String format_minutes;
    public static String format_hours;
    public static String format_days;
    public static String format_months;
    public static String format_years;

    @Override
    protected void loadVariables() {
        is_debug_enabled = config.getBoolean("use-debug", false);
        time_format = config.getString("time-format", "MM/dd/yyyy hh:mm:ss");

        ConfigurationSection format = config.getConfigurationSection("format");
        if (format != null) {
            format_seconds = format.getString("seconds", " сек.");
            format_minutes = format.getString("minutes", " мин. ");
            format_hours = format.getString("hours", " ч. ");
            format_days = format.getString("days", " д. ");
            format_months = format.getString("months", " мес. ");
            format_years = format.getString("years", " лет ");
        }
    }
}
