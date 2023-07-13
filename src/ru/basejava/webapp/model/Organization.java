package ru.basejava.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Organization {
    private Link homePage;
    private String title;
    private String description;
    private LocalDate since;
    private LocalDate until;

    public Organization(String name, String url, String title, String description, LocalDate since, LocalDate until) {
        Objects.requireNonNull(title, "Organization title must be not null");
        Objects.requireNonNull(since, "Organization start date must be not null");
        Objects.requireNonNull(until, "Organization end date must be not null");
        this.homePage = new Link(name, url);
        this.title = title;
        this.description = description;
        this.since = since;
        this.until = until;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        if (!homePage.equals(that.homePage)) return false;
        if (!title.equals(that.title)) return false;
        if (!since.equals(that.since)) return false;
        if (!until.equals(that.until)) return false;
        return Objects.equals(description, that.description);
    }

    public int hashCode() {
        int res = homePage.hashCode();
        res = res * 37 + title.hashCode();
        res = res * 37 + since.hashCode();
        res = res * 37 + until.hashCode();
        res = res * 37 + (description != null ? description.hashCode() : 0);
        return res;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "homePage=" + homePage +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", since=" + since +
                ", until=" + until +
                '}';
    }

}
