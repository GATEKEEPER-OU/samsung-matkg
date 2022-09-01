package org.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * @todo description
 */
public class PropertiesUtils {

  /**
   * @todo description
   */
  public static Properties loadConfiguration(String path) {
    Properties config = new Properties();
    try {
      InputStream configStream = ResourceUtils.getResourceAsStream(path);
      config.load(configStream);

    } catch (IOException e) {
      // @todo improve message and use LOGGER
      System.out.println(" >>>> " + path + " not set");
      System.out.println(e);
    }
    return config;
  }

}
