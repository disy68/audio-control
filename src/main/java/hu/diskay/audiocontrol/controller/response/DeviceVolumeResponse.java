package hu.diskay.audiocontrol.controller.response;

public class DeviceVolumeResponse {

    private final String deviceName;
    private final VolumeInformation volumeInformation;

    public DeviceVolumeResponse(String deviceName,
        VolumeInformation volumeInformation) {
        this.deviceName = deviceName;
        this.volumeInformation = volumeInformation;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public VolumeInformation getVolumeInformation() {
        return volumeInformation;
    }
}
