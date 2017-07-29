package hu.diskay.audiocontrol.config;

import hu.diskay.audiocontrol.controller.DeviceHandlerController;
import hu.diskay.audiocontrol.controller.VolumeController;
import hu.diskay.audiocontrol.service.AudioDeviceService;
import hu.diskay.audiocontrol.service.AudioDeviceServiceImpl;
import hu.diskay.audiocontrol.service.AudioVolumeService;
import hu.diskay.audiocontrol.service.AudioVolumeServiceImpl;
import hu.diskay.audiocontrol.service.db.DbVolumeStore;
import hu.diskay.audiocontrol.service.TempFileService;
import hu.diskay.audiocontrol.service.TempFileServiceImpl;
import hu.diskay.audiocontrol.service.VolumeStore;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableAutoConfiguration
@PropertySource({
    "file:/${WORK_DIR}/config/device.properties",
    "file:/${WORK_DIR}/config/application.properties"
})
@Import({
    DatabaseConfig.class,
    ComponentConfig.class
})
public class ApplicationConfig {

    @Autowired
    private Environment env;

    @Bean
    public AudioDeviceService audioDeviceService(TempFileService tempFileService) throws IOException {
        List<String> allowed = PropertyReader.getStrings("devices.allowed", env);
        String path = PropertyReader.getString("util.nircmdc.path", env);
        String baseDeviceName = PropertyReader.getString("devices.base", env);

        return new AudioDeviceServiceImpl(path, tempFileService, allowed, baseDeviceName);
    }

    @Bean
    public VolumeStore volumeStore(JdbcTemplate jdbcTemplate) {
        return new DbVolumeStore(jdbcTemplate);
    }

    @Bean
    public DeviceHandlerController deviceHandlerController(
        AudioDeviceService audioDeviceService,
        VolumeStore volumeStore) {

        return new DeviceHandlerController(audioDeviceService, volumeStore);
    }

    @Bean
    public TempFileService tempFileService() {
        String workDir = PropertyReader.getString("WORK_DIR", env);

        return new TempFileServiceImpl(workDir);
    }

    @Bean
    public AudioVolumeService audioVolumeService(
        TempFileService tempFileService,
        AudioDeviceService audioDeviceService,
        VolumeStore volumeStore)
        throws IOException {

        String path = PropertyReader.getString("util.nircmdc.path", env);

        return new AudioVolumeServiceImpl(path, tempFileService, audioDeviceService, volumeStore);
    }

    @Bean
    public VolumeController volumeController(AudioVolumeService audioVolumeService) {
        return new VolumeController(audioVolumeService);
    }
}
