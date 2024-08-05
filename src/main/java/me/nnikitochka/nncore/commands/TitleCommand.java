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
import me.nnikitochka.nncore.utils.Parser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class TitleCommand extends AbstractCommand {
    @Override
    public String getCommandLabel() {
        return "[TITLE]";
    }

    @Override
    public void perform(Player player, String[] args, String withoutCMD) {
        String title = "";
        String subTitle = "";
        int fadeIn = 1;
        int stay = 3;
        int fadeOut = 1;

        for (String arg : args) {
            if (arg.startsWith("-fadeIn:")) {
                fadeIn = Integer.parseInt(arg.replace("-fadeIn:", ""));
                withoutCMD = withoutCMD.replace(arg, "");
            } else if (arg.startsWith("-stay:")) {
                stay = Integer.parseInt(arg.replace("-stay:", ""));
                withoutCMD = withoutCMD.replace(arg, "");
            } else if (arg.startsWith("-fadeOut:")) {
                fadeOut = Integer.parseInt(arg.replace("-fadeOut:", ""));
                withoutCMD = withoutCMD.replace(arg, "");
            }
        }

        String[] message = Parser.color(withoutCMD).split(";");

        if (message.length >= 1) {
            title = message[0];
            if (message.length >= 2)
                subTitle = message[1];
        }

        player.sendTitle(title, subTitle, fadeIn*20, stay*20, fadeOut*20);
    }

    @Override
    public List<JavaPlugin> getAllowedPlugin() {
        return null;
    }
}
