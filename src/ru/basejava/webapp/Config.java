package ru.basejava.webapp;

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
    private final Storage storage;

    private Config() {
        try (InputStream is = Files.newInputStream(Paths.get(PROPS_PATH))) {
            Properties props = new Properties();
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            storage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getStorageDir(){
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }

    public static Config getInstance() {
        return INSTANCE;
    }
}
