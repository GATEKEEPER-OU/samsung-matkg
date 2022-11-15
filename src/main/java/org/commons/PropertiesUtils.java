package org.commons;

import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
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
  public static Properties loadConfiguration(String path) throws IOException {
    InputStream configStream = ResourceUtils.getResourceAsStream(path);
    if (configStream == null) {
      String exceptionMessage = String.format("The file '%s' not exists. Please copy and rename the '-dist' file into resources folder.", path);
      throw new FileNotFoundException(exceptionMessage);
    }
    Properties config = new Properties();
    config.load(configStream);
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
