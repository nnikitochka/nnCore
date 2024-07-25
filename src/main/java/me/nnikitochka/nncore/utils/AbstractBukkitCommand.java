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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBukkitCommand extends Command {
    public AbstractBukkitCommand(String prefix, String commandLabel) {
        super(commandLabel);
        nnCore.commandMap.register(prefix, this);
    }

    public abstract void toExecute(CommandSender sender, String s, String[] args);

    public boolean execute(CommandSender sender, String label, String[] args) {
        this.toExecute(sender, label, args);
        return true;
    }

    public abstract List<String> complete(CommandSender sender, String[] args);

    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return this.filter(this.complete(sender, args), args);
    }

    private List<String> filter(List<String> list, String[] args) {
        if (list == null) {
            return null;
        } else {
            String last = args[args.length - 1];
            List<String> result = new ArrayList<>();

            for (String arg : list) {
                if (arg.toLowerCase().startsWith(last.toLowerCase())) {
                    result.add(arg);
                }
            }

            return result;
        }
    }
}
