package com.abderrahimlach.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import com.abderrahimlach.ChatTagsMain;

public class MySQLManager {
	
	private Connection connection;
	private String host, user, pass, database;
	private ChatTagsMain pl = ChatTagsMain.getPlugin(ChatTagsMain.class);
	public MySQLManager(String host, String user, String pass, String database) {
		
		this.host = host;
		this.user = user;
		this.pass = pass;
		this.database = database;
		
	}
	
	
	public Connection getConnection() {
		return connection;
	}
	
	public boolean isConnected() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public void connect() {
		try {
			if(connection != null && !connection.isClosed()) {
				return;
			}
			synchronized (this) {
				if(connection != null && !connection.isClosed()) {
					return;
				}
				if(pl.getConfig().getString("database.storage-method").equalsIgnoreCase("mysql")) {
					Bukkit.getConsoleSender().sendMessage("§a[ChatTags] Connecting to mysql server...");
					Class.forName("com.mysql.jdbc.Driver");
					connection = (Connection) DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + getPort() + "/" + this.database+"?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false&tcpKeepAlive=true&autoReconnect=true", this.user, this.pass);
					Bukkit.getConsoleSender().sendMessage("§a[ChatTags] MySQL has been successfully connected.");
					
				}else if(pl.getConfig().getString("database.storage-method").equalsIgnoreCase("sqlite")) {
					Bukkit.getConsoleSender().sendMessage("§a[ChatTags] Connecting to sqlite database...");
					Class.forName("org.sqlite.JDBC");
					connection = (Connection) DriverManager.getConnection("jdbc:sqlite:"+pl.getStorage().getAbsolutePath());
					Bukkit.getConsoleSender().sendMessage("§a[ChatTags] SQLite has been successfully connected.");
					
				}else {
					pl.getConfig().set("database.storage-method", "sqlite");
					pl.saveConfig();
				}
			}
		}catch (SQLException | ClassNotFoundException e) {
			
			Bukkit.getConsoleSender().sendMessage("§c[ChatTags] Could not connect to mysql/sqlite database.");
			e.printStackTrace();
		}
	}
	
	public void createTable(String table, String value) {
		
		try {
			String query =
					"CREATE TABLE IF NOT EXISTS "+table+"("+value+");";
							PreparedStatement ps = this.getConnection().prepareStatement(query);
							ps.executeUpdate();
								
		}catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("§c[ChatTags] An error occurred while creating a table.");
			e.printStackTrace();
		}
		
			
		
	}
	
	
	public String getDatabase() {
		return database;
	}
	public String getHost() {
		return host;
	}
	public String getUsername() {
		return user;
	}
	public String getPassword() {
		return pass;
	}
	public int getPort() {
		return 3306;
	}
	
	
}
