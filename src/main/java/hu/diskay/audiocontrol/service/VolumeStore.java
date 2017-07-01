package hu.diskay.audiocontrol.service;

public interface VolumeStore {

    void put(String deviceName, int volume);

    int get(String deviceName);
}
