package ru.basejava.webapp.storage;

import ru.basejava.webapp.model.Resume;
import ru.basejava.webapp.exception.*;

public abstract class AbstractStorage implements Storage {

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doUpdate(Resume resume, Object searchKey);

    protected abstract void doSave(Resume resume, Object searchKey);

    protected abstract void doDelete(Object searchKey);

    protected abstract boolean isExist(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    @Override
    public Resume get(String uuid) {
        return doGet(getKeyForExisted(uuid));
    }

    @Override
    public void update(Resume resume) {
        doUpdate(resume, getKeyForExisted(resume.getUuid()));
    }

    @Override
    public void save(Resume resume) {
        doSave(resume, getKeyForNotExisted(resume.getUuid()));
    }

    @Override
    public void delete(String uuid) {
        doDelete(getKeyForExisted(uuid));
    }

    private Object getKeyForNotExisted(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey))
            throw new AlreadyExistStorageException(uuid);
        return searchKey;
    }

    private Object getKeyForExisted(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey))
            throw new NotExistStorageException(uuid);
        return searchKey;
    }
}
