//    ███╗   ██╗ ███╗   ██╗   ███████╗ ██████╗  ██╗ ████████╗ ██╗  ██████╗  ███╗   ██╗
//    ████╗  ██║ ████╗  ██║   ██╔════╝ ██╔══██╗ ██║ ╚══██╔══╝ ██║ ██╔═══██╗ ████╗  ██║
//    ██╔██╗ ██║ ██╔██╗ ██║   █████╗   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██╔██╗ ██║
//    ██║╚██╗██║ ██║╚██╗██║   ██╔══╝   ██║  ██║ ██║    ██║    ██║ ██║   ██║ ██║╚██╗██║
//    ██║ ╚████║ ██║ ╚████║   ███████╗ ██████╔╝ ██║    ██║    ██║ ╚██████╔╝ ██║ ╚████║
//    ╚═╝  ╚═══╝ ╚═╝  ╚═══╝   ╚══════╝ ╚═════╝  ╚═╝    ╚═╝    ╚═╝  ╚═════╝  ╚═╝  ╚═══╝
//    Связь с разработчиком в любой соц. сети - @nnikitochka
//    Официальное сообщество https://dsc.gg/nnedition


package me.nnikitochka.nncore.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    ItemStack item;
    ItemMeta itemMeta;
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.itemMeta = this.item.getItemMeta();
    }


    public ItemBuilder addPersistentValue(Plugin plugin, String key, PersistentDataType persistentDataType, Object value) {
        PersistentDataContainer container = this.itemMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, key), persistentDataType, value);
        return this;
    }

    public ItemBuilder removePersistentValue(Plugin plugin, String key) {
        this.itemMeta.getPersistentDataContainer().remove(new NamespacedKey(plugin, key));
        return this;
    }

    public ItemBuilder setAmount(int value) {
        this.item.setAmount(value);
        return this;
    }

    public ItemBuilder setName(String name) {
        this.itemMeta.setDisplayName(Parser.color(name));
        return this;
    }

    public ItemBuilder addLore(String loreLine) {
        this.itemMeta.getLore().add(Parser.color(loreLine));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {

        List<String> colLore = new ArrayList<>();
        for (String line : lore)
            colLore.add(Parser.color(line));

        this.itemMeta.setLore(colLore);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        this.itemMeta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder setCustomModelData(int value) {
        this.itemMeta.setCustomModelData(value);
        return this;
    }

    public ItemBuilder hideEnchantment() {
        this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.itemMeta);
        return this.item;
    }
}
