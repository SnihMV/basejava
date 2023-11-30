package ru.basejava.webapp.storage;

import ru.basejava.webapp.exception.NotExistStorageException;
import ru.basejava.webapp.model.ContactType;
import ru.basejava.webapp.model.Resume;
import ru.basejava.webapp.model.Section;
import ru.basejava.webapp.model.SectionType;
import ru.basejava.webapp.sql.SqlHelper;
import ru.basejava.webapp.util.JsonParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    /*@Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                "   SELECT r.full_name, c.type AS con_type, c.value, s.type AS sec_type, s.content \n" +
                "     FROM resume r LEFT JOIN contact c \n" +
                "       ON r.uuid = c.resume_uuid \n" +
                "LEFT JOIN section s ON r.uuid = s.resume_uuid" +
                "    WHERE r.uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                throw new NotExistStorageException(uuid);
            Resume resume = new Resume(uuid, rs.getString("full_name"));
                do {
                    addContact(rs, resume);
                    addSection(rs, resume);
                }
                while (rs.next());
            return resume;
        });
    }*/

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume resume;
            try (PreparedStatement ps = conn.prepareStatement("SELECT full_name FROM resume WHERE uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next())
                    throw new NotExistStorageException(uuid);
                resume = new Resume(uuid, rs.getString("full_name"));
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT type AS con_type, value FROM contact WHERE resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                    addContact(rs, resume);
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT type AS sec_type, content FROM section WHERE resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                    addSection(rs, resume);
            }
            return resume;
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            insertContacts(conn, resume);
            insertSections(conn, resume);
            return null;
        });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0)
                    throw new NotExistStorageException(resume.getUuid());
                deleteAttributes(conn, resume, "DELETE FROM contact WHERE resume_uuid = ?");
                deleteAttributes(conn, resume, "DELETE FROM section WHERE resume_uuid = ?");

                insertContacts(conn, resume);
                insertSections(conn, resume);
            }
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0)
                throw new NotExistStorageException(uuid);
            return null;
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

/*    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("" +
                "  SELECT r.uuid, r.full_name, c.type, c.value " +
                "    FROM resume r LEFT JOIN contact c " +
                "      ON r.uuid=c.resume_uuid " +
                "ORDER BY r.full_name, r.uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            Map<String, Resume> resumeMap = new LinkedHashMap<>();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                Resume resume = resumeMap.getOrDefault(uuid, new Resume(uuid, rs.getString("full_name")));
                addContact(rs, resume);
                resumeMap.put(uuid, resume);
            }
            return new ArrayList<>(resumeMap.values());
        });
    }*/


    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r ORDER BY full_name, uuid; ")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("" +
                    "SELECT resume_uuid AS uuid, type AS con_type, value FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = resumes.get(rs.getString("uuid"));
                    addContact(rs, r);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("" +
                    "SELECT resume_uuid AS uuid, type AS sec_type, content FROM section")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = resumes.get(rs.getString("uuid"));
                    addSection(rs, r);
                }
            }
            return new ArrayList<>(resumes.values());
        });
    }

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        if (value != null)
            resume.setContact(ContactType.valueOf(rs.getString("con_type")), value);
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String content = rs.getString("content");
        if (content != null)
            r.setSection(SectionType.valueOf(rs.getString("sec_type")), JsonParser.read(content, Section.class));
    }

    private void insertContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("" +
                "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, contact.getKey().name());
                ps.setString(3, contact.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("" +
                "INSERT INTO section (resume_uuid, type, content) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> section : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, section.getKey().name());
                ps.setString(3, JsonParser.write(section.getValue(), Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteAttributes(Connection conn, Resume resume, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }

}
