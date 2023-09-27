package ru.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Link implements Serializable {
    private String name;
    private String url;

    public Link() {
    }

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
        return Objects.equals(name, that.name) && Objects.equals(url, that.url);
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
