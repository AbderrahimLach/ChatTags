package com.abderrahimlach.api.events.abstracts;

import com.abderrahimlach.tag.Tag;
import com.abderrahimlach.utility.PluginUtility;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author AbderrahimLach
 */
@Getter
public abstract class AbstractTagEvent extends Event {

    public static HandlerList handlerList = new HandlerList();

    private final Tag tag;

    public AbstractTagEvent(Tag tag){
        this.tag = tag;
    }

    public void call(){
        PluginUtility.callEvent(this);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
