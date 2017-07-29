package hu.diskay.audiocontrol.service.db;

public class VolumeStoreEntry {
    private final String deviceName;
    private final int volumeValue;
    private final boolean muted;

    public VolumeStoreEntry(String deviceName, int volumeValue, boolean muted) {
        this.deviceName = deviceName;
        this.volumeValue = volumeValue;
        this.muted = muted;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getVolumeValue() {
        return volumeValue;
    }

    public boolean isMuted() {
        return muted;
    }
}
