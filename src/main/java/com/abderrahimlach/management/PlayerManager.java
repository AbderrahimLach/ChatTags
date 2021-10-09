package com.abderrahimlach.management;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.data.PlayerData;
import com.abderrahimlach.data.cache.AbstractSimpleMapCache;
import com.abderrahimlach.data.connection.Storage;
import com.abderrahimlach.utility.Util;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author AbderrahimLach
 */
public class PlayerManager extends AbstractSimpleMapCache<UUID, PlayerData> {

    private final Storage storage;

    public PlayerManager(TagPlugin plugin){
        this.storage = plugin.getStorage();
    }

    public PlayerData getPlayer(UUID uuid){
        PlayerData playerData = get(uuid);
        if(playerData != null) return playerData;

        CompletableFuture<PlayerData> futurePlayerData = storage.loadPlayer(uuid);
        return futurePlayerData.join();
    }

    public void addPlayer(UUID uuid, PlayerData playerData){
        this.addObject(uuid, playerData);
    }

    public void addPlayer(PlayerData playerData){
        this.addPlayer(playerData.getUuid(), playerData);
    }

    public void savePlayer(PlayerData playerData){
        Util.future(() -> storage.savePlayer(playerData));
    }

    public void savePlayers(){
        Collection<PlayerData> players = values();
        for(PlayerData playerData : players){
            storage.savePlayer(playerData);
        }
    }

    public void removeAndSave(UUID uuid){
        PlayerData playerData = removeObject(uuid);
        savePlayer(playerData);
    }

    public Collection<PlayerData> getPlayers() {
        return values();
    }
}
