package com.abderrahimlach.config;

import com.abderrahimlach.TagPlugin;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author AbderrahimLach
 */
public class ConfigurationAdapter {

    private final TagPlugin plugin;

    @Getter private final File file;
    @Getter private FileConfiguration config;

    public ConfigurationAdapter(TagPlugin plugin, String file) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), file);
    }

    public void loadConfiguration(){
        if(!this.file.exists()){
            plugin.saveResource(file.getName(), false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfiguration(){
        try {
            this.config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String path){
        return config.getString(path);
    }

    public boolean getBoolean(String path){
        return config.getBoolean(path, false);
    }

    public int getInteger(String path){
        return config.getInt(path);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public void set(String path, Object object) {
        config.set(path, object);
    }

    public Object get(String path) {
        return config.get(path);
    }
}
