package com.abderrahimlach.api;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.player.PlayerData;
import com.abderrahimlach.player.PlayerManager;
import com.abderrahimlach.tag.TagManager;
import com.abderrahimlach.tag.Tag;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

/**
 * @author AbderrahimLach
 */
public class ChatTagAPI {
    
    @Getter public static ChatTagAPI api;
    
    private final PlayerManager playerManager;
    private final TagManager tagManager;

    public ChatTagAPI(TagPlugin plugin){
        api = this;
        this.playerManager = plugin.getPlayerManager();
        this.tagManager = plugin.getTagManager();
    }

    /**
     * Gets the {@link Tag} which contain the display name and prefix
     *
     * @param name The name of the {@link Tag}
     * @return The {@link Tag}
     */
    public Tag getTag(@NonNull String name) {
        return tagManager.getTag(name);
    }

    /**
     * Gets the {@link Player} is currently equipped {@link Tag}
     *
     * @param player The targeted {@link Player}
     * @return The {@link Player} is currently equipped {@link Tag}
     */
    public Tag getEquippedTag(@NonNull Player player) {
        return getEquippedTag(player.getUniqueId());
    }

    /**
     * Gets the {@link Player} is current equipped {@link Tag} from their uuid
     *
     * @param uuid The targeted uuid of the {@link Player}
     * @return The {@link Player} is current equipped {@link Tag}
     */
    public Tag getEquippedTag(@NonNull UUID uuid) {
        PlayerData playerData = playerManager.getPlayer(uuid);
        return playerData.getEquippedTag();
    }

    /**
     * Gets the {@link Player} is currently owned tags
     *
     * @param player The targeted {@link Player}
     * @return A {@link Collection} of the player's currently owned tags
     */
    public Collection<Tag> getTags(@NonNull Player player) {
        return getTags(player.getUniqueId());
    }

    /**
     * Gets the player's currently owned tags from their uuid
     *
     * @param uuid The targeted uuid of the player
     * @return A {@link Collection} of the player's currently owned tags
     */
    public Collection<Tag> getTags(@NonNull UUID uuid) {
        PlayerData playerData = playerManager.getPlayer(uuid);
        return playerData.getTags().values();
    }

    /**
     * @return The current tags
     */
    public Collection<Tag> getTags() {
        return tagManager.getTags();
    }
}
