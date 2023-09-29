package ru.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationSection extends Section {
    private Collection<Organization> organizations;

    public OrganizationSection() {
    }

    public OrganizationSection(Organization...organizations){
        this(new ArrayList<>(Arrays.asList(organizations)));
    }

    public OrganizationSection(Collection<Organization> organizations) {
        Objects.requireNonNull(organizations, "Organizations must be not null");
//        this.organizations.addAll(organizations);
        this.organizations = organizations;
    }

    public Collection<Organization> getOrganizations() {
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
