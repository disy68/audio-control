package hu.diskay.audiocontrol.controller.response;

public class VolumeInformation {

    private final int volume;
    private final boolean isMuted;

    public VolumeInformation(int volume, boolean isMuted) {
        this.volume = volume;
        this.isMuted = isMuted;
    }

    public int getVolume() {
        return volume;
    }

    public boolean isMuted() {
        return isMuted;
    }

    @Override
    public String toString() {
        return "VolumeInformation{" +
            "volume=" + volume +
            ", isMuted=" + isMuted +
            '}';
    }
}
