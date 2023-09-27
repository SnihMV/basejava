package ru.basejava.webapp.model;

import ru.basejava.webapp.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static java.util.Arrays.asList;
import static ru.basejava.webapp.util.DateUtil.of;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    private Link homePage;
    private Set<Position> positions = new TreeSet<>(new SinceComparator());

    public Organization() {
    }

    public Organization(String name, String url, Position... positions) {
        this(new Link(name, url), asList(positions));
    }

    public Organization(Link homePage, Collection<Position> positions) {
        Objects.requireNonNull(homePage, "Homepage must be not null");
        Objects.requireNonNull(positions, "Positions list must be not null");
        this.homePage = homePage;
        this.positions.addAll(positions);
    }

    public void addPosition(Position position) {
        positions.add(position);
    }

    public void removePosition(Position position) {
        positions.remove(position);
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Organization that = (Organization) other;
        return homePage.equals(that.homePage);
    }

    public int hashCode() {
        return homePage.hashCode();
    }

    @Override
    public String toString() {
        return "Organization{"
                + homePage +
                ", positions=" + positions +
                '}';
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Comparable<Position>, Serializable {
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate since;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate until;
        private String title;
        private String description;

        public Position() {
        }

        public Position(int startYear, Month startMonth, int endYear, Month endMonth, String title, String description) {
            this(of(startYear, startMonth), of(endYear, endMonth), title, description);
        }

        public Position(int startYear, Month startMonth, String title, String description) {
            this(of(startYear, startMonth), null, title, description);
        }

        public Position(LocalDate since, LocalDate until, String title, String description) {
            Objects.requireNonNull(since, "Position start date must not be null");
            Objects.requireNonNull(title, "Position title must not be null");
            this.since = since;
            this.until = until;
            this.title = title;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public LocalDate getSince() {
            return since;
        }

        public LocalDate getUntil() {
            return until;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return Objects.equals(since, position.since) && Objects.equals(until, position.until) && Objects.equals(title, position.title) && Objects.equals(description, position.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(since, until, title, description);
        }

        @Override
        public String toString() {
            return "Position [" + since + " - " + (until != null ? until : "NOW") + " \"" + title + "\"]";
        }

        @Override
        public int compareTo(Position that) {
            int res = title.compareTo(that.title);
            return res != 0 ? res : since.compareTo(that.since);
        }
    }

    private static class SinceComparator implements Comparator<Position>, Serializable {
        @Override
        public int compare(Position o1, Position o2) {
            return o1.since.compareTo(o2.since);
        }
    }

}
