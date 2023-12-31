package ru.basejava.webapp.storage;

import org.junit.Test;
import ru.basejava.webapp.exception.StorageException;
import ru.basejava.webapp.model.Resume;

import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest{

    protected AbstractArrayStorageTest(Storage storage){
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveOverFlow(){
        try {
            for (int i = 4; i <= AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume("name"+i));
            }
        } catch (Exception e) {
            fail();
        }
        storage.save(new Resume("OverFlow"));
    }
}