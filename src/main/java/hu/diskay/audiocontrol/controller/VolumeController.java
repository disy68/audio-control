package hu.diskay.audiocontrol.controller;

import hu.diskay.audiocontrol.service.AudioVolumeService;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VolumeController {

    private static final Logger LOG = LoggerFactory.getLogger(VolumeController.class);

    private final AudioVolumeService audioVolumeService;

    public VolumeController(AudioVolumeService audioVolumeService) {
        this.audioVolumeService = audioVolumeService;
    }

    @RequestMapping("/set-volume/{device}/{volume}")
    public String setVolume(@PathVariable("volume") int volume, @PathVariable("device") String device) {
        try {
            audioVolumeService.setVolume(device, volume);
            return "ok";
        } catch (IOException | InterruptedException e) {
            LOG.error("Fuckup: {}", e);
            return "fuckup";
        }
    }

    @RequestMapping("/mute/{device}")
    public String mute(@PathVariable("device") String device) {
        try {
            audioVolumeService.mute(device);
            return "ok";
        } catch (IOException | InterruptedException e) {
            LOG.error("Fuckup: {}", e);
            return "fuckup";
        }
    }

    @RequestMapping("/unmute/{device}")
    public String unmute(@PathVariable("device") String device) {
        try {
            audioVolumeService.unmute(device);
            return "ok";
        } catch (IOException | InterruptedException e) {
            LOG.error("Fuckup: {}", e);
            return "fuckup";
        }
    }
}
