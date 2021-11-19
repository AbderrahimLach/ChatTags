package com.abderrahimlach.data.connection;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.config.ConfigKeys;
import com.abderrahimlach.data.PlayerData;
import com.abderrahimlach.data.connection.impl.MongoDBConnection;
import com.abderrahimlach.data.connection.impl.MySQLConnection;
import com.abderrahimlach.data.connection.misc.ConnectionCredentials;
import com.abderrahimlach.data.connection.misc.StorageMethod;
import com.abderrahimlach.management.TagManager;
import com.abderrahimlach.tag.Tag;
import com.abderrahimlach.utility.Util;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * @author AbderrahimLach
 */
public class Storage {

    private StorageRepository<?> connectionRepository;

    public Storage(TagPlugin plugin){
        String host = ConfigKeys.STORAGE_HOSTNAME.getString();
        String username = ConfigKeys.STORAGE_USERNAME.getString();
        String password = ConfigKeys.STORAGE_PASSWORD.getString();
        String database = ConfigKeys.STORAGE_DATABASE.getString();
        int maximumPoolSize = ConfigKeys.STORAGE_MAXIMUM_POOL_SIZE.getInteger();

        StorageMethod storageMethod = StorageMethod.valueOf(ConfigKeys.STORAGE_METHOD.getString().toUpperCase());

        int port = storageMethod.getDefaultPort();
        ConnectionCredentials credentials = new ConnectionCredentials(host, username, password, database, port, maximumPoolSize);

        switch (storageMethod) {
            case MYSQL: {
                connectionRepository = new MySQLConnection(plugin, credentials);
                break;
            }
            case MONGODB: {
                connectionRepository = new MongoDBConnection(plugin, credentials);
                break;
            }
            default: {
                plugin.getLogger().log(Level.SEVERE, "Invalid storage method provided in config.yml. Disabling plugin");
                plugin.getPluginLoader().disablePlugin(plugin);
            }
        }
    }

    public String getName(){
        return connectionRepository.getName();
    }

    public void connect(){
        connectionRepository.connect();
    }

    public void close(){
        connectionRepository.close();
    }

    public CompletableFuture<PlayerData> loadPlayer(UUID uuid){
        return Util.future(() -> this.connectionRepository.loadPlayer(uuid));
    }

    public void savePlayer(PlayerData playerData){
        this.connectionRepository.savePlayer(playerData);
    }

    public void loadTags(TagManager tagManager){
        this.connectionRepository.loadTags(tagManager);
    }

    public void deleteTag(Tag tag) {
        Util.future(() -> this.connectionRepository.deleteTag(tag));
    }

    public void saveTags(TagManager tagManager){
        this.connectionRepository.saveTags(tagManager);
    }

}