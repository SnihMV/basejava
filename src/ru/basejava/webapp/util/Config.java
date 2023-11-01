package ru.basejava.webapp.util;

import ru.basejava.webapp.storage.SqlStorage;
import ru.basejava.webapp.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private static final String PROPS_PATH = "config\\resumes.properties";
    private final File storageDir;
    private final Storage sqlStorage;


    private Config() {
        try(InputStream is = Files.newInputStream(Paths.get(PROPS_PATH))) {
            Properties properties = new Properties();
            properties.load(is);
            storageDir = new File(properties.getProperty("storage.dir"));
            sqlStorage = new SqlStorage(properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.password"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Storage getSqlStorage(){
        return sqlStorage;
    }

    public static Config getInstance(){
        return INSTANCE;
    }
}
