package ru.basejava.webapp.exception;

import org.postgresql.util.PSQLException;

import java.sql.SQLException;

public class ExceptionUtil {
    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException && e.getSQLState().equals("23505"))
            return new AlreadyExistStorageException(null);
        return new StorageException(e);
    }
}
