package com.abderrahimlach.internal.inventory;

import org.bukkit.entity.Player;

/**
 * @author DirectPlan
 */
public interface ActionableItem {
    void performAction(MenuItem item, Player clicker);
}
