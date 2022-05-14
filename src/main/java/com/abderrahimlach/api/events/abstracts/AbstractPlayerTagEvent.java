package com.abderrahimlach.api.events.abstracts;

import com.abderrahimlach.player.PlayerData;
import com.abderrahimlach.tag.Tag;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

/**
 * @author AbderrahimLach
 */
@Getter
public abstract class AbstractPlayerTagEvent extends AbstractTagEvent implements Cancellable {

    private final PlayerData playerData;
    @Setter private boolean cancelled;

    public AbstractPlayerTagEvent(PlayerData playerData, Tag tag){
        super(tag);
        this.playerData = playerData;
    }
}
