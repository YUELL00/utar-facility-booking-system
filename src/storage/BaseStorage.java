package storage;

public abstract class BaseStorage {
    protected String filePath;

    public BaseStorage(String filePath) {
        this.filePath = filePath;
    }
}