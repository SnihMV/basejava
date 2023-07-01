package ru.basejava.webapp.storage;

import org.junit.Assert;
import ru.basejava.webapp.exception.StorageException;

public class ListStorageTest extends AbstractStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

}