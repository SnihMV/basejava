import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int size = 0;

    void clear() {
        size = 0;
    }

    void save(Resume r) {
        if (size < storage.length && getIndex(r.uuid) == -1)
            storage[size++] = r;
    }

    Resume get(String uuid) {
        int i = getIndex(uuid);
        return i != -1 ? storage[i] : null;
    }

    void delete(String uuid) {
        int i = getIndex(uuid);
        if (i != -1)
            System.arraycopy(storage, i + 1, storage, i, --size - i);
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] copied = new Resume[size];
        System.arraycopy(storage, 0, copied, 0, size);
        return copied;
    }

    int size() {
        return size;
    }

    private int getIndex(String uuid) {
        if (!isEmpty())
            for (int i = 0; i < size; i++) {
                if (Objects.equals(storage[i].uuid, uuid))
                    return i;
            }
        return -1;
    }

    private boolean isEmpty() {
        return size == 0;
    }
}
