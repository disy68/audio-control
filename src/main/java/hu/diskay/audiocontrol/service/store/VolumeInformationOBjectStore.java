package hu.diskay.audiocontrol.service.store;

import static java.util.Objects.isNull;

import hu.diskay.audiocontrol.controller.response.VolumeInformation;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class VolumeInformationOBjectStore implements FileObjectStore<VolumeInformation> {

    private static final Logger LOG = LoggerFactory.getLogger(VolumeInformationOBjectStore.class);
    public static final String WRITE_TO_FILE_ERROR_MSG = "Can't write to file %s";
    private final String delimiter;

    public VolumeInformationOBjectStore() {
        delimiter = "||";
    }

    @Override
    public synchronized void write(String path, VolumeInformation volumeInformation) {
        File file = new File(path);
        try {
            FileUtils.write(file, getContent(volumeInformation), Charsets.UTF_8);
        } catch (IOException e) {
            throwWriteException(path, e);
        }
    }

    @Override
    public synchronized VolumeInformation read(String path) {
        File file = new File(path);

        if (!file.exists()) {
            return null;
        }

        try {
            String fileContent = FileUtils.readFileToString(file);
            return parseContent(fileContent);
        } catch (IOException e) {
            LOG.info(e.getMessage());
            return null;
        }
    }

    private VolumeInformation parseContent(String fileContent) {
        if (isNull(fileContent)) {
            return null;
        }
        String[] parts = StringUtils.delimitedListToStringArray(fileContent, delimiter);

        if (parts.length == 2) {
            int volume = Integer.parseInt(parts[0]);
            boolean muted = Boolean.parseBoolean(parts[1]);

            return new VolumeInformation(volume, muted);
        }

        throw new StoreException(String.format("Parse exception with data: %s", fileContent));
    }

    private void createIfNotExists(String path, File file) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (Exception e) {
            throwWriteException(path, e);
        }
    }

    private void throwWriteException(String path, Exception e) {
        throw new StoreException(String.format(WRITE_TO_FILE_ERROR_MSG, path), e);
    }

    private String getContent(VolumeInformation volumeInformation) {
        StringBuilder builder = new StringBuilder();

        builder.append(volumeInformation.getVolume());
        builder.append(delimiter);
        builder.append(volumeInformation.isMuted());

        return builder.toString();
    }
}
