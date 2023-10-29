package ru.basejava.webapp.storage;

import ru.basejava.webapp.Config;

import static org.junit.Assert.*;

public class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest() {
        super(Config.getInstance().getStorage());
    }
}