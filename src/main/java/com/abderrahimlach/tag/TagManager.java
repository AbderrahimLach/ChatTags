package com.abderrahimlach.tag;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.api.events.TagCreatedEvent;
import com.abderrahimlach.api.events.TagDeletedEvent;
import com.abderrahimlach.api.events.TagModifiedEvent;
import com.abderrahimlach.internal.storage.Storage;
import com.abderrahimlach.player.PlayerManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AbderrahimLach
 */
public class TagManager {

    private final Map<String, Tag> tags = new HashMap<>();

    private final Storage storage;
    private final PlayerManager playerManager;

    public TagManager(TagPlugin plugin){
        storage = plugin.getStorage();
        playerManager = plugin.getPlayerManager();
        storage.loadTags().forEach(tag -> tags.put(tag.getName(), tag));
    }

    public boolean createTag(String name) {
        if(containsTag(name)) return false;
        Tag tag = new Tag(name);
        new TagDeletedEvent(tag).call();
        tags.put(name, tag);
        return true;
    }

    public void createTag(String name, String prefix, String displayName){
        if(containsTag(name)) return;
        Tag tag = new Tag(name, prefix, displayName);
        new TagCreatedEvent(tag).call();
        tags.put(name, tag);
    }

    public Tag deleteTag(String name){
        Tag tag = tags.remove(name);
        if(tag != null) {
            new TagDeletedEvent(tag).call();
            storage.deleteTag(tag);
            playerManager.getPlayers().stream().filter(playerData -> playerData.getTags().containsKey(tag.getName())).
                    forEach(playerData -> {
                        if(playerData.getEquippedTag() == tag) {
                            playerData.setEquippedTag(null);
                        }
                        playerData.removeTag(tag);
                    });
        }
        return tag;
    }

    public Tag getTag(String name){
        return tags.get(name);
    }

    public boolean containsTag(String name){
        return tags.containsKey(name);
    }

    public void setPrefix(Tag tag, String prefix){
        TagModifiedEvent tagEvent = new TagModifiedEvent(tag);
        tagEvent.call();
        tag.setPrefix(prefix);
    }

    public void setDisplayName(Tag tag, String displayName){
        TagModifiedEvent tagEvent = new TagModifiedEvent(tag);
        tagEvent.call();
        tag.setDisplayName(displayName);
    }

    public void setPrefix(String tagName, String prefix) {
        Tag tag = getTag(tagName);
        if(tag == null) return;
        setPrefix(tag, prefix);
    }

    public void setDisplayName(String tagName, String displayName) {
        Tag tag = getTag(tagName);
        if(tag == null) return;
        setDisplayName(tag, displayName);
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    public void saveTags(){
        this.storage.saveTags(this.getTags());
    }

    public Collection<Tag> getTags(){
        return tags.values();
    }
}
