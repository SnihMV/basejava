package ru.basejava.webapp.storage;

import org.junit.Before;
import org.junit.Test;
import ru.basejava.webapp.exception.AlreadyExistStorageException;
import ru.basejava.webapp.exception.NotExistStorageException;
import ru.basejava.webapp.model.*;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public abstract class AbstractStorageTest {

    protected final Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    public static final Resume R1;
    public static final Resume R2;
    public static final Resume R3;

    static {
        R1 = new Resume(UUID_1, "Name1");
        R2 = new Resume(UUID_2, "Name2");
        R3 = new Resume(UUID_3, "Name3");

        R1.addContact(ContactType.EMAIL,"SnihMV@gmail.com");
        R1.addContact(ContactType.PHONE,"89216548976");
        R1.addSection(SectionType.OBJECTIVE, new TextSection("Objective R1"));
        R1.addSection(SectionType.PERSONAL, new TextSection("Personal data R1"));
        R1.addSection(SectionType.ACHIEVEMENT, new ListSection("R1_Achievement1", "R1_Achievement2", "R1_Achievement3"));
        R1.addSection(SectionType.QUALIFICATIONS, new ListSection("Java", "SQL", "Scala"));
        R1.addSection(SectionType.EXPERIENCE, new OrganizationSection(
                new Organization("OrgName1", "www.org1.ru",
                        new Organization.Position(2005, Month.AUGUST, 2008, Month.MARCH, "R1_Title_1", "R1_descriptor_1"),
                        new Organization.Position(2006, Month.SEPTEMBER,2009,Month.NOVEMBER, "R1_Title_2", null))));
        R1.addSection(SectionType.EDUCATION, new OrganizationSection(
                new Organization("College","www.college.com",
                        new Organization.Position(1999, Month.SEPTEMBER, 2004,Month.JULY, "Aspirant","learning"),
                        new Organization.Position(1994, Month.SEPTEMBER,1999,Month.JULY,"Student",null)),
                new Organization("Institute", "www.cypress-institution.net")));
        R2.addContact(ContactType.GITHUB,"github.com/pupa");
        R2.addContact(ContactType.SKYPE,"R2_Skype");
        R2.addSection(SectionType.EXPERIENCE, new OrganizationSection(
                new Organization("OrgName2", "www.org2.su",
                        new Organization.Position(2005, Month.AUGUST, "R1_Title_1", "R1_descriptor_1"))));

    }

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void init() {
        storage.clear();
        storage.save(R2);
        storage.save(R3);
        storage.save(R1);
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
        assertGet(R1);
        assertGet(R2);
        assertGet(R3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistElem() {
        storage.get("dummy");
    }

    @Test
    public void save() {
        Resume newResume = new Resume("name4");
        storage.save(newResume);
        assertSize(4);
        assertGet(newResume);
    }

    @Test(expected = AlreadyExistStorageException.class)
    public void saveAlredyExist() {
        storage.save(R1);
    }


    @Test
    public void update() {
        Resume newResume = new Resume(UUID_2, "newName");
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
        assertGet(R2);
    }


    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistElem() {
        storage.delete("dummy");
    }


    @Test
    public void getAllSorted() {
        List<Resume> resumes = storage.getAllSorted();
        assertEquals(3, resumes.size());
        assertEquals(resumes, Arrays.asList(R1, R2, R3));
    }

    private void assertSize(int expectedSize) {
        assertEquals(expectedSize, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }
}