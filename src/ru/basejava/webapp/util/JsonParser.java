package ru.basejava.webapp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.basejava.webapp.model.Resume;
import ru.basejava.webapp.model.Section;

import java.io.Reader;
import java.io.Writer;

public class JsonParser {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Section.class, new JsonSectionAdapter<>())
            .create();

    public static <T> T read(Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }

    public static <T> void write(T object, Writer writer) {
        GSON.toJson(object, writer);
    }

    public static <T> T read(String content, Class<T> clazz) {
        return GSON.fromJson(content, clazz);
    }

    public static <T> String write(T object, Class<T> klass) {
        return GSON.toJson(object, klass);
    }

    public static <T> String write(T object) {
        return GSON.toJson(object);
    }
}
