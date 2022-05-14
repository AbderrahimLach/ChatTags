package com.abderrahimlach.internal.config;

import java.util.List;

/**
 * @author DirectPlan
 */
public interface ConfigurationMemory<K> {

    void set(K key, Object value);

    String getString(K key);

    boolean getBoolean(K key);

    int getInteger(K key);

    List<String> getStringList(K key);

    boolean containsKey(K key);

    Object get(K key);
}
