package hu.diskay.audiocontrol.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class TempFileServiceImpl implements TempFileService {

    private final String workDir;

    public TempFileServiceImpl(String workDir) {
        this.workDir = workDir;
    }

    @Override
    public File getTempFile(String resourcePath) throws IOException {
        File tempFile = null;
        try (InputStream inputStream = getResourceAsStream(resourcePath)) {
            String pathname = workDir + "\\nircmdc.exe";
            tempFile = new File(pathname);
            tempFile.mkdirs();
            Path target = tempFile.toPath();

            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile;
    }

    private InputStream getResourceAsStream(String resourcePath) {
        return getClass().getClassLoader().getResourceAsStream(resourcePath);
    }
}
