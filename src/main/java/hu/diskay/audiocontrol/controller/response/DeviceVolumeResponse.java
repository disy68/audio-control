package hu.diskay.audiocontrol.controller.response;

public class DeviceVolumeResponse {

    private final String deviceName;
    private final int volume;

    public DeviceVolumeResponse(String deviceName, int volume) {
        this.deviceName = deviceName;
        this.volume = volume;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getVolume() {
        return volume;
    }
}
