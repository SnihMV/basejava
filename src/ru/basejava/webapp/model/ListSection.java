package ru.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListSection extends Section {
    private Collection<String> items = new ArrayList<>();

    public ListSection() {
    }

    public ListSection(String... items) {
        this(new ArrayList<>(Arrays.asList(items)));
    }

    public ListSection(Collection<String> items) {
        Objects.requireNonNull(items);
        this.items = items;
    }

    public Collection<String> getItems() {
        return items;
    }

    public void add(String item) {
        items.add(item);
    }

    public void remove(String item) {
        items.remove(item);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return items.equals(that.items);
    }

    public int hashCode() {
        return items.hashCode();
    }

    public String toString() {
        return items.toString();
    }
}
