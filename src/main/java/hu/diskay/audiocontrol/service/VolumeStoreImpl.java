package hu.diskay.audiocontrol.service;

import hu.diskay.audiocontrol.controller.response.VolumeInformation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VolumeStoreImpl implements VolumeStore {

    private final ConcurrentMap<String, VolumeInformation> volumeInformations;

    public VolumeStoreImpl() {
        volumeInformations = new ConcurrentHashMap<>();
    }

    @Override
    public void put(String deviceName, VolumeInformation volumeInformation) {
        volumeInformations.put(deviceName, volumeInformation);
    }

    @Override
    public VolumeInformation get(String deviceName) {
        return volumeInformations.getOrDefault(deviceName, getDefaultInformation());
    }

    private VolumeInformation getDefaultInformation() {
        return new VolumeInformation(DEFAULT_VALUE, DEFAULT_MUTED_STATUS);
    }
}
