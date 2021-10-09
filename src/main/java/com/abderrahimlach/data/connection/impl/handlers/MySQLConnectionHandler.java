package com.abderrahimlach.data.connection.impl.handlers;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.data.connection.StorageRepository;
import com.abderrahimlach.data.connection.misc.ConnectionCredentials;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * @author AbderrahimLach
 */
public abstract class MySQLConnectionHandler implements StorageRepository<Connection> {

    public final TagPlugin plugin;
    private final ConnectionCredentials credentials;
    private HikariDataSource hikari;

    public MySQLConnectionHandler(TagPlugin plugin, ConnectionCredentials credentials) {
        this.plugin = plugin;
        this.credentials = credentials;
    }

    @Override
    public String getName() {
        return "MySQL";
    }

    @Override
    public void connect() {

        HikariConfig config = new HikariConfig();

        config.setPoolName("ChatTags");

        config.setMaximumPoolSize(credentials.getPoolSize());
        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        Properties properties = new Properties();
        properties.put("serverName", credentials.getHost());
        properties.put("port", credentials.getPort());
        properties.put("databaseName", credentials.getDatabase());
        properties.put("user", credentials.getUsername());
        properties.put("password", credentials.getPassword());
        config.setDataSourceProperties(properties);

        this.hikari = new HikariDataSource(config);
        initTables();
    }

    @Override
    public void close() {
        this.hikari.close();
    }

    public void initTables(){
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

    public void requestConnection(Consumer<Connection> request){
        try (Connection connection = this.getConnection()){
            request.accept(connection);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public Connection getConnection() {
        try {
            return hikari.getConnection();
        } catch (SQLException e) {
            plugin.log("An error occurred while pooling connection: "+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
