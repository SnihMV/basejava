package ru.basejava.webapp.exception;

public class StorageException extends RuntimeException {
    protected String uuid;

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }
}
