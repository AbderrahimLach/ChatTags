package com.abderrahimlach.data.connection;

/**
 * @author AbderrahimLach
 */
public interface StorageConnection<T> {

    String getName();

    void connect();

    void close();

    T getConnection();
}
