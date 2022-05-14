package com.abderrahimlach.player;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.internal.storage.Storage;
import com.abderrahimlach.utility.PluginUtility;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author AbderrahimLach
 */
public class PlayerManager {

    private final Map<UUID, PlayerData> players = new HashMap<>();
    private final ExecutorService asyncService = Executors.newFixedThreadPool(15, r -> {
        Thread thread = new Thread(r);
        thread.setName("ChatTags - Async Player Thread");
        return thread;
    });
    private final Storage storage;

    public PlayerManager(TagPlugin plugin){
        this.storage = plugin.getStorage();
    }

    public void loadPlayer(UUID uuid, boolean cache) {
        PlayerData playerData = getPlayer(uuid);
        if(playerData == null) {
            asyncService.execute(() -> {
                PlayerData loadedPlayer = storage.loadPlayer(uuid);
                if(cache) {
                    players.put(uuid, loadedPlayer);
                }
            });
        }
    }
    public PlayerData getPlayer(UUID uuid){
        return players.get(uuid);
    }

    public void savePlayer(PlayerData playerData){
        PluginUtility.future(() -> storage.savePlayer(playerData));
    }

    public void savePlayers(){
        Collection<PlayerData> players = getPlayers();
        for(PlayerData playerData : players){
            storage.savePlayer(playerData);
        }
    }

    public void removeAndSave(UUID uuid){
        PlayerData playerData = players.remove(uuid);
        savePlayer(playerData);
    }

    public Collection<PlayerData> getPlayers() {
        return players.values();
    }
}
