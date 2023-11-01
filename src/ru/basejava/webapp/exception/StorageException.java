package ru.basejava.webapp.exception;

import java.sql.SQLException;

public class StorageException extends RuntimeException {
    protected String uuid;

    public StorageException(String message, String uuid, Throwable cause) {
        super(message, cause);
        this.uuid = uuid;
    }

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public StorageException(String message) {
        this(message, null, null);
    }

    public StorageException(Exception e) {
        this(e.getMessage(), e);
    }
}
