//    ███╗   ██╗ ███╗   ██╗   ███████╗ ██████╗  ██╗ ████████╗ ██╗  ██████╗  ███╗   ██╗
//    ████╗  ██║ ████╗  ██║   ██╔════╝ ██╔══██╗ ██║ ╚══██╔══╝ ██║ ██╔═══██╗ ████╗  ██║
//    ██╔██╗ ██║ ██╔██╗ ██║   █████╗   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██╔██╗ ██║
//    ██║╚██╗██║ ██║╚██╗██║   ██╔══╝   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██║╚██╗██║
//    ██║ ╚████║ ██║ ╚████║   ███████╗ ██████╔╝ ██║    ██║    ██║ ╚██████╔╝ ██║ ╚████║
//    ╚═╝  ╚═══╝ ╚═╝  ╚═══╝   ╚══════╝ ╚═════╝  ╚═╝    ╚═╝    ╚═╝  ╚═════╝  ╚═╝  ╚═══╝
//    Связь с разработчиком в любой соц. сети - @nnikitochka
//    Официальное сообщество https://discord.gg/7wRDxvbbjd


package me.nnikitochka.nncore.blockData.events;

import me.nnikitochka.nncore.blockData.BlockData;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class BlockDataEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    final @NotNull Plugin plugin;
    final @NotNull Block block;
    final @NotNull BlockData cbd;
    final @NotNull Event bukkitEvent;
    boolean isCancelled = false;

    protected BlockDataEvent(@NotNull Plugin plugin, @NotNull Block block, @NotNull Event bukkitEvent) {
        this.plugin = plugin;
        this.block = block;
        this.bukkitEvent = bukkitEvent;
        this.cbd = new BlockData(block, plugin);
    }

    public @NotNull Block getBlock() {
        return block;
    }

    public @NotNull Event getBukkitEvent() {
        return bukkitEvent;
    }

    public @NotNull BlockData getCustomBlockData() {
        return cbd;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public @NotNull Reason getReason() {
        if (bukkitEvent == null) return Reason.UNKNOWN;
        for (Reason reason : Reason.values()) {
            if (reason == Reason.UNKNOWN) continue;
            if (reason.eventClasses.stream().anyMatch(clazz -> clazz.equals(bukkitEvent.getClass()))) return reason;
        }
        return Reason.UNKNOWN;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public enum Reason {
        BLOCK_BREAK(BlockBreakEvent.class),
        BLOCK_PLACE(BlockPlaceEvent.class, BlockMultiPlaceEvent.class),
        EXPLOSION(EntityExplodeEvent.class, BlockExplodeEvent.class),
        PISTON(BlockPistonExtendEvent.class, BlockPistonRetractEvent.class),
        BURN(BlockBurnEvent.class),
        ENTITY_CHANGE_BLOCK(EntityChangeBlockEvent.class),
        FADE(BlockFadeEvent.class),
        STRUCTURE_GROW(StructureGrowEvent.class),
        FERTILIZE(BlockFertilizeEvent.class),
        @Deprecated
        LEAVES_DECAY(LeavesDecayEvent.class),

        UNKNOWN((Class<? extends Event>) null);

        private final @NotNull List<Class<? extends Event>> eventClasses;

        @SafeVarargs
        Reason(Class<? extends Event>... eventClasses) {
            this.eventClasses = Arrays.asList(eventClasses);
        }

        public @NotNull List<Class<? extends Event>> getApplicableEvents() {
            return this.eventClasses;
        }
    }
}
