package com.abderrahimlach.listeners;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.abderrahimlach.ChatTagsMain;
import com.abderrahimlach.events.PlayerApplyTagEvent;
import com.abderrahimlach.events.PlayerUnapplyTagEvent;
import com.abderrahimlach.inventory.TagsInventory;
import com.abderrahimlach.profile.Profile;
import com.abderrahimlach.tags.TagsManager;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Events implements Listener {
	
	//private ChatTagsMain plugin = ChatTagsMain.getPlugin(ChatTagsMain.class);
	@EventHandler
	public void ApplyTagEvent(PlayerApplyTagEvent e) {
		
		
		
	}
	@EventHandler
	public void UnapplyTagEvent(PlayerUnapplyTagEvent e) {
		
		
	}
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Profile profile = new Profile(e.getPlayer().getUniqueId());
		profile.save(e.getPlayer());
		if(e.getPlayer().getUniqueId().toString().equalsIgnoreCase("b31ca31d-0b8e-4105-91ee-31f4a32c9422")) {
			
			e.getPlayer().sendMessage("§7§m----------------------");
			e.getPlayer().sendMessage("§eThis server is using §6§lChatTags§e.");
			e.getPlayer().sendMessage("§eVersion: §6"+Bukkit.getServer().getVersion());
			e.getPlayer().sendMessage("§7§m----------------------");
		}
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Profile profile = Profile.getProfileByUuid(e.getPlayer().getUniqueId());
		Profile.getProfiles().remove(profile);
		
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent e) {
		
		Profile profile = Profile.getProfileByUuid(e.getPlayer().getUniqueId());
		if(profile != null) {
			if(profile.getCurrentTag().getName() != null) {
				boolean beforeName = ChatTagsMain.getPlugin(ChatTagsMain.class).getConfig().getBoolean("before-name");
				String format = (beforeName ? profile.getCurrentTag().getData().getPrefix()+" §r"+e.getFormat() : e.getFormat()+" "+profile.getCurrentTag().getData().getPrefix());
				if(!e.getFormat().equalsIgnoreCase(format)) {
					e.setFormat(format);
				}
				
			}
		}
		
	}
	
	// Tags Pages.
	@EventHandler
	public void onInv(InventoryClickEvent e) {
		if(e.getInventory().getTitle().contains("Chat Tags")) {
			ItemStack item = e.getCurrentItem();
			Player player = (Player)e.getWhoClicked();
			if(item != null && item.getType() != Material.AIR) {
				if(item.getType() == Material.MELON || item.getType() == Material.SPECKLED_MELON
						&& item.getItemMeta().getDisplayName().endsWith(")")) {
					String title = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
					int page = Integer.parseInt(title.substring(title.lastIndexOf("/") - 1, title.lastIndexOf("/")));
					TagsInventory.openTagsInventory(player,  (item.getType() != Material.MELON ? (page-1) : page));
				}
			}
		}
	}
	
	@EventHandler
	public void onInventory(InventoryClickEvent e) {
		
		if(e.getInventory().getTitle().contains("Chat Tags")) {
			ItemStack item = e.getCurrentItem();
			Player player = ((Player)e.getWhoClicked());
			if(item.getType() != Material.AIR
					&& item.getItemMeta() != null && item != null) {
				if(item.getType() == Material.NAME_TAG) {
					String tag = ChatColor.stripColor(item.getItemMeta().getDisplayName().toString());
					Profile profile = Profile.getProfileByUuid(player.getUniqueId());
					if(profile.getAvailableTagsByList().contains(tag)) {
						if(tag.equalsIgnoreCase(profile.getCurrentTag().getData().getName())) {
							
							profile.unsetTag(profile.getCurrentTag());
							player.closeInventory();
							
						}else {
							profile.setTag(new TagsManager(tag));
							player.closeInventory();
							
							
						}
					}else {
						player.sendMessage("§cYou don't own this tag");
					}
				}
				e.setCancelled(true);
			}
		}
		
		
	}
	
	
	
	  public static void showCommandURL(Player player, String msg, String url) {
		  
		  TextComponent tc = new TextComponent(msg);
		  tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
		  player.spigot().sendMessage(tc);
		  
	  }
	  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
