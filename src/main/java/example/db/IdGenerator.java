package example.db;

import jakarta.inject.Singleton;

@Singleton
public class IdGenerator {

    private int id = 0;

    public synchronized int nextId() {
        id += 1;
        return id;
    }
}
