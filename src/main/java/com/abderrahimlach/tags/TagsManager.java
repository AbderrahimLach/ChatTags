package com.abderrahimlach.tags;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

import com.abderrahimlach.ChatTagsMain;
import com.abderrahimlach.database.type.RequestType;
import com.abderrahimlach.tags.data.TagsData;

public class TagsManager {
	private ChatTagsMain pl = ChatTagsMain.getPlugin(ChatTagsMain.class);

	private String name;
	public TagsManager(String name) {
		this.name = name;
	}
	
	
	
	public TagsData getData() {
		return new TagsData(name);
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
	
	
	public void delete() {
		
		if(isAlreadyExists()) {
			
			String query = "DELETE FROM tags WHERE Name = '"+name+"'";
			try {
				Statement ps = pl.getMySQLManager().getConnection().createStatement();
				ps.executeUpdate(query);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void create() {
		
		if(!isAlreadyExists()) {
			try {
				PreparedStatement ps = pl.getMySQLManager().getConnection()
.prepareStatement("INSERT INTO tags(Name, color, prefix) VALUES(?,?,?)");
				ps.setString(1, name);
				ps.setString(2, "");
				ps.setString(3, "");
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public boolean isAlreadyExists() {
		try {
			Statement stat = pl.getMySQLManager().getConnection().createStatement();
			ResultSet set = stat.executeQuery("SELECT * FROM tags WHERE Name = '"+name+"'");
			while(set.next()) {
				
				return (set.getString("color") != null);
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public void update(RequestType type, String value) {
		Connection connection = pl.getMySQLManager().getConnection();
		if(type == RequestType.NAME) {
		      String query = "UPDATE tags SET Name = '"+value+"' WHERE Name = '"+name+"'";
			try {
				Statement preparedStmt = connection.createStatement();
			      preparedStmt.executeUpdate(query);
			} catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage("§c[ChatTags] An error occurred while updating a column.");
				e.printStackTrace();
			}
			
		}else if(type == RequestType.PREFIX) {
			
		      String query = "UPDATE tags SET prefix = '"+value+"' WHERE Name = '"+name+"'";
			try {
				Statement preparedStmt = connection.createStatement();
			      preparedStmt.executeUpdate(query);
			} catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage("§c[ChatTags] An error occurred while updating a column.");
				e.printStackTrace();
			}
		}else if(type == RequestType.COLOR) {
		      String query = "UPDATE tags SET color = '"+value+"' WHERE Name = '"+name+"'";
			try {
				Statement preparedStmt = connection.createStatement();
			      preparedStmt.executeUpdate(query);
			} catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage("§c[ChatTags] An error occurred while updating a column.");
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
