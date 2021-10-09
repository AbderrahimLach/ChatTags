package com.abderrahimlach.management;

import com.abderrahimlach.data.cache.AbstractSimpleMapCache;
import com.abderrahimlach.tag.inventory.InventoryUI;

import java.util.Map;
import java.util.UUID;

/**
 * @author AbderrahimLach
 */
public class MenuManager extends AbstractSimpleMapCache<UUID, InventoryUI> {

    public InventoryUI getInventory(UUID uuid){
        return get(uuid);
    }

    public void removeInventory(UUID uuid){
        this.removeObject(uuid);
    }

    public void addInventory(UUID uuid, InventoryUI inventoryUI){
        this.addObject(uuid, inventoryUI);
    }

    public Map<UUID, InventoryUI> getInventories(){
        return getObjects();
    }
}
