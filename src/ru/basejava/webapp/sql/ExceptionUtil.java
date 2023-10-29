package ru.basejava.webapp.sql;

import org.postgresql.util.PSQLException;
import ru.basejava.webapp.exception.AlreadyExistStorageException;
import ru.basejava.webapp.exception.StorageException;

import java.sql.SQLException;

public class ExceptionUtil {
    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException && e.getSQLState().equals("23505"))
            throw new AlreadyExistStorageException(null);
        return new StorageException(e);
    }

}
