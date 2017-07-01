package hu.diskay.audiocontrol.service;

import java.io.IOException;

/**
 * Created by Szabolcs on 2017. 07. 01..
 */
public interface AudioVolumeService {

  void setVolume(String device, int volume) throws IOException, InterruptedException;

  int getVolume(String device);
}
