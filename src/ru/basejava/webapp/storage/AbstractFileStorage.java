package ru.basejava.webapp.storage;

import ru.basejava.webapp.model.Resume;

import java.io.File;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    protected final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "Directory must be not null");
        if (!directory.isDirectory())
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");
        if (!directory.canRead() || !directory.canWrite())
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        this.directory = directory;
    }

    protected abstract Resume doRead(File file);

    protected abstract void doWrite(File file, Resume resume);

    @Override
    protected Resume doGet(File file) {
        return doRead(file);
    }
    @Override
    protected void doSave(Resume resume, File file) {
        doWrite(file, resume);
    }


    @Override
    protected void doUpdate(Resume resume, File searchKey) {

    }

    @Override
    protected void doDelete(File searchKey) {

    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File searchKey) {
        return searchKey.exists();
    }

    @Override
    protected List<Resume> doCopyAll() {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }
}