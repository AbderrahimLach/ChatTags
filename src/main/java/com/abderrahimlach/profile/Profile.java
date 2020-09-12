package com.abderrahimlach.profile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.abderrahimlach.ChatTagsMain;
import com.abderrahimlach.events.PlayerApplyTagEvent;
import com.abderrahimlach.events.PlayerUnapplyTagEvent;
import com.abderrahimlach.tags.TagsManager;

public class Profile {
	
	private UUID uuid;
	public ChatTagsMain pl = ChatTagsMain.getPlugin(ChatTagsMain.class);
	public static Set<Profile> profiles = new HashSet<Profile>();
	public Profile(UUID uuid) {
		this.uuid = uuid;
		profiles.add(this);
	}
	public static Profile getProfileByUuid(UUID uuid) {
		for(Profile profile : profiles) {
			if(profile.getUuid().equals(uuid)) {
				return profile;
			}
			
		}
		return null;	
	}
	public static Set<Profile> getProfiles(){
		return profiles;
	}
	public static Profile addProfile(UUID uuid) {
		return new Profile(uuid);
	}
	public UUID getUuid() {
		return uuid;
	}
	public String getName() {
		try {
			
			Statement stat = pl.getMySQLManager().getConnection().createStatement();
			ResultSet set = stat.executeQuery("SELECT * FROM playerdata WHERE UUID = '"+uuid+"'");
			while(set.next()) {
				return set.getString("username");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	public void apply(TagsManager tag) {
		
	      String query = "UPDATE playerdata SET currentTag = '"+tag.getData().getName()+"' WHERE UUID = '"+uuid+"'";
		try {
			Statement preparedStmt = pl.getMySQLManager().getConnection().createStatement();
		      preparedStmt.executeUpdate(query);
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("§c[ChatTags] An error occurred while updating a column.");
			e.printStackTrace();
		}
		Player p = Bukkit.getPlayer(uuid);
		if(p != null) {
			p.sendMessage("§aYou've been given access to the "+tag.getData().getColor()+tag.getData().getName()+" §atag.");
		}
		
		List<String> allTags = getAvailableTagsByList();
		allTags.add(tag.getName());
		String asString = allTags.toString().replace("[", "").replace("]", "");
		try {
			
			PreparedStatement ps = pl.getMySQLManager().getConnection().
					prepareStatement("UPDATE playerdata SET tags = '"+asString+"' WHERE UUID = '"+uuid+"'");
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getPluginManager().callEvent(new PlayerApplyTagEvent(this, tag));
		
	}
	
	public void unapply(TagsManager tag) {
		List<String> allTags = getAvailableTagsByList();
		allTags.remove(tag.getName());
		String asString = allTags.toString().replace("[", "").replace("]", "");
		try {
			
			PreparedStatement ps = pl.getMySQLManager().getConnection().
					prepareStatement("UPDATE playerdata SET tags = '"+asString+"' WHERE UUID = '"+uuid+"'");
			ps.executeUpdate();
			if(getCurrentTag().getData().getName().equalsIgnoreCase(tag.getName())) {
				PreparedStatement psa = pl.getMySQLManager().getConnection().
						prepareStatement("UPDATE playerdata SET currentTag = '"+(getAvailableTags().get(0) == null ? null : getAvailableTags().get(0).getName())+"' WHERE UUID = '"+uuid+"'");
				psa.executeUpdate();
				
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		Player player = Bukkit.getPlayer(uuid);
		if(player != null) {
			player.sendMessage("§cYour tag "+tag.getData().getColor()+tag.getData().getName()+" §chas been removed.");
		}
		Bukkit.getPluginManager().callEvent(new PlayerUnapplyTagEvent(this, tag));
		
	}
	public void setTag(TagsManager tag) {
		
		try {
			
			PreparedStatement ps = pl.getMySQLManager().getConnection().
					prepareStatement("UPDATE playerdata SET currentTag = '"+tag.getData().getName()+"' WHERE UUID = '"+uuid+"'");
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		Player player = Bukkit.getPlayer(uuid);
		if(player != null) {
			player.sendMessage("§aYou've set your tag to "+tag.getData().getColor()+tag.getData().getName()+"§a.");
		}
	}
	
	public void sendMessage(String msg) {
		msg = msg.replace("&", "§");
		Player p = Bukkit.getPlayer(uuid);
		if(p != null) {p.sendMessage(msg);}
	}
	public boolean hasPermission(String perm) {
		Player p = Bukkit.getPlayer(uuid);
		if(p != null) {return p.hasPermission(perm);}return false;}
	public boolean isOp() {
		Player p = Bukkit.getPlayer(uuid);
		if(p != null) {return p.isOp();}return false;}
	public void unsetTag(TagsManager tag) {
		
		try {
			
			PreparedStatement ps = pl.getMySQLManager().getConnection().
					prepareStatement("UPDATE playerdata SET currentTag = '"+"null"+"' WHERE UUID = '"+uuid+"'");
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		Player player = Bukkit.getPlayer(uuid);
		if(player != null) {
			player.sendMessage("§aYou've unset your tag from "+tag.getData().getColor()+tag.getData().getName()+"§a.");
		}
	}
	public void setTag$(TagsManager tag) {
		
		try {
			
			PreparedStatement ps = pl.getMySQLManager().getConnection().
					prepareStatement("UPDATE playerdata SET currentTag = '"+tag.getData().getName()+"' WHERE UUID = '"+uuid+"'");
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}

	}
	public void unsetTag$() {
		
		try {
			
			PreparedStatement ps = pl.getMySQLManager().getConnection().
					prepareStatement("UPDATE playerdata SET currentTag = '"+"null"+"' WHERE UUID = '"+uuid+"'");
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public TagsManager getCurrentTag() {
		
		try {
			Statement statement = pl.getMySQLManager().getConnection().createStatement();
			ResultSet set = statement.executeQuery("SELECT * FROM playerdata WHERE UUID = '"+uuid+"'");
			while(set.next()) {
				return new TagsManager(set.getString("currentTag"));
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public ArrayList<TagsManager> getAvailableTags(){
		ArrayList<TagsManager> bb = new ArrayList<>();
		try {
			Statement statement = pl.getMySQLManager().getConnection().createStatement();
			ResultSet set = statement.executeQuery("SELECT * FROM playerdata WHERE UUID = '"+uuid+"'");
			while(set.next()) {
				ArrayList<String> a = new ArrayList<>(Arrays.asList(set.getString("tags").split(", ")));
				for(String aa : a) {
					bb.add(new TagsManager(aa));
				}
				
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bb;
	}
	
	public ArrayList<String> getAvailableTagsByList(){
		ArrayList<String> bb = new ArrayList<>();
		try {
			Statement statement = pl.getMySQLManager().getConnection().createStatement();
			ResultSet set = statement.executeQuery("SELECT * FROM playerdata WHERE UUID = '"+uuid+"'");
			if(set.next()) {
				ArrayList<String> a = new ArrayList<>(Arrays.asList(set.getString("tags").split(", ")));
				for(String aa : a) {
					bb.add(aa);
					
				}
				bb.remove("");
				
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return bb;
	}
	public boolean containsTag(String tag) {
		if(getAvailableTagsByList() == null) {
			
			return false;
		}else {
			return getAvailableTagsByList().contains(tag);
		}
		
		
	}
	public Player getPlayer() {
		Player p = Bukkit.getPlayer(uuid);
		return (p != null ? p : null);
	}
	public void save(Player player) {
		try {
			
			PreparedStatement stat = pl.getMySQLManager().getConnection().prepareStatement("SELECT * FROM playerdata WHERE UUID = '"+uuid+"'");
			ResultSet set = stat.executeQuery();
			if(!set.next()) {
				PreparedStatement ps = pl.getMySQLManager().getConnection()
						.prepareStatement("INSERT INTO playerdata(UUID, username, currentTag, tags) VALUES (?,?,?,?)");
									ps.setString(1, player.getUniqueId().toString());
									ps.setString(2, player.getName());
									ps.setString(3, "");
									ps.setString(4, "");
									ps.executeUpdate();
			}else if(set.next() && set.getString("UUID") == player.toString()) {
					
					String query = "UPDATE playerdata SET username = '"+player.getName()+"' WHERE UUID = '"+player.getUniqueId().toString()+"'";
					PreparedStatement ps = pl.getMySQLManager().getConnection().prepareStatement(query);
					ps.executeUpdate();
				
				
				
			}
			
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
}
