package ru.basejava.webapp.storage;

import ru.basejava.webapp.exception.StorageException;
import ru.basejava.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    @Override
    protected Resume doGet(Integer searchKey) {
        return storage[searchKey];
    }

    @Override
    protected void doSave(Resume resume, Integer searchKey) {
        if (isFull())
            throw new StorageException("Storage is full. Can't save new resume", resume.getUuid());
        insert(resume, searchKey);
        size++;
    }

    @Override
    protected void doUpdate(Resume resume, Integer searchKey) {
        storage[searchKey] = resume;
    }

    @Override
    protected void doDelete(Integer searchKey) {
        storage[searchKey] = null;
        if (searchKey != size - 1)
            fillDeleted(searchKey);
        size--;
    }

    public int size() {
        return size;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

    public boolean isFull() {
        return size == STORAGE_LIMIT;
    }

    protected abstract Integer getSearchKey(String uuid);

    protected abstract void insert(Resume r, int index);

    protected abstract void fillDeleted(int index);

}
