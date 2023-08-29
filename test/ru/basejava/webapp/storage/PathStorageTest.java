package ru.basejava.webapp.storage;

import ru.basejava.webapp.storage.serializer.ObjectStreamSerializer;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new ObjectStreamSerializer()));
    }
}