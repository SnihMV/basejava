package ru.basejava.webapp.storage;

import ru.basejava.webapp.exception.*;
import ru.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
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

    @Override
    protected Resume doGet(Object searchKey) {
        return storage[(Integer) searchKey];
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        storage[(Integer) searchKey] = resume;
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        if (isFull())
            throw new StorageException("Storage is full. Can't save new resume", resume.getUuid());
        insert(resume, (Integer) searchKey);
        size++;
    }

    @Override
    protected void doDelete(Object searchKey) {
        int index = (Integer) searchKey;
        storage[index] = null;
        if (index != size - 1)
            fillDeleted(index);
        size--;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (Integer) searchKey >= 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public boolean isFull() {
        return size == STORAGE_LIMIT;
    }

    protected abstract Integer getSearchKey(String uuid);

    protected abstract void insert(Resume r, int index);

    protected abstract void fillDeleted(int index);
}
