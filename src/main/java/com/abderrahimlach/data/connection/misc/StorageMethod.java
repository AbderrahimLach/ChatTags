package com.abderrahimlach.data.connection.misc;

import lombok.Getter;

/**
 * @author AbderrahimLach
 */
@Getter
public enum StorageMethod {

    MYSQL(3306),
    MONGODB(27017);

    private final int defaultPort;

    StorageMethod(int defaultPort) {
        this.defaultPort = defaultPort;
    }
}
