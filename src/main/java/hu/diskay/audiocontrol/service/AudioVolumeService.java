package hu.diskay.audiocontrol.service;

import java.io.IOException;

public interface AudioVolumeService {

  void setVolume(String device, int volume) throws IOException, InterruptedException;

  int getVolume(String device);

  void mute(String device) throws IOException, InterruptedException;

  void unmute(String device) throws IOException, InterruptedException;
}
