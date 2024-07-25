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
import me.nnikitochka.nncore.utils.Parser;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class SoundCommand extends AbstractCommand {
    @Override
    public String getCommandLabel() {
        return "[SOUND]";
    }

    @Override
    public void perform(Player player, String[] args, String withoutCMD) {
        Sound sound = Sound.valueOf(args[1]);
        if (sound == null) {
            ConsoleMSG.debug("Неверное название звука!");
            return;
        }

        float volume = 1F;
        float pitch = 1F;

        for (String arg : args) {
            if (arg.startsWith("-volume:")) {
                volume = Float.parseFloat(arg.replace("-volume:", ""));
            } else if (arg.startsWith("-pitch:")) {
                pitch = Float.parseFloat(arg.replace("-pitch:", ""));
            }
        }

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    @Override
    public List<JavaPlugin> getAllowedPlugin() {
        return null;
    }
}
