package com.abderrahimlach.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

/**
 * @author AbderrahimLach
 */
@AllArgsConstructor
@Getter
@Setter
public class Tag {

    private String name;
    private String prefix;
    private String displayName;

    public Tag(String name){
        this(name, null, ChatColor.WHITE + name);
    }
}
