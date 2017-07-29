package hu.diskay.audiocontrol.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class TempFileServiceImpl implements TempFileService {

    private final String workDir;
    private final Map<String, File> tempFileStore;

    public TempFileServiceImpl(String workDir) {
        this.workDir = workDir;
        tempFileStore = new HashMap<>();
    }

    @Override
    public synchronized File getTempFile(String resourcePath) throws IOException {
        if (tempFileStore.containsKey(resourcePath)) {
            return tempFileStore.get(resourcePath);
        }
        File tempFile = null;
        try (InputStream inputStream = getResourceAsStream(resourcePath)) {
            String pathname = workDir + "\\nircmdc.exe";
            tempFile = new File(pathname);
            tempFile.mkdirs();
            Path target = tempFile.toPath();

            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            tempFileStore.put(resourcePath, tempFile);
        }
        return tempFile;
    }

    private synchronized InputStream getResourceAsStream(String resourcePath) {
        return getClass().getClassLoader().getResourceAsStream(resourcePath);
    }
}
