package ru.basejava.webapp.storage;

import ru.basejava.webapp.model.Resume;

import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10_000];
    private int size;

    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index != -1)
            storage[index]=resume;
        else System.err.println("No such resume to update");
    }

    public void save(Resume r) {
        if (size == storage.length) {
            System.err.println("Can't save this resume due to the storage is full");
            return;
        }
        if (getIndex(r.getUuid()) > -1) {
            System.err.println("Resume with the same uuid is already contained in the storage");
            return;
        }
        storage[size++] = r;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        return index > -1 ? storage[index] : null;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index > -1)
            System.arraycopy(storage, index + 1, storage, index, --size - index);
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] allResumes = new Resume[size];
        System.arraycopy(storage, 0, allResumes, 0, size);
        return allResumes;
    }

    public int size() {
        return size;
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(storage[i].getUuid(), uuid))
                return i;
        }
        return -1;
    }
}
