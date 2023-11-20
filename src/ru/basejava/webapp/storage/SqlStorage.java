package ru.basejava.webapp.storage;

import ru.basejava.webapp.exception.NotExistStorageException;
import ru.basejava.webapp.model.ContactType;
import ru.basejava.webapp.model.Resume;
import ru.basejava.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                "   SELECT r.full_name, c.type, c.value \n" +
                "     FROM resume r LEFT JOIN contact c \n" +
                "       ON r.uuid=c.resume_uuid \n" +
                "    WHERE r.uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                throw new NotExistStorageException(uuid);
            Resume resume = new Resume(uuid, rs.getString("full_name"));
            if (rs.getString("type") != null)
                do addContact(rs, resume);
                while (rs.next());
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
                deleteContacts(conn, resume);
                insertContacts(conn, resume);
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

    @Override
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
    }


/*    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resmes =  new ArrayList<>(sqlHelper.transactionalExecute(conn -> {
            Map<String, Resume> resumes = new HashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r; ")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("" +
                    "SELECT resume_uuid as uuid, type, value FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumes.get(uuid).setContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                }
            }
            return resumes.values();
        }));
        Collections.sort(resmes);
        return resmes;
    }*/

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        String type = rs.getString("type");
        if (type != null)
            resume.setContact(ContactType.valueOf(type), rs.getString("value"));
    }

    private void deleteContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid=?")) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }

    private void insertContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, contact.getKey().name());
                ps.setString(3, contact.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
