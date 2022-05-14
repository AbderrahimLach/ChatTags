package com.abderrahimlach.internal.storage;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.internal.config.ConfigKeys;
import com.abderrahimlach.internal.storage.impl.MongoStorage;
import com.abderrahimlach.internal.storage.impl.SQLStorage;
import com.abderrahimlach.internal.storage.misc.ConnectionCredentials;
import com.abderrahimlach.internal.storage.misc.StorageMethod;
import com.abderrahimlach.player.PlayerData;
import com.abderrahimlach.tag.Tag;
import com.abderrahimlach.utility.PluginUtility;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * @author DirectPlan
 */

public class Storage {

    private StorageRepository repository;
    private final JavaPlugin plugin;

    public Storage(TagPlugin plugin) {
        this.plugin = plugin;
        String host = ConfigKeys.STORAGE_HOSTNAME.getValue();
        String username = ConfigKeys.STORAGE_USERNAME.getValue();
        String password = ConfigKeys.STORAGE_PASSWORD.getValue();
        String database = ConfigKeys.STORAGE_DATABASE.getValue();
        int maximumPoolSize = ConfigKeys.STORAGE_MAXIMUM_POOL_SIZE.getInteger();

        StorageMethod storageMethod = StorageMethod.valueOf(ConfigKeys.STORAGE_METHOD.getValue().toUpperCase());

        int port = storageMethod.getPort();
        ConnectionCredentials credentials = new ConnectionCredentials(host, username, password, database, port, maximumPoolSize);

        switch (storageMethod) {
            case MYSQL: {
                repository = new SQLStorage(plugin, credentials);
                break;
            }
            case MONGODB: {
                repository = new MongoStorage(plugin, credentials);
                break;
            }
            default: {
                plugin.getLogger().log(Level.SEVERE, "Invalid storage method provided in config.yml. Disabling plugin");
                plugin.getPluginLoader().disablePlugin(plugin);
            }
        }

    }

    public void connect() {
        plugin.getLogger().log(Level.WARNING, "Connecting to the "+repository.getName()+" server...");
        repository.connect();
    }

    public void close() {
        repository.close();
    }

    public void savePlayer(PlayerData playerData){
        repository.savePlayer(playerData);
    }

    public Collection<Tag> loadTags(){
        return repository.loadTags();
    }

    public void deleteTag(Tag tag) {
        repository.deleteTag(tag);
    }

    public void saveTags(Collection<Tag> tags){
        repository.saveTags(tags);
    }

    public PlayerData loadPlayer(UUID uuid){
        return repository.loadPlayer(uuid);
    }
}