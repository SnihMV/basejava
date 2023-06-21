package ru.basejava.webapp.exception;

public class NotExistStorageException extends StorageException {

    public NotExistStorageException(String uuid) {
        super("Resume with "+uuid+" does not exist in the storage",uuid);
    }
}
