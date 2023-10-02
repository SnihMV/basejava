package ru.basejava.webapp.storage;

import ru.basejava.webapp.exception.StorageException;
import ru.basejava.webapp.model.Resume;
import ru.basejava.webapp.storage.serializer.StreamSerializer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final StreamSerializer serializer;


    protected PathStorage(String pathName, StreamSerializer serializer) {
        Objects.requireNonNull(pathName, "Directory must be not null");
        this.directory = getDirectory(pathName);
        this.serializer = serializer;
    }

    private Path getDirectory(String pathName) {
        Path directory = Paths.get(pathName);
        if (Files.exists(directory)) {
            checkDir(directory);
        } else {
            try {
                Files.createDirectory(directory);
            } catch (IOException e) {
                throw new StorageException("Couldn't creat storage directory: " + pathName, e);
            }
        }
        return directory;
    }

    private static void checkDir(Path directory) {
        if (!Files.isDirectory(directory))
            throw new IllegalArgumentException(directory + " is not a directory");
        if (!Files.isWritable(directory) || !Files.isReadable(directory))
            throw new IllegalArgumentException(directory + " is not writable/writable");
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return serializer.doRead(new BufferedInputStream(Files.newInputStream(path)));
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
            serializer.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(path)));
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