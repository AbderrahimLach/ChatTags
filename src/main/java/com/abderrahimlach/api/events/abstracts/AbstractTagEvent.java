package com.abderrahimlach.api.events.abstracts;

import com.abderrahimlach.tag.Tag;
import com.abderrahimlach.utility.Util;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
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
        Util.callEvent(this);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
