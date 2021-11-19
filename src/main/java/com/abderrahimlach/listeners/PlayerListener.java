package com.abderrahimlach.listeners;

import com.abderrahimlach.config.ConfigKeys;
import com.abderrahimlach.config.replacement.BracketReplacement;
import com.abderrahimlach.config.replacement.Replacement;
import com.abderrahimlach.data.PlayerData;
import com.abderrahimlach.management.PlayerManager;
import com.abderrahimlach.tag.Tag;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author AbderrahimLach
 */
@Data
public class PlayerListener implements Listener {

    private final PlayerManager playerManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        // Join Logic
        Player player = e.getPlayer();
        PlayerData playerData = this.playerManager.getPlayer(player.getUniqueId());
        playerManager.addPlayer(playerData);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e){

        boolean chatFormatEnabled = ConfigKeys.TAG_FORMAT_ENABLED.getBoolean();
        if(!chatFormatEnabled) return;

        Player player = e.getPlayer();
        PlayerData playerData = this.playerManager.getPlayer(player.getUniqueId());
        Tag equippedTag = playerData.getEquippedTag();
        if(equippedTag == null) return;

        String prefix = equippedTag.getPrefix();
        if(prefix == null) return;

        String format = ConfigKeys.TAG_FORMAT.getString(new Replacement("tag", prefix, new BracketReplacement()));
        e.setFormat(format);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        // Quit Logic
        Player player = e.getPlayer();
        playerManager.removeAndSave(player.getUniqueId());
    }
}
