package org.commons;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * @todo description
 */
public class EmailUtils {

  /**
   * @todo description
   */
  public static String getUsername(String emailAddress) {
    String[] parts = emailAddress.split("@");
    // @todo check if an email has been parsed
    return parts[0];
  }

  //--------------------------------------------------------------------------//
  // Class definition
  //--------------------------------------------------------------------------//

  /**
   * @todo description
   */
  private EmailUtils() {
  }

}