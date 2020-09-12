package com.abderrahimlach.inventory;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.abderrahimlach.profile.Profile;
import com.abderrahimlach.tags.TagsManager;
import com.abderrahimlach.tags.data.TagsData;
import com.abderrahimlach.utils.Glow;
import com.abderrahimlach.utils.Pagination;



public class TagsInventory {
	
	
    public static void openTagsInventory(Player player, int page){
    	
    	Pagination<TagsManager> pagination = new Pagination<>(21, TagsData.getTags());
    	Inventory inv = Bukkit.createInventory(null, 45, "Chat Tags §7(§6"+(page+1)+"/"+pagination.totalPages()+"§7)");
		Profile profile = Profile.getProfileByUuid(player.getUniqueId());
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
		ItemMeta glassm = glass.getItemMeta();
		glassm.setDisplayName("§c");
		
		glass.setItemMeta(glassm);
        ItemStack nextpage =  new ItemStack(Material.MELON);
        ItemMeta meta = nextpage.getItemMeta();
        meta.setDisplayName("§aNext page §7(§6"+(page+1)+"§7/"+"§6"+pagination.totalPages()+"§7)");
        nextpage.setItemMeta(meta);
        
        ItemStack prevpage = new ItemStack(Material.SPECKLED_MELON);
        ItemMeta meta2 = prevpage.getItemMeta();
        meta2.setDisplayName("§cPrevious page §7(§6"+(page)+"§7/"+"§6"+pagination.totalPages()+"§7)");
        prevpage.setItemMeta(meta2);
        ItemStack tag = new ItemStack(Material.NAME_TAG);
        ItemMeta metaa = tag.getItemMeta();
        List<String> lore = new ArrayList<>();
		inv.setItem(0, glass);
		inv.setItem(1, glass);
		inv.setItem(2, glass);
		inv.setItem(3, glass);
		inv.setItem(4, glass);
		inv.setItem(5, glass);
		inv.setItem(6, glass);
		inv.setItem(7, glass);
		inv.setItem(8, glass);
		if((page+1) < pagination.totalPages()) {
			inv.setItem(8, nextpage);
		}
		if((page+1) >= pagination.totalPages() && pagination.totalPages() != 1) {
			inv.setItem(0, prevpage);
		}
		inv.setItem(9, glass);
		inv.setItem(17, glass);
		inv.setItem(18, glass);
		inv.setItem(26, glass);
		inv.setItem(27, glass);
		inv.setItem(35, glass);
		inv.setItem(36, glass);
		inv.setItem(37, glass);
		inv.setItem(38, glass);
		inv.setItem(39, glass);
		inv.setItem(40, glass);
		inv.setItem(41, glass);
		inv.setItem(42, glass);
		inv.setItem(43, glass);
		inv.setItem(44, glass);
		int i = 10;
		for(TagsManager tags : pagination.getPage(page)) {
			
			lore.add("§aShows up as:");
			lore.add(tags.getData().getPrefix());
			lore.add(" ");
			Glow glow = new Glow(1);
			if(profile.getAvailableTagsByList().contains(tags.getData().getName())) {
				if(tags.getData().getName().equals(profile.getCurrentTag().getData().getName())) {
					lore.add("§eClick to unequip");
					metaa.addEnchant(glow, 1, true);
					metaa.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					
				}else {
					lore.add("§aClick to equip");
				}
			}else {
				lore.add("§cYou don't own this tag");
			}
			metaa.setDisplayName(tags.getData().getColor()+tags.getData().getName());
			metaa.setLore(lore);
			tag.setItemMeta(metaa);
			
	 		if(i == 17) {
				i = 19;
			}else if(i == 26) {
				i = 28;
			}else if(i == 35) {
				continue;
			}
			inv.setItem(i, tag);
			metaa.removeEnchant(glow);
			i++;// 28
			lore.clear();
			
		}
		
		
        player.openInventory(inv);
    }
	
	
	
	
	
	
	
	

	
	
	
	

}
