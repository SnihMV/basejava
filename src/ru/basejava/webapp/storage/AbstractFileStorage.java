package ru.basejava.webapp.storage;

import ru.basejava.webapp.exception.StorageException;
import ru.basejava.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    protected final File directory;

    protected AbstractFileStorage(String dir) {
        Objects.requireNonNull(dir, "Directory path must be not null");
        this.directory = getValidDir(dir);
    }

    private File getValidDir(String dir){
        File directory = new File(dir);
        if (!directory.isDirectory())
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");
        if (!directory.canRead() || !directory.canWrite())
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        return directory;
    }

    protected abstract Resume doRead(File file) throws IOException;

    protected abstract void doWrite(File file, Resume resume) throws IOException;

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("File read error" + file.getAbsolutePath(), file.getName(), e);
        }
    }

    @Override
    protected void doSave(Resume resume, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Couldn't create file" + file.getName(), resume.getUuid(), e);
        }
        doUpdate(resume, file);
    }

    @Override
    protected void doUpdate(Resume resume, File file) {
        try {
            doWrite(file, resume);
        } catch (IOException e) {
            throw new StorageException("File write error", resume.getUuid(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete())
            throw new StorageException("File delete error", file.getName());
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected List<Resume> doCopyAll() {
        File[] files = directory.listFiles();
        if (files == null)
            throw new StorageException("Directory read error");
        ArrayList<Resume> resumes = new ArrayList<>(files.length);
        for (File file : files) {
            resumes.add(doGet(file));
        }
        return resumes;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                doDelete(file);
            }
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if (list == null)
            throw new StorageException("Directory read error");
        return list.length;
    }
}