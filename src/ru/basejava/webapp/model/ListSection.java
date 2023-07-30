package ru.basejava.webapp.model;

import java.util.*;

public class ListSection extends Section {
    private final Set<String> items;

    public ListSection(String... items) {
        this(new LinkedHashSet<>(Arrays.asList(items)));
    }

    public ListSection(Set<String> items) {
        Objects.requireNonNull(items);
        this.items = items;
    }

    public Set<String> getItems(){
        return items;
    }

    public void add(String item){
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

    public int hashCode(){
        return items.hashCode();
    }

    public String toString(){
        return items.toString();
    }
}
