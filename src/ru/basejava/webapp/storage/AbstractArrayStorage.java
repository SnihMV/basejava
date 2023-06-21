package ru.basejava.webapp.storage;

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
        return index >= 0 ? storage[index] : null;
    }

    public void save(Resume r) {
        if (!isFull()) {
            int index = getIndex(r.getUuid());
            if (index < 0) {
                insert(r, index);
                size++;
            }
            else System.err.println("Such resume already exists in the storage");
        } else System.err.println("Can't save this resume due to the storage is full");
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0)
            storage[index] = r;
        else System.err.println("The storage doesn't contain such resume to update");
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0)
            System.arraycopy(storage, index + 1, storage, index, --size - index);
        else System.err.println("The storage doesn't contain such resume to delete");
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public boolean isFull() {
        return size == STORAGE_LIMIT;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void insert(Resume r, int index);
}
