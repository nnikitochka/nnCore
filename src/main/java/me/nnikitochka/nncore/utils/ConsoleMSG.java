//    ███╗   ██╗ ███╗   ██╗   ███████╗ ██████╗  ██╗ ████████╗ ██╗  ██████╗  ███╗   ██╗
//    ████╗  ██║ ████╗  ██║   ██╔════╝ ██╔══██╗ ██║ ╚══██╔══╝ ██║ ██╔═══██╗ ████╗  ██║
//    ██╔██╗ ██║ ██╔██╗ ██║   █████╗   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██╔██╗ ██║
//    ██║╚██╗██║ ██║╚██╗██║   ██╔══╝   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██║╚██╗██║
//    ██║ ╚████║ ██║ ╚████║   ███████╗ ██████╔╝ ██║    ██║    ██║ ╚██████╔╝ ██║ ╚████║
//    ╚═╝  ╚═══╝ ╚═╝  ╚═══╝   ╚══════╝ ╚═════╝  ╚═╝    ╚═╝    ╚═╝  ╚═════╝  ╚═╝  ╚═══╝
//    Связь с разработчиком в любой соц. сети - @nnikitochka
//    Официальное сообщество https://dsc.gg/nnedition


package me.nnikitochka.nncore.utils;

import me.nnikitochka.nncore.nnCore;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ConsoleMSG {
    private static final String dsp = "&#ff6600[DEBUG] ";
    private static final Server server = Bukkit.getServer();
    private static final Logger logger = server.getLogger();

    public static void color(String message) {
        server.getConsoleSender().sendMessage(Parser.color(message));
    }
    public static void info(String message) {
        logger.info(message);
    }
    public static void warning(String message) {
        logger.warning(message);
    }
    public static void fatalError(String message) {
        logger.severe(message);
    }

    public static void enable(JavaPlugin plugin) {
        color("&x&f&d&e&4&0&0"+plugin.getName()+"&f был &x&1&f&f&b&0&0включен&f!");
    }
    public static void disable(JavaPlugin plugin) {
        color("&x&f&d&e&4&0&0"+plugin.getName()+"&f был &x&f&b&0&0&0&0выключен&f!");
    }
    public static void reload(JavaPlugin plugin) {
        color("&x&f&d&e&4&0&0"+plugin.getName()+"&f был &x&1&f&f&b&0&0перезагружен&f!");
    }


    public static void debug(Object message, boolean debug) {
        debug(String.valueOf(message), debug);
    }
    public static void debug(String message, boolean debug) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        if (debug) color(dsp+caller.getClassName()+"."+caller.getMethodName()+": "+message);
    }


    public static void debug(Object message) {
        debug(String.valueOf(message));
    }
    public static void debug(String message) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        if (nnCore.config.is_debug_enabled) color(dsp+caller.getClassName()+"."+caller.getMethodName()+": "+message);
    }
}
