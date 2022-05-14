package com.abderrahimlach.internal.storage;

import com.abderrahimlach.player.PlayerData;
import com.abderrahimlach.tag.Tag;

import java.util.Collection;
import java.util.UUID;

/**
 * @author DirectPlan
 */
public interface StorageRepository extends StorageConnection {

    PlayerData loadPlayer(UUID uuid);

    void savePlayer(PlayerData playerData);

    Collection<Tag> loadTags();

    void deleteTag(Tag tag);

    void saveTags(Collection<Tag> tags);
}
