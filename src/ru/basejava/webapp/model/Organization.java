package ru.basejava.webapp.model;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static java.util.Arrays.asList;
import static ru.basejava.webapp.util.DateUtil.of;

public class Organization {
    private final Link homePage;
    private final Set<Position> positions;

    {
        positions = new TreeSet<>(Comparator.comparing(p -> p.since));
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

    public static class Position implements Comparable<Position> {
        private final LocalDate since;
        private final LocalDate until;
        private final String title;
        private final String description;

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

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position that = (Position) o;
            return title.equals(that.title) && since.equals(that.since);
        }

        public int hashCode() {
            return title.hashCode() * 31 + since.hashCode();
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
}
