package ru.basejava.webapp.model;

import java.util.Objects;

/**
 * Initial resume class
 */
public class Resume {

    // Unique identifier
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Resume that = (Resume) o;
        return Objects.equals(uuid, that.uuid);
    }

    public int hashCode(){
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return uuid;
    }
}
