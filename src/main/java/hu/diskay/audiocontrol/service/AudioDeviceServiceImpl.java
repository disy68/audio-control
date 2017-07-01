package hu.diskay.audiocontrol.service;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer.Info;

public class AudioDeviceServiceImpl implements AudioDeviceService {

    public static final String PREFIX = "Port";
    private final String nircmdcPath;
    private final List<String> enabledDeviceNames;
    private final String baseDeviceName;

    public AudioDeviceServiceImpl(String nircmdcPath, List<String> enabledDeviceNames, String baseDeviceName) {
        this.nircmdcPath = nircmdcPath;
        this.enabledDeviceNames = enabledDeviceNames;
        this.baseDeviceName = baseDeviceName;
    }

    @Override
    public List<String> getWhiteList() {
        return enabledDeviceNames;
    }

    @Override
    public List<Info> getDeviceInformations() {
        return Arrays.asList(AudioSystem.getMixerInfo());
    }

    @Override
    public List<Info> getEnabledDeviceInformations() {
        return getDeviceInformations().stream()
            .filter(this::isNotPort)
            .filter(this::isEnabled)
            .collect(toList());
    }

    private boolean isNotPort(Info info) {
        return !info.getName().startsWith(PREFIX);
    }

    @Override
    public void changeDevice(String deviceName) throws IOException {
        if (!enabledDeviceNames.contains(deviceName)) {
            throw new RuntimeException("The device name is not whitelisted.");
        }
        runChangeProcess(deviceName);
    }

    @Override
    public void changeToBaseDevice() throws IOException {
        runChangeProcess(baseDeviceName);
    }

    private void runChangeProcess(String deviceName) throws IOException {
        new ProcessBuilder().command(nircmdcPath, "setdefaultsounddevice", deviceName).start();
    }

    private boolean isEnabled(Info info) {
        return isNameEnabled(info.getName());
    }

    private boolean isNameEnabled(String name) {
        for (String enabled : enabledDeviceNames) {
            if (name.contains(enabled)) {
                return true;
            }
        }
        return false;
    }
}
