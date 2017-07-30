package hu.diskay.audiocontrol.service.store;

public interface FileObjectStore<T> {
    void write(String path, T object);

    T read(String path);
}
