package hu.diskay.audiocontrol.service;

import static java.util.Objects.nonNull;

import hu.diskay.audiocontrol.controller.response.VolumeInformation;
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
        VolumeInformation volumeInformation = createVolumeInformation(deviceName, newVolume);
        processVolumeChange(deviceName, volumeInformation);
        switchToDefault();
    }

    @Override
    public VolumeInformation getVolumeInformation(String device) {
        return volumeStore.get(device);
    }

    @Override
    public void mute(String device) throws IOException, InterruptedException {
        switchToDevice(device);
        VolumeInformation volumeInformation = createVolumeInformation(device, true);
        processMutedChange(device, volumeInformation);
        switchToDefault();
    }

    @Override
    public void unmute(String device) throws IOException, InterruptedException {
        switchToDevice(device);
        VolumeInformation volumeInformation = createVolumeInformation(device, false);
        processMutedChange(device, volumeInformation);
        switchToDefault();
    }

    private void processMutedChange(String deviceName, VolumeInformation volumeInformation)
        throws IOException {

        if (volumeInformation.isMuted()) {
            new ProcessBuilder().command(nircmdcTempFile.getAbsolutePath(), "mutesysvolume", "1").start();
        } else {
            new ProcessBuilder().command(nircmdcTempFile.getAbsolutePath(), "mutesysvolume", "0").start();
        }

        volumeStore.put(deviceName, volumeInformation);
    }

    private void switchToDevice(String deviceName) throws IOException, InterruptedException {
        audioDeviceService.changeDevice(deviceName);
        Thread.sleep(100);
    }

    private VolumeInformation createVolumeInformation(String deviceName, int volume) {
        VolumeInformation volumeInformation = volumeStore.get(deviceName);
        boolean muted = false;
        if (nonNull(volumeInformation)) {
            muted = volumeInformation.isMuted();
        }
        return new VolumeInformation(volume, muted);
    }

    private VolumeInformation createVolumeInformation(String deviceName, boolean muted) {
        VolumeInformation volumeInformation = volumeStore.get(deviceName);
        int volume = VolumeStoreImpl.DEFAULT_VALUE;
        if (nonNull(volumeInformation)) {
            volume = volumeInformation.getVolume();
        }
        return new VolumeInformation(volume, muted);
    }

    private void processVolumeChange(
        String deviceName,
        VolumeInformation volumeInformation)
        throws IOException {

        int newVolume = volumeInformation.getVolume();

        if (newVolume < 0) {
            newVolume = 0;
        } else if (newVolume > 100) {
            newVolume = 100;
        }

        float realValue = getRealValue(newVolume);

        new ProcessBuilder()
            .command(
                nircmdcTempFile.getAbsolutePath(),
                "setsysvolume",
                String.format("%.1f", realValue))
            .start();

        volumeStore.put(deviceName, volumeInformation);
    }

    private float getRealValue(float volume) {
        return MAX_VALUE * (volume / 100);
    }

    private void switchToDefault() throws IOException, InterruptedException {
        Thread.sleep(100);
        audioDeviceService.changeToBaseDevice();
    }
}
