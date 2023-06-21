package ru.basejava.webapp.storage;

import ru.basejava.webapp.exception.AlreadyExistStorageException;
import ru.basejava.webapp.exception.NotExistStorageException;
import ru.basejava.webapp.exception.StorageException;
import ru.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {

    protected static final int STORAGE_LIMIT = 100_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0)
            throw new NotExistStorageException(uuid);
        return storage[index];
    }

    public void save(Resume r) {
        if (!isFull()) {
            int index = getIndex(r.getUuid());
            if (index < 0) {
                insert(r, index);
                size++;
            } else throw new AlreadyExistStorageException(r.getUuid());
        } else throw new StorageException("Storage overflow", r.getUuid());
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0)
            storage[index] = r;
        else throw new NotExistStorageException(r.getUuid());
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            storage[index] = null;
            if (index != size - 1)
                fillDeleted(index);
            size--;
        } else throw new NotExistStorageException(uuid);
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public boolean isFull() {
        return size == STORAGE_LIMIT;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void insert(Resume r, int index);

    protected abstract void fillDeleted(int index);
}
