package ru.basejava.webapp.storage;

import ru.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {

    private List<Resume> storage = new ArrayList<>();

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get((Integer) searchKey);
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        storage.set((Integer) searchKey, resume);
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        storage.add(resume);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove(((Integer) searchKey).intValue());
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid))
                return Integer.valueOf(i);
        }
        return null;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[size()]);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
