package hu.diskay.audiocontrol.service;

import hu.diskay.audiocontrol.controller.response.VolumeInformation;

public interface VolumeStore {
    int DEFAULT_VALUE = 20;
    boolean DEFAULT_MUTED_STATUS = false;

    void put(String deviceName, VolumeInformation volumeInformation);

    VolumeInformation get(String deviceName);
}
