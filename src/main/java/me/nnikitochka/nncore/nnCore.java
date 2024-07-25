package me.nnikitochka.nncore;

import me.nnikitochka.nncore.bstats.Metrics;
import me.nnikitochka.nncore.commands.utils.AbstractCommand;
import me.nnikitochka.nncore.commands.utils.CommandsUtils;
import me.nnikitochka.nncore.configs.Config;
import me.nnikitochka.nncore.utils.ConsoleMSG;
import me.nnikitochka.nncore.utils.lpAPI;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class nnCore extends JavaPlugin {
    private static nnCore instance;
    public static nnCore getInstance() { return instance; }
    public nnCore() {}

    public static Config config;
    public static CommandMap commandMap;
    public static boolean isLPEnabled;

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = Bukkit.getPluginManager();
        isLPEnabled = pm.isPluginEnabled("LuckPerms");

        config = new Config(getInstance(), "config.yml");
        config.load();

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap)bukkitCommandMap.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        CommandsUtils.registerCommands();

        new Metrics(this, 22776);
        ConsoleMSG.enable(this);
    }

    @Override
    public void onDisable() {
        CommandsUtils.unregisterAll();
        ConsoleMSG.disable(this);
    }

    public void reloadPlugin() {
        CommandsUtils.unregisterAll();
        config.load();
        CommandsUtils.registerCommands();
    }
}
