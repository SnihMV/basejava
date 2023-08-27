package ru.basejava.webapp.storage;

import ru.basejava.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ObjectStreamPathStorage extends AbstractPathStorage {
    protected ObjectStreamPathStorage(String directory) {
        super(directory);
    }

    @Override
    protected void doWrite(Path path, Resume resume) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(path)))) {
            out.writeObject(resume);
        }
    }

    @Override
    protected Resume doRead(Path path) throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
            return (Resume) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
