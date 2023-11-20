package ru.basejava.webapp.storage.serializer;

import ru.basejava.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            writeCollection(dos, resume.getContacts().entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            writeCollection(dos, resume.getSections().entrySet(), entry -> {
                SectionType type = entry.getKey();
                Section section = entry.getValue();
                dos.writeUTF(type.name());
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(((TextSection) section).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeCollection(dos, ((ListSection) section).getItems(), dos::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeCollection(dos, ((OrganizationSection) section).getOrganizations(), org -> {
                            dos.writeUTF(org.getHomePage().getName());
                            writeNullable(dos, org.getHomePage().getUrl(), dos::writeUTF);
                            writeCollection(dos, org.getPositions(), pos -> {
                                writeLocalDate(dos, pos.getSince());
                                writeNullable(dos, pos.getUntil(), until -> writeLocalDate(dos, until));
                                dos.writeUTF(pos.getTitle());
                                writeNullable(dos, pos.getDescription(), dos::writeUTF);
                            });
                        });
                        break;
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readItems(dis, () -> resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readItems(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.setSection(sectionType, readSection(dis, sectionType));
            });
            return resume;
        }
    }

    private Section readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                return new TextSection(dis.readUTF());
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return new ListSection(readCollection(dis, dis::readUTF));
            case EXPERIENCE:
            case EDUCATION:
                return new OrganizationSection(readCollection(dis, () ->
                        new Organization(new Link(dis.readUTF(), readNullable(dis, dis::readUTF)),
                                readCollection(dis, () ->
                                        new Organization.Position(readLocalDate(dis),
                                                readNullable(dis, () -> readLocalDate(dis)),
                                                dis.readUTF(), readNullable(dis, dis::readUTF))
                                ))
                ));
            default:
                throw new IllegalStateException();
        }
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }

    private <T> T readNullable(DataInputStream dis, ElementReader<T> elementReader) throws IOException {
        return dis.readBoolean() ? elementReader.read() : null;
    }

    private <T> Collection<T> readCollection(DataInputStream dis, ElementReader<T> elementReader) throws IOException {
        int size = dis.readInt();
        Collection<T> collection = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            collection.add(elementReader.read());
        }
        return collection;
    }


    private <T> void writeNullable(DataOutputStream dos, T element, ElementWriter<T> elementWriter) throws IOException {
        if (element != null) {
            dos.writeBoolean(true);
            elementWriter.write(element);
        } else dos.writeBoolean(false);
    }

    private void writeLocalDate(DataOutputStream dos, LocalDate ld) throws IOException {
        dos.writeInt(ld.getYear());
        dos.writeInt(ld.getMonth().getValue());
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, ElementWriter<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T item : collection) {
            writer.write(item);
        }
    }

    private void readItems(DataInputStream dis, ElementProcessor processor) throws IOException {
        int count = dis.readInt();
        for (int i = 0; i < count; i++) {
            processor.process();
        }
    }

    private interface ElementReader<E> {
        E read() throws IOException;
    }

    private interface ElementWriter<E> {
        void write(E e) throws IOException;
    }

    private interface ElementProcessor {
        void process() throws IOException;
    }
}
