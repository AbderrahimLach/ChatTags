package com.abderrahimlach.internal.inventory;

import com.abderrahimlach.utility.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author DirectPlan
 */
@Getter
public class MenuItem {

    private ItemStack item;
    private final ItemBuilder builder;
    private final String displayName;
    @Setter private Object itemKey;
    @Setter private boolean cancelAction = true;
    @Setter private int slot;

    private final ActionableItem action;

    public MenuItem(Material type, String displayName, int durability){
        this(type, displayName, durability, null);
    }

    public MenuItem(Material type, String displayName, ActionableItem action){
        this(type, displayName, 0, action);
    }

    public MenuItem(Material type, String displayName, int durability, ActionableItem action){
        this.builder = new ItemBuilder(type).name(displayName);
        if(durability > 0){
            this.builder.durability(durability);
        }
        this.item = builder.build();
        this.displayName = item.getItemMeta().getDisplayName();
        this.action = action;
    }

    public void setCustomSkullName(String name) {
        this.item = builder.type(Material.SKULL_ITEM).durability(3).skullOwner(name).build();
    }
//    public void setCustomSkullProperty(String value) {
//        ItemStack item = builder.type(Material.SKULL_ITEM).durability(3).build();
//
//        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
//        gameProfile.getProperties().put("textures", new Property("textures", value));
//
//        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
//        Field profileField;
//        try {
//            profileField = skullMeta.getClass().getDeclaredField("profile");
//            profileField.setAccessible(true);
//            profileField.set(skullMeta, gameProfile);
//        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
//            exception.printStackTrace();
//        }
//        item.setItemMeta(skullMeta);
//
//        this.item = item;
//    }

    public List<String> getLore() {
        return item.getItemMeta().getLore();
    }

    public void setAmount(int amount) {
        item = builder.amount(amount).build();
    }
    public void markUnbreakable() {
        item.getItemMeta().spigot().setUnbreakable(true);
    }

    public void setLore(List<String> lore){
        this.item = builder.lore(lore).build();
    }
    public void setDisplayName(String displayName) {
        this.item = builder.name(displayName).build();
    }

    public boolean isClickable(){
        return action != null;
    }

    public void performAction(MenuItem item, Player clicker){
        if(this.action != null){
            this.action.performAction(item, clicker);
        }
    }
}
