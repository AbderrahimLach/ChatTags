package com.abderrahimlach.internal.storage.impl;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.internal.storage.StorageRepository;
import com.abderrahimlach.internal.storage.misc.ConnectionCredentials;
import com.abderrahimlach.player.PlayerData;
import com.abderrahimlach.tag.Tag;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author DirectPlan
 */
@RequiredArgsConstructor
public class SQLStorage implements StorageRepository {

    private final TagPlugin plugin;
    private final ConnectionCredentials credentials;
    private HikariDataSource hikariDataSource;

    @Override
    public String getName() {
        return "MySQL";
    }

    @Override
    public void connect() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName("ChatTags - MySQL Pool");

        hikariConfig.setMaximumPoolSize(credentials.getMaximumPoolSize());

        hikariConfig.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        Properties properties = new Properties();
        properties.put("serverName", credentials.getHost());
        properties.put("port", credentials.getPort());
        properties.put("databaseName", credentials.getDatabase());
        properties.put("user", credentials.getUsername());
        properties.put("password", credentials.getPassword());
        hikariConfig.setDataSourceProperties(properties);
        hikariDataSource = new HikariDataSource(hikariConfig);

        checkTable();
    }

    @Override
    public void close() {
        hikariDataSource.close();
    }

    @Override
    public PlayerData loadPlayer(UUID uuid) {
        PlayerData playerData = new PlayerData(uuid);
        requestConnection(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM playerTags WHERE uuid = ?")){
                preparedStatement.setString(1, uuid.toString());
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while(rs.next()){
                        String name = rs.getString("tag");
                        Tag tag = plugin.getTagManager().getTag(name);
                        if(tag == null){
                            continue;
                        }
                        boolean equipped = rs.getBoolean("equipped");
                        if(equipped){
                            playerData.setEquippedTag(tag);
                        }
                        playerData.addTag(tag);
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        });
        return playerData;
    }

    @Override
    public void savePlayer(PlayerData playerData) {
        Collection<Tag> tags = playerData.getTags().values();
        Set<String> removedTags = playerData.getRemovedTags();
        if(tags.isEmpty() && removedTags.isEmpty()) return;
        requestConnection(connection -> {
            if(!tags.isEmpty()) {
                try (PreparedStatement ps = connection.prepareStatement("INSERT INTO playerTags(uuid, tag, equipped) VALUES (?,?,?) ON DUPLICATE KEY UPDATE tag = ?, equipped = ?")){
                    for(Tag tag : tags){
                        String tagName = tag.getName();
                        UUID uuid = playerData.getUuid();
                        boolean equipped = (playerData.getEquippedTag() == tag);
                        ps.setString(1, uuid.toString());
                        ps.setString(2, tagName);
                        ps.setBoolean(3, equipped);
                        ps.setString(4, tagName);
                        ps.setBoolean(5, equipped);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(!removedTags.isEmpty()) {
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM playerTags WHERE uuid = ? AND tag = ?")) {
                    UUID uuid = playerData.getUuid();
                    ps.setString(1, uuid.toString());
                    for(String removedTag : removedTags) {
                        ps.setString(2, removedTag);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Collection<Tag> loadTags() {
        List<Tag> tags = new ArrayList<>();
        requestConnection(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags")) {
                try (ResultSet set = statement.executeQuery()){
                    while(set.next()){
                        String name = set.getString("name");
                        String displayName = set.getString("displayName"); // CHANGE THIS
                        String prefix = set.getString("prefix");
                        tags.add(new Tag(name, displayName, prefix));
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        });
        return tags;
    }

    @Override
    public void deleteTag(Tag tag) {
        requestConnection(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tags WHERE name = ?")) {
                ps.setString(1, tag.getName());
                ps.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        });
    }

    @Override
    public void saveTags(Collection<Tag> tags) {
        if(tags.isEmpty()) return;
        requestConnection(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO tags(name, displayName, prefix) VALUES (?,?,?) ON DUPLICATE KEY UPDATE displayName = ?, prefix = ?")) {
                for (Tag tag : tags) {
                    String name = tag.getName();
                    String prefix = tag.getPrefix();
                    String displayName = tag.getDisplayName();
                    ps.setString(1, name);
                    ps.setString(2, displayName);
                    ps.setString(3, prefix);
                    ps.setString(4, displayName);
                    ps.setString(5, prefix);
                    ps.addBatch();
                }
                ps.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void requestConnection(Consumer<Connection> connectionConsumer) {
        try (Connection connection = hikariDataSource.getConnection()) {
            connectionConsumer.accept(connection);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkTable() {
        requestConnection(connection -> {
            try(Statement statement = connection.createStatement()){
                statement.addBatch("CREATE TABLE IF NOT EXISTS tags(name varchar(16), displayName TEXT, prefix TEXT, CONSTRAINT tags_pk PRIMARY KEY (name));");
                statement.addBatch("CREATE TABLE IF NOT EXISTS playerTags(uuid varchar(36), tag varchar(16), equipped BOOLEAN, CONSTRAINT player_pk PRIMARY KEY (uuid, tag));");
                statement.executeBatch();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
