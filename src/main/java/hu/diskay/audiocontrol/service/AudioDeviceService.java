package hu.diskay.audiocontrol.service;

import java.io.IOException;
import java.util.List;
import javax.sound.sampled.Mixer.Info;


public interface AudioDeviceService {
  List<String> getWhiteList();

  List<Info> getDeviceInformations();

  List<Info> getEnabledDeviceInformations();

  void changeDevice(String deviceName) throws IOException;

  void changeToBaseDevice() throws IOException;
}
