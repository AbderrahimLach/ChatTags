package com.abderrahimlach.api.events;

import com.abderrahimlach.api.events.abstracts.AbstractTagEvent;
import com.abderrahimlach.tag.Tag;

/**
 * @author AbderrahimLach
 */
public class TagDeletedEvent extends AbstractTagEvent {

    public TagDeletedEvent(Tag tag){
        super(tag);
    }
}
