package ru.basejava.webapp.storage;

import ru.basejava.webapp.exception.StorageException;
import ru.basejava.webapp.model.Resume;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    protected final Path directory;

    protected AbstractPathStorage(String dir) {
        Objects.requireNonNull(dir, "Directory must be not null");
        this.directory = getValidDir(dir);
    }

    private Path getValidDir(String dir) {
        Path directory = Paths.get(dir);
        if (!Files.isDirectory(directory))
            throw new IllegalArgumentException(dir + " is not a directory");
        if (!Files.isWritable(directory) || !Files.isReadable(directory))
            throw new IllegalArgumentException(dir + " is not writable/writable");
        return directory;
    }

    protected abstract Resume doRead(Path path) throws IOException;

    protected abstract void doWrite(Path path, Resume resume) throws IOException;

    @Override
    protected Resume doGet(Path path) {
        try {
            return doRead(path);
        } catch (IOException e) {
            throw new StorageException("Path read error: " + path, getFileName(path), e);
        }
    }

    @Override
    protected void doSave(Resume resume, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Couldn't create file: " + path, getFileName(path), e);
        }
        doUpdate(resume, path);
    }

    @Override
    protected void doUpdate(Resume resume, Path path) {
        try {
            doWrite(path, resume);
        } catch (IOException e) {
            throw new StorageException("Path write error: " + path, getFileName(path), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error: " + path, getFileName(path), e);
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected List<Resume> doCopyAll() {
        return getFilesList().map(this::doGet).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        getFilesList().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getFilesList().count();
    }

    private String getFileName(Path path) {
        return path.getFileName().toString();
    }

    private Stream<Path> getFilesList() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Storage directory read error", e);
        }
    }
}