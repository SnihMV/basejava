package ru.basejava.webapp.storage;

import org.junit.Before;
import org.junit.Test;
import ru.basejava.webapp.exception.AlreadyExistStorageException;
import ru.basejava.webapp.exception.NotExistStorageException;
import ru.basejava.webapp.model.*;
import ru.basejava.webapp.util.Config;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.getInstance().getStorageDir();
    protected final Storage storage;
    private static final String UUID_1 = UUID.randomUUID().toString();
    private static final String UUID_2 = UUID.randomUUID().toString();
    private static final String UUID_3 = UUID.randomUUID().toString();
    private static final String UUID_4 = UUID.randomUUID().toString();
    public static final Resume R1;
    public static final Resume R2;
    public static final Resume R3;
    public static final Resume R4;

    static {
        R1 = new Resume(UUID_1, "Name1");
        R2 = new Resume(UUID_2, "Name2");
        R3 = new Resume(UUID_3, "Name3");
        R4 = new Resume(UUID_4, "Name4");

        R1.setContact(ContactType.EMAIL, "SnihMV@gmail.com");
        R1.setContact(ContactType.PHONE, "89216548976");
//        R1.addSection(SectionType.OBJECTIVE, new TextSection("Objective R1"));
//        R1.addSection(SectionType.PERSONAL, new TextSection("Personal data R1"));
//        R1.addSection(SectionType.ACHIEVEMENT, new ListSection("R1_Achievement3", "R1_Achievement2", "R1_Achievement1"));
//        R1.addSection(SectionType.QUALIFICATIONS, new ListSection("Java", "SQL", "Scala"));
//        R1.addSection(SectionType.EXPERIENCE, new OrganizationSection(
//                new Organization("OrgName1", "www.org1.ru",
//                        new Organization.Position(2005, Month.AUGUST, 2008, Month.MARCH, "R1_Title_1", "R1_descriptor_1"),
//                        new Organization.Position(2003, Month.SEPTEMBER, 2009, Month.NOVEMBER, "R1_Title_2", null))));
//        R1.addSection(SectionType.EDUCATION, new OrganizationSection(
//                new Organization("College", "www.college.com",
//                        new Organization.Position(1999, Month.SEPTEMBER, 2004, Month.JULY, "Aspirant", "learning"),
//                        new Organization.Position(1994, Month.SEPTEMBER, 1999, Month.JULY, "Student", null)),
//                new Organization("Institute", "www.cypress-institution.net")));
        R2.setContact(ContactType.GITHUB, "github.com/pupa");
        R2.setContact(ContactType.SKYPE, "R2_Skype");
//        R2.addSection(SectionType.EXPERIENCE, new OrganizationSection(
//                new Organization("OrgName2", "www.org2.su",
//                        new Organization.Position(2005, Month.AUGUST, 2008, Month.JULY, "R1_Title_1", "R1_descriptor_1"))));

    }

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void init() throws Exception {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @Test
    public void size() throws Exception {
        assertSize(3);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void get() throws Exception {
        assertGet(R1);
        assertGet(R2);
        assertGet(R3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistElem() throws Exception {
        storage.get("dummy");
    }

    @Test
    public void save() throws Exception {
        storage.save(R4);
        assertSize(4);
        assertGet(R4);
    }

    @Test(expected = AlreadyExistStorageException.class)
    public void saveAlreadyExist() throws Exception {
        storage.save(R1);
    }


    @Test
    public void update() throws Exception {
        Resume newResume = new Resume(UUID_2, "newName");
        storage.update(newResume);
        assertTrue(newResume.equals(storage.get(UUID_2)));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExistElem() throws Exception {
        storage.update(new Resume("dummy"));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(UUID_2);
        assertSize(2);
        assertGet(R2);
    }


    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistElem() throws Exception {
        storage.delete("dummy");
    }


    @Test
    public void getAllSorted() throws Exception {
        List<Resume> resumes = storage.getAllSorted();
        assertEquals(3, resumes.size());
        List<Resume> sortedResumes = Arrays.asList(R1, R2, R3);
        Collections.sort(sortedResumes);
        assertEquals(sortedResumes, resumes);
    }

    private void assertSize(int expectedSize) {
        assertEquals(expectedSize, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }
}