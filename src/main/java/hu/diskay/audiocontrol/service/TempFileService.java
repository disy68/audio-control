package hu.diskay.audiocontrol.service;

import java.io.File;
import java.io.IOException;

/**
 * Created by Szabolcs on 2017. 07. 02..
 */
public interface TempFileService {

    File getTempFile(String resourcePath) throws IOException;
}
