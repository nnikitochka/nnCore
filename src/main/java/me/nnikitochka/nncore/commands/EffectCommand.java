//    ███╗   ██╗ ███╗   ██╗   ███████╗ ██████╗  ██╗ ████████╗ ██╗  ██████╗  ███╗   ██╗
//    ████╗  ██║ ████╗  ██║   ██╔════╝ ██╔══██╗ ██║ ╚══██╔══╝ ██║ ██╔═══██╗ ████╗  ██║
//    ██╔██╗ ██║ ██╔██╗ ██║   █████╗   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██╔██╗ ██║
//    ██║╚██╗██║ ██║╚██╗██║   ██╔══╝   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██║╚██╗██║
//    ██║ ╚████║ ██║ ╚████║   ███████╗ ██████╔╝ ██║    ██║    ██║ ╚██████╔╝ ██║ ╚████║
//    ╚═╝  ╚═══╝ ╚═╝  ╚═══╝   ╚══════╝ ╚═════╝  ╚═╝    ╚═╝    ╚═╝  ╚═════╝  ╚═╝  ╚═══╝
//    Связь с разработчиком в любой соц. сети - @nnikitochka
//    Официальное сообщество https://dsc.gg/nnedition


package me.nnikitochka.nncore.commands;

import me.nnikitochka.nncore.commands.utils.AbstractCommand;
import me.nnikitochka.nncore.utils.ConsoleMSG;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class EffectCommand extends AbstractCommand {
    @Override
    public String getCommandLabel() {
        return "[EFFECT]";
    }

    @Override
    public void perform(Player player, String[] args, String withoutCMD) {
        PotionEffectType effectType = PotionEffectType.getByName(args[1]);
        if (effectType == null) {
            ConsoleMSG.debug("Неверное название эффекта!");
            return;
        }

        int strength = 0;
        int duration = 1;

        for (String arg : args) {
            if (arg.startsWith("-strength:")) {
                strength = Integer.parseInt(arg.replace("-strength:", ""));
            } else if (arg.startsWith("-duration:")) {
                duration = Integer.parseInt(arg.replace("-duration:", ""));
            }
        }

        if (player.hasPotionEffect(effectType)) return;
        player.addPotionEffect(new PotionEffect(effectType, duration*20, strength));
    }

    @Override
    public List<JavaPlugin> getAllowedPlugin() {
        return null;
    }
}
