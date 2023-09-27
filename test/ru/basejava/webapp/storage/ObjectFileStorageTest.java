package ru.basejava.webapp.storage;

import ru.basejava.webapp.storage.serializer.ObjectStreamSerializer;

public class ObjectFileStorageTest extends AbstractStorageTest {

    public ObjectFileStorageTest() {
        super(new FileStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStreamSerializer()));
    }
}