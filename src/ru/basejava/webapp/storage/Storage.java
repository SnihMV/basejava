package ru.basejava.webapp.storage;

import ru.basejava.webapp.model.Resume;

import java.util.List;

public interface Storage {

    void clear();

    Resume get(String uuid);

    void save(Resume r);

    void update(Resume resume);

    void delete(String uuid);

    int size();

    List<Resume> getAllSorted();

}
