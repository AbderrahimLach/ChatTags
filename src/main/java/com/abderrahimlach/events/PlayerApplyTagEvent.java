package com.abderrahimlach.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.abderrahimlach.profile.Profile;
import com.abderrahimlach.tags.TagsManager;

public class PlayerApplyTagEvent extends Event {
	
	private static final HandlerList HANDLERS = new HandlerList();
	

	private Profile profile;
	private TagsManager tag;
	public PlayerApplyTagEvent(Profile profile, TagsManager tag) {
		
		this.tag = tag;
		this.profile = profile;
	}
	
	
	
	
	public TagsManager getTag() {
		return tag;
	}
	public Profile getProfile() {
		return profile;
	}
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
