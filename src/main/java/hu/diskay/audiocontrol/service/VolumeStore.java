package hu.diskay.audiocontrol.service;

import hu.diskay.audiocontrol.controller.response.VolumeInformation;

public interface VolumeStore {

    void put(String deviceName, VolumeInformation volumeInformation);

    VolumeInformation get(String deviceName);
}
