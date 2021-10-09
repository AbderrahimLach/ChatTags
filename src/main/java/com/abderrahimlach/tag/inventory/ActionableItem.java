package com.abderrahimlach.tag.inventory;

import org.bukkit.entity.Player;

/**
 * @author AbderrahimLach
 */
public interface ActionableItem {
    void performAction(MenuItem item, Player clicker);
}
