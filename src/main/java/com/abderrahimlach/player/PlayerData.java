package com.abderrahimlach.player;

import com.abderrahimlach.tag.Tag;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author AbderrahimLach
 */
@Getter
public class PlayerData {

    private String name;
    private final UUID uuid;
    private final Player player;
    private final Map<String, Tag> tags;
    @Getter private final Set<String> removedTags;
    @Setter private Tag equippedTag;

    public PlayerData(UUID uuid){
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        if(this.player != null){
            this.name = player.getName();
        }
        this.tags = new HashMap<>();
        this.removedTags = new HashSet<>();
    }

    public boolean isTagEquipped() {
        return equippedTag != null;
    }

    public boolean hasTag(String name){
        return tags.containsKey(name);
    }

    public Tag getTag(String name){
        return tags.get(name);
    }

    public void addTag(Tag tag) {
        tags.put(tag.getName(), tag);
    }

    public void removeTag(String name) {
        tags.remove(name);
        this.removedTags.add(name);
    }

    public void removeTag(Tag tag){
        removeTag(tag.getName());
    }
}
