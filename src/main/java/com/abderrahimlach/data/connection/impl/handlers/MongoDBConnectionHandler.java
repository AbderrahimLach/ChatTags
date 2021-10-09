package com.abderrahimlach.data.connection.impl.handlers;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.data.connection.StorageRepository;
import com.abderrahimlach.data.connection.misc.ConnectionCredentials;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.Collections;

/**
 * @author AbderrahimLach
 */
public abstract class MongoDBConnectionHandler implements StorageRepository<MongoClient> {

    public final TagPlugin plugin;
    private final ConnectionCredentials credentials;
    private MongoClient mongoClient;
    public MongoDatabase mongoDatabase;

    public MongoDBConnectionHandler(TagPlugin plugin, ConnectionCredentials credentials) {
        this.plugin = plugin;
        this.credentials = credentials;
    }

    @Override
    public String getName() {
        return "MongoDB";
    }

    @Override
    public void connect() {

        ServerAddress serverAddress = new ServerAddress(credentials.getHost(), credentials.getPort());

        String password = credentials.getPassword();
        String database = credentials.getDatabase();

        if(!password.equals("password") && password.length() > 1) {
            MongoCredential mongoCredential = MongoCredential.createCredential(credentials.getUsername(), database, password.toCharArray());
            this.mongoClient = new MongoClient(serverAddress, Collections.singletonList(mongoCredential));
        } else {
            this.mongoClient = new MongoClient(serverAddress);
        }
        this.mongoDatabase = this.mongoClient.getDatabase(database);
    }

    @Override
    public void close() {
        this.mongoClient.close();
    }

    @Override
    public MongoClient getConnection() {
        return mongoClient;
    }
}
