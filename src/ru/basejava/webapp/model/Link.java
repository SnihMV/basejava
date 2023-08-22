package ru.basejava.webapp.model;

import java.io.Serializable;
import java.util.Objects;

public class Link implements Serializable {
    private final String name;
    private String url;

    public Link(String name, String url) {
        Objects.requireNonNull(name, "Organization name must be not null");
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link that = (Link) o;
        if (!name.equals(that.name)) return false;
        return Objects.equals(url, that.url);
    }

    public int hashCode() {
        int res = name.hashCode();
        res = res * 47 + (url != null ? url.hashCode() : 0);
        return res;
    }

    public String toString() {
        return name + (url != null ? (" URL: '" + url + "'") : "");
    }

}
