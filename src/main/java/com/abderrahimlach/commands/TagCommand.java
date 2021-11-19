package com.abderrahimlach.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.config.ConfigKeys;
import com.abderrahimlach.config.replacement.Replacement;
import com.abderrahimlach.data.PlayerData;
import com.abderrahimlach.management.PlayerManager;
import com.abderrahimlach.management.TagManager;
import com.abderrahimlach.tag.Tag;
import com.abderrahimlach.utility.Util;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;



/**
 * @author AbderrahimLach
 */

@CommandAlias("tag")
@CommandPermission("chattags.admin")
@Data
public class TagCommand extends BaseCommand {

    private final TagPlugin plugin;

    @Dependency
    private TagManager tagManager;
    @Dependency
    private PlayerManager playerManager;

    @Default
    @HelpCommand
    @Description("Shows this help")
    @Syntax("[page]")
    public void onHelp(CommandHelp help){
        help.showHelp();
    }

    @Subcommand("create")
    @Syntax("<tagName>")
    @Description("Create a unique tag")
    public void onCreate(CommandSender sender, String tagName){
        if(tagName.length() > 16){
            ConfigKeys.sendMessage(ConfigKeys.NAME_EXCEEDED, sender);
            return;
        }
        boolean success = this.tagManager.createTag(tagName);
        if(!success){
            ConfigKeys.sendMessage(ConfigKeys.TAG_ALREADY_EXIST, sender);
            return;
        }
        Replacement replacement = new Replacement("tag", ChatColor.WHITE + tagName);
        ConfigKeys.sendMessage(ConfigKeys.TAG_CREATED, sender, replacement);
    }

    @Subcommand("delete")
    @Syntax("<tagName>")
    @Description("Delete an existing tag")
    public void onDelete(CommandSender sender, String tagName){
        Tag tag = this.tagManager.deleteTag(tagName);
        if(tag == null){
            ConfigKeys.sendMessage(ConfigKeys.TAG_NOT_FOUND, sender);
            return;
        }
        ConfigKeys.sendMessage(ConfigKeys.TAG_DELETED, sender, new Replacement("tag", tag.getName()));
    }

    @Subcommand("setprefix")
    @Syntax("<tag> <prefix>")
    @Description("Set prefix for a tag")
    public void onPrefixSet(CommandSender sender, String tagName, String prefix) {
        Tag tag = this.tagManager.getTag(tagName);
        if(tag == null) {
            ConfigKeys.sendMessage(ConfigKeys.TAG_NOT_FOUND, sender);
            return;
        }
        this.tagManager.setPrefix(tag, prefix);
        ConfigKeys.sendMessage(ConfigKeys.TAG_MODIFIED, sender,
                new Replacement("tag", tag.getName()),
                new Replacement("modified_key", "prefix"),
                new Replacement("modified_value", prefix));
    }

    @Subcommand("setdisplayname")
    @Syntax("<tag> <displayName>")
    @Description("Set display name for a tag")
    public void onDisplayNameSet(CommandSender sender, Tag tag, String displayName) {
        if(tag == null) return;
        this.tagManager.setDisplayName(tag, displayName);
        ConfigKeys.sendMessage(ConfigKeys.TAG_MODIFIED, sender,
                new Replacement("tag", tag.getName()),
                new Replacement("modified_key", "display name"),
                new Replacement("modified_value", displayName));
    }

    @Subcommand("addtag")
    @Syntax("<player> <tag>")
    @Description("Give a player access to a tag")
    public void onAddTag(CommandSender sender, @Flags("other") Player target, Tag tag){
        if(tag == null) return;

        UUID uuid = target.getUniqueId();
        PlayerData playerData = this.playerManager.getPlayer(uuid);
        if(playerData.hasTag(tag.getName())){
            ConfigKeys.sendMessage(ConfigKeys.TAG_ALREADY_OWNED, sender);
            return;
        }
        playerData.addTag(tag);
        String displayName = tag.getDisplayName();
        ConfigKeys.sendMessage(ConfigKeys.TAG_GRANTED, sender, new Replacement("player", target.getName()),
                new Replacement("tag", displayName));
        ConfigKeys.sendMessage(ConfigKeys.TAG_GRANTED_PLAYER, target, new Replacement("tag", displayName));
    }

    @Subcommand("removetag")
    @Syntax("<player> <tag>")
    @Description("Remove a player's access to a tag")
    public void onRemoveTag(CommandSender sender, @Flags("other") Player target, Tag tag){
        if(tag == null) return;

        UUID uuid = target.getUniqueId();
        PlayerData playerData = this.playerManager.getPlayer(uuid);

        if(playerData.getEquippedTag() == tag){
            playerData.setEquippedTag(null);
        }
        playerData.removeTag(tag);

        String displayName = tag.getDisplayName();
        ConfigKeys.sendMessage(ConfigKeys.TAG_REVOKED, sender, new Replacement("player", target.getName()), new Replacement("tag", displayName));
        ConfigKeys.sendMessage(ConfigKeys.TAG_REVOKED_PLAYER, target, new Replacement("tag", displayName));
    }


    @Subcommand("showtags")
    @Syntax("<player>")
    @Description("Show all of player's tags")
    public void onShowTags(CommandSender sender, @Flags("other") OfflinePlayer target) {
        UUID uuid = target.getUniqueId();
        PlayerData playerData = this.playerManager.getPlayer(uuid);

        Collection<Tag> playerTags = playerData.getTags().values();
        if(playerTags.isEmpty()) {
            sender.sendMessage(Util.translateMessage("&cThis player does not own any tag"));
            return;
        }
        sender.sendMessage(Util.translateMessage("&b&l"+target.getName()+"'s Tags:"));
        sender.sendMessage("");
        for(Tag tag : playerTags) {
            boolean equipped = playerData.getEquippedTag() == tag;
            sender.sendMessage(Util.translateMessage("&7 - &b"+tag.getName() + (equipped ? " &7(Equipped)" : "")));
        }
    }

    @Subcommand("list")
    @Description("Lists all the current created tags")
    public void onList(CommandSender sender){
        Collection<Tag> collection = this.tagManager.getTags();
        if(collection.isEmpty()){
            ConfigKeys.sendMessage(ConfigKeys.NO_TAGS, sender);
            return;
        }
        sender.sendMessage(Util.translateMessage("&b&lTags: &7(Display Name, Prefix)"));
        sender.sendMessage(" ");
        for(Tag tag : collection){
            String prefix = tag.getPrefix() != null ? tag.getPrefix() : "N/A";
            sender.sendMessage(Util.translateMessage("&7 - &b" + tag.getName() + " &7("+tag.getDisplayName()+"&7) - "+prefix));
        }
    }

    @Subcommand("reload")
    @Description("Reload all the config files")
    public void onReload(CommandSender sender) {
        plugin.getConfigHandler().updateConfigurationsKeys();
        sender.sendMessage(Util.translateMessage("&bChatTags configurations has been reloaded!"));
    }
}
