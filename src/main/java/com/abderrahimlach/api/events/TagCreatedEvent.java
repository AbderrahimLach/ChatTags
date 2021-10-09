package com.abderrahimlach.api.events;

import com.abderrahimlach.api.events.abstracts.AbstractTagEvent;
import com.abderrahimlach.tag.Tag;

/**
 * @author AbderrahimLach
 */
public class TagCreatedEvent extends AbstractTagEvent {

    public TagCreatedEvent(Tag tag){
        super(tag);
    }
}
