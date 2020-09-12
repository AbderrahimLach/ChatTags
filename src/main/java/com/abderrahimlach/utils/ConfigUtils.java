package com.abderrahimlach.utils;

import com.abderrahimlach.ChatTagsMain;

public class ConfigUtils {
	
	private static ChatTagsMain plugin = ChatTagsMain.getPlugin(ChatTagsMain.class);
	
	public static String MYSQL_HOST = plugin.getConfig().getString("database.mysql.host");
	public static String MYSQL_PASS = plugin.getConfig().getString("database.mysql.password");
	public static int MYSQL_PORT = 3306;
	public static String MYSQL_DATABASE = plugin.getConfig().getString("database.mysql.database");
	public static String MYSQL_USER = plugin.getConfig().getString("database.mysql.username");

}
