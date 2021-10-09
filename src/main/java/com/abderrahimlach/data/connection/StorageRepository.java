package com.abderrahimlach.data.connection;

import com.abderrahimlach.data.PlayerData;
import com.abderrahimlach.management.TagManager;
import com.abderrahimlach.tag.Tag;

import java.util.UUID;

/**
 * @author AbderrahimLach
 */
public interface StorageRepository<T> extends StorageConnection<T> {

    PlayerData loadPlayer(UUID uuid);

    void savePlayer(PlayerData playerData);

    void loadTags(TagManager tagManager);

    void deleteTag(Tag tag);

    void saveTags(TagManager tagManager);

}
