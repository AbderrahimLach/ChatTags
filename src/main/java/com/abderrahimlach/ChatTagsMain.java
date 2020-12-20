package com.abderrahimlach;

import java.io.File;

import java.lang.reflect.Field;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import com.abderrahimlach.commands.TagsCommand;
import com.abderrahimlach.database.MySQLManager;
import com.abderrahimlach.listeners.Events;
import com.abderrahimlach.placeholders.PlaceHoldersHook;
import com.abderrahimlach.utils.ConfigUtils;
import com.abderrahimlach.utils.Glow;

public class ChatTagsMain extends JavaPlugin {
	
	
	
	private MySQLManager mySQLManager;
	private File file;
	public void onEnable() {
		handleConfig();
		this.mySQLManager = new MySQLManager(ConfigUtils.MYSQL_HOST, 
				ConfigUtils.MYSQL_USER, ConfigUtils.MYSQL_PASS, ConfigUtils.MYSQL_DATABASE);
		this.mySQLManager.connect();
		this.mySQLManager.createTable("tags", "Name TEXT, color TEXT, prefix TEXT");
		this.mySQLManager.createTable("playerdata", "UUID TEXT, username TEXT, currentTag TEXT, tags TEXT");
		register();
		PlaceHoldersHook placeholder = new PlaceHoldersHook();
		if(placeholder.canRegister()) {
			placeholder.register();
		}
	}
	public MySQLManager getMySQLManager() {
		return mySQLManager;
	}

	
	public void register() {
		
		getServer().getPluginManager().registerEvents(new Events(), this);
		getCommand("tag").setExecutor(new TagsCommand());
		getCommand("tags").setExecutor(new TagsCommand());
	}
	public File getStorage() {
		return file;
	}
	public void handleConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		saveConfig();
		file = new File(getDataFolder()+"/database/storage.db");
		if(!file.exists()) {
			file.getParentFile().mkdirs();

		}
		
	}
	public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(Enchantment.PROTECTION_ENVIRONMENTAL.getKey());
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
	}
}
