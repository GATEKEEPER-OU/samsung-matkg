package org.commons;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * @todo description
 */
public class OutputUtils {

  /**
   * @todo description
   */
  public static void clean(File dir) {
    try {
      FileUtils.cleanDirectory(dir);

    } catch (IOException e) {
      // @todo Message
//      LOGGER.error(e.getMessage());
      e.printStackTrace(); // DEBUG
    }
  }

  //--------------------------------------------------------------------------//
  // Class definition
  //--------------------------------------------------------------------------//

  /**
   * @todo description
   */
  private OutputUtils() {
  }

}