package ru.basejava.webapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends Section {
    private final List<Organization> organizations;

    public OrganizationSection(Organization...organizations){
        this(new ArrayList<>(Arrays.asList(organizations)));
    }

    public OrganizationSection(List<Organization> organizations) {
        Objects.requireNonNull(organizations, "Organizations must be not null");
        this.organizations = organizations;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void addOrganization(Organization o) {
        organizations.add(o);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return organizations.equals(that.organizations);
    }

    public int hashCode() {
        return organizations.hashCode();
    }

    public String toString() {
        return organizations.toString();
    }

}
