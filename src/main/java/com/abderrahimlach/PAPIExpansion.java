package com.abderrahimlach;

import com.abderrahimlach.player.PlayerData;
import com.abderrahimlach.player.PlayerManager;
import com.abderrahimlach.tag.Tag;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * @author AbderrahimLach
 */
public class PAPIExpansion extends PlaceholderExpansion {

    private final TagPlugin plugin;
    private final PlayerManager playerManager;

    public PAPIExpansion(TagPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @Override
    public String getIdentifier() {
        return "chattags";
    }

    @Override
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if(player == null) return "";

        PlayerData playerData = playerManager.getPlayer(player.getUniqueId());
        Tag tag = playerData.getEquippedTag();
        if(tag == null) return "";

        switch (identifier) {
            case "identifier": {
                return tag.getName();
            }
            case "prefix": {
                return tag.getPrefix();
            }
            case "display-name": {
                return tag.getDisplayName();
            }
        }
        return "";
    }

    @Override
    public boolean register() {
        this.plugin.log("PlaceholderAPI found! Registering identifier.");
        return super.register();
    }
}
