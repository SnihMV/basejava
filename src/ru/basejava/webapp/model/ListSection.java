package ru.basejava.webapp.model;

import java.util.List;
import java.util.Objects;

public class ListSection extends Section {
    private final List<String> items;

    public ListSection(List<String> items) {
        Objects.requireNonNull(items);
        this.items = items;
    }

    public List<String> getItems(){
        return items;
    }

    public void add(String item){
        items.add(item);
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
