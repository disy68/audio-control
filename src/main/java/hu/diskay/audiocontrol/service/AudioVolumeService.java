package hu.diskay.audiocontrol.service;

import hu.diskay.audiocontrol.controller.response.VolumeInformation;
import java.io.IOException;

public interface AudioVolumeService {

  void setVolume(String device, int volume) throws IOException, InterruptedException;

  VolumeInformation getVolumeInformation(String device);

  void mute(String device) throws IOException, InterruptedException;

  void unmute(String device) throws IOException, InterruptedException;
}
