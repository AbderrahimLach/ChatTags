package com.abderrahimlach.tag.inventory;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.config.ConfigKeys;
import com.abderrahimlach.config.replacement.Replacement;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author AbderrahimLach
 */
public abstract class PaginatedMenu<T> extends InventoryUI {

    @Getter private int page = 1;
    @Getter private final int pageSize;

    public PaginatedMenu(TagPlugin plugin, String title, int rows, int pageSize) {
        super(plugin, title, rows);
        this.pageSize = pageSize;
    }

    public void nextPage(Player player){
        this.page += 1;
        this.clearItems();
        player.playSound(player.getLocation(), Sound.CLICK, 1L, 1L);
        this.open(player);
    }

    public void previousPage(Player player){
        this.page -= 1;
        this.clearItems();
        player.playSound(player.getLocation(), Sound.CLICK, 1L, 1L);
        this.open(player);
    }

    public int getTotalPages(){
        int size = getList().size();
        return (int) Math.ceil(((double)size / pageSize));
    }

    @Override
    public Inventory build(Player player) {
        int nextPageSlot = 8;
        int previousPageSlot = 0;
        int totalPages = this.getTotalPages();

        Replacement totalPageReplacement = new Replacement("total_pages", String.valueOf(totalPages));

        String title = ConfigKeys.GUI_TITLE.getString(
                new Replacement("current_page", String.valueOf(page)), totalPageReplacement);

        this.setTitle(title);

        if(this.page < totalPages){
            String nextPageDisplayName = ConfigKeys.GUI_NEXT_PAGE.getString(
                    new Replacement("next_page", String.valueOf(page + 1)), totalPageReplacement);

            MenuItem nextPageItem = new MenuItem(Material.ARROW, nextPageDisplayName, (item, clicker) -> this.nextPage(clicker));
            this.setSlot(nextPageSlot, nextPageItem);
        }
        if(this.page > 1){
            String previousPageDisplayName = ConfigKeys.GUI_PREVIOUS_PAGE.getString(
                    new Replacement("previous_page", String.valueOf(page - 1)), totalPageReplacement);
            MenuItem previousPageItem = new MenuItem(Material.ARROW, previousPageDisplayName, (item, clicker) -> this.previousPage(clicker));
            this.setSlot(previousPageSlot, previousPageItem);
        }
        return this.buildInventory();
    }

    public List<T> getCurrentPageList(){
        List<T> objects = new ArrayList<>(this.getList());

        List<T> paginatedObjects = new ArrayList<>();

        int page = (this.page - 1);

        if(page < 0 || page >= getTotalPages()) return objects;

        int min = page * pageSize;
        int max = (min + pageSize);
        if(max > objects.size()) {
            max = objects.size();
        }
        for(int i = min; i < max; i++){
            paginatedObjects.add(objects.get(i));
        }
        return paginatedObjects;
    }

    public abstract Collection<T> getList();
}