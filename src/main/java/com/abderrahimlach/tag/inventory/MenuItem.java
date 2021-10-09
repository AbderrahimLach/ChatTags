package com.abderrahimlach.tag.inventory;

import com.abderrahimlach.utility.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author AbderrahimLach
 */
public class MenuItem {

    @Getter private ItemStack item;
    private final ItemBuilder builder;
    @Getter private final String displayName;
    @Getter @Setter private String itemKey;
    @Getter private final ActionableItem action;

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

    public void setLore(List<String> lore){
        this.item = this.builder.lore(lore).build();
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
