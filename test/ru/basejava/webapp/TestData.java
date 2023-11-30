package ru.basejava.webapp;

import ru.basejava.webapp.model.*;

import java.time.Month;
import java.util.UUID;

public class TestData {
    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String UUID_4 = UUID.randomUUID().toString();
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
        R1.setSection(SectionType.OBJECTIVE, new TextSection("Objective R1"));
        R1.setSection(SectionType.PERSONAL, new TextSection("Personal data R1"));
        R1.setSection(SectionType.ACHIEVEMENT, new ListSection("R1_Achievement3", "R1_Achievement2", "R1_Achievement1"));
        R1.setSection(SectionType.QUALIFICATIONS, new ListSection("Java", "SQL", "Scala"));
        R1.setSection(SectionType.EXPERIENCE, new OrganizationSection(
                new Organization("OrgName1", "www.org1.ru",
                        new Organization.Position(2005, Month.AUGUST, 2008, Month.MARCH, "R1_Title_1", "R1_descriptor_1"),
                        new Organization.Position(2003, Month.SEPTEMBER, 2009, Month.NOVEMBER, "R1_Title_2", null))));
        R1.setSection(SectionType.EDUCATION, new OrganizationSection(
                new Organization("College", "www.college.com",
                        new Organization.Position(1999, Month.SEPTEMBER, 2004, Month.JULY, "Aspirant", "learning"),
                        new Organization.Position(1994, Month.SEPTEMBER, 1999, Month.JULY, "Student", null)),
                new Organization("Institute", "www.cypress-institution.net")));
        R2.setContact(ContactType.GITHUB, "github.com/pupa");
        R2.setContact(ContactType.SKYPE, "R2_Skype");
        R2.setSection(SectionType.EXPERIENCE, new OrganizationSection(
                new Organization("OrgName2", "www.org2.su",
                        new Organization.Position(2005, Month.AUGUST, 2008, Month.JULY, "R1_Title_1", "R1_descriptor_1"))));

    }
}
