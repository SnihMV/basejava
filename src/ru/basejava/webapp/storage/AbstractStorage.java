package ru.basejava.webapp.storage;

import ru.basejava.webapp.model.Resume;
import ru.basejava.webapp.exception.*;

public abstract class AbstractStorage<K> implements Storage {

    protected abstract Resume doGet(K searchKey);

    protected abstract void doUpdate(Resume resume, K searchKey);

    protected abstract void doSave(Resume resume, K searchKey);

    protected abstract void doDelete(K searchKey);

    protected abstract boolean isExist(K searchKey);

    protected abstract K getSearchKey(String uuid);

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

    private K getKeyForNotExisted(String uuid) {
        K searchKey = getSearchKey(uuid);
        if (isExist(searchKey))
            throw new AlreadyExistStorageException(uuid);
        return searchKey;
    }

    private K getKeyForExisted(String uuid) {
        K searchKey = getSearchKey(uuid);
        if (!isExist(searchKey))
            throw new NotExistStorageException(uuid);
        return searchKey;
    }
}
