package ru.basejava.webapp.util;

import org.junit.Test;
import ru.basejava.webapp.model.Resume;
import ru.basejava.webapp.model.Section;
import ru.basejava.webapp.model.TextSection;

import static org.junit.Assert.assertEquals;
import static ru.basejava.webapp.TestData.R1;

public class JsonParserTest {

    @Test
    public void testResume() {
        String resumeJson = JsonParser.write(R1);
        System.out.println(resumeJson);
        Resume resume = JsonParser.read(resumeJson, Resume.class);
        assertEquals(R1, resume);
    }

    @Test
    public void testSection() {
        Section section = new TextSection("New text section R1");
        String sectionJson = JsonParser.write(section, Section.class);
        System.out.println(sectionJson);
        Section actual = JsonParser.read(sectionJson, Section.class);
        assertEquals(section, actual);
    }
}