package com.abderrahimlach.internal.inventory;

import com.abderrahimlach.player.PlayerData;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.util.function.Function;

/**
 * @author DirectPlan
 */
@Getter
public abstract class PlayerInventoryUI extends InventoryUI {

    private final Player player;
    protected final PlayerData playerData;

    public PlayerInventoryUI(PlayerData playerData) {
        super(null, null, 5); // Default player inventory rows (9 * 5 = 45), so 45 slots
        this.playerData = playerData;
        player = playerData.getPlayer();
    }

    public void onInteract(PlayerInteractEvent event) {
        if(!(event.hasBlock() || event.hasItem())) return;


        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player player = event.getPlayer();
        // We need a slot.
        int slot = player.getInventory().getHeldItemSlot();

        MenuItem item = getItem(slot);
        if(item == null) {
            return;
        }
        event.setCancelled(true);
        if(!item.isClickable()) {
            return;
        }

        item.performAction(item, player);
    }


    public void apply() {
        build(player);
    }

    @Override
    public void updateSlot(int slot, Function<MenuItem, MenuItem> itemFunction) {
        MenuItem item = getItem(slot);
        if(item == null) return;
        setSlot(slot, itemFunction.apply(item));

        buildInventory();
    }

    @Override
    public Inventory buildInventory() {
        PlayerInventory inventory = player.getInventory();

        MenuItem[] buttons = super.getItems();
        for(int i = 0; i < buttons.length; i++) {
            MenuItem button = buttons[i];
            if(button == null) continue;

            inventory.setItem(i, button.getItem());
        }
        return inventory;
    }

    @Override
    public void updateInventory() {
        player.updateInventory();
    }
}