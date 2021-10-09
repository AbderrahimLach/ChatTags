package com.abderrahimlach.data.connection.misc;

import lombok.Data;

/**
 * @author AbderrahimLach
 */
@Data
public class ConnectionCredentials {

    private final String host, username, password, database;
    private final int port;
    private final int poolSize;
}
