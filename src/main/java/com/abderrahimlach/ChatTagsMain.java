package com.abderrahimlach;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.abderrahimlach.commands.TagCommand;
import com.abderrahimlach.commands.TagsCommand;
import com.abderrahimlach.config.ConfigUtil;
import com.abderrahimlach.database.type.StorageType;
import com.abderrahimlach.listeners.PlayerListener;
import com.abderrahimlach.tag.Tag;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.abderrahimlach.database.MySQLManager;
import com.abderrahimlach.placeholders.PlaceHoldersHook;

public class ChatTagsMain extends JavaPlugin {

	private MySQLManager mySQLManager;
	public static ChatTagsMain instance;

	private File sqliteStorage;
	private ConfigUtil configUtil;
	private List<Tag> tags;

	public void onEnable() {
		instance = this;
		handleConfig();
		String host = this.configUtil.getSqlHost();
		int port = this.configUtil.getSqlPort();
		String user = this.configUtil.getSqlUser();
		String password = this.configUtil.getSqlPassword();
		String database = this.configUtil.getSqlDatabase();
		int maximumPoolSize = this.configUtil.getMaximumPoolSize();
		this.mySQLManager = new MySQLManager(host, port, user, password, database);
		this.mySQLManager.setMaximumPoolSize(maximumPoolSize);
		this.mySQLManager.initializePool();
		this.mySQLManager.createTable();


		initTags();
		registerCommands();
		registerListener();
		registerPlaceholder();
	}

	@Override
	public void onDisable() {
		this.mySQLManager.close();
	}

	public MySQLManager getMySQLManager() {
		return mySQLManager;
	}

	private void registerPlaceholder(){
		PlaceHoldersHook placeholder = new PlaceHoldersHook();
		if(placeholder.canRegister()) {
			placeholder.register();
		}
	}

	private void initTags(){
		this.tags = new ArrayList<>();

		try (Connection connection = this.mySQLManager.getConnection()){
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags");
			ResultSet set = statement.executeQuery();
			while(set.next()){
				String name = set.getString("name");
				String color = set.getString("color");
				String prefix = set.getString("prefix");
				Tag tag = new Tag(name, color, prefix);
				this.tags.add(tag);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	private void registerCommands() {
		getCommand("tag").setExecutor(new TagCommand());
		getCommand("tags").setExecutor(new TagsCommand());
	}

	private void registerListener(){
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new PlayerListener(), this);
	}

	private void handleConfig() {
		Configuration configuration = getConfig();
		configuration.options().copyDefaults(true);
		saveDefaultConfig();
		this.configUtil = new ConfigUtil(configuration);
		if(this.configUtil.getStorageType() == StorageType.SQLITE){
			this.sqliteStorage = new File(getDataFolder()+"/database/storage.db");
			if(!this.sqliteStorage.mkdirs()){
				getLogger().info("File '/database/storage.db' is ignored since it already exists.");
			}
		}
	}

	public List<Tag> getTags() {
		return tags;
	}

	public Tag getTag(String name){
		return this.tags.stream().filter(tag -> tag.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	public boolean containTag(String name){
		return this.getTag(name) != null;
	}

	public ConfigUtil getConfigUtil() {
		return configUtil;
	}

	public File getSqliteStorage() {
		return sqliteStorage;
	}

	public static ChatTagsMain getInstance() {
		return instance;
	}

}
