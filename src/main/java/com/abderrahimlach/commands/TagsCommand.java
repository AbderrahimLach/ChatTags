package com.abderrahimlach.commands;



import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.abderrahimlach.ChatTagsMain;
import com.abderrahimlach.database.type.RequestType;
import com.abderrahimlach.inventory.TagsInventory;
import com.abderrahimlach.profile.Profile;
import com.abderrahimlach.tags.TagsManager;
import com.abderrahimlach.tags.data.TagsData;

public class TagsCommand implements CommandExecutor {

	private ChatTagsMain plugin = ChatTagsMain.getPlugin(ChatTagsMain.class);
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("tag")) {
			if(sender.hasPermission("chattags.admin")) {
				// /tag create <name>
				// /tag setcolor <name> <color>
				// /tag setprefix <name> <prefix>
				// /tag delete <name>
				// /tag list
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("list")) {
						int i = 0;
						sender.sendMessage("§7§m-----------------------");
						
						for(TagsManager tags : TagsData.getTags()) {
							i++;
							sender.sendMessage("§7"+i+" - ("+tags.getData().getColor()+tags.getData().getName()+"§7) §ePrefix: "+tags.getData().getPrefix());
						}
						
						
						sender.sendMessage("§7§m-----------------------");
						
					}
				}else if(args.length >= 2) {
					if(args[0].equalsIgnoreCase("create")) {
						
						String tag = args[1];
						TagsManager tagsManager = new TagsManager(tag);
						if(!tagsManager.isAlreadyExists()) {
							tagsManager.create();
							sender.sendMessage("§aTag '"+tag+"' has been created.");
							
						}else {
							sender.sendMessage("§cThis tag is already exists.");
						}
						
					}else if(args[0].equalsIgnoreCase("setcolor")) {
						String tag = args[1];
						String value = args[2];
						TagsManager tagsManager = new TagsManager(tag);
						if(tagsManager.isAlreadyExists()) {
							tagsManager.update(RequestType.COLOR, value);
							sender.sendMessage("§aTag color has been set to "+tagsManager.getData().getColor()+tagsManager.getData().getName()+"§a.");
						}else {
							sender.sendMessage("§cThis tag is not exists.");
						}
						
					}else if(args[0].equalsIgnoreCase("setprefix")) {
						String tag = args[1];
						String value = args[2];
						TagsManager tagsManager = new TagsManager(tag);
						if(tagsManager.isAlreadyExists()) {
							tagsManager.update(RequestType.PREFIX, value);
							sender.sendMessage("§aTag prefix has been set to '"+tagsManager.getData().getPrefix()+"§a'.");
						}else {
							sender.sendMessage("§cThis tag is not exists.");
						}
						
					}else if(args[0].equalsIgnoreCase("delete")) {
						
						String tag = args[1];
						TagsManager tagsManager = new TagsManager(tag);
						if(tagsManager.isAlreadyExists()) {
							tagsManager.delete();
							sender.sendMessage("§aTag '"+tag+"' has been deleted.");
							
						}else {
							sender.sendMessage("§cThis tag is not exists.");
						}
						
					}else if(args[0].equalsIgnoreCase("apply")) {
						Profile profile = Profile.getProfileByUuid(Bukkit.getPlayer(args[1]).getUniqueId());
						TagsManager tag = new TagsManager(args[2]);
						if(tag.isAlreadyExists()) {
							if(!profile.containsTag(tag.getName())) {
								sender.sendMessage("§aYou've gived "+profile.getName()+" access to the "+tag.getData().getColor()+tag.getData().getName()+"§a tag.");
								profile.apply(tag);
								
							}else {
								sender.sendMessage("§cThis player is already have this tag.");
							}
						}else {
							sender.sendMessage("§cThis tag is not exists.");
						}
						
						
					}else if(args[0].equalsIgnoreCase("unapply")) {
						Profile profile = Profile.getProfileByUuid(Bukkit.getPlayer(args[1]).getUniqueId());
						TagsManager tag = new TagsManager(args[2]);
						if(tag.isAlreadyExists()) {
							
							if(profile.containsTag(tag.getName())) {
								
								sender.sendMessage("§aYou've removed "+profile.getName()+"'s access to the "+tag.getData().getColor()+tag.getData().getName()+"§a tag.");
								profile.unapply(tag);
								
							}else {
								sender.sendMessage("§cThis player does not have this tag.");
							}
							
							
						}else {
							sender.sendMessage("§cThis tag is not exists");
						}
						
						
					}else if(args[0].equalsIgnoreCase("set")) {
						Profile profile = Profile.getProfileByUuid(Bukkit.getPlayer(args[1]).getUniqueId());
						if(!args[2].equalsIgnoreCase("off")) {
							TagsManager tag = new TagsManager(args[2]);
							if(tag.isAlreadyExists()) {
								sender.sendMessage("§aYou've set "+profile.getName()+"'s tag to "+tag.getData().getColor()+tag.getData().getName()+"§a.");
								profile.setTag$(tag);
							}else {
								sender.sendMessage("§cThis tag is not exists.");
							}
						}else {
							sender.sendMessage("§aYou've unset "+profile.getName()+"'s tag.");
							profile.unsetTag$();
						}
						
						
					}else {
						sender.sendMessage("§e====== §6Showing help of /tag §e======");
						sender.sendMessage("§e/tag create <name> §6Create a new tag.");
						sender.sendMessage("§e/tag delete <name> §6Delete a tag.");
						sender.sendMessage("§e/tag setcolor <name> <color> §6Apply a color for a tag.");
						sender.sendMessage("§e/tag setprefix <name> <prefix> §6Set a prefix for a tag.");
						sender.sendMessage("§e/tag apply <player> <tag> §6Apply a tag to a player.");
						sender.sendMessage("§e/tag unapply <player> <tag> §6Unapply a tag from a player.");
						sender.sendMessage("§e/tag set <player> <tag/off> set tag for a player, 'off' to remove it.");
						sender.sendMessage("§e/tag list §6Shows the available tags.");
						sender.sendMessage("§e===============================");
					}
				}else {
					sender.sendMessage("§e====== §6Showing help of /tag §e======");
					sender.sendMessage("§e/tag create <name> §6Create a new tag.");
					sender.sendMessage("§e/tag delete <name> §6Delete a tag.");
					sender.sendMessage("§e/tag setcolor <name> <color> §6Set a color for a tag.");
					sender.sendMessage("§e/tag setprefix <name> <prefix> §6Set a prefix for a tag.");
					sender.sendMessage("§e/tag apply <player> <tag> §6Apply a tag to a player.");
					sender.sendMessage("§e/tag unapply <player> <tag> §6Unapply a tag from a player.");
					sender.sendMessage("§e/tag set <player> <tag/off> set a tag for a player, 'off' to remove it.");
					sender.sendMessage("§e/tag list §6Shows the available tags.");
					sender.sendMessage("§e===============================");
				}
				
			}else {
				sender.sendMessage(plugin.getConfig().getString("no-permission").replace("&", "§"));
			}
		}else if(cmd.getName().equalsIgnoreCase("tags")) {
			
			if(sender instanceof Player) {
				Player player = (Player)sender;
				TagsInventory.openTagsInventory(player, 0);
				
			}else {
				sender.sendMessage("§cYou cannot perform this command on console.");
			}
			
		}
		
		
		return true;
	}
	
	

}
