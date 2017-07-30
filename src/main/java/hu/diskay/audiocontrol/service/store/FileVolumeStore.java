package hu.diskay.audiocontrol.service.store;

import static java.util.Objects.nonNull;

import hu.diskay.audiocontrol.controller.response.VolumeInformation;
import java.nio.file.Paths;
import org.apache.commons.codec.digest.DigestUtils;

public class FileVolumeStore implements VolumeStore {

    private final String storeDirectoryPath;
    private final FileObjectStore<VolumeInformation> objectStore;

    public FileVolumeStore(String storeDirectoryPath, FileObjectStore<VolumeInformation> objectStore) {
        this.storeDirectoryPath = storeDirectoryPath;
        this.objectStore = objectStore;
    }

    @Override
    public void put(String deviceName, VolumeInformation volumeInformation) {
        String filePath = getFilePath(deviceName);

        objectStore.write(filePath, volumeInformation);
    }

    @Override
    public VolumeInformation get(String deviceName) {
        String filePath = getFilePath(deviceName);

        VolumeInformation volumeInformation = objectStore.read(filePath);

        if (nonNull(volumeInformation)) {
            return volumeInformation;
        } else {
            return getDefaultInformation();
        }
    }

    private String getFilePath(String deviceName) {
        String shaName = DigestUtils.sha1Hex(deviceName);

        return Paths.get(storeDirectoryPath, shaName).toString();
    }
}
