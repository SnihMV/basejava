package ru.basejava.webapp.model;

import java.util.Objects;

public class TextSection extends Section {
    private String content;

    public TextSection() {
    }

    public TextSection(String content) {
        Objects.requireNonNull(content, "Text content must not be null");
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        TextSection that = (TextSection) o;
        return Objects.equals(content, that.content);
    }

    public int hashCode() {
        return content.hashCode();
    }

    public String toString(){
        return content;
    }
}
