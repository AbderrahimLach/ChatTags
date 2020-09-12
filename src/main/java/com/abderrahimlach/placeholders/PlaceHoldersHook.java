package com.abderrahimlach.placeholders;

import org.bukkit.entity.Player;

import com.abderrahimlach.ChatTagsMain;
import com.abderrahimlach.profile.Profile;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceHoldersHook extends PlaceholderExpansion {
	
	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
		if(p == null) {
			return "...";
		}
		Profile profile = Profile.getProfileByUuid(p.getUniqueId());
		if(identifier.equalsIgnoreCase("prefix")) {
			return profile.getCurrentTag().getData().getPrefix();
		}
		if(identifier.equalsIgnoreCase("name")) {
			return profile.getCurrentTag().getData().getName();
		}
		if(identifier.equalsIgnoreCase("color")) {
			return profile.getCurrentTag().getData().getColor();
		}
		return "...";
		
	}

	@Override
	public String getAuthor() {
		return "AbderrahimLach";
	}

	@Override
	public String getIdentifier() {
		return "chattags";
	}

	@Override
	public String getVersion() {
		return ChatTagsMain.getPlugin(ChatTagsMain.class).getDescription().getVersion();
	}
	
	

}
