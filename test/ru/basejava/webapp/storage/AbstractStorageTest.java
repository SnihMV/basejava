package ru.basejava.webapp.storage;

import org.junit.Before;
import org.junit.Test;
import ru.basejava.webapp.exception.AlreadyExistStorageException;
import ru.basejava.webapp.exception.NotExistStorageException;
import ru.basejava.webapp.exception.StorageException;
import ru.basejava.webapp.model.Resume;

import static org.junit.Assert.*;

public abstract class AbstractStorageTest {

    protected final Storage storage;
    private static final String UUID_1 = "uuid_1";
    public static final Resume RESUME_1 = new Resume(UUID_1);
    private static final String UUID_2 = "uuid_2";
    public static final Resume RESUME_2 = new Resume(UUID_2);
    private static final String UUID_3 = "uuid_3";
    public static final Resume RESUME_3 = new Resume(UUID_3);

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void init() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistElem() {
        storage.get("dummy");
    }

    @Test
    public void save() {
        Resume newResume = new Resume();
        storage.save(newResume);
        assertSize(4);
        assertGet(newResume);
    }

    @Test(expected = AlreadyExistStorageException.class)
    public void saveAlredyExist() {
        storage.save(RESUME_1);
    }


    @Test
    public void update() {
        Resume newResume = new Resume(UUID_2);
        storage.update(newResume);
        assertTrue(newResume == storage.get(UUID_2));
        assertSize(3);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExistElem() {
        storage.update(new Resume("dummy"));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_2);
        assertSize(2);
        assertGet(RESUME_2);
    }


    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistElem() {
        storage.delete("dummy");
    }


    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        assertEquals(3, resumes.length);
        for (Resume r : resumes) {
            assertGet(r);
        }
    }

    private void assertSize(int expectedSize) {
        assertEquals(expectedSize, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }
}