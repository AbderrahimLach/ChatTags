package com.abderrahimlach.tags.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.abderrahimlach.ChatTagsMain;
import com.abderrahimlach.tags.TagsManager;

public class TagsData {

	private static ChatTagsMain pl = ChatTagsMain.getPlugin(ChatTagsMain.class);
	private String name;
	
	public TagsData(String name) {
		this.name = name;
	}
	
	public String getName() {
		try {
			
			Statement stat = pl.getMySQLManager().getConnection().createStatement();
			ResultSet set = stat.executeQuery("SELECT * FROM tags WHERE Name = '"+name+"'");
			while(set.next()) {
				return set.getString("Name");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getColor() {
		try {
			Statement stat = pl.getMySQLManager().getConnection().createStatement();
			ResultSet set = stat.executeQuery("SELECT * FROM tags WHERE Name = '"+name+"'");
			while(set.next()) {
				
				return set.getString("color").replace("&", "§");
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public static ArrayList<TagsManager> getTags() {
		
		ArrayList<TagsManager> tags = new ArrayList<>();
		try {
			Statement st = pl.getMySQLManager().getConnection().createStatement();
			ResultSet set = st.executeQuery("SELECT * FROM tags");
			while(set.next()) {
				tags.add(new TagsManager(set.getString("Name")));
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return tags;
		
	}
	public String getPrefix() {
		try {
			Statement stat = pl.getMySQLManager().getConnection().createStatement();
			ResultSet set = stat.executeQuery("SELECT * FROM tags WHERE Name = '"+name+"'");
			while(set.next()) {
				
				return set.getString("prefix").replace("&", "§");
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
