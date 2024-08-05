//    ███╗   ██╗ ███╗   ██╗   ███████╗ ██████╗  ██╗ ████████╗ ██╗  ██████╗  ███╗   ██╗
//    ████╗  ██║ ████╗  ██║   ██╔════╝ ██╔══██╗ ██║ ╚══██╔══╝ ██║ ██╔═══██╗ ████╗  ██║
//    ██╔██╗ ██║ ██╔██╗ ██║   █████╗   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██╔██╗ ██║
//    ██║╚██╗██║ ██║╚██╗██║   ██╔══╝   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██║╚██╗██║
//    ██║ ╚████║ ██║ ╚████║   ███████╗ ██████╔╝ ██║    ██║    ██║ ╚██████╔╝ ██║ ╚████║
//    ╚═╝  ╚═══╝ ╚═╝  ╚═══╝   ╚══════╝ ╚═════╝  ╚═╝    ╚═╝    ╚═╝  ╚═════╝  ╚═╝  ╚═══╝
//    Связь с разработчиком в любой соц. сети - @nnikitochka
//    Официальное сообщество https://dsc.gg/nnedition


package me.nnikitochka.nncore.utils;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

import java.util.List;

public abstract class AbstractListenersHandler {
    public final Plugin plugin;
    public final PluginManager pm;
    public AbstractListenersHandler(Plugin plugin) {
        this.plugin = plugin;
        this.pm = plugin.getServer().getPluginManager();
    }
    public AbstractListenersHandler(Plugin plugin, PluginManager pm) {
        this.plugin = plugin;
        this.pm = pm;
    }

    public abstract void register();

    public void unregisterAll() {
        HandlerList.unregisterAll(this.plugin);
    }

    public List<RegisteredListener> getListeners() {
        return HandlerList.getRegisteredListeners(this.plugin);
    }
}
