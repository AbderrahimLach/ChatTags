package com.abderrahimlach.api.events;

import com.abderrahimlach.api.events.abstracts.AbstractPlayerTagEvent;
import com.abderrahimlach.data.PlayerData;
import com.abderrahimlach.tag.Tag;

/**
 * @author AbderrahimLach
 */
public class PlayerEquippedTagEvent extends AbstractPlayerTagEvent {

    public PlayerEquippedTagEvent(PlayerData playerData, Tag tag){
        super(playerData, tag);
    }
}
