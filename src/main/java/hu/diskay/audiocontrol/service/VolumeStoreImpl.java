package hu.diskay.audiocontrol.service;

import java.util.HashMap;
import java.util.Map;

public class VolumeStoreImpl implements VolumeStore {

    public static final int DEFAULT_VALUE = 20;
    private final Map<String, Integer> volumes;

    public VolumeStoreImpl() {
        volumes = new HashMap<>();
    }

    @Override
    public void put(String deviceName, int volume) {
        volumes.put(deviceName, volume);
    }

    @Override
    public int get(String deviceName) {
        return volumes.getOrDefault(deviceName, DEFAULT_VALUE);
    }
}
