package com.abderrahimlach.internal.config;

import com.abderrahimlach.internal.config.replacement.Replacement;
import com.abderrahimlach.utility.PluginUtility;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author DirectPlan
 */
@Getter
public enum ConfigKeys {

    // ---- config.yml messages ----
    TAG_FORMAT_ENABLED( "tag-format.enabled", "config.yml", false),
    TAG_FORMAT("tag-format.format", "config.yml", "{tag} <%1$s> %2$s"),

    // Storage
    STORAGE_METHOD("storage-method", "config.yml",  "MYSQL"),

    STORAGE_HOSTNAME("storage.host","config.yml",  "localhost"),
    STORAGE_USERNAME("storage.username", "config.yml",  "username"),
    STORAGE_PASSWORD( "storage.password", "config.yml", "password"),
    STORAGE_DATABASE("storage.database", "config.yml",  "chattags"),
    STORAGE_MAXIMUM_POOL_SIZE( "storage.maximum-pool-size", "config.yml", 10, false),

    // ---- messages.yml messages ----
    TAG_CREATED("tag.created", "&aTag %tag% &ahas been created!"),
    TAG_DELETED("tag.deleted", "&aTag %tag% &ahas been deleted!"),
    TAG_MODIFIED("tag.modified", "&aYou have set %tag%&a's %modified_key% to %modified_value%&a."),
    TAG_EQUIPPED("tag.equipped", "&aYou've equipped your %tag% &atag."),
    TAG_UNEQUIPPED("tag.un-equipped", "&aYou've un-equipped your %tag% &atag."),
    TAG_GRANTED("tag.granted", "&aYou've granted %player% access to the %tag% &atag!"),
    TAG_GRANTED_PLAYER("tag.granted-player", "&aYou've been granted access to the %tag% &atag!"),
    TAG_REVOKED("tag.revoked", "&cYou've revoked %player%'s access to the %tag% &ctag!"),
    TAG_REVOKED_PLAYER("tag.revoked-player", "&cYour access to the %tag% &ctag has been revoked!"),
    // Errors
    TAG_ALREADY_EXIST("error.exists", "&cThis tag already exists!"),
    TAG_NOT_FOUND("error.tag-not-found", "&cThis tag does not exist!"),
    NAME_EXCEEDED("error.name-exceeded", "&cYou have exceeded the limit of 16 characters"),
    NO_TAGS("error.no-tags", "&cThere are currently no tags."),
    TAG_ALREADY_OWNED("error.tag-already-owned", "&cThis player already owns this tag."),
    TAG_NOT_OWNED("error.tag-not-owned", "&cThis player does not own this tag."),
    TAG_NOT_OWNED_PLAYER("error.tag-not-owned-player", "&cYou don't own this tag."),
    // GUI
    GUI_TITLE("gui.title", "&aChatTags &7(%current_page%/%total_pages%)"),
    GUI_TAG_DISPLAY_NAME("gui.tag-display-name", "%tag%"),
    GUI_TAG_LORE("gui.tag-lore", Arrays.asList("&aPrefix:", "%tag_prefix%", " ", "%tag_player_info%")),
    GUI_NEXT_PAGE("gui.next-page", "&aNext page &7(%next_page%/%total_pages%)"),
    GUI_PREVIOUS_PAGE("gui.previous-page", "&aPrevious page &7(%previous_page%/%total_pages%)"),
    GUI_TAG_INFO_NOT_OWNED("gui.tag-player-info.not-owned", "&cYou don't own this tag"),
    GUI_TAG_INFO_EQUIP("gui.tag-player-info.equip", "&aClick here to equip this tag"),
    GUI_TAG_INFO_UNEQUIP("gui.tag-player-info.un-equip", "&eClick here to un-equip this tag"),


    ;


    private final String key;
    private final String configFile;
    private final boolean overwrite;
    private Object defaultValue;

    ConfigKeys(String key, String configFile, Object defaultValue, boolean overwrite) {
        this.key = key;
        this.configFile = configFile;
        this.defaultValue = defaultValue;

        this.overwrite = overwrite;
    }
    ConfigKeys(String key, String configFile, Object defaultValue) {
        this(key, configFile, defaultValue, true);
    }

    ConfigKeys(String key, Object defaultValue) {
        this(key, "messages.yml", defaultValue, true);
    }


    private String proceedReplacements(String text, Replacement... replacements) {
        for(Replacement replacement : replacements) {
            text = replacement.replace(text);
        }
        return text;
    }

    public int getInteger() {
        return (int) defaultValue;
    }

    public boolean getBoolean() {
        return (boolean) defaultValue;
    }

    public String getValue(Replacement... replacements) {
        if (!(defaultValue instanceof String)) return null;

        String text = defaultValue.toString();
        return proceedReplacements(text, replacements);
    }

    public void setValue(Object value) {
        this.defaultValue = value;
    }

    public List<String> getStringList(boolean colored, Replacement... replacements){
        List<String> tempStringList = new ArrayList<>();
        if(!(defaultValue instanceof List)) {
            return tempStringList;
        }
        List<?> list = (List<?>) defaultValue;
        for(Object obj : list) {
            if(obj instanceof String) {
                tempStringList.add(obj.toString());
            }
        }

        tempStringList.replaceAll(value ->  {
            value = proceedReplacements(value, replacements);
            return (colored ? ChatColor.translateAlternateColorCodes('&', value) : value);
        });
        return tempStringList;
    }

    public List<String> getStringList(Replacement... replacements) {
        return getStringList(true, replacements);
    }

    public void sendMessage(CommandSender sender, Replacement... replacements) {
        if(defaultValue instanceof List) {
            List<String> lines = getStringList(replacements);
            for(String line : lines) {
                sender.sendMessage(line);
            }
            return;
        }
        String message = getValue(replacements);
        sender.sendMessage(PluginUtility.translateMessage(message));
    }

    public static void sendMessage(ConfigKeys key, CommandSender sender, Replacement... replacements) {
        key.sendMessage(sender, replacements);
    }
}
