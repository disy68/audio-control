package hu.diskay.audiocontrol.service;

import java.io.File;
import java.io.IOException;

public class AudioVolumeServiceImpl implements AudioVolumeService {

    private static final float MAX_VALUE = 65535;
    private final File nircmdcTempFile;
    private final AudioDeviceService audioDeviceService;
    private final VolumeStore volumeStore;

    public AudioVolumeServiceImpl(
        String nircmdPath,
        TempFileService tempFileService,
        AudioDeviceService audioDeviceService,
        VolumeStore volumeStore) throws IOException {

        this.nircmdcTempFile = tempFileService.getTempFile(nircmdPath);
        this.audioDeviceService = audioDeviceService;
        this.volumeStore = volumeStore;
    }

    @Override
    public void setVolume(String deviceName, int newVolume) throws IOException, InterruptedException {
        switchToDevice(deviceName);
        processChange(deviceName, newVolume);
        switchToDefault();
    }

    @Override
    public int getVolume(String device) {
        return volumeStore.get(device);
    }

    @Override
    public void mute(String device) throws IOException, InterruptedException {
        // TODO: persist mute information
        switchToDevice(device);
        new ProcessBuilder().command(nircmdcTempFile.getAbsolutePath(), "mutesysvolume", "1").start();
        switchToDefault();
    }

    @Override
    public void unmute(String device) throws IOException, InterruptedException {
        // TODO: persist mute information
        switchToDevice(device);
        new ProcessBuilder().command(nircmdcTempFile.getAbsolutePath(), "mutesysvolume", "0").start();
        switchToDefault();
    }

    private void switchToDevice(String deviceName) throws IOException, InterruptedException {
        audioDeviceService.changeDevice(deviceName);
        Thread.sleep(100);
    }

    private void processChange(String deviceName, int newVolume) throws IOException {
        if (newVolume < 0) {
            newVolume = 0;
        } else if (newVolume > 100) {
            newVolume = 100;
        }

        float realValue = getRealValue(newVolume);

        new ProcessBuilder().command(nircmdcTempFile.getAbsolutePath(), "setsysvolume", String.format("%.1f", realValue)).start();

        volumeStore.put(deviceName, newVolume);
    }

    private float getRealValue(float volume) {
        return MAX_VALUE * (volume / 100);
    }

    private void switchToDefault() throws IOException, InterruptedException {
        Thread.sleep(100);
        audioDeviceService.changeToBaseDevice();
    }
}
