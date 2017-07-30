package hu.diskay.audiocontrol.controller;

import static java.util.stream.Collectors.toList;

import hu.diskay.audiocontrol.controller.response.DeviceVolumeResponse;
import hu.diskay.audiocontrol.service.AudioDeviceService;
import hu.diskay.audiocontrol.service.store.VolumeStore;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceHandlerController.class);
    private final AudioDeviceService audioDeviceService;
    private final VolumeStore volumeStore;

    public DeviceHandlerController(
        AudioDeviceService audioDeviceService,
        VolumeStore volumeStore) {

        this.audioDeviceService = audioDeviceService;
        this.volumeStore = volumeStore;
    }

    @RequestMapping("/change-device/{name}")
    public String changeDevice(@PathVariable("name") String name) {
        try {
            audioDeviceService.changeDevice(name);
            return "ok";
        } catch (IOException e) {
            LOG.error("fuckup: {}", e);
            return "fuckup";
        }
    }

    @RequestMapping("/device-white-list")
    public List<DeviceVolumeResponse> getWhiteList() {
        return audioDeviceService.getWhiteList().stream()
            .map(this::getResponse)
            .collect(toList());
    }

    @RequestMapping("/device")
    public List<DeviceVolumeResponse> getDeviceInfo() {
        return audioDeviceService.getDeviceInformations().stream()
            .map(info -> getResponse(info.getName()))
            .collect(toList());
    }

    @RequestMapping("/enabled-device")
    public List<DeviceVolumeResponse> getEnabledDeviceInfo() {
        return audioDeviceService.getEnabledDeviceInformations().stream()
            .map(info -> getResponse(info.getName()))
            .collect(toList());
    }

    private DeviceVolumeResponse getResponse(String fullName) {
        int firstCaret = fullName.indexOf(" (");

        if (firstCaret >= 0) {
            fullName = fullName.substring(0, firstCaret);
        }

        return new DeviceVolumeResponse(fullName, volumeStore.get(fullName));
    }
}
