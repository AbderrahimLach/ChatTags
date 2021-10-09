package com.abderrahimlach.utility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @author AbderrahimLach
 */
public class Util {

    public static void future(Runnable runnable){
        CompletableFuture.runAsync(runnable);
    }

    public static <T> CompletableFuture<T> future(Callable<T> callable){
        return CompletableFuture.supplyAsync(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                throw (RuntimeException)e;
            }
        });
    }

    public static void callEvent(Event event){
        Bukkit.getPluginManager().callEvent(event);
    }

    public static String translateMessage(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void translateMessages(String... messages){
        for(int i = 0; i < messages.length; i++){
            messages[i] = translateMessage(messages[i]);
        }
    }
}
