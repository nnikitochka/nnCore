//    ███╗   ██╗ ███╗   ██╗   ███████╗ ██████╗  ██╗ ████████╗ ██╗  ██████╗  ███╗   ██╗
//    ████╗  ██║ ████╗  ██║   ██╔════╝ ██╔══██╗ ██║ ╚══██╔══╝ ██║ ██╔═══██╗ ████╗  ██║
//    ██╔██╗ ██║ ██╔██╗ ██║   █████╗   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██╔██╗ ██║
//    ██║╚██╗██║ ██║╚██╗██║   ██╔══╝   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██║╚██╗██║
//    ██║ ╚████║ ██║ ╚████║   ███████╗ ██████╔╝ ██║    ██║    ██║ ╚██████╔╝ ██║ ╚████║
//    ╚═╝  ╚═══╝ ╚═╝  ╚═══╝   ╚══════╝ ╚═════╝  ╚═╝    ╚═╝    ╚═╝  ╚═════╝  ╚═╝  ╚═══╝
//    Связь с разработчиком в любой соц. сети - @nnikitochka
//    Официальное сообщество https://discord.gg/7wRDxvbbjd


package me.nnikitochka.nncore.blockData;

import me.nnikitochka.nncore.blockData.events.BlockDataMoveEvent;
import me.nnikitochka.nncore.blockData.events.BlockDataRemoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.Predicate;

final class BlockDataListener implements Listener {
    private final Plugin plugin;
    private final Predicate<Block> customDataPredicate;

    public BlockDataListener(Plugin plugin) {
        this.plugin = plugin;
        this.customDataPredicate = block -> BlockData.hasCustomBlockData(block, plugin);
    }

    private BlockData getCbd(BlockEvent event) {
        return getCbd(event.getBlock());
    }

    private BlockData getCbd(Block block) {
        return new BlockData(block, plugin);
    }

    private void callAndRemove(BlockEvent blockEvent) {
        if (callEvent(blockEvent)) {
            getCbd(blockEvent).clear();
        }
    }

    private boolean callEvent(BlockEvent blockEvent) {
        return callEvent(blockEvent.getBlock(), blockEvent);
    }

    private boolean callEvent(Block block, Event bukkitEvent) {
        if(!BlockData.hasCustomBlockData(block, plugin) || BlockData.isProtected(block, plugin)) {
            return false;
        }

        BlockDataRemoveEvent cbdEvent = new BlockDataRemoveEvent(plugin, block, bukkitEvent);
        Bukkit.getPluginManager().callEvent(cbdEvent);

        return !cbdEvent.isCancelled();
    }

    private void callAndRemoveBlockStateList(List<BlockState> blockStates, Event bukkitEvent) {
        blockStates.stream()
                .map(BlockState::getBlock)
                .filter(customDataPredicate)
                .forEach(block -> callAndRemove(block,bukkitEvent));
    }

    private void callAndRemoveBlockList(List<Block> blocks, Event bukkitEvent) {
        blocks.stream()
                .filter(customDataPredicate)
                .forEach(block -> callAndRemove(block,bukkitEvent));
    }

    private void callAndRemove(Block block, Event bukkitEvent) {
        if (callEvent(block, bukkitEvent)) {
            getCbd(block).clear();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        callAndRemove(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if(!BlockData.isDirty(event.getBlock())) {
            callAndRemove(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntity(EntityChangeBlockEvent event) {
        if(event.getTo() != event.getBlock().getType()) {
            callAndRemove(event.getBlock(), event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExplode(BlockExplodeEvent event) {
        callAndRemoveBlockList(event.blockList(),event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        callAndRemoveBlockList(event.blockList(), event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        callAndRemove(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPiston(BlockPistonExtendEvent event) {
        onPiston(event.getBlocks(), event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPiston(BlockPistonRetractEvent event) {
        onPiston(event.getBlocks(), event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFade(BlockFadeEvent event) {
        if(event.getBlock().getType() == Material.FIRE) return;
        if(event.getNewState().getType() != event.getBlock().getType()) {
            callAndRemove(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStructure(StructureGrowEvent event) {
        callAndRemoveBlockStateList(event.getBlocks(), event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFertilize(BlockFertilizeEvent event) {
        callAndRemoveBlockStateList(event.getBlocks(), event);
    }

    private void onPiston(List<Block> blocks, BlockPistonEvent bukkitEvent) {
        Map<Block, BlockData> map = new LinkedHashMap<>();
        BlockFace direction = bukkitEvent.getDirection();
        blocks.stream().filter(customDataPredicate).forEach(block -> {
            BlockData cbd = new BlockData(block, plugin);
            if(cbd.isEmpty() || cbd.isProtected()) return;
            Block destinationBlock = block.getRelative(direction);
            BlockDataMoveEvent moveEvent = new BlockDataMoveEvent(plugin, block, destinationBlock, bukkitEvent);
            Bukkit.getPluginManager().callEvent(moveEvent);
            if (moveEvent.isCancelled()) return;
            map.put(destinationBlock, cbd);
        });
        Utils.reverse(map).forEach((block, cbd) -> {
            cbd.copyTo(block, plugin);
            cbd.clear();
        });
    }

    private static final class Utils {

        private static <K, V> Map<K, V> reverse(Map<K, V> map) {
            LinkedHashMap<K, V> reversed = new LinkedHashMap<>();
            List<K> keys = new ArrayList<>(map.keySet());
            Collections.reverse(keys);
            keys.forEach((key) -> reversed.put(key, map.get(key)));
            return reversed;
        }

    }

}
