package com.abderrahimlach.internal.storage.impl;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.internal.storage.StorageRepository;
import com.abderrahimlach.internal.storage.misc.ConnectionCredentials;
import com.abderrahimlach.player.PlayerData;
import com.abderrahimlach.tag.Tag;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

import java.util.*;

/**
 * @author DirectPlan
 */
@RequiredArgsConstructor
public class MongoStorage implements StorageRepository {

    private final TagPlugin plugin;
    private final ConnectionCredentials credentials;
    private MongoClient mongoClient;
    public MongoDatabase mongoDatabase;

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
    public PlayerData loadPlayer(UUID uuid) {

        PlayerData playerData = new PlayerData(uuid);
        MongoCollection<Document> playerDocument = this.mongoDatabase.getCollection("playerTags");
        try (MongoCursor<Document> cursor = playerDocument.find(Filters.eq("uuid", uuid.toString())).iterator()) {
            if(cursor.hasNext()) {
                Document document = cursor.next();
                if(document.containsKey("tags") && document.get("tags") instanceof List) {
                    List<Document> tags = (List<Document>) document.get("tags");
                    for(Document documentTag : tags) {
                        String tagName = documentTag.getString("tag");
                        Tag tag = plugin.getTagManager().getTag(tagName);
                        if(tag == null) {
                            continue;
                        }
                        boolean equipped = documentTag.getBoolean("equipped");
                        if(equipped) {
                            playerData.setEquippedTag(tag);
                        }
                        playerData.addTag(tag);
                    }
                }
            }
        }
        return playerData;
    }

    @Override
    public void savePlayer(PlayerData playerData) {
        Collection<Tag> playerTags = playerData.getTags().values();
        if(playerTags.isEmpty()) return;
        MongoCollection<Document> playerDocument = this.mongoDatabase.getCollection("playerTags");
        List<Document> documentTags = new ArrayList<>();
        for(Tag tag : playerTags) {
            Document tagDocument = new Document();
            tagDocument.put("tag", tag.getName());
            boolean equipped = playerData.getEquippedTag() == tag;
            tagDocument.put("equipped", equipped);

            documentTags.add(tagDocument);
        }
        UUID uuid = playerData.getUuid();

        Document document = new Document();
        document.put("uuid", uuid.toString());
        document.put("tags", documentTags);

        playerDocument.replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
    }

    @Override
    public Collection<Tag> loadTags() {
        List<Tag> tags = new ArrayList<>();
        MongoCollection<Document> tagsDocument = this.mongoDatabase.getCollection("tags");
        try(MongoCursor<Document> cursor = tagsDocument.find().iterator()) {
            if(cursor.hasNext()) {
                Document document = cursor.next();

                String name = document.getString("name");
                String displayName = document.getString("displayName");
                String prefix = document.getString("prefix");

                tags.add(new Tag(name, prefix, displayName));
            }
        }
        return tags;
    }

    @Override
    public void deleteTag(Tag tag) {
        MongoCollection<Document> tagsDocument = this.mongoDatabase.getCollection("tags");
        tagsDocument.deleteOne(Filters.eq("name", tag.getName()));
    }

    @Override
    public void saveTags(Collection<Tag> tags) {
        if(tags.isEmpty()) return;

        MongoCollection<Document> tagsDocument = this.mongoDatabase.getCollection("tags");
        for(Tag tag : tags) {
            Document document = new Document();
            String name = tag.getName();
            document.put("name", name);
            document.put("displayName", tag.getDisplayName());
            document.put("prefix", tag.getPrefix());
            tagsDocument.replaceOne(Filters.eq("name", name), document, new ReplaceOptions().upsert(true));
        }
    }
}
