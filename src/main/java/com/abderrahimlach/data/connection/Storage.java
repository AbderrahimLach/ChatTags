package com.abderrahimlach.data.connection;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.config.ConfigurationAdapter;
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

/**
 * @author AbderrahimLach
 */
public class Storage {

    private final TagPlugin plugin;
    private StorageRepository<?> connectionRepository;

    public Storage(TagPlugin plugin, ConfigurationAdapter configuration){
        this.plugin = plugin;
        loadStorageMethod(configuration);
    }


    private void loadStorageMethod(ConfigurationAdapter config){
        String host = config.getString("storage.host");
        String username = config.getString("storage.username");
        String password = config.getString("storage.password");
        String database = config.getString("storage.database");
        int maximumPoolSize = config.getInteger("storage.maximum-pool-size");

        String storageMethodString = config.getString("storage-method");
        StorageMethod storageMethod = StorageMethod.valueOf(storageMethodString.toUpperCase());
        int port = storageMethod.getDefaultPort();
        if(config.contains("storage.port")) {
            port = config.getInteger("storage.port");
        }
        ConnectionCredentials credentials = new ConnectionCredentials(host, username, password, database, port, maximumPoolSize);

        switch (storageMethod) {
            case MYSQL: {
                this.connectionRepository = new MySQLConnection(this.plugin, credentials);
                return;
            }
            case MONGODB: {
                this.connectionRepository = new MongoDBConnection(this.plugin, credentials);
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