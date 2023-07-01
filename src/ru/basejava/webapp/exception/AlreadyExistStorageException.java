package ru.basejava.webapp.exception;

public class AlreadyExistStorageException extends StorageException {

    public AlreadyExistStorageException(String uuid) {
        super("Resume with uuid '"+uuid+"' already exist in the storage",uuid);
    }
}
