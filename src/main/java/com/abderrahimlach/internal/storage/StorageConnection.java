package com.abderrahimlach.internal.storage;

/**
 * @author DirectPlan
 */
public interface StorageConnection {

    String getName();

    void connect();

    void close();
}
