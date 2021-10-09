package com.abderrahimlach.api.events;

import com.abderrahimlach.api.events.abstracts.AbstractTagEvent;
import com.abderrahimlach.tag.Tag;

/**
 * @author AbderrahimLach
 */
public class TagModifiedEvent extends AbstractTagEvent {

    public TagModifiedEvent(Tag tag) {
        super(tag);
    }
}
