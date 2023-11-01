package ru.basejava.webapp.storage;

import ru.basejava.webapp.util.Config;

public class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest() {
        super(Config.getInstance().getSqlStorage());
    }
}