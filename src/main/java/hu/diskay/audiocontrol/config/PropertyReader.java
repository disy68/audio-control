package hu.diskay.audiocontrol.config;

import static java.util.Objects.isNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

public class PropertyReader {

  private static final Logger LOG = LoggerFactory.getLogger(PropertyReader.class);
  private static final String SEPARATOR = "//";

  public static String getString(String propertyName, Environment env) {
    String property = env.getProperty(propertyName);
    if (isNull(property)) {
      LOG.warn("String property {} was null.", propertyName);
    } else {
      LOG.info("String property {} was read as: {}", propertyName, property);
    }
    return property;
  }

  public static List<String> getStrings(String propertyName, Environment env) {
    String devices = env.getProperty(propertyName);
    if (isNull(devices)) {
      LOG.warn("List property {} was NULL", propertyName);
      devices = "";
    }

    List<String> strings = Arrays.asList(devices.split(SEPARATOR));

    LOG.info("List property {} was parsed as {}", propertyName, strings);

    return strings;
  }
}
