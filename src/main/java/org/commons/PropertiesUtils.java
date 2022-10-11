package org.commons;

import org.apache.commons.lang3.StringUtils;

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
  public static Properties getSubset(Properties props, String subsetPath) {
    Properties subset = new Properties();
    props.forEach((k, v) -> {
      String completeKey = k.toString();
      if (completeKey.startsWith(subsetPath)) {
        String key = StringUtils.difference(subsetPath, completeKey);
        subset.setProperty(
          StringUtils.stripStart(key, "."),
          v.toString()
        );
      }
    });
    return subset;
  }

  //--------------------------------------------------------------------------//
  // Class definition
  //--------------------------------------------------------------------------//

  /**
   * This class is not instantiable
   */
  private PropertiesUtils() {
  }

}
