//    ███╗   ██╗ ███╗   ██╗   ███████╗ ██████╗  ██╗ ████████╗ ██╗  ██████╗  ███╗   ██╗
//    ████╗  ██║ ████╗  ██║   ██╔════╝ ██╔══██╗ ██║ ╚══██╔══╝ ██║ ██╔═══██╗ ████╗  ██║
//    ██╔██╗ ██║ ██╔██╗ ██║   █████╗   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██╔██╗ ██║
//    ██║╚██╗██║ ██║╚██╗██║   ██╔══╝   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██║╚██╗██║
//    ██║ ╚████║ ██║ ╚████║   ███████╗ ██████╔╝ ██║    ██║    ██║ ╚██████╔╝ ██║ ╚████║
//    ╚═╝  ╚═══╝ ╚═╝  ╚═══╝   ╚══════╝ ╚═════╝  ╚═╝    ╚═╝    ╚═╝  ╚═════╝  ╚═╝  ╚═══╝
//    Связь с разработчиком в любой соц. сети - @nnikitochka
//    Официальное сообщество https://discord.gg/7wRDxvbbjd


package me.nnikitochka.nncore.blockData;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BlockData implements PersistentDataContainer {

    private static final char[] DEFAULT_PACKAGE = new char[]{'c', 'o', 'm', '.', 'j', 'e', 'f', 'f', '_', 'm', 'e', 'd', 'i', 'a', '.', 'c', 'u', 's', 't', 'o', 'm', 'b', 'l', 'o', 'c', 'k', 'd', 'a', 't', 'a'};

    private static final Set<Map.Entry<UUID, BlockVector>> DIRTY_BLOCKS = new HashSet<>();

    private static final PersistentDataType<?, ?>[] PRIMITIVE_DATA_TYPES = new PersistentDataType<?, ?>[]{
            PersistentDataType.BYTE,
            PersistentDataType.SHORT,
            PersistentDataType.INTEGER,
            PersistentDataType.LONG,
            PersistentDataType.FLOAT,
            PersistentDataType.DOUBLE,
            PersistentDataType.STRING,
            PersistentDataType.BYTE_ARRAY,
            PersistentDataType.INTEGER_ARRAY,
            PersistentDataType.LONG_ARRAY,
            PersistentDataType.TAG_CONTAINER_ARRAY,
            PersistentDataType.TAG_CONTAINER};

    private static final NamespacedKey PERSISTENCE_KEY = Objects.requireNonNull(NamespacedKey.fromString("customblockdata:protected"), "Could not create persistence NamespacedKey");

    private static final Pattern KEY_REGEX = Pattern.compile("^x(\\d+)y(-?\\d+)z(\\d+)$");

    private static final int CHUNK_MIN_XZ = 0;

    private static final int CHUNK_MAX_XZ = (2 << 3) -1;

    private static final boolean HAS_MIN_HEIGHT_METHOD;

    static {
        boolean tmpHasMinHeightMethod = false;
        try {
            World.class.getMethod("getMinHeight");
            tmpHasMinHeightMethod = true;
        } catch (final ReflectiveOperationException ignored) {
        }
        HAS_MIN_HEIGHT_METHOD = tmpHasMinHeightMethod;
    }

    private final PersistentDataContainer pdc;

    private final Chunk chunk;

    private final NamespacedKey key;

    private final Map.Entry<UUID, BlockVector> blockEntry;

    private final Plugin plugin;

    public BlockData(final @NotNull Block block, final @NotNull Plugin plugin) {
        this.chunk = block.getChunk();
        this.key = getKey(plugin, block);
        this.pdc = getPersistentDataContainer();
        this.blockEntry = getBlockEntry(block);
        this.plugin = plugin;
    }

    @Deprecated
    public BlockData(final @NotNull Block block, final @NotNull String namespace) {
        this.chunk = block.getChunk();
        this.key = new NamespacedKey(namespace, getKey(block));
        this.pdc = getPersistentDataContainer();
        this.plugin = JavaPlugin.getProvidingPlugin(BlockData.class);
        this.blockEntry = getBlockEntry(block);
    }

    private static Map.Entry<UUID, BlockVector> getBlockEntry(final @NotNull Block block) {
        final UUID uuid = block.getWorld().getUID();
        final BlockVector blockVector = new BlockVector(block.getX(), block.getY(), block.getZ());
        return new AbstractMap.SimpleEntry<>(uuid, blockVector);
    }

    static boolean isDirty(Block block) {
        return DIRTY_BLOCKS.contains(getBlockEntry(block));
    }

    static void setDirty(Plugin plugin, Map.Entry<UUID, BlockVector> blockEntry) {
        if (!plugin.isEnabled()) //checks if the plugin is disabled to prevent the IllegalPluginAccessException
            return;

        DIRTY_BLOCKS.add(blockEntry);
        Bukkit.getScheduler().runTask(plugin, () -> DIRTY_BLOCKS.remove(blockEntry));
    }

    private static NamespacedKey getKey(Plugin plugin, Block block) {
        return new NamespacedKey(plugin, getKey(block));
    }

    @NotNull
    static String getKey(@NotNull final Block block) {
        final int x = block.getX() & 0x000F;
        final int y = block.getY();
        final int z = block.getZ() & 0x000F;
        return "x" + x + "y" + y + "z" + z;
    }

    @Nullable
    static Block getBlockFromKey(final NamespacedKey key, final Chunk chunk) {
        final Matcher matcher = KEY_REGEX.matcher(key.getKey());
        if (!matcher.matches()) return null;
        final int x = Integer.parseInt(matcher.group(1));
        final int y = Integer.parseInt(matcher.group(2));
        final int z = Integer.parseInt(matcher.group(3));
        if ((x < CHUNK_MIN_XZ || x > CHUNK_MAX_XZ) || (z < CHUNK_MIN_XZ || z > CHUNK_MAX_XZ) || (y < getWorldMinHeight(chunk.getWorld()) || y > chunk.getWorld().getMaxHeight() - 1))
            return null;
        return chunk.getBlock(x, y, z);
    }

    static int getWorldMinHeight(final World world) {
        if (HAS_MIN_HEIGHT_METHOD) {
            return world.getMinHeight();
        } else {
            return 0;
        }
    }

    public static boolean hasCustomBlockData(Block block, Plugin plugin) {
        return block.getChunk().getPersistentDataContainer().has(getKey(plugin, block), PersistentDataType.TAG_CONTAINER);
    }

    public static boolean isProtected(Block block, Plugin plugin) {
        return new BlockData(block, plugin).isProtected();
    }

    public static void registerListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new BlockDataListener(plugin), plugin);
    }

    @NotNull
    public static Set<Block> getBlocksWithCustomData(final Plugin plugin, final Chunk chunk) {
        final NamespacedKey dummy = new NamespacedKey(plugin, "dummy");
        return getBlocksWithCustomData(chunk, dummy);
    }

    @NotNull
    private static Set<Block> getBlocksWithCustomData(final @NotNull Chunk chunk, final @NotNull NamespacedKey namespace) {
        final PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
        return chunkPDC.getKeys().stream().filter(key -> key.getNamespace().equals(namespace.getNamespace()))
                .map(key -> getBlockFromKey(key, chunk))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @NotNull
    public static Set<Block> getBlocksWithCustomData(final String namespace, final Chunk chunk) {
        @SuppressWarnings("deprecation") final NamespacedKey dummy = new NamespacedKey(namespace, "dummy");
        return getBlocksWithCustomData(chunk, dummy);
    }

    public static PersistentDataType<?, ?> getDataType(PersistentDataContainer pdc, NamespacedKey key) {
        for (PersistentDataType<?, ?> dataType : PRIMITIVE_DATA_TYPES) {
            if (pdc.has(key, dataType)) return dataType;
        }
        return null;
    }

    public @Nullable Block getBlock() {
        World world = Bukkit.getWorld(blockEntry.getKey());
        if (world == null) return null;
        BlockVector vector = blockEntry.getValue();
        return world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    @NotNull
    private PersistentDataContainer getPersistentDataContainer() {
        final PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
        final PersistentDataContainer blockPDC = chunkPDC.get(key, PersistentDataType.TAG_CONTAINER);
        if (blockPDC != null) return blockPDC;
        return chunkPDC.getAdapterContext().newPersistentDataContainer();
    }

    public boolean isProtected() {
        return has(PERSISTENCE_KEY, DataType.BOOLEAN);
    }

    public void setProtected(boolean isProtected) {
        if (isProtected) {
            set(PERSISTENCE_KEY, DataType.BOOLEAN, true);
        } else {
            remove(PERSISTENCE_KEY);
        }
    }

    public void clear() {
        pdc.getKeys().forEach(pdc::remove);
        save();
    }

    private void save() {
        setDirty(plugin, blockEntry);
        if (pdc.isEmpty()) {
            chunk.getPersistentDataContainer().remove(key);
        } else {
            chunk.getPersistentDataContainer().set(key, PersistentDataType.TAG_CONTAINER, pdc);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes", "ConstantConditions"})
    public void copyTo(Block block, Plugin plugin) {
        BlockData newCbd = new BlockData(block, plugin);
        getKeys().forEach(key -> {
            PersistentDataType dataType = getDataType(this, key);
            if (dataType == null) return;
            newCbd.set(key, dataType, get(key, dataType));
        });
    }

    @Override
    public <T, Z> void set(final @NotNull NamespacedKey namespacedKey, final @NotNull PersistentDataType<T, Z> persistentDataType, final @NotNull Z z) {
        pdc.set(namespacedKey, persistentDataType, z);
        save();
    }

    @Override
    public <T, Z> boolean has(final @NotNull NamespacedKey namespacedKey, final @NotNull PersistentDataType<T, Z> persistentDataType) {
        return pdc.has(namespacedKey, persistentDataType);
    }

    /* 1.18+
    @Override
    public boolean has(final @NotNull NamespacedKey namespacedKey) {
        for (PersistentDataType<?, ?> type : PRIMITIVE_DATA_TYPES) {
            if (pdc.has(namespacedKey, type)) return true;
        }
        return false;
    }
     */

    @Nullable
    @Override
    public <T, Z> Z get(final @NotNull NamespacedKey namespacedKey, final @NotNull PersistentDataType<T, Z> persistentDataType) {
        return pdc.get(namespacedKey, persistentDataType);
    }

    @NotNull
    @Override
    public <T, Z> Z getOrDefault(final @NotNull NamespacedKey namespacedKey, final @NotNull PersistentDataType<T, Z> persistentDataType, final @NotNull Z z) {
        return pdc.getOrDefault(namespacedKey, persistentDataType, z);
    }

    @NotNull
    @Override
    public Set<NamespacedKey> getKeys() {
        return pdc.getKeys();
    }

    @Override
    public void remove(final @NotNull NamespacedKey namespacedKey) {
        pdc.remove(namespacedKey);
        save();
    }

    @Override
    public boolean isEmpty() {
        return pdc.isEmpty();
    }

    @NotNull
    @Override
    public PersistentDataAdapterContext getAdapterContext() {
        return pdc.getAdapterContext();
    }

    public PersistentDataType<?, ?> getDataType(NamespacedKey key) {
        return getDataType(this, key);
    }

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.METHOD)
    private @interface PaperOnly {
    }

    private static final class DataType {
        private static final PersistentDataType<Byte, Boolean> BOOLEAN = new PersistentDataType<Byte, Boolean>() {
            @NotNull
            @Override
            public Class<Byte> getPrimitiveType() {
                return Byte.class;
            }

            @NotNull
            @Override
            public Class<Boolean> getComplexType() {
                return Boolean.class;
            }

            @NotNull
            @Override
            public Byte toPrimitive(@NotNull Boolean complex, @NotNull PersistentDataAdapterContext context) {
                return complex ? (byte) 1 : (byte) 0;
            }

            @NotNull
            @Override
            public Boolean fromPrimitive(@NotNull Byte primitive, @NotNull PersistentDataAdapterContext context) {
                return primitive == (byte) 1;
            }
        };
    }
}

