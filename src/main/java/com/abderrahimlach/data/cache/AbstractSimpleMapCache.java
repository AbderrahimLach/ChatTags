package com.abderrahimlach.data.cache;

import java.util.*;

/**
 * @author AbderrahimLach
 */
public abstract class AbstractSimpleMapCache<K, V> {

    protected final Map<K, V> objects = new HashMap<>();

    protected void addObject(K key, V value){
        this.objects.put(key, value);
    }

    protected V removeObject(K key){
        return objects.remove(key);
    }

    protected V get(K key){
        return objects.get(key);
    }

    protected boolean containsKey(K key){
        return objects.containsKey(key);
    }

    protected Map<K, V> getObjects() {
        return objects;
    }

    protected Set<Map.Entry<K, V>> entrySet(){
        return objects.entrySet();
    }

    protected Collection<V> values() {
        return objects.values();
    }
}
