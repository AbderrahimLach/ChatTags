package com.abderrahimlach.management;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.api.events.TagCreatedEvent;
import com.abderrahimlach.api.events.TagDeletedEvent;
import com.abderrahimlach.api.events.TagModifiedEvent;
import com.abderrahimlach.data.cache.AbstractSimpleMapCache;
import com.abderrahimlach.data.connection.Storage;
import com.abderrahimlach.tag.Tag;

import java.util.Collection;

/**
 * @author AbderrahimLach
 */
public class TagManager extends AbstractSimpleMapCache<String, Tag> {

    private final Storage storage;
    private final PlayerManager playerManager;

    public TagManager(TagPlugin plugin){
        this.storage = plugin.getStorage();
        this.playerManager = plugin.getPlayerManager();
        this.storage.loadTags(this);
    }

    /**
     * @return true if the tag didn't exist and is successfully created, else false
     */
    public boolean createTag(String name) {
        if(containsTag(name)) return false;
        Tag tag = new Tag(name);
        new TagDeletedEvent(tag).call();
        this.addObject(name, tag);
        return true;
    }

    public void createTag(String name, String prefix, String displayName){
        if(containsTag(name)) return;
        Tag tag = new Tag(name, prefix, displayName);
        new TagCreatedEvent(tag).call();
        this.addObject(name, tag);
    }

    public Tag deleteTag(String name){
        Tag tag = removeObject(name);
        if(tag != null) {
            new TagDeletedEvent(tag).call();
            this.storage.deleteTag(tag);
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
        return get(name);
    }

    public boolean containsTag(String name){
        return containsKey(name);
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

    public void saveTags(){
        this.storage.saveTags(this);
    }

    public Collection<Tag> getTags(){
        return values();
    }
}
