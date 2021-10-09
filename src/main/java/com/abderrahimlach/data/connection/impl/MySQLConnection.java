package com.abderrahimlach.data.connection.impl;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.data.PlayerData;
import com.abderrahimlach.data.connection.impl.handlers.MySQLConnectionHandler;
import com.abderrahimlach.data.connection.misc.ConnectionCredentials;
import com.abderrahimlach.management.TagManager;
import com.abderrahimlach.tag.Tag;

import java.sql.*;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * @author AbderrahimLach
 */
public class MySQLConnection extends MySQLConnectionHandler {

    public MySQLConnection(TagPlugin plugin, ConnectionCredentials credentials){
        super(plugin, credentials);
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
    public void loadTags(TagManager tagManager) {
        requestConnection(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags")) {
                try (ResultSet set = statement.executeQuery()){
                    while(set.next()){
                        String name = set.getString("name");
                        String displayName = set.getString("displayName"); // CHANGE THIS
                        String prefix = set.getString("prefix");
                        tagManager.createTag(name, prefix, displayName);
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        });
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
    public void saveTags(TagManager tagManager) {
        Collection<Tag> tags = tagManager.getTags();
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
}
