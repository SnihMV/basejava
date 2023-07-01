package ru.basejava.webapp.model;

import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    // Unique identifier
    private final String uuid;

    public Resume(String uuid) {
        this.uuid = uuid;
    }

    public Resume() {
        this(UUID.randomUUID().toString());
    }

    public String getUuid() {
        return uuid;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Resume that = (Resume) o;
        return uuid.equals(that.uuid);
    }

    public int hashCode(){
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }
}
