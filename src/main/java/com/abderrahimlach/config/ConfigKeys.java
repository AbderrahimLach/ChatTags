package com.abderrahimlach.config;

import com.abderrahimlach.config.replacement.Replacement;
import com.abderrahimlach.utility.Util;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.util.NumberConversions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author AbderrahimLach
 */
@Getter
public enum ConfigKeys {

    // ---- config.yml messages ----
    TAG_FORMAT_ENABLED("config.yml", "tag-format.enabled", false),
    TAG_FORMAT("config.yml", "tag-format.format", "{tag} <%1$s> %2$s"),

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
    GUI_TAG_INFO_UNEQUIP("gui.tag-player-info.un-equip", "&eClick here to un-equip this tag");

    private final String key;
    private final String configFile;
    @Setter private Object defaultValue;

    ConfigKeys(String configFile, String key, Object defaultValue) {
        this.configFile = configFile;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    ConfigKeys(String key, Object defaultValue){
        this("messages.yml", key, defaultValue);
    }

    public String getString(boolean colored, Replacement... replacements){
        String tempValue = this.defaultValue.toString();
        for(Replacement replacement : replacements){
            tempValue = replacement.replace(tempValue);
        }
        return (colored ? Util.translateMessage(tempValue) : tempValue);
    }
    public String getString(Replacement... replacements) {
        return getString(true, replacements);
    }

    public boolean getBoolean() {
        return (this.defaultValue instanceof Boolean ? (Boolean)this.defaultValue : false);
    }

    public int getInteger() {
        return (this.defaultValue instanceof Integer ? NumberConversions.toInt(this.defaultValue) : 0);
    }

    public List<String> getStringList(boolean colored, Replacement... replacements){
        List<String> tempStringList = new ArrayList<>();
        if(!(this.defaultValue instanceof List)) {
            return tempStringList;
        }
        List<?> list = (List<?>) this.defaultValue;
        for(Object obj : list) {
            if(obj instanceof String) {
                tempStringList.add(obj.toString());
            }
        }

        tempStringList.replaceAll(value ->  {
            for(Replacement replacement : replacements){
                value = replacement.replace(value);
            }
            return (colored ? Util.translateMessage(value) : value);
        });
        return tempStringList;
    }

    public List<String> getStringList(Replacement... replacements) {
        return getStringList(true, replacements);
    }

    public void sendMessage(CommandSender sender, Replacement... replacements){
        String value = this.getString(replacements);
        sender.sendMessage(value);
    }

    public static void sendMessage(ConfigKeys key, CommandSender sender, Replacement... replacements){
        key.sendMessage(sender, replacements);
    }
}
